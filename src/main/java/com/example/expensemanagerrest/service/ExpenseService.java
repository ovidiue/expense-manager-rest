package com.example.expensemanagerrest.service;

import com.example.expensemanagerrest.model.Expense;
import com.example.expensemanagerrest.model.Tag;
import com.example.expensemanagerrest.model.filters.ExpenseFilter;
import com.example.expensemanagerrest.repository.ExpenseRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Ovidiu on 26-Jan-19.
 */
@Slf4j
@Service
public class ExpenseService {

  @PersistenceContext
  EntityManager em;

  @Autowired
  private TagService tagService;

  @Autowired
  private ExpenseRepository expenseRepository;

  public List<Expense> findAll() {
    return this.expenseRepository.findAll();
  }

  public void saveExpense(Expense expense) {
    this.expenseRepository.save(expense);
  }

  public void deleteExpense(Expense expense) {
    this.expenseRepository.delete(expense);
  }

  public Expense getExpense(Long catId) {
    return this.expenseRepository.getOne(catId);
  }

  @Transactional
  public void deleteExpenses(List<Long> list) {
    this.expenseRepository.deleteByIdIn(list);
  }

  public List<Expense> findAllWithIdIn(List<Long> ids) {
    return this.expenseRepository.findByIdIn(ids);
  }

  public List<Expense> findAllWithCategoryIdIn(List<Long> ids) {
    return this.expenseRepository.findAllByCategoryId(ids);
  }

  public List<Expense> findAll(ExpenseFilter filter) {
    log.info("findAll with filter called {}", filter);
    CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
    CriteriaQuery<Expense> query = criteriaBuilder.createQuery(Expense.class);
    Root<Expense> r = query.from(Expense.class);
    query.select(r);

    List<Predicate> predicates = new ArrayList<>();

    if (filter.getTitle() != null) {
      predicates.add(
          criteriaBuilder.like(
              r.get("title"), "%" + filter.getTitle() + "%"
          ));
    }

    if (filter.getDueDateFrom() != null) {
      predicates.add(
          criteriaBuilder.greaterThanOrEqualTo(
              r.get("dueDate"), filter.getDueDateFrom()
          )
      );
    }

    if (filter.getDueDateTo() != null) {
      predicates.add(
          criteriaBuilder.lessThanOrEqualTo(
              r.get("dueDate"), filter.getDueDateTo()
          )
      );
    }

    if (filter.getAmountFrom() != null) {
      predicates.add(
          criteriaBuilder.greaterThanOrEqualTo(
              r.get("amount"), filter.getAmountFrom()
          )
      );
    }

    if (filter.getAmountTo() != null) {
      predicates.add(
          criteriaBuilder.lessThanOrEqualTo(
              r.get("amount"), filter.getAmountTo()
          )
      );
    }

    if (filter.getCreatedFrom() != null) {
      predicates.add(
          criteriaBuilder.greaterThanOrEqualTo(
              r.get("createdOn"), filter.getCreatedFrom()
          )
      );
    }

    if (filter.getCreatedTo() != null) {
      predicates.add(
          criteriaBuilder.lessThanOrEqualTo(
              r.get("createdOn"), filter.getCreatedTo()
          )
      );
    }

    if (filter.getDescription() != null) {
      predicates.add(
          criteriaBuilder.like(
              r.get("description"), "%" + filter.getDescription() + "%")
      );
    }

    if (filter.isRecurrent()) {
      predicates.add(
          criteriaBuilder.equal(
              r.get("recurrent"), filter.isRecurrent()
          )
      );
    }

    if (filter.getCategoryId() != null) {
      predicates.add(
          criteriaBuilder.equal(
              r.get("category").get("id"), filter.getCategoryId()
          )
      );
    }

    if (filter.getTagIds() != null && filter.getTagIds().size() > 0) {
      Expression<Collection<Tag>> tags = r.get("tags");
      filter.getTagIds().forEach(tagId -> {
        Tag tag = this.tagService.getTag(tagId);
        Predicate containsTag = criteriaBuilder.isMember(tag, tags);
        predicates.add(containsTag);
      });
    }

    query.select(r).where(predicates.toArray(new Predicate[]{}));
    return this.em.createQuery(query).getResultList();
  }

  public List<Expense> findAllWhereTagsIdIn(List<Tag> tags) {
    return this.expenseRepository.findDistinctByTagsIn(tags);
  }

}
