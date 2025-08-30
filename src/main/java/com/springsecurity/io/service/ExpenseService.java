package com.springsecurity.io.service;

import com.springsecurity.io.dto.ExpenseResponse;
import com.springsecurity.io.dto.ExpenseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


public interface ExpenseService {

    ExpenseRequest create(ExpenseRequest request);

//    Page<ExpenseResponse>getAll(LocalDate from, LocalDate to, String category, Pageable pageable);
//
//    ExpenseResponse update(Long id, ExpenseRequest request);
//
//    void delete(Long id);

}
