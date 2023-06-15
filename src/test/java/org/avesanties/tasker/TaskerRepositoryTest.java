package org.avesanties.tasker;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.avesanties.tasker.task.States;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

class TaskerRepositoryTest extends TaskerApplicationTestConfiguration {

  @Test
  void whenFilterByDateOrderByAsc_thenOk() {
    createTasksDone();
    createTasksTodo();

    assertAll(
        () -> assertEquals(3, taskRepository.filterByDateOrderByAsc(LocalDate.now(),
            LocalDate.now().plusDays(100L), Sort.by("name")).size()),
        () -> assertEquals(6, taskRepository.filterByDateOrderByAsc(LocalDate.now().minusDays(100L),
            LocalDate.now().plusDays(100L), Sort.by("name")).size()),
        () -> assertEquals(4, taskRepository.filterByDateOrderByAsc(LocalDate.now().minusDays(3L),
            LocalDate.now().plusDays(10L), Sort.by("name")).size()),
        () -> assertEquals(1, taskRepository
            .filterByDateOrderByAsc(LocalDate.now(), LocalDate.now(), Sort.by("name")).size()),
        () -> assertEquals(0, taskRepository
            .filterByDateOrderByAsc(LocalDate.now(), LocalDate.now().minusDays(5L), Sort.by("name"))
            .size()));
  }

  @Test
  void whenFilterByDateAndStateOrderByAsc_thenOk() {
    createTasksDone();
    createTasksTodo();

    assertAll(
        () -> assertEquals(3, taskRepository.filterByDateAndStateOrderByAsc(States.DONE,
                LocalDate.now().minusDays(100L), LocalDate.now().plusDays(100L), Sort.by("name"))
            .size()),
        () -> assertEquals(3, taskRepository.filterByDateAndStateOrderByAsc(States.TODO,
                LocalDate.now().minusDays(100L), LocalDate.now().plusDays(100L), Sort.by("name"))
            .size()));
  }
}
