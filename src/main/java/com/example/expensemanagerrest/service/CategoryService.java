package com.example.expensemanagerrest.service;

import com.example.expensemanagerrest.model.Category;
import com.example.expensemanagerrest.repository.CategoryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Ovidiu on 19-Jan-19.
 */
@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Category> findAll() {
    return this.categoryRepository.findAll();
  }

  public void saveCategory(Category category) {
    this.categoryRepository.save(category);
  }

  public void deleteCategory(Category category) {
    this.categoryRepository.delete(category);
  }

}
