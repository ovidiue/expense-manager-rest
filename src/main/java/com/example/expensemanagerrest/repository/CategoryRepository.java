package com.example.expensemanagerrest.repository;

import com.example.expensemanagerrest.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Ovidiu on 19-Jan-19.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
