package com.rinha.errors;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllersExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ExceptionDTO> handleEntityNotFound(EntityNotFoundException exception) {
    ExceptionDTO response = new ExceptionDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ExceptionDTO> handleBadRequest(BadRequestException exception) {
    ExceptionDTO response = new ExceptionDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ExceptionDTO> handleRuntimeException(RuntimeException exception) {
    ExceptionDTO response = new ExceptionDTO("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ExceptionDTO> handleNoHandlerFound(NoHandlerFoundException exception) {
    ExceptionDTO response = new ExceptionDTO("Resource not found", HttpStatus.NOT_FOUND.value());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionDTO> handleGenericException(Exception exception) {
    ExceptionDTO response = new ExceptionDTO("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
