package net.decodex.loghub.backend.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.decodex.loghub.backend.annotations.IsMongoId;
import org.bson.types.ObjectId;

public class MongoIdValidator implements ConstraintValidator<IsMongoId, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            new ObjectId(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}