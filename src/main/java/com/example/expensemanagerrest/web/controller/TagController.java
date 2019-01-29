package com.example.expensemanagerrest.web.controller;

import com.example.expensemanagerrest.model.Tag;
import com.example.expensemanagerrest.service.TagService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

  @GetMapping("/tags")
  public List<Tag> gettags() {
    List<Tag> tags = tagService.findAll();
    //return ResponseEntity.status(HttpStatus.OK).body(tags);
    return tags;
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
  public ResponseEntity<String> deletetags(@RequestBody List<Long> list) {
    this.tagService.deleteTags(list);
    return new ResponseEntity<String>("Deleted", HttpStatus.OK);
  }

  @GetMapping("/tags/name/{name}")
  public Optional<Tag> getTagByName(@PathVariable String name) {
    return this.tagService.findByName(name);
  }

}
