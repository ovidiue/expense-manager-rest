package com.example.expensemanagerrest.web.controller;

import com.example.expensemanagerrest.service.CategoryService;
import com.example.expensemanagerrest.service.ExpenseService;
import com.example.expensemanagerrest.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class DashboardController {

  @Autowired
  private CategoryService categoryService;
  @Autowired
  private TagService tagService;
  @Autowired
  private ExpenseService expenseService;

  /*@GetMapping("/categories/count")
  public ResponseEntity<Map<String, Integer>> getCategoryCount() {
    Map<String, Integer> categories = this.categoryService.getCategoryCount();
    return ResponseEntity.ok(categories);
  }*/

  /*@GetMapping("/categories/info")
  public ResponseEntity<List<CategoryStats>> getCategoryInfo() {
    List<CategoryStats> stats = this.categoryService.getCategoryInfo();
    return ResponseEntity.ok(stats);
  }*/

  /*@GetMapping("/categories/tag-info")
  public ResponseEntity<List<CategoryStats>> getTagInfo() {
    List<CategoryStats> stats = this.tagService.getTagInfo();
    return ResponseEntity.ok(stats);
  }*/

  /*@GetMapping("/categories/simple-expenses")
  public ResponseEntity<List<ExpenseSimplified>> getSimpleExpenses() {
    List<ExpenseSimplified> list = this.expenseService.findAllSimple();
    return ResponseEntity.ok(list);
  }*/

  /*@GetMapping("/categories/expense-stats")
  public ResponseEntity<ExpenseStats> getExpenseStats() {
    return ResponseEntity.ok(this.expenseService.getStatsInfo());
  }*/

}
