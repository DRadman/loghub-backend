package net.decodex.loghub.backend.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import net.decodex.loghub.backend.annotations.validators.SecurePasswordValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SecurePasswordValidator.class)
@Documented
public @interface SecurePassword {

    String message() default "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 8 characters long";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}