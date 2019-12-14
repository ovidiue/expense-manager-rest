package com.example.expensemanagerrest.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseStats {
  private long noOfExpenses;
  private double max;
  private double min;
  private double averageNonRecurrent;
  private double averageRecurrent;
  private double partialPayed;
  private double payed;
}
