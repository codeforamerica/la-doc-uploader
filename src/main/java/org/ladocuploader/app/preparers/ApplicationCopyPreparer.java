package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@Component
public class ApplicationCopyPreparer implements SubmissionFieldPreparer {

    @Override
    public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
        Map<String, SubmissionField> results = new HashMap<>();

        List<String> applicationCopySendMethods = (List<String>) submission.getInputData().getOrDefault("copyReceiveMethod[]", emptyList());

        // Convert "who does this apply to" responses to a boolean indicator
        results.put("paperCopyInd", new SingleField("paperCopyInd", Boolean.toString(applicationCopySendMethods.contains("Paper")), null));
        results.put("electronicCopyInd", new SingleField("electronicCopyInd", Boolean.toString(applicationCopySendMethods.contains("Electronic")), null));


        return results;
    }
}
