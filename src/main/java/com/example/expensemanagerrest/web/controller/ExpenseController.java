package com.example.expensemanagerrest.web.controller;

import com.example.expensemanagerrest.model.Expense;
import com.example.expensemanagerrest.model.filters.ExpenseFilter;
import com.example.expensemanagerrest.service.ExpenseService;
import com.example.expensemanagerrest.service.RateService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

  @Autowired
  private RateService rateService;

  @GetMapping("/expenses")
  public ResponseEntity<Page<Expense>> getExpenses(ExpenseFilter expenseFilter, Pageable pageable) {
    Page<Expense> expenses = expenseService.findAll(expenseFilter, pageable);
    log.info("\nLARGEST EXPENSE {} ", this.expenseService.getLargestExpense());
    return ResponseEntity.ok(expenses);
  }

  @GetMapping("/expenses/max")
  public ResponseEntity<Double> getLargestExpense() {
    return ResponseEntity.ok(this.expenseService.getLargestExpense());
  }

  @GetMapping("/expenses/{expId}")
  public Expense getExpense(@PathVariable Long expId) {
    log.info("catId {}", expId);
    Expense expense = this.expenseService.getExpense(expId);
    log.info("expense {}", expense);
    return expense;
  }

  @PostMapping(value = "/expenses/save", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void saveExpense(@RequestBody Expense expense) {
    log.info("expense to save {}", expense);
    this.expenseService.saveExpense(expense);
  }

  @PostMapping("/expenses/delete")
  public ResponseEntity deleteExpenses(@RequestBody List<Long> list,
      @RequestParam Boolean ratesToo) {
    log.info("\nDELETE_EXPENSES_CALLED");
    log.info("\nLIST_IDS_TO_DELETE {}", list);
    log.info("\nRATES_TOO {}", ratesToo);
    if (ratesToo == true) {
      list.forEach(id -> {
        List<Long> rateIds = this.rateService.findAllByExpenseId(id, Pageable.unpaged())
            .stream()
            .map(rate -> rate.getId())
            .collect(Collectors.toList());
        this.rateService.deleteRates(rateIds);
      });
    } else {
      list.forEach(id -> {
        this.rateService.findAllByExpenseId(id, Pageable.unpaged())
            .stream()
            .filter(rate -> rate.getExpense() != null)
            .forEach(rate -> {
              rate.setExpense(null);
              this.rateService.saveRate(rate);
            });
      });
    }

    this.expenseService.deleteExpenses(list);
    return ResponseEntity.ok(list);
  }

}
