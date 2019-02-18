package com.example.expensemanagerrest.service;

import com.example.expensemanagerrest.model.Tag;
import com.example.expensemanagerrest.repository.TagRepository;
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

}
