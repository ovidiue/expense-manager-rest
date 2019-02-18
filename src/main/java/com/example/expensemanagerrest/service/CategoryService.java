package com.example.expensemanagerrest.service;

import com.example.expensemanagerrest.model.Category;
import com.example.expensemanagerrest.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Ovidiu on 19-Jan-19.
 */
@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public Page<Category> findAll(Pageable pageable) {
    return this.categoryRepository.findAll(pageable);
  }

  public List<Category> findAllWhereIdInList(List<Long> ids) {
    return this.categoryRepository.findAllByIdIn(ids);
  }

  public void saveCategory(Category category) {
    this.categoryRepository.save(category);
  }

  public void deleteCategory(Category category) {
    this.categoryRepository.delete(category);
  }

  public Optional<Category> findById(Long catId) {
    return this.categoryRepository.findById(catId);
  }

  @Transactional
  public void deleteCategories(List<Long> list) {
    this.categoryRepository.deleteByIdIn(list);
  }

  public Category getByName(String name) {
    return this.categoryRepository.findByName(name);
  }

}
