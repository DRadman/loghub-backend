package net.decodex.loghub.backend.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.decodex.loghub.backend.annotations.IsEnum;

public class EnumValidator implements ConstraintValidator<IsEnum, Enum<?>> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(IsEnum annotation) {
        this.enumClass = annotation.value();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are considered valid
        }

        // Check if the value is one of the enum constants
        for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.equals(value)) {
                return true; // Value is a valid enum constant
            }
        }

        return false; // Value is not a valid enum constant
    }
}