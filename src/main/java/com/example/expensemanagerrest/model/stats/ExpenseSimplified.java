package com.example.expensemanagerrest.model.stats;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseSimplified {

  private String title;

  private boolean recurrent;

  private Date createdOn;

  private Date dueDate;

  private Double amount;

  private Long id;

  private Double payed;

  private int month;
}
