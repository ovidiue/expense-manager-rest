package com.example.expensemanagerrest.web.controller;

import com.example.expensemanagerrest.model.Category;
import com.example.expensemanagerrest.service.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ovidiu on 19-Jan-19.
 */
@RestController
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @GetMapping("/categories")
  public List<Category> getCategories() {
    List<Category> categories = categoryService.findAll();
    return categories;
  }

  @PostMapping("/categories/save")
  public void saveCategory(@RequestBody Category category) {
    this.categoryService.saveCategory(category);
  }

  @DeleteMapping("/categories/delete/")
  public void deleteCategory(@RequestBody Category category) {
    this.categoryService.deleteCategory(category);
  }


}
