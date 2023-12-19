package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

@Component
public class SetExperimentGroups implements Action {
  private final LocalDate FIVE_YEARS_AGO = LocalDate.now().minusYears(5);

  public enum ExperimentGroup {
    CONTROL,
    LINK,
    APPLY
  }

  @Override
  public void run(Submission submission) {
    if (isEligibleForExperiment(submission)) {
      Random rand = new Random();
      int group = rand.nextInt(3);
      switch (group) {
        case 0 -> submission.getInputData().put("experimentGroup", ExperimentGroup.CONTROL);
        case 1 -> submission.getInputData().put("experimentGroup", ExperimentGroup.LINK);
        case 2 -> submission.getInputData().put("experimentGroup", ExperimentGroup.APPLY);
      }
    }
  }

  private boolean isEligibleForExperiment(Submission submission) {
    // Someone in household is pregnant
    var pregnancies = (List) submission.getInputData().getOrDefault("pregnancies[]", emptyList());
    if (!pregnancies.isEmpty()) {
      return true;
    }

    // Has child under 5
    var household = submission.getInputData().get("household");
    if (household != null) {
      for (Map<String, Object> member : ((List<Map<String, Object>>) household)) {
        if ("child".equals(member.get("householdMemberRelationship"))) {
          var birthday = Stream.of("householdMemberBirthYear","householdMemberBirthMonth", "householdMemberBirthDay")
              .map(key -> (String)member.get(key))
              .reduce((e, c) -> e + "-" + c)
              .get();
          LocalDate birthdate = LocalDate.parse(birthday);
          if (birthdate.isAfter(FIVE_YEARS_AGO)) {
            return true;
          }
        }
      }
    }

    return false;
  }
}
