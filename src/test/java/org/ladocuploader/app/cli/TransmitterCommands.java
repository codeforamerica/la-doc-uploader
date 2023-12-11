package org.ladocuploader.app.cli;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.csv.CsvService;
import org.ladocuploader.app.csv.enums.CsvPackageType;
import org.ladocuploader.app.data.Transmission;
import org.ladocuploader.app.data.enums.TransmissionStatus;
import org.ladocuploader.app.data.enums.TransmissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
public class TransmitterCommands {

    @Autowired
    TransmitterCommands transmitterCommands;

    @Autowired
    SubmissionRepository submissionRepository;

    @MockBean
    CsvService csvService;

    Submission submission;

    List<Submission> submissionList;

    @BeforeEach
    void setup() {
        submission = Submission.builder()
                .submittedAt(now())
                .urlParams(new HashMap<>())
                .inputData(Map.of(
                        "firstName", "first",
                        "lastName", "last",
                        "signature", "first last"
                ))
                .build();
        submissionRepository.save(submission);
        Transmission transmission = Transmission.fromSubmission(submission);
        transmission.setTransmissionType(TransmissionType.ECE);
        transmission.setStatus(TransmissionStatus.Queued);
        submissionList.add(submission);
    }

    @Test
    @Ignore
    void transmitZipFile() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        transmitterCommands.transmitZipFile();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        File zipFile = new File("Apps__" + date + "__1001-1006.zip");
        assertTrue(zipFile.exists());
    }







}
