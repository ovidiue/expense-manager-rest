package com.example.expensemanagerrest.web.controller;

import com.example.expensemanagerrest.model.Expense;
import com.example.expensemanagerrest.service.ExpenseService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ovidiu on 26-Jan-19.
 */
@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class ExpenseController {

  @Autowired
  private ExpenseService expenseService;

  @GetMapping("/expenses")
  public List<Expense> getExpenses() {
    List<Expense> expenses = expenseService.findAll();
    //return ResponseEntity.status(HttpStatus.OK).body(categories);
    return expenses;
  }

  @GetMapping("/expenses/{expId}")
  public Expense getExpense(@PathVariable Long expId) {
    log.info("catId {}", expId);
    Expense expense = this.expenseService.getExpense(expId);
    log.info("expense {}", expense);
    return expense;
  }

  @PostMapping("/expenses/save")
  public void saveExpense(@RequestBody Expense expense) {
    this.expenseService.saveExpense(expense);
  }

  @PostMapping("/expenses/delete")
  public ResponseEntity<String> deleteExpenses(@RequestBody List<Long> list) {
    this.expenseService.deleteExpenses(list);
    return new ResponseEntity<String>("Deleted", HttpStatus.OK);
  }

}
