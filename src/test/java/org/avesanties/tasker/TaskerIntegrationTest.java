package org.avesanties.tasker;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.avesanties.tasker.task.States;
import org.avesanties.tasker.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.DefaultUriBuilderFactory;

class TaskerIntegrationTest extends TaskerApplicationTestConfiguration {

  private static final TestRestTemplate rest = new TestRestTemplate();

  private static final DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();

  private static final String HOST = "localhost";

  private static final String SCHEME = "http";

  @Autowired
  private ObjectMapper objectMapper;

  @LocalServerPort
  protected int port;

  @Test
  void whenGetAllTasks_thenOk() {
    URI uri = uriBuilderFactory.builder().scheme(SCHEME).host(HOST).port(port).path("/tasks")
        .path("/all").build();

    ResponseEntity<String> response = rest.getForEntity(uri, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void whenGetById_thenOk() {
    Task t = createOneTask();

    URI uri = uriBuilderFactory.builder().scheme(SCHEME).host(HOST).port(port).path("/tasks")
        .path("/" + t.getId()).build();

    ResponseEntity<Task> response = rest.getForEntity(uri, Task.class);
    assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
        () -> assertEquals(t.getName(), response.getBody().getName()));
  }

  @Test
  void whenGetById_thenNotFound() {
    URI uri = uriBuilderFactory.builder().scheme(SCHEME).host(HOST).port(port).path("/tasks")
        .path("/" + new Random().nextInt(100)).build();

    ResponseEntity<Task> response = rest.getForEntity(uri, Task.class);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void whenGetByPeriod_thenOk() throws JsonMappingException, JsonProcessingException {
    createTasksTodo();
    createTasksDone();

    URI uri = uriBuilderFactory.builder().scheme(SCHEME).host(HOST).port(port).path("/tasks")
        .queryParam("dateStart", LocalDate.now())
        .queryParam("dateEnd", LocalDate.now().plusDays(100L)).build();

    System.out.println(uri.toString());
    ResponseEntity<String> response = rest.getForEntity(uri, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    List<Task> tasks =
        objectMapper.readValue(response.getBody(), new TypeReference<List<Task>>() {});

    // amount of tasks created with createTasksTodo()
    assertEquals(3, tasks.size());
  }

  @Test
  void whenGetByPeriodAndState_thenOk() throws JsonMappingException, JsonProcessingException {
    createTasksTodo();
    createTasksDone();

    URI uri = uriBuilderFactory.builder().scheme(SCHEME).host(HOST).port(port).path("/tasks")
        .queryParam("dateStart", LocalDate.now().minusDays(100L))
        .queryParam("dateEnd", LocalDate.now().plusDays(100L)).queryParam("state", States.DONE)
        .build();
    System.out.println(uri.toString());
    ResponseEntity<String> response = rest.getForEntity(uri, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    List<Task> tasks =
        objectMapper.readValue(response.getBody(), new TypeReference<List<Task>>() {});

    // amount of tasks created with createTasksDone()
    assertEquals(3, tasks.size());
  }

  @Test
  void whenGetByPeriodAndState_thenBadRequest() {
    URI uri = uriBuilderFactory.builder().scheme(SCHEME).host(HOST).port(port).path("/tasks")
        .queryParam("dateStart", LocalDate.now().plusDays(100L))
        .queryParam("dateEnd", LocalDate.now().minusDays(100L)).build();

    ResponseEntity<String> response = rest.getForEntity(uri, String.class);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void whenCreateNewTask_thenCreated() {
    URI uri = uriBuilderFactory.builder().scheme(SCHEME).host(HOST).port(port).path("/tasks")
        .path("/new").build();

    Task task = new Task("Task #1", "important task #1", States.TODO, LocalDate.now());

    RequestEntity<Task> request =
        RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON).body(task);

    ResponseEntity<Task> response = rest.exchange(request, Task.class);
    assertAll(() -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
        () -> assertEquals(task.getName(), response.getBody().getName()));
  }

  @Test
  void whenCreateNewTask_thenBadRequest() {
    URI uri = uriBuilderFactory.builder().scheme(SCHEME).host(HOST).port(port).path("/tasks")
        .path("/new").build();

    Task task = new Task("", "important task #1", States.TODO, LocalDate.now());

    RequestEntity<Task> request =
        RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON).body(task);
    ResponseEntity<Task> response = rest.exchange(request, Task.class);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void whenUpdateTask_thenOk() {
    URI uri = uriBuilderFactory.builder().scheme(SCHEME).host(HOST).port(port).path("/tasks")
        .path("/1").build();

    Task task = new Task("Task #1", "important task #1", States.TODO, LocalDate.now());

    RequestEntity<Task> request =
        RequestEntity.put(uri).accept(MediaType.APPLICATION_JSON).body(task);
    ResponseEntity<Task> response = rest.exchange(request, Task.class);
    assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
        () -> assertEquals(task.getName(), response.getBody().getName()));
  }

  @Test
  void whenUpdateTask_thenBadRequest() {
    URI uri = uriBuilderFactory.builder().scheme(SCHEME).host(HOST).port(port).path("/tasks")
        .path("/1").build();

    Task task = new Task("", "important task #1", States.TODO, LocalDate.now());

    RequestEntity<Task> request =
        RequestEntity.put(uri).accept(MediaType.APPLICATION_JSON).body(task);
    ResponseEntity<Task> response = rest.exchange(request, Task.class);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void whenDeleteTask_thenOk() {
    URI uri = uriBuilderFactory.builder().scheme(SCHEME).host(HOST).port(port).path("/tasks")
        .path("/1").build();

    createOneTask();

    RequestEntity<Void> request = RequestEntity.delete(uri).build();

    ResponseEntity<Void> response = rest.exchange(request, Void.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
