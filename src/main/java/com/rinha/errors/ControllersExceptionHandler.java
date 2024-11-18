package com.rinha.errors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllersExceptionHandler {

  private ResponseEntity<ExceptionDTO> createExceptionResponse(String message, HttpStatus status) {
    ExceptionDTO response = new ExceptionDTO(message, status.value());

    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ExceptionDTO> handleEntityNotFound(EntityNotFoundException exception) {

    return createExceptionResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ExceptionDTO> handleBadRequest(BadRequestException exception) {

    return createExceptionResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ExceptionDTO> handleRuntimeException(RuntimeException exception) {

    return createExceptionResponse("Internal server error: " + exception.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionDTO> handleGenericException(Exception exception) {

    return createExceptionResponse("An unexpected error occurred. Please try again later.",
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
