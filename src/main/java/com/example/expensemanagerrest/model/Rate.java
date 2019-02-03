package com.example.expensemanagerrest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by Ovidiu on 26-Jan-19.
 */
@Entity
@Data
@ToString(exclude = "expense")
/*@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id",
    scope = Long.class)*/
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Rate {

  @Column
  @NotNull(message = "Required: must be a number")
  private Double amount;
  @Column
  private Date creationDate;
  @Column(columnDefinition = "clob")
  @Lob
  private String observation;
  @Column
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private Date payedOn;
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "expense_id")
  private Expense expense;

  public Rate() {
    this.creationDate = new Date();
  }
}
