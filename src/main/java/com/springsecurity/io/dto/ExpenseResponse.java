package com.springsecurity.io.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseResponse {

    private String description;

    private Double amount;

    private String category;

    private LocalDate date;
}
