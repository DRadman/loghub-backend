package net.decodex.loghub.backend.annotations.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.decodex.loghub.backend.annotations.SecurePassword;

import java.util.regex.Pattern;

public class SecurePasswordValidator implements ConstraintValidator<SecurePassword, String> {
    String PASSWORD_SPECIAL_CHARS = "@#$%^`<>&+=\"!ºª·#~%&'¿¡€,:;*/+-.=_\\[\\]\\(\\)\\|\\_\\?\\\\";
    int PASSWORD_MIN_SIZE = 8;
    String PASSWORD_REGEXP = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[" + PASSWORD_SPECIAL_CHARS + "])(?=\\S+$).{"+PASSWORD_MIN_SIZE+",}$";

    @Override
    public void initialize(SecurePassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && password.matches(PASSWORD_REGEXP);
    }
}