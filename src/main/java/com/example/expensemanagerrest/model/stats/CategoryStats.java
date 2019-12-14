package com.example.expensemanagerrest.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CategoryStats {
  private String name;
  private String color;
  private double payed;
  private double total;
  private double min;
  private double max;
  private long totalRecurrent;
  private long nonRecurrent;
  private long closed;
  private long noOfExpenses;
}
