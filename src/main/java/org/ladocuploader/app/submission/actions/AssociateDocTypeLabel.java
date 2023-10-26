package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import formflow.library.data.UserFile;
import formflow.library.data.UserFileRepositoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AssociateDocTypeLabel implements Action {

    @Autowired
    UserFileRepositoryService userFileRepositoryService;

    @Override
    public void run(Submission submission){
        Map<String, Object> docTypeEntries = submission.getInputData().entrySet().stream()
                .filter(entry -> entry.getKey().contains("docTypeWildcard_"))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        docTypeEntries.forEach((inputName, inputValue) -> {
            String fileId = StringUtils.substringAfterLast(inputName, "docTypeWildcard_");
            Optional<UserFile> maybeUserFile = userFileRepositoryService.findById(UUID.fromString(fileId));
            if (maybeUserFile.isPresent()){
                UserFile userFile = maybeUserFile.get();
                userFile.setDocTypeLabel(String.valueOf(inputValue));
                userFileRepositoryService.save(userFile);
                submission.getInputData().remove(inputName);
            }

        });

    }
}
