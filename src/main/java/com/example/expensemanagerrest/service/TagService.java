package com.example.expensemanagerrest.service;

import com.example.expensemanagerrest.model.Expense;
import com.example.expensemanagerrest.model.Tag;
import com.example.expensemanagerrest.model.stats.CategoryStats;
import com.example.expensemanagerrest.repository.TagRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Ovidiu on 25-Jan-19.
 */
@Service
public class TagService {

  @Autowired
  private TagRepository tagRepository;

  @Autowired
  private ExpenseService expenseService;

  public Page<Tag> findAll(Pageable pageable) {
    return this.tagRepository.findAll(pageable);
  }

  public void saveTag(Tag tag) {
    this.tagRepository.save(tag);
  }

  public void deleteTag(Tag tag) {
    this.tagRepository.delete(tag);
  }

  public Tag getTag(Long tagId) {
    return this.tagRepository.getOne(tagId);
  }

  @Transactional
  public void deleteTags(List<Long> list) {
    this.tagRepository.deleteByIdIn(list);
  }

  public Optional<Tag> findByName(String name) {
    return this.tagRepository.findByName(name);
  }

  public List<CategoryStats> getTagInfo() {
    List<CategoryStats> result = new ArrayList();
    List<Tag> tags = this.findAll();

    tags.forEach(tag -> {
      List<Expense> expenses = this.expenseService.findAllThatHaveTag(tag);

      double total = expenses.stream().mapToDouble(e -> e.getAmount()).sum();
      double totalPayed = expenses.stream().mapToDouble(e -> e.getPayed()).sum();
      double min = expenses.stream().mapToDouble(e -> e.getAmount()).min().orElse(0);
      double max = expenses.stream().mapToDouble(e -> e.getAmount()).max().orElse(0);
      long recurrentCount = expenses.stream().filter(ex -> ex.isRecurrent() == true).count();
      long nonRecurrentCount = expenses.stream().filter(ex -> ex.isRecurrent() == false).count();
      long noOfExpenses = expenses.stream().count();
      long closed = expenses.stream().filter(ex -> {
        return Double.compare(ex.getPayed(), ex.getAmount()) == 0;
      }).count();

      result.add(new CategoryStats(tag.getName(),
          tag.getColor(),
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

  public List<Tag> findAll() {
    return tagRepository.findAll();
  }


}
