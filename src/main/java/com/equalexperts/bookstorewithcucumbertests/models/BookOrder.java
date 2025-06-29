package com.equalexperts.bookstorewithcucumbertests.models;

import com.equalexperts.bookstorewithcucumbertests.entities.Book;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookOrder {
    private Book book;
    private Integer quantity;
    private Double cost;

    public static class BookOrderBuilder {
        public BookOrderBuilder cost() {
            this.cost = this.book.getPrice() * this.quantity;
            return this;
        }
    }
}
