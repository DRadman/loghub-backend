package net.decodex.loghub.backend.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import net.decodex.loghub.backend.annotations.validators.MongoIdValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MongoIdValidator.class)
public @interface IsMongoId {
    String message() default "Invalid MongoDB ObjectId";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}