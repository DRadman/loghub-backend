package net.decodex.loghub.backend.exceptions.handlers;

import net.decodex.loghub.backend.exceptions.specifications.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errorMessage.append(fieldErrorToString((FieldError) error)).append("; ");
        });
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(this::fieldErrorToString).toList();
        var response = new ExceptionResponse(HttpStatus.BAD_REQUEST, errorMessage.toString(), ex.getLocalizedMessage(), errors);
        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleBindException(BindException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errorMessage.append(fieldErrorToString((FieldError) error)).append("; ");
        });
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(this::fieldErrorToString).toList();
        var response = new ExceptionResponse(HttpStatus.BAD_REQUEST, errorMessage.toString(), ex.getLocalizedMessage(), errors);
        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }

    private String fieldErrorToString(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }
}