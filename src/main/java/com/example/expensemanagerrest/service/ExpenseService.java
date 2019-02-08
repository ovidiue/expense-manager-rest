package com.example.expensemanagerrest.service;

import com.example.expensemanagerrest.model.Expense;
import com.example.expensemanagerrest.repository.ExpenseRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Ovidiu on 26-Jan-19.
 */
@Service
public class ExpenseService {

  @Autowired
  private ExpenseRepository expenseRepository;

  public List<Expense> findAll() {
    return this.expenseRepository.findAll();
  }

  public void saveExpense(Expense expense) {
    this.expenseRepository.save(expense);
  }

  public void deleteExpense(Expense expense) {
    this.expenseRepository.delete(expense);
  }

  public Expense getExpense(Long catId) {
    return this.expenseRepository.getOne(catId);
  }

  @Transactional
  public void deleteExpenses(List<Long> list) {
    this.expenseRepository.deleteByIdIn(list);
  }

  public List<Expense> findAllWithIdIn(List<Long> ids) {
    return this.expenseRepository.findByIdIn(ids);
  }

  public List<Expense> findAllWithCategoryIdIn(List<Long> ids) {
    return this.expenseRepository.findAllByCategoryId(ids);
  }

}
