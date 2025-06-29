package com.equalexperts.bookstorewithcucumbertests.models;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Invoice {
    private List<BookOrder> bookOrders;
    private Double totalCost;

    public static class InvoiceBuilder {
        public InvoiceBuilder totalCost() {
            this.totalCost = this.bookOrders.stream().map(BookOrder::getCost).reduce(0.0, Double::sum);
            return this;
        }
    }
}
