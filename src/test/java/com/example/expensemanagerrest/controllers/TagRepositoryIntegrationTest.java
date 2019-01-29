package com.example.expensemanagerrest.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.expensemanagerrest.model.Tag;
import com.example.expensemanagerrest.repository.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Ovidiu on 29-Jan-19.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TagRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private TagRepository tagRepository;

  @Test
  public void whenFindByName_thenReturnEmployee() {
    // given
    Tag alex = new Tag();
    alex.setName("alex");
    entityManager.persist(alex);
    entityManager.flush();

    // when
    Tag found = tagRepository.findByName(alex.getName()).get();

    // then
    assertThat(found.getName())
        .isEqualTo(alex.getName());
  }

}
