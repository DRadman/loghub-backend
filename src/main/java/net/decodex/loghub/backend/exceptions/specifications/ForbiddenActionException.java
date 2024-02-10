package net.decodex.loghub.backend.exceptions.specifications;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.FORBIDDEN)
public class ForbiddenActionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ForbiddenActionException(String infoError) {
		super(infoError);
	}

	
}
