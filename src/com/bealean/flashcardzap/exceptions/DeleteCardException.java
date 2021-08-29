package com.bealean.flashcardzap.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Specified Flashcard ID could not be deleted.")
public class DeleteCardException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
