package com.example.expensemanagerrest.web.controller;

import com.example.expensemanagerrest.model.Expense;
import com.example.expensemanagerrest.model.Rate;
import com.example.expensemanagerrest.service.ExpenseService;
import com.example.expensemanagerrest.service.RateService;
import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ovidiu on 26-Jan-19.
 */
@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/rates")
public class RateController {

  @Autowired
  private RateService rateService;
  @Autowired
  private ExpenseService expenseService;

  @GetMapping("")
  public ResponseEntity<Page<Rate>> getRates(Pageable pageable) {
    log.info("\ngetRates called");
    Page<Rate> rates = rateService.findAll(pageable);
    return ResponseEntity.ok(rates);
  }

  @GetMapping("/exp")
  public ResponseEntity<Page<Rate>> getRatesByExpenseId(@RequestParam Long expId,
      Pageable pageable) {
    log.info("\nExpense id {}", expId);
    Page<Rate> rates = rateService.findAllByExpenseId(expId, pageable);
    log.info("\nrates by expense id {}", rates);
    return ResponseEntity.ok(rates);
  }

  @GetMapping("/expenses")
  public List<Rate> getRatesByExpenseIdList(@RequestParam List<Long> expenseIds) {
    List<Rate> rates = rateService.findAllByExpenseIdsList(expenseIds);
    return rates;
  }

  @GetMapping("/{rateId}")
  public Rate getRate(@PathVariable Long rateId) {
    log.info("\nGET_RATE_CALLED");
    log.info("\nCAT_ID {}", rateId);
    Rate rate = this.rateService.getRate(rateId);
    log.info("\nRATE {}", rate);
    return rate;
  }

  @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void saveRate(@RequestBody Rate rate) {
    log.info("\nSAVE_RATE CALLED");
    log.info("\nRATE: {}", rate);
    if (rate.getExpense() != null) {
      log.info("\nIN IF RATE GET EXPENSE NOT NULL");
      Expense currentChosenExpense = rate.getExpense();
      log.info("\nCURRENT_CHOSEN_EXPENSE {}", currentChosenExpense);
      currentChosenExpense.addRate(rate);
      this.expenseService.saveExpense(rate.getExpense());
    }

    this.rateService.saveRate(rate);
  }

  @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void updateRate(@RequestBody Rate rate,
      @RequestParam(required = false) Long initialExpenseId) {
    Rate initialRate = this.rateService.getRate(rate.getId());

    if (rate.getExpense() != null) {
      Long currentExpenseId = rate.getExpense().getId();

      if (initialExpenseId != null && initialExpenseId == currentExpenseId) {
        Expense currentExpense = this.expenseService.getExpense(currentExpenseId);
        currentExpense.removeRate(initialRate);
        currentExpense.addRate(rate);
        this.expenseService.saveExpense(currentExpense);
      } else if (initialExpenseId != null && initialExpenseId != currentExpenseId) {
        Expense oldExpense = this.expenseService.getExpense(initialExpenseId);
        Expense newExpense = this.expenseService.getExpense(currentExpenseId);
        oldExpense.removeRate(rate);
        newExpense.addRate(rate);
        this.expenseService.saveExpense(oldExpense);
        this.expenseService.saveExpense(newExpense);
      } else if (initialExpenseId == null) {
        Expense expense = this.expenseService.getExpense(currentExpenseId);
        expense.addRate(rate);
        this.expenseService.saveExpense(expense);
      }

    } else {
      if (initialExpenseId != null) {
        Expense expense = this.expenseService.getExpense(initialExpenseId);
        expense.removeRate(rate);
        this.expenseService.saveExpense(expense);
      }
    }

    this.rateService.saveRate(rate);
  }

  @PostMapping("/delete")
  public ResponseEntity deleteRates(@RequestBody List<Long> list) {
    this.rateService.findAllWithIdIn(list)
        .stream()
        .filter(rate -> rate.getExpense() != null)
        .forEach(rate -> {
          Expense expense = rate.getExpense();
          expense.removeRate(rate);
          this.expenseService.saveExpense(expense);
        });

    this.rateService.deleteRates(list);
    return ResponseEntity.ok(list);
  }

}
