package com.example.expensemanagerrest.model.filters;

import java.util.ArrayList;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

/**
 * Created by Ovidiu on 10-Feb-19.
 */
@Data
@Component
public class ExpenseFilter {

  private Double amountFrom;
  private Double amountTo;
  private String title;
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private Date dueDateFrom;
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private Date dueDateTo;
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private Date createdFrom;
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private Date createdTo;
  private String description;
  private boolean recurrent;
  private Long categoryId;
  private ArrayList<Long> tagIds;

  public boolean isEmpty() {
    return this.amountFrom == null &&
        this.amountTo == null &&
        this.title == null &&
        this.dueDateFrom == null &&
        this.dueDateTo == null &&
        this.createdFrom == null &&
        this.createdTo == null &&
        this.description == null &&
        this.recurrent == false &&
        this.categoryId == null &&
        this.tagIds == null;
  }
}
