package com.example.expensemanagerrest.repository;

import com.example.expensemanagerrest.model.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Ovidiu on 25-Jan-19.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

  void deleteByIdIn(List<Long> list);

  Optional<Tag> findByName(String name);
}
