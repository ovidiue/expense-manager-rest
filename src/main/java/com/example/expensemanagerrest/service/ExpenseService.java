package com.example.expensemanagerrest.service;

import com.example.expensemanagerrest.model.Category;
import com.example.expensemanagerrest.model.Expense;
import com.example.expensemanagerrest.model.Tag;
import com.example.expensemanagerrest.model.filters.ExpenseFilter;
import com.example.expensemanagerrest.model.stats.ExpenseSimplified;
import com.example.expensemanagerrest.model.stats.ExpenseStats;
import com.example.expensemanagerrest.repository.ExpenseRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

  public int countWithCategory(Category category) {
    return this.expenseRepository.countAllByCategory(category);
  }

  public List<Expense> findAllWithCategory(Category category) {
    return expenseRepository.findAllWithCategory(category);
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

  public Double getLargestExpense() {
    return this.expenseRepository.findLargestExpense();
  }

  @Transactional
  public void deleteExpenses(List<Long> list) {
    this.expenseRepository.deleteByIdIn(list);
  }

  public List<Expense> findAllWithIdIn(List<Long> ids) {
    return this.expenseRepository.findByIdIn(ids);
  }

  public List<Expense> findAllWithCategoryInList(List<Category> categories) {
    return this.expenseRepository.findAllByCategoryIn(categories);
  }

  public Page<Expense> findAll(ExpenseFilter filter, Pageable pageable) {
    log.info("\nfindAll with filter called {}", filter);
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
    if (pageable.getSort().get().count() > 0) {
      query.orderBy(getOrderByExpression(criteriaBuilder, r, pageable));
    }
    TypedQuery<Expense> typedQuery = this.em.createQuery(query);
    log.info("\nPageable {}", pageable);
    log.info("\nPageable size {}", pageable.getPageSize());
    log.info("\nPageable nr {}", pageable.getPageNumber());
    int totalRows = typedQuery.getResultList().size();
    typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
    typedQuery.setMaxResults(pageable.getPageSize());

    Page<Expense> page = new PageImpl<Expense>(typedQuery.getResultList(), pageable,
        totalRows);
    log.info("\nPAGE {}", page.getNumber());
    return page;
  }

  public List<Expense> findAllWhereTagsIdIn(List<Tag> tags) {
    return this.expenseRepository.findDistinctByTagsIn(tags);
  }

  private Order getOrderByExpression(CriteriaBuilder cb, Root<Expense> r, Pageable pageable) {
    String field = pageable.getSort().get().findFirst().get().getProperty();
    String direction = pageable.getSort().equals(new Sort(Direction.ASC, field)) ? "asc" : "desc";
    Expression expression = r.get(field);
    if (direction.equalsIgnoreCase("asc")) {
      return cb.asc(expression);
    } else {
      return cb.desc(expression);
    }

  }

  public List<Expense> findAllThatHaveTag(Tag tag) {
    return expenseRepository.findAllWhereTag(tag);
  }

  public List<ExpenseSimplified> findAllSimple() {
    List<ExpenseSimplified> result = new ArrayList<>();
    return this.expenseRepository.findAll().stream()
        .map(ex -> new ExpenseSimplified(ex.getTitle(), ex.isRecurrent(), ex.getCreatedOn(),
            ex.getDueDate(), ex.getAmount(), ex.getId(), ex.getPayed(), ex.getCreatedOn().getMonth()))
        .collect(Collectors.toList());
  }

  public ExpenseStats getStatsInfo() {
    List<ExpenseSimplified> expenses = this.findAllSimple();
    double min = expenses.stream().mapToDouble(ex -> ex.getAmount()).min().orElse(0);
    double max = expenses.stream().mapToDouble(ex -> ex.getAmount()).max().orElse(0);
    double averageReccurent = expenses.stream()
        .filter(ex -> ex.isRecurrent() == true)
        .mapToDouble(ex -> ex.getAmount()).average()
        .orElse(0);
    double averageNonReccurent = expenses.stream()
        .filter(ex -> ex.isRecurrent() == false)
        .mapToDouble(ex -> ex.getAmount()).average()
        .orElse(0);
    long noOfExpenses = expenses.stream().count();
    long partialPayed = expenses.stream()
        .filter(ex -> {
          return ex.getPayed() != 0.0 && Double.compare(ex.getAmount(), ex.getPayed()) != 0;
        }).count();
    long payed = expenses.stream()
        .filter(ex -> {
          return Double.compare(ex.getAmount(), ex.getPayed()) == 0;
        }).count();

    return new ExpenseStats(noOfExpenses, max, min, averageNonReccurent, averageReccurent,
        partialPayed, payed);

  }

}
