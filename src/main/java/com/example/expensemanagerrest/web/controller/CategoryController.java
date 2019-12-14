package com.example.expensemanagerrest.web.controller;

import com.example.expensemanagerrest.model.Category;
import com.example.expensemanagerrest.model.Expense;
import com.example.expensemanagerrest.model.Rate;
import com.example.expensemanagerrest.model.stats.CategoryStats;
import com.example.expensemanagerrest.service.CategoryService;
import com.example.expensemanagerrest.service.ExpenseService;
import com.example.expensemanagerrest.service.RateService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.ws.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ovidiu on 19-Jan-19.
 */
@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private ExpenseService expenseService;

  @Autowired
  private RateService rateService;

  @GetMapping("/categories")
  public ResponseEntity<Page<Category>> getCategories(Pageable pageable) {
    Page<Category> categories = categoryService.findAll(pageable);
    return ResponseEntity.ok(categories);
  }

  @GetMapping("/categories/{catId}")
  public Category getCategory(@PathVariable Long catId) {
    log.info("catId {}", catId);
    Optional<Category> category = this.categoryService.findById(catId);
    log.info("category {}", category.get());
    return category.get();
  }

  @GetMapping("/categories/name/{name}")
  public Category getCategoryByName(@PathVariable String name) {
    return this.categoryService.getByName(name);
  }

  @PutMapping("/categories/update")
  public Category updateCategory(@RequestBody Category category) {
    return this.categoryService.update(category);
  }

  @PostMapping("/categories/save")
  public ResponseEntity saveCategory(@RequestBody Category category) {
    if (this.categoryService.getByName(category.getName()) != null) {
      return ResponseEntity.badRequest().body("name exists");
    } else {
      this.categoryService.saveCategory(category);
      return ResponseEntity.ok().build();
    }
  }

  @PutMapping("/categories/update/{catId}")
  public ResponseEntity updateCategory(@RequestBody Category category, @PathVariable Long catId) {
    Optional<Category> optCategory = this.categoryService.findById(catId);
    if (!optCategory.isPresent()) {
      return ResponseEntity.notFound().build();
    } else {
      category.setId(catId);
      this.categoryService.saveCategory(category);
      return ResponseEntity.ok().build();
    }
  }

  @PostMapping("/categories/delete")
  public ResponseEntity deleteCategories(@RequestBody List<Long> list,
      @RequestParam Boolean withExpenses) {

    List<Category> categoriesToDelete = this.categoryService.findAllWhereIdInList(list);
    List<Expense> expensesAttached = this.expenseService
        .findAllWithCategoryInList(categoriesToDelete);
    log.info("\nLIST: {}", list);
    log.info("\nexpensesAttached: {}", expensesAttached);
    log.info("\nexpensesAttachedSize: {}", expensesAttached.size());

    expensesAttached.forEach(expense -> expense.setCategory(null));
    List<Long> expensesIds = expensesAttached.stream().map(expense -> expense.getId()).collect(
        Collectors.toList());

    log.info("expensesIDS {}", expensesIds);

    if (withExpenses == true) {
      List<Long> ratesIds = this.rateService.findAllByExpenseIdsList(expensesIds)
          .stream().map(Rate::getId).collect(Collectors.toList());
      this.rateService.deleteRates(ratesIds);
      this.expenseService.deleteExpenses(expensesIds);
    }

    this.categoryService.deleteCategories(list);
    return ResponseEntity.ok(list);
  }

  @GetMapping("/categories/count")
  public ResponseEntity<Map<String, Integer>> getCategoryCount() {
    Map<String, Integer> categories = this.categoryService.getCategoryCount();
    return ResponseEntity.ok(categories);
  }

  @GetMapping("/categories/info")
  public ResponseEntity<List<CategoryStats>> getCategoryInfo() {
    List<CategoryStats> stats = this.categoryService.getCategoryInfo();
    return ResponseEntity.ok(stats);
  }

}
