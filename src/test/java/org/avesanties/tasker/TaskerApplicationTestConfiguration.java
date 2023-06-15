package org.avesanties.tasker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import org.avesanties.tasker.task.States;
import org.avesanties.tasker.task.Task;
import org.avesanties.tasker.task.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
abstract class TaskerApplicationTestConfiguration {

  @Autowired
  protected TaskRepository taskRepository;

  private static final String RDBMS_VERSION = "postgres:15";

  private static final String APP_DB = "app_db";

  private static final String POSTGRES_USERNAME = "root";

  private static final String POSTGRES_PASSWORD = "root";

  private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER;

  static {
    POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
        RDBMS_VERSION).withUsername(POSTGRES_USERNAME).withPassword(POSTGRES_PASSWORD)
        .withDatabaseName(APP_DB);

    POSTGRE_SQL_CONTAINER.start();
  }

  @DynamicPropertySource
  private static void registerPgProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
  }

  @BeforeEach
  void clearTaskRepository() {
    taskRepository.deleteAll();
  }

  protected Task createOneTask() {
    Task t = new Task("Task #1", "important task #1", States.TODO, LocalDate.now());
    taskRepository.save(t);
    return t;
  }

  protected void createTasksTodo() {
    Task t1 = new Task("Task #1", "important task #1", States.TODO, LocalDate.now());

    Task t2 = new Task("Task #2", "important task #2", States.TODO, LocalDate.now().plusDays(5L));

    Task t3 = new Task("Task #3", "important task #3", States.TODO, LocalDate.now().plusDays(10L));

    taskRepository.saveAll(Arrays.asList(t1, t2, t3));
  }

  protected void createTasksDone() {
    Task t1 = new Task("Task #4", "important task 4", States.DONE, LocalDate.now().minusDays(1L));

    Task t2 = new Task("Task #5", "important task #5", States.DONE, LocalDate.now().minusDays(5L));

    Task t3 = new Task("Task #6", "important task #6", States.DONE, LocalDate.now().minusDays(10L));

    taskRepository.saveAll(Arrays.asList(t1, t2, t3));
  }

  @Test
  void postgreSqlContainerIsUpAndRunning() {

    assertTrue(POSTGRE_SQL_CONTAINER.isRunning());

    Task task = new Task("Task #1", "important task", States.TODO, LocalDate.now());

    taskRepository.save(task);

    assertEquals(1, taskRepository.findAll().size());
  }
}
