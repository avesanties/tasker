package org.avesanties.tasker.task;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.avesanties.tasker.exceptions.ChronologicalOrderViolationException;
import org.avesanties.tasker.exceptions.InvalidTaskParametersException;
import org.avesanties.tasker.exceptions.NoSuchTaskException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;

@RestController
public class TaskController {
	
	private final TaskRepository tasks;
	
	public TaskController(TaskRepository taskRepository) {
		tasks = taskRepository;
	}
	
	@PostMapping("/tasks/new")
	@ResponseStatus(HttpStatus.CREATED)
	public Task createNewTask(@Valid @RequestBody Task task,
			BindingResult result) throws InvalidTaskParametersException {
		
		if(result.hasErrors())
			throw new InvalidTaskParametersException();
			
		return tasks.save(task);
		
	}
	
	@GetMapping("/tasks")
	public List<Task> getByPeriodAndState(@RequestParam(name = "dateStart") LocalDate dateStart, 
			@RequestParam(name = "dateEnd") LocalDate dateEnd,
			@RequestParam(name = "state", defaultValue = "UNDEFINED") States state) 
					throws ChronologicalOrderViolationException{
		
		if(dateStart.isAfter(dateEnd))
			throw new ChronologicalOrderViolationException();
		
		if(state == States.UNDEFINED)
			return tasks.filterByDateOrderByAsc(dateStart, dateEnd, Sort.by("name"));
		else
			return tasks.filterByDateAndStateOrderByAsc(state, dateStart, dateEnd, Sort.by("name"));
	}
	
	@GetMapping("/tasks/all")
	public List<Task> getAllTasks(){
		
		return tasks.findAll();
		
	}
	
	@GetMapping("/tasks/{id}")
	public Task getById(@PathVariable(name = "id") long id) 
			throws NoSuchTaskException {
		
		Optional<Task> task = tasks.findById(id);
		return task.orElseThrow(
				() -> new NoSuchTaskException("There is not task with id=" + id));
		
	}
	
	@PutMapping("/tasks/{id}")
	public Task updateTask(@PathVariable(name = "id") long id, 
			@Valid @RequestBody Task newTask,
			BindingResult result) throws InvalidTaskParametersException {
		
		if(result.hasErrors())
			throw new InvalidTaskParametersException();
		
		return tasks.findById(id).map(task -> {
			task.setName(newTask.getName());
			task.setDescription(newTask.getDescription());
			task.setState(newTask.getState());
			task.setDate(newTask.getDate());
			return tasks.save(task);
		}).orElseGet(() -> {
			return tasks.save(newTask);
			});
	}
	
	@DeleteMapping("/tasks/{id}")
	public void deleteTask(@PathVariable(name = "id") long id) {
		tasks.deleteById(id);
	}
	
	@GetMapping("/")
	public String index() {
		return "<!DOCTYPE html>\n"
				+ "<html>\n"
				+ "<head>\n"
				+ "<title>\n"
				+ "</title>\n"
				+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
				+ "</head>\n"
				+ "<body>\n"
				+ "<h1>Tasker</h1>\n"
				+ "<p>Simple REST service which supports basic CRUD operations.</p>\n"
				+ "</body>\n"
				+ "</html>\n";
	}
}
