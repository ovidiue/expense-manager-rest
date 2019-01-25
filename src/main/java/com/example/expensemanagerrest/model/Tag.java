package com.example.expensemanagerrest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Ovidiu on 25-Jan-19.
 */
@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Transactional
public class Tag {

  @Column
  @NotNull(message = "Required")
  @NotEmpty(message = "Required")
  private String name;
  @Column
  private String color;
  @Column(columnDefinition = "clob")
  @Lob
  private String description;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

}
