package com.example.expensemanagerrest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by Ovidiu on 26-Jan-19.
 */
@Entity
@Data
@Slf4j
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
/*@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id",
    scope = Long.class)*/
public class Expense {

  @Column
  @NotNull(message = "Required")
  @NotEmpty(message = "Required")
  private String title;
  @Column(columnDefinition = "clob")
  @Lob
  private String description;
  @Column
  private boolean recurrent;
  @Column
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private Date createdOn;
  @Column
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private Date dueDate;
  @Column
  @NotNull(message = "Required: must be a number")
  private Double amount;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  private Double payed;
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
  private List<Tag> tags;
  @ManyToOne
  private Category category;


  public Expense() {
    this.createdOn = new Date();
    this.payed = 0.0;
  }

  public void addRate(Rate rate) {
    this.payed += rate.getAmount();
  }

  public void removeRate(Rate rate) {
    this.payed -= rate.getAmount();
  }


}
