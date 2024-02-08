package net.decodex.loghub.backend.exceptions.specifications;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_ACCEPTABLE)
public class RegistrationDisabledException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegistrationDisabledException(String infoError) {
		super(infoError);
	}

	
}
