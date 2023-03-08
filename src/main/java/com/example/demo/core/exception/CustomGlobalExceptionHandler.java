package com.example.demo.core.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@AllArgsConstructor
public class CustomGlobalExceptionHandler {
  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseError handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
    return new ResponseError().setTimeStamp(LocalDate.now())
                              .setErrors(ex.getBindingResult()
                                           .getFieldErrors()
                                           .stream()
                                           .collect(Collectors.toMap(FieldError::getField,
                                               DefaultMessageSourceResolvable::getDefaultMessage)))
                              .build();
  }

  @ExceptionHandler({NoSuchElementException.class})
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ResponseError handleNoSuchElement() {
    Map<String, String> errors = new HashMap<>();
    errors.put("element", "Element wurde nicht gefunden");
    return new ResponseError().setTimeStamp(LocalDate.now())
                              .setErrors(errors)
                              .build();
  }

  @ExceptionHandler({UsernameNotFoundException.class})
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ResponseError handleUsernameNotFound(Throwable e) {
    Map<String, String> errors = new HashMap<>();
    errors.put("username", String.format("Email %s wurde nicht gefunden", e.getMessage()));
    return new ResponseError().setTimeStamp(LocalDate.now())
                              .setErrors(errors)
                              .build();
  }

  @ExceptionHandler({HttpMessageNotReadableException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseError handleHttp(Throwable e) {
    Map<String, String> errors = new HashMap<>();
    errors.put("status", e.getMessage());
    return new ResponseError().setTimeStamp(LocalDate.now())
                              .setErrors(errors)
                              .build();
  }

  @ExceptionHandler({AccessDeniedException.class})
  @ResponseStatus(value = HttpStatus.FORBIDDEN)
  public ResponseError handleFailedPreauthorize(Throwable e) {
    Map<String, String> errors = new HashMap<>();
    errors.put("no access to resource", e.getMessage());
    return new ResponseError().setTimeStamp(LocalDate.now())
            .setErrors(errors)
            .build();
  }

  @ExceptionHandler({MultipartException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseError handleMultipartException(Throwable e) {
    Map<String, String> errors = new HashMap<>();
    errors.put("multipart", e.getMessage());
    return new ResponseError().setTimeStamp(LocalDate.now())
                              .setErrors(errors)
                              .build();
  }

  @ExceptionHandler({FileNotFoundException.class})
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ResponseError handleFileNotFound(Throwable e) {
    Map<String, String> errors = new HashMap<>();
    errors.put("file", e.getMessage());
    return new ResponseError().setTimeStamp(LocalDate.now())
                              .setErrors(errors)
                              .build();
  }

  @ExceptionHandler({IOException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseError handleIOException(Throwable e) {
    Map<String, String> errors = new HashMap<>();
    errors.put("ioException", e.getMessage());
    return new ResponseError().setTimeStamp(LocalDate.now())
                              .setErrors(errors)
                              .build();
  }
}


