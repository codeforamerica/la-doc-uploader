package org.ladocuploader.app.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import formflow.library.data.Submission;
import formflow.library.data.UserFile;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.testutils.TestUtils;

class SubmissionUtilitiesTest {

    @Test
    public void testFileNameGeneration() {
        // Mock objects
        Submission submission = new Submission();
        UserFile userFile = new UserFile();
        UUID uuid = UUID.randomUUID();
        userFile.setFileId(uuid);
        userFile.setOriginalName("foo.pdf");
        userFile.setDocTypeLabel("test");
        submission.setInputData(Map.of("documentOwner_wildcard_" + uuid, "John Doe"));
        submission.setSubmittedAt(TestUtils.makeOffsetDateTime("2024-04-18"));
        
        int currentFileCount = 1;
        int totalFiles = 3;

        // Expected output

        // Call the method to generate the file name
        String generatedFileName = SubmissionUtilities.createFileNameForUploadedDocument(submission, userFile, currentFileCount, totalFiles);

        // Assert the output matches the expected result
        assertThat(generatedFileName).isEqualTo("John_Doe_test_1_of_3_041820240300.pdf");
    }
}