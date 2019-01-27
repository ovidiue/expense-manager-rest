package com.example.expensemanagerrest.web.controller;

import com.example.expensemanagerrest.model.Category;
import com.example.expensemanagerrest.service.CategoryService;
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

  @GetMapping("/categories")
  public List<Category> getCategories() {
    List<Category> categories = categoryService.findAll();
    //return ResponseEntity.status(HttpStatus.OK).body(categories);
    return categories;
  }

  @GetMapping("/categories/{catId}")
  public Category getCategory(@PathVariable Long catId) {
    log.info("catId {}", catId);
    Category category = this.categoryService.getCategory(catId);
    log.info("category {}", category);
    return category;
  }

  @GetMapping("/categories/name/{name}")
  public Category getCategoryByName(@PathVariable String name) {
    return this.categoryService.getByName(name);
  }

  @PostMapping("/categories/save")
  public ResponseEntity saveCategory(@RequestBody Category category) {
    if (this.categoryService.getByName(category.getName()) != null) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    } else {
      this.categoryService.saveCategory(category);
      return new ResponseEntity(HttpStatus.OK);
    }
  }

  @PostMapping("/categories/delete")
  public ResponseEntity<String> deleteCategories(@RequestBody List<Long> list) {
    this.categoryService.deleteCategories(list);
    return new ResponseEntity<String>("Deleted", HttpStatus.OK);
  }


}
