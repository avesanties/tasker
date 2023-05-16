package org.avesanties.tasker;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.avesanties.tasker.task.States;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

class TaskerRepositoryTest extends TaskerApplicationTestConfiguration {

	@Test
	void whenFilterByDateOrderByAsc_thenOK() {
		createTasksDone();
		createTasksTodo();

		assertAll(
				() -> assertEquals(taskRepository
						.filterByDateOrderByAsc(LocalDate.now(), LocalDate.now().plusDays(100L), Sort.by("name")).size(),
						3),
				() -> assertEquals(taskRepository.filterByDateOrderByAsc(LocalDate.now().minusDays(100L),
						LocalDate.now().plusDays(100L), Sort.by("name")).size(),
						6),
				() -> assertEquals(taskRepository.filterByDateOrderByAsc(LocalDate.now().minusDays(3l),
						LocalDate.now().plusDays(10L), Sort.by("name")).size(),
						4),
				() -> assertEquals(
						taskRepository.filterByDateOrderByAsc(LocalDate.now(), LocalDate.now(), Sort.by("name")).size(),
						1),
				() -> assertEquals(taskRepository
						.filterByDateOrderByAsc(LocalDate.now(), LocalDate.now().minusDays(5L), Sort.by("name")).size(),
						0));
	}

	@Test
	void whenFilterByDateAndStateOrderByAsc_thenOK() {
		createTasksDone();
		createTasksTodo();

		assertAll(
				() -> assertEquals(taskRepository.filterByDateAndStateOrderByAsc(States.DONE,
						LocalDate.now().minusDays(100L), LocalDate.now().plusDays(100L), Sort.by("name")).size(),
						3),
				() -> assertEquals(taskRepository.filterByDateAndStateOrderByAsc(States.TODO,
						LocalDate.now().minusDays(100L), LocalDate.now().plusDays(100L), Sort.by("name")).size(),
						3));
	}


}
