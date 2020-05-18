package de.hsrm.mi.web.bratenbank.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class GuteAdresseValidator implements ConstraintValidator<GuteAdresse, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Pattern.matches("^[\\p{L}+ ]+[0-9]+, [0-9][0-9][0-9][0-9][0-9] [\\p{L}+ ]+$", value);
    }
    
}
