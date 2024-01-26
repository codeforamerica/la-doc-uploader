package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.utils.HouseholdUtilities;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
public class AdultsProvidingSupportConverter<T, I> extends AbstractBeanField<T, I> {

    @Override
    protected Object convert(String value)  {
        return value;
    }

    @Override
    protected String convertToWrite(Object value) {
        List<Map<String, Object>> householdList = (List) value;
        String result = "";
        if (householdList != null) {

            List<Map<String, Object>> householdAdults = householdList
                    .stream()
                    .filter(
                        member -> HouseholdUtilities.isMember18orOlder(
                                Integer.parseInt((String)member.get("householdMemberBirthDay")),
                                Integer.parseInt((String)member.get("householdMemberBirthMonth")),
                                Integer.parseInt((String)member.get("householdMemberBirthYear")))
                    )
                    .toList();

//            for (Map<String, Object> member : householdList) {
//                int birthDay = 0;
//                int birthMonth = 0;
//                int birthYear = 0;
//                boolean is18orOlder = false;
//                boolean providesFinancialSupport = false;
//                try {
//                    birthDay = Integer.parseInt((String)member.get("householdMemberBirthDay"));
//                    birthMonth = Integer.parseInt((String)member.get("householdMemberBirthMonth"));
//                    birthYear = Integer.parseInt((String)member.get("householdMemberBirthYear"));
//                    providesFinancialSupport = HouseholdUtilities.doesMemberProvideFinancialSupport(member);
//                    is18orOlder = HouseholdUtilities.isMember18orOlder(birthDay, birthMonth, birthYear);
//
//                } catch (Exception e){
//                    log.error("unable to work with household member");
//                }
//            }
        } else {
            log.warn("household is null");
        }
        return "";
    }
}
