package org.formflowstartertemplate.app.data.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE_USE, TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy= { DateRangeValidator.class })
public @interface DateWithinRange{

    String message() default "Please make sure that the date of birth is between 1/1/1990 and today.";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
