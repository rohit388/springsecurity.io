package com.springsecurity.io.controller;

import com.springsecurity.io.dto.ExpenseRequest;
import com.springsecurity.io.service.ExpenseProducer;
import com.springsecurity.io.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    private final ExpenseProducer expenseProducer;

    public ExpenseController(ExpenseService expenseService, ExpenseProducer expenseProducer){
        this.expenseService = expenseService;
        this.expenseProducer = expenseProducer;
    }

    @PostMapping("/create")
    public ResponseEntity<ExpenseRequest> create(@Valid @RequestBody ExpenseRequest expenseRequest){
        ExpenseRequest savedExpense = expenseService.create(expenseRequest);

        for(int i=0;i<=50;i++) {
            expenseProducer.sendExpenseEvent(savedExpense);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }
}
