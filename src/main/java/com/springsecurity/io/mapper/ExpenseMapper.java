package com.springsecurity.io.mapper;

import com.springsecurity.io.dto.ExpenseRequest;
import com.springsecurity.io.entity.Expense;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseMapper extends EntityMapper<ExpenseRequest, Expense> {

    @Override
    Expense toEntity(ExpenseRequest request);

    @Override
    ExpenseRequest toDto(Expense entity);

}
