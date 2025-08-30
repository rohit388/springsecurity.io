package com.springsecurity.io.repo;

import com.springsecurity.io.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense,Long>, JpaSpecificationExecutor<Expense> {
}
