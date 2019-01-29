package com.example.expensemanagerrest.web.controller;

import com.example.expensemanagerrest.model.Rate;
import com.example.expensemanagerrest.service.ExpenseService;
import com.example.expensemanagerrest.service.RateService;
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
import org.springframework.web.bind.annotation.RequestMapping;
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

  @GetMapping()
  public List<Rate> getRates() {
    List<Rate> rates = rateService.findAll();
    //return ResponseEntity.status(HttpStatus.OK).body(rates);
    return rates;
  }

  @GetMapping("/{rateId}")
  public Rate getRate(@PathVariable Long rateId) {
    log.info("catId {}", rateId);
    Rate rate = this.rateService.getRate(rateId);
    log.info("rate {}", rate);
    return rate;
  }

  @PostMapping("/save")
  public void saveRate(@RequestBody Rate rate) {
    if (rate.getExpense() != null) {
      rate.getExpense().addRate(rate);
      this.expenseService.saveExpense(rate.getExpense());
    }
    this.rateService.saveRate(rate);
  }

  @PostMapping("/delete")
  public ResponseEntity<String> deleteRates(@RequestBody List<Long> list) {
    this.rateService.deleteRates(list);
    return ResponseEntity.ok().build();
  }

}
