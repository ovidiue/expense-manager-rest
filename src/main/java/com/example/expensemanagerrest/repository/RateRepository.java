package com.example.expensemanagerrest.repository;

import com.example.expensemanagerrest.model.Rate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Ovidiu on 26-Jan-19.
 */
@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

  void deleteByIdIn(List<Long> list);

  List<Rate> findAllByExpense_Id(Long id);

}
