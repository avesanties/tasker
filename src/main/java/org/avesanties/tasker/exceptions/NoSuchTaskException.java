package org.avesanties.tasker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoSuchTaskException extends Exception {
  public NoSuchTaskException(String message) {
    super(message);
  }
}
