package org.avesanties.tasker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidTaskParametersException extends Exception {
	
	public InvalidTaskParametersException() {
		super();
	}
	
	public InvalidTaskParametersException(String message) {
		super(message);
	}
	
	public InvalidTaskParametersException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidTaskParametersException(Throwable cause) {
		super(cause);
	}
}
