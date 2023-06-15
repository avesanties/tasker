package org.avesanties.tasker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ChronologicalOrderViolationException extends Exception {
  public ChronologicalOrderViolationException() {
    super();
  }
}
