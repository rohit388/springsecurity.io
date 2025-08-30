package com.springsecurity.io.service.imple;

import com.springsecurity.io.dto.ExpenseRequest;
import com.springsecurity.io.entity.Expense;
import com.springsecurity.io.mapper.ExpenseMapper;
import com.springsecurity.io.repo.ExpenseRepo;
import com.springsecurity.io.service.ExpenseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ExpenseServiceImple implements ExpenseService {

    private ExpenseRepo expenseRepo;

    private ExpenseMapper expenseMapper;

    ExpenseServiceImple(ExpenseRepo expenseRepo, ExpenseMapper expenseMapper){
        this.expenseRepo = expenseRepo;
        this.expenseMapper = expenseMapper;
    }

    @Override
    public ExpenseRequest create(ExpenseRequest expenseRequest){
        Expense expense = expenseMapper.toEntity(expenseRequest);
        Expense save = expenseRepo.save(expense);
        return expenseMapper.toDto(save);
    }
}
