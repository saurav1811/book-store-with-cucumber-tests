package com.equalexperts.bookstorewithcucumbertests.services;

import com.equalexperts.bookstorewithcucumbertests.entities.Stock;
import com.equalexperts.bookstorewithcucumbertests.models.BookOrder;
import com.equalexperts.bookstorewithcucumbertests.models.BookOrderRequest;
import com.equalexperts.bookstorewithcucumbertests.models.Invoice;
import com.equalexperts.bookstorewithcucumbertests.repositories.StockRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service
public class InventoryService {

    private final StockRepository stockRepository;

    public InventoryService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Invoice purchaseBooksByName(List<BookOrderRequest> bookOrderRequests) {
        List<Stock> stocks = stockRepository.findStocksByBookNames(bookOrderRequests.stream().map(BookOrderRequest::getBookName).map(String::toUpperCase).toList());

        Set<BookOrderRequest> missingBookOrderRequests = new HashSet<>();
        bookOrderRequests.forEach(bookOrderRequest -> {
            Stock stock = stocks.stream().filter(
                    it -> it.getBook().getName().equalsIgnoreCase(bookOrderRequest.getBookName().toUpperCase())
            ).findFirst().orElse(null);
            if (stock == null) {
                missingBookOrderRequests.add(bookOrderRequest);
            }
        });
        if (!missingBookOrderRequests.isEmpty()) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "Missing Books in Inventory for:- " + missingBookOrderRequests);
        }

        Set<Stock> outOfStocks = new HashSet<>();
        stocks.forEach(stock -> {
            BookOrderRequest bookOrderRequest = bookOrderRequests.stream().filter(
                    it -> it.getBookName().equalsIgnoreCase(stock.getBook().getName().toUpperCase())
            ).findFirst().orElse(null);
            if (bookOrderRequest != null && bookOrderRequest.getQuantity() > stock.getQuantity()) {
                outOfStocks.add(stock);
            }
        });
        if (!outOfStocks.isEmpty()) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "Out of Stock for:- " + outOfStocks);
        }

        List<BookOrder> bookOrders = new ArrayList<>();
        stocks.forEach(stock -> {
            BookOrderRequest bookOrderRequest = bookOrderRequests.stream().filter(
                    it -> it.getBookName().equalsIgnoreCase(stock.getBook().getName().toUpperCase())
            ).findFirst().orElseThrow();
            int balanceQuantity = stock.getQuantity() - bookOrderRequest.getQuantity();
            stock.setQuantity(Math.max(balanceQuantity, 0));
            bookOrders.add(BookOrder.builder().book(stock.getBook()).quantity(bookOrderRequest.getQuantity()).cost().build());
        });
        stockRepository.saveAll(stocks);

        bookOrders.sort(Comparator.comparing(bo -> bo.getBook().getName()));
        return Invoice.builder().bookOrders(bookOrders).totalCost().build();
    }
}
