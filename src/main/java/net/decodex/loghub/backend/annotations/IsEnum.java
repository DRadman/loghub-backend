package net.decodex.loghub.backend.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import net.decodex.loghub.backend.annotations.validators.EnumValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
@Documented
public @interface IsEnum {
    String message() default "Value is not a valid enum";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> value();
}