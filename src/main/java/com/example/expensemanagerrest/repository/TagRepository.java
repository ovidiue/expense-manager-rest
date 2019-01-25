package com.example.expensemanagerrest.repository;

import com.example.expensemanagerrest.model.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Ovidiu on 25-Jan-19.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

  void deleteByIdIn(List<Long> list);
}
