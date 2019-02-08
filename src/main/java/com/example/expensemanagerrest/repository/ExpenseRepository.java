package com.example.expensemanagerrest.repository;

import com.example.expensemanagerrest.model.Expense;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Ovidiu on 26-Jan-19.
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  void deleteByIdIn(List<Long> list);

  List<Expense> findByIdIn(List<Long> ids);

  List<Expense> findAllByCategoryId(List<Long> ids);
}
