package org.avesanties.tasker.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue
  private Long id;

  @NotEmpty
  @Column(nullable = false)
  private String name;

  @Column
  private String description;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private States state;

  @NotNull
  @Column(nullable = false)
  private LocalDate date;

  public Task() {

  }

  public Task(String name, String description, @NotNull States state, @NotNull LocalDate date) {
    this.name = name;
    this.date = date;
    this.state = state;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public @NotNull States getState() {
    return state;
  }

  public void setState(@NotNull States state) {
    this.state = state;
  }

  public @NotNull LocalDate getDate() {
    return date;
  }

  public void setDate(@NotNull LocalDate date) {
    this.date = date;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Task [id=" + id + ", name=" + name + ", description=" + description + ", state=" + state
        + ", date=" + date + "]";
  }
}


