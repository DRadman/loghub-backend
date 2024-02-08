package net.decodex.loghub.backend.exceptions.specifications;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
public class OrganizationNotPresentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public OrganizationNotPresentException() {
        super("Organization not present");
    }
}
