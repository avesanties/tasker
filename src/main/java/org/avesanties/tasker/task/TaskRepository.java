package org.avesanties.tasker.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository <Task, Long> {
	
	@Query("SELECT t FROM Task t WHERE t.date >= :start AND t.date <= :end")
	List<Task> filterByDateOrderByAsc(@Param("start") LocalDate start, 
			@Param("end") LocalDate end, 
			Sort sort);
	
	@Query("SELECT t FROM Task t WHERE t.date >= :start AND t.date <= :end AND t.state = :state")
	List<Task> filterByDateAndStateOrderByAsc(@Param("state") States state, 
			@Param("start") LocalDate start, 
			@Param("end") LocalDate end, 
			Sort sort);
}
