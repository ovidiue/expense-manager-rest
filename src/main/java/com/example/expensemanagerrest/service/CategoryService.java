package com.example.expensemanagerrest.service;

import com.example.expensemanagerrest.model.Category;
import com.example.expensemanagerrest.model.Expense;
import com.example.expensemanagerrest.model.stats.CategoryStats;
import com.example.expensemanagerrest.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Ovidiu on 19-Jan-19.
 */
@Service
@Slf4j
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ExpenseService expenseService;

  public Page<Category> findAll(Pageable pageable) {
    return this.categoryRepository.findAll(pageable);
  }

  public Category update(Category category) {
    return this.categoryRepository.save(category);
  }

  public List<Category> findAll() {
    return categoryRepository.findAll();
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

  public Map<String, Integer> getCategoryCount() {
    Map<String, Integer> result = new HashMap<>();
    List<Category> categories = this.findAll();

    categories.forEach(cat -> {
      result.put(cat.getName(), this.expenseService.countWithCategory(cat));
    });

    return result;
  }

  public List<CategoryStats> getCategoryInfo() {
    List result = new ArrayList();
    List<Category> categories = this.findAll();

    categories.forEach(cat -> {
      List<Expense> expenses = this.expenseService.findAllWithCategory(cat);

      double total = expenses.stream().mapToDouble(e -> e.getAmount()).sum();
      double totalPayed = expenses.stream().mapToDouble(e -> e.getPayed()).sum();
      double min = expenses.stream().mapToDouble(e -> e.getAmount()).min().orElse(0);
      double max = expenses.stream().mapToDouble(e -> e.getAmount()).max().orElse(0);
      long recurrentCount = expenses.stream().filter(ex -> ex.isRecurrent() == true).count();
      long nonRecurrentCount = expenses.stream().filter(ex -> ex.isRecurrent() == false).count();
      long noOfExpenses = expenses.stream().count();
      long closed = expenses.stream().filter(ex -> {
        log.info("ex {}", ex);
        return Double.compare(ex.getPayed(), ex.getAmount()) == 0;
      }).count();

      result.add(new CategoryStats(cat.getName(),
          cat.getColor(),
          totalPayed,
          total,
          min,
          max,
          recurrentCount,
          nonRecurrentCount,
          closed,
          noOfExpenses));

    });

    return result;
  }

  @Transactional
  public void deleteCategories(List<Long> list) {
    this.categoryRepository.deleteByIdIn(list);
  }

  public Category getByName(String name) {
    return this.categoryRepository.findByName(name);
  }

}
