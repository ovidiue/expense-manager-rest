package com.example.expensemanagerrest.web.controller;

import com.example.expensemanagerrest.model.Expense;
import com.example.expensemanagerrest.model.Tag;
import com.example.expensemanagerrest.model.stats.CategoryStats;
import com.example.expensemanagerrest.service.ExpenseService;
import com.example.expensemanagerrest.service.TagService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ovidiu on 25-Jan-19.
 */
@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class TagController {

  @Autowired
  private TagService tagService;

  @Autowired
  private ExpenseService expenseService;

  @GetMapping("/tags")
  public ResponseEntity<Page<Tag>> getTags(Pageable pageable) {
    Page<Tag> tags = tagService.findAll(pageable);
    return ResponseEntity.ok(tags);
  }

  @GetMapping("/tags/{tagId}")
  public Tag getTag(@PathVariable Long tagId) {
    log.info("tagId {}", tagId);
    Tag tag = this.tagService.getTag(tagId);
    log.info("Tag {}", tag);
    return tag;
  }

  @PostMapping("/tags/save")
  public void saveTag(@RequestBody Tag tag) {
    this.tagService.saveTag(tag);
  }

  @PostMapping("/tags/delete")
  public ResponseEntity deleteTags(@RequestBody List<Tag> list) {
    List<Expense> result = this.expenseService.findAllWhereTagsIdIn(list);
    log.info("\nRESULT {}", result);
    this.expenseService.findAllWhereTagsIdIn(list)
        .forEach(expense -> {
          log.info("\n\texpense {}", expense);
          List<Tag> toRemove = expense.getTags()
              .stream()
              .filter(tag -> list.contains(tag))
              .collect(Collectors.toList());
          log.info("\n\ttoRemove {}", toRemove);
          expense.getTags().removeAll(toRemove);
          this.expenseService.saveExpense(expense);
        });
    this.tagService.deleteTags(
        list.stream()
            .map(Tag::getId)
            .collect(Collectors.toList()));
    return ResponseEntity.ok(list);
  }

  @GetMapping("/tags/name/{name}")
  public Optional<Tag> getTagByName(@PathVariable String name) {
    return this.tagService.findByName(name);
  }

  @GetMapping("/tags/tag-info")
  public ResponseEntity<List<CategoryStats>> getTagInfo() {
    List<CategoryStats> stats = this.tagService.getTagInfo();
    return ResponseEntity.ok(stats);
  }

}
