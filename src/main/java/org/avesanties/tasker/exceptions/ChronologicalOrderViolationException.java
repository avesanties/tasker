package org.avesanties.tasker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ChronologicalOrderViolationException extends Exception {
	public ChronologicalOrderViolationException() {
		super();
	}
	
	public ChronologicalOrderViolationException(String message) {
		super(message);
	}
	
	public ChronologicalOrderViolationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ChronologicalOrderViolationException(Throwable cause) {
		super(cause);
	}
}