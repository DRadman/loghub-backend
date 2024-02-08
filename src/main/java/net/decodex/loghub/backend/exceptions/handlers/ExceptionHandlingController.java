package net.decodex.loghub.backend.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import net.decodex.loghub.backend.exceptions.specifications.*;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionHandlingController {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> resourceNotFoundException(ResourceNotFoundException ex) {
		ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND, ex.getMessage(),
				ex.getLocalizedMessage());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ExceptionResponse> resourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
		ExceptionResponse response = new ExceptionResponse(HttpStatus.CONFLICT, ex.getMessage(),
				ex.getLocalizedMessage());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ExceptionResponse> resourceNotFoundException(BadRequestException ex) {
		ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST, ex.getMessage(),
				ex.getLocalizedMessage());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ExceptionResponse> authenticationException(AuthenticationException ex) {
		ExceptionResponse response = new ExceptionResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(),
				ex.getLocalizedMessage());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(RegistrationDisabledException.class)
	public ResponseEntity<ExceptionResponse> registrationDisabledException(RegistrationDisabledException ex) {
		ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(),
				ex.getLocalizedMessage());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(OrganizationNotPresentException.class)
	public ResponseEntity<ExceptionResponse> organizationNotPresentException(OrganizationNotPresentException ex) {
		ExceptionResponse response = new ExceptionResponse(HttpStatus.FAILED_DEPENDENCY, ex.getMessage(),
				ex.getLocalizedMessage());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.FAILED_DEPENDENCY);
	}
}