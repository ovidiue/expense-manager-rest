package com.example.expensemanagerrest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Category {

  private static final String DEFAULT_COLOR = "#fff";
  @Column
  @NotNull(message = "Required")
  @NotEmpty(message = "Required")
  private String name;
  @Column(columnDefinition = "clob")
  @Lob
  private String description;
  @Column
  private String color;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
}