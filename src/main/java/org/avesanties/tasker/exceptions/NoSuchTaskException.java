package org.avesanties.tasker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoSuchTaskException extends Exception {
	
	public NoSuchTaskException() {
		super();
	}
	
	public NoSuchTaskException(String message) {
		super(message);
	}
	
	public NoSuchTaskException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoSuchTaskException(Throwable cause) {
		super(cause);
	}
}
