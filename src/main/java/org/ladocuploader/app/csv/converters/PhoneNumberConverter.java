package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import lombok.NoArgsConstructor;

/**
 * This converter will convert a phone number field from this format
 * <code>(123) 456-7890</code> to this <code>+1234567890</code> format.
 *
 */
@NoArgsConstructor
public class PhoneNumberConverter<T, I> extends AbstractBeanField<T, I> {

    @Override
    protected Object convert(String value)  {
        return value;
    }

    @Override
    protected String convertToWrite(Object value) {
        String phoneNumber = (String)value;

        phoneNumber = phoneNumber.replaceAll("\\(", "")
            .replaceAll("\\)", "")
            .replaceAll("\\W", "");
        phoneNumber = "+1" + phoneNumber;
        return phoneNumber;
    }

}
