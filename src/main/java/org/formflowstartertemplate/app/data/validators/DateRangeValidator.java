package org.formflowstartertemplate.app.data.validators;

import lombok.extern.slf4j.Slf4j;
import org.formflowstartertemplate.app.inputs.LaDocUpload;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
@Slf4j
public class DateRangeValidator implements ConstraintValidator<DateWithinRange, LaDocUpload> {
    @Override
    public void initialize(DateWithinRange constraintAnnotation){
    }
    @Override
    public boolean isValid(LaDocUpload laDocUpload, ConstraintValidatorContext context){

        log.info("CHECKING IF THE DATE IS VALID");

        LocalDate inputDate = LocalDate.of(Integer.parseInt(laDocUpload.getBirthDateYear()),
                Integer.parseInt(laDocUpload.getBirthDateMonth()),
                Integer.parseInt(laDocUpload.getBirthDateYear())
        );


        LocalDate minimumDate = LocalDate.of(1900, 1, 1);

        boolean isValid = (inputDate.isBefore(LocalDate.now()) && inputDate.isAfter(minimumDate));

        if ( !isValid ){
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate() )
                    .addPropertyNode( "birthDate" )
                    .addConstraintViolation();

        }

        return false;

    }
}
