package com.example.expensemanagerrest.repository;

import com.example.expensemanagerrest.model.Category;
import com.example.expensemanagerrest.model.Expense;
import com.example.expensemanagerrest.model.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Ovidiu on 26-Jan-19.
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  void deleteByIdIn(List<Long> list);

  List<Expense> findByIdIn(List<Long> ids);

  List<Expense> findAllByCategoryIn(List<Category> categories);

  List<Expense> findDistinctByTagsIn(List<Tag> tags);

  int countAllByCategory(Category category);

  @Query(value = "select max(e.amount) from Expense e")
  Double findLargestExpense();

  @Query("select e from Expense e where e.category=:category")
  List<Expense> findAllWithCategory(@Param("category") Category category);

  @Query("select e from Expense e where :tag member e.tags")
  List<Expense> findAllWhereTag(@Param("tag") Tag tag);
}
