package com.example.expensemanagerrest.web.controller;

import com.example.expensemanagerrest.model.Category;
import com.example.expensemanagerrest.service.CategoryService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    Optional<Category> category = this.categoryService.findById(catId);
    log.info("category {}", category.get());
    return category.get();
  }

  @GetMapping("/categories/name/{name}")
  public Category getCategoryByName(@PathVariable String name) {
    return this.categoryService.getByName(name);
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
  public ResponseEntity<String> deleteCategories(@RequestBody List<Long> list) {
    this.categoryService.deleteCategories(list);
    return new ResponseEntity<String>("Deleted", HttpStatus.OK);
  }


}
