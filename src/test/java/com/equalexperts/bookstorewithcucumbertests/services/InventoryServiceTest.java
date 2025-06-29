package com.equalexperts.bookstorewithcucumbertests.services;

import com.equalexperts.bookstorewithcucumbertests.entities.Book;
import com.equalexperts.bookstorewithcucumbertests.entities.Stock;
import com.equalexperts.bookstorewithcucumbertests.models.BookOrder;
import com.equalexperts.bookstorewithcucumbertests.models.BookOrderRequest;
import com.equalexperts.bookstorewithcucumbertests.models.Invoice;
import com.equalexperts.bookstorewithcucumbertests.repositories.StockRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Test
    void shouldThrowMissingBookOrdersInInventory() {
        InventoryService inventoryService = new InventoryService(stockRepository);

        List<BookOrderRequest> bookOrderRequests = Arrays.asList(
                BookOrderRequest.builder().bookName("Harry Potter: The Chamber of Secrets").quantity(2).build(),
                BookOrderRequest.builder().bookName("Sherlock Holmes: The Hound of Baskervilles").quantity(1).build()
        );

        Mockito.when(stockRepository.findStocksByBookNames(Mockito.anyList())).thenReturn(Collections.emptyList());

        Assertions.assertThrows(
                ResponseStatusException.class,
                () -> inventoryService.purchaseBooksByName(bookOrderRequests),
                "Missing Books in Inventory for:- " + bookOrderRequests
        );
    }

    @Test
    void shouldThrowOutOfStockInInventory() {
        InventoryService inventoryService = new InventoryService(stockRepository);
        List<BookOrderRequest> bookOrderRequests = Arrays.asList(
                BookOrderRequest.builder().bookName("Harry Potter: The Chamber of Secrets").quantity(4).build(),
                BookOrderRequest.builder().bookName("Sherlock Holmes: The Hound of Baskervilles").quantity(2).build(),
                BookOrderRequest.builder().bookName("The Alchemist").quantity(2).build()
        );

        Mockito.when(stockRepository.findStocksByBookNames(Mockito.anyList()))
                .thenReturn(
                        Arrays.asList(
                                Stock.builder().book(Book.builder().name("Harry Potter: The Chamber of Secrets").author("J K Rowling").price(150.75).build()).quantity(3).build(),
                                Stock.builder().book(Book.builder().name("Sherlock Holmes: The Hound of Baskervilles").author("Arthur Conan Doyle").price(220.15).build()).quantity(5).build(),
                                Stock.builder().book(Book.builder().name("The Alchemist").author("Paulo Coelho").price(125.45).build()).quantity(1).build()
                        )
                );

        Assertions.assertThrows(
                ResponseStatusException.class,
                () -> inventoryService.purchaseBooksByName(bookOrderRequests),
                "Missing Books in Inventory for:- " + Arrays.asList(
                        Stock.builder().book(Book.builder().name("Harry Potter: The Chamber of Secrets").author("J K Rowling").price(150.75).build()).quantity(3).build(),
                        Stock.builder().book(Book.builder().name("The Alchemist").author("Paulo Coelho").price(125.45).build()).quantity(1).build()
                )
        );
    }

    @Test
    void shouldUpdateInventoryAndCreateInvoiceForSuccessfulPurchase() {
        InventoryService inventoryService = new InventoryService(stockRepository);
        List<BookOrderRequest> bookOrderRequests = Arrays.asList(
                BookOrderRequest.builder().bookName("Harry Potter: The Chamber of Secrets").quantity(4).build(),
                BookOrderRequest.builder().bookName("The Alchemist").quantity(2).build(),
                BookOrderRequest.builder().bookName("Sherlock Holmes: The Hound of Baskervilles").quantity(2).build()
        );
        Invoice expectedInvoice = Invoice.builder().bookOrders(Arrays.asList(
                        BookOrder.builder().book(Book.builder().name("Harry Potter: The Chamber of Secrets").author("J K Rowling").price(150.75).build()).quantity(4).cost().build(),
                        BookOrder.builder().book(Book.builder().name("Sherlock Holmes: The Hound of Baskervilles").author("Arthur Conan Doyle").price(220.15).build()).quantity(2).cost().build(),
                        BookOrder.builder().book(Book.builder().name("The Alchemist").author("Paulo Coelho").price(125.45).build()).quantity(2).cost().build()
                )
        ).totalCost().build();

        Mockito.when(stockRepository.findStocksByBookNames(Mockito.anyList()))
                .thenReturn(
                        Arrays.asList(
                                Stock.builder().book(Book.builder().name("Harry Potter: The Chamber of Secrets").author("J K Rowling").price(150.75).build()).quantity(6).build(),
                                Stock.builder().book(Book.builder().name("Sherlock Holmes: The Hound of Baskervilles").author("Arthur Conan Doyle").price(220.15).build()).quantity(7).build(),
                                Stock.builder().book(Book.builder().name("The Alchemist").author("Paulo Coelho").price(125.45).build()).quantity(2).build()
                        )
                );

        Invoice actualInvoice = inventoryService.purchaseBooksByName(bookOrderRequests);
        Assertions.assertEquals(expectedInvoice.toString(), actualInvoice.toString());
        verify(stockRepository, times(1)).saveAll(
                Arrays.asList(
                        Stock.builder().book(Book.builder().name("Harry Potter: The Chamber of Secrets").author("J K Rowling").price(150.75).build()).quantity(2).build(),
                        Stock.builder().book(Book.builder().name("Sherlock Holmes: The Hound of Baskervilles").author("Arthur Conan Doyle").price(220.15).build()).quantity(5).build(),
                        Stock.builder().book(Book.builder().name("The Alchemist").author("Paulo Coelho").price(125.45).build()).quantity(0).build()
                )
        );
    }
}