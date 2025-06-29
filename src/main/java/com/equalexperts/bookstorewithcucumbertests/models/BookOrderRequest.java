package com.equalexperts.bookstorewithcucumbertests.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookOrderRequest {
    private String bookName;
    private Integer quantity;
}
