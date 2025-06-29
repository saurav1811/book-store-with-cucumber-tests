package com.equalexperts.bookstorewithcucumbertests.controllers;

import com.equalexperts.bookstorewithcucumbertests.models.BookOrderRequest;
import com.equalexperts.bookstorewithcucumbertests.models.Invoice;
import com.equalexperts.bookstorewithcucumbertests.services.InventoryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book-store")
public class BookStoreController {

    private final InventoryService inventoryService;

    public BookStoreController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/purchase-books-by-name")
    public ResponseEntity<Invoice> purchaseBooksByName(
            @RequestBody @Validated List<BookOrderRequest> bookOrderRequests
    ) {
        return ResponseEntity.ok().body(inventoryService.purchaseBooksByName(bookOrderRequests));
    }
}
