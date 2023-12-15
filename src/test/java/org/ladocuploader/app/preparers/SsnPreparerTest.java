package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;
import org.ladocuploader.app.submission.StringEncryptor;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class SsnPreparerTest {

    private StringEncryptor encryptor;
    private SsnPreparer preparer;

    @BeforeEach
    public void setUp() {
        encryptor = Mockito.mock(StringEncryptor.class);
        preparer = new SsnPreparer(encryptor);
    }

    @Test
    public void testNoSSNs() {
        Submission submission = new Submission();
        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);

        assertThat(result).isEmpty();
    }

    @Test
    public void testApplicantSSN() {
        Submission submission = new SubmissionTestBuilder()
                .withPersonalInfo("","","","","", "","","","",
                        "somethingencrypted")
                .build();
        when(encryptor.decrypt(eq("somethingencrypted"))).thenReturn("111223333");
        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get("applicantSsn")).isEqualTo(new SingleField("applicantSsn", "111223333", null));
    }

    @Test
    public void testHouseholdSSNs() {
        Submission submission = new SubmissionTestBuilder()
            .withPersonalInfo("applicant","test","","","", "","","","",
                "")
            .withHouseholdMember("member1", "test", "", "", "", "", "", "", "",
                "somethingencrypted")
            .withHouseholdMember("member2", "test", "", "", "", "", "", "", "",
                "")
            .withHouseholdMember("member3", "test", "", "", "", "", "", "", "",
                "somethingelseencrypted")
            .build();
        when(encryptor.decrypt(eq("somethingencrypted"))).thenReturn("111223333");
        when(encryptor.decrypt(eq("somethingelseencrypted"))).thenReturn("444556666");

        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);

        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get("applicantSsn")).isEqualTo(new SingleField("applicantSsn", null, null));
        assertThat(result.get("ssns_1")).isEqualTo(new SingleField("ssns_1", "111223333", null));
        assertThat(result.get("ssns_2")).isEqualTo(new SingleField("ssns_2", null, null));
        assertThat(result.get("ssns_3")).isEqualTo(new SingleField("ssns_3", "444556666", null));
    }
}