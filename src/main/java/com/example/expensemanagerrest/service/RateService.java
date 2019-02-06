package com.example.expensemanagerrest.service;

import com.example.expensemanagerrest.model.Rate;
import com.example.expensemanagerrest.repository.RateRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Ovidiu on 26-Jan-19.
 */
@Service
public class RateService {

  @Autowired
  private RateRepository rateRepository;

  public List<Rate> findAll() {
    return this.rateRepository.findAll();
  }

  public void saveRate(Rate rate) {
    this.rateRepository.save(rate);
  }

  public void deleteRate(Rate rate) {
    this.rateRepository.delete(rate);
  }

  public Rate getRate(Long catId) {
    return this.rateRepository.getOne(catId);
  }

  public List<Rate> findAllByExpenseId(Long id) {
    return this.rateRepository.findAllByExpense_Id(id);
  }

  public List<Rate> findAllByExpenseIdsList(List<Long> ids) {
    return this.rateRepository.findByExpenseIdIn(ids);
  }

  @Transactional
  public void deleteRates(List<Long> list) {
    this.rateRepository.deleteByIdIn(list);
  }

}
