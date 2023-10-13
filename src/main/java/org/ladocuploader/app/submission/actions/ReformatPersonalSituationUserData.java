package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Slf4j
public class ReformatPersonalSituationUserData implements Action {

  @Override
  public void run(Submission submission) {
    Map<String, Object> inputData = submission.getInputData();

    submission.getInputData().put("affectedByPersonalSituations[]", reformatPersonalSituations(inputData));
  }

  private ArrayList<LinkedHashMap> reformatPersonalSituations(Map<String, Object> inputData) {
    ArrayList<LinkedHashMap> householdMembers = (ArrayList) inputData.get("household");
    ArrayList<String> hasPersonalSituations = (ArrayList) inputData.get("personalSituationsHouseholdUUID[]");
    ArrayList<LinkedHashMap> personalSituationsObject = new ArrayList<>();

    hasPersonalSituations.forEach((String id) -> {
          LinkedHashMap user = new LinkedHashMap();
          if (id.equals("you")) {
            user.put("uuid", id);
            user.put("firstName", inputData.get("firstName") + " (you)");
            user.put("lastName", inputData.get("lastName"));
            personalSituationsObject.add(user);
          } else {
            personalSituationsObject.add(householdData(householdMembers, id));
          }
        }
    );

   return personalSituationsObject;

  }

  private LinkedHashMap householdData(ArrayList<LinkedHashMap> household, String uuid) {
    LinkedHashMap user = new LinkedHashMap();
    for (LinkedHashMap hhmember : household) {
      if (hhmember.get("uuid").equals(uuid)) {
        user.put("uuid", uuid);
        user.put("firstName", hhmember.get("householdMemberFirstName"));
        user.put("lastName", hhmember.get("householdMemberLastName"));
        continue;
      }
    }

    return user;
  };

}
