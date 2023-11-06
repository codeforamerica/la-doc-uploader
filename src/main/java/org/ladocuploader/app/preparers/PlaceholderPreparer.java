package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlaceholderPreparer implements SubmissionFieldPreparer {

//    public List<String> getPreparerTypes() {return List.of("parentGuardian");}

    @Override
    public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
        var fields = new HashMap<String, SubmissionField>();

        fields.put("active", new SingleField("active", "TRUE", null) );
        fields.put("middle_name", new SingleField("middle_name", "", null));
        fields.put("reference_id", new SingleField("reference_id", "", null));

        return fields;
    }
}
