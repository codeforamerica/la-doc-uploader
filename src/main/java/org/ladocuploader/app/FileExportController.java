package org.ladocuploader.app;


import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import formflow.library.config.FlowConfiguration;
import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepositoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.csv.CsvDocument;
import org.ladocuploader.app.csv.CsvService;
import org.ladocuploader.app.csv.enums.CsvType;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import static formflow.library.FormFlowController.getSubmissionIdForFlow;

@Controller
@EnableAutoConfiguration
@Slf4j
@RequestMapping("/download-csv")
public class FileExportController {

    private final MessageSource messageSource;

    private final SubmissionRepositoryService submissionRepositoryService;

    private final List<FlowConfiguration> flowConfigurations;

    private final CsvService csvService;

    public FileExportController(MessageSource messageSource,
        SubmissionRepositoryService submissionRepositoryService,
        List<FlowConfiguration> flowConfigurations, CsvService csvService) {

        this.submissionRepositoryService = submissionRepositoryService;
        this.flowConfigurations = flowConfigurations;
        this.messageSource = messageSource;
        this.csvService = csvService;
    }

    @GetMapping("{flow}/pg/{submissionId}")
    ResponseEntity<?> downloadPGCsv(
        @PathVariable String flow,
        @PathVariable String submissionId,
        HttpSession httpSession,
        HttpServletRequest request,
        Locale locale
    ) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        log.info("GET downloadCSV ParentGuardian (url: {}): flow: {}, submissionId: {}", request.getRequestURI().toLowerCase(), flow,
            submissionId);

        return handleCsvGeneration(flow, request, submissionId, httpSession, locale, CsvType.PARENT_GUARDIAN);
    }

    @GetMapping("{flow}/student/{submissionId}")
    ResponseEntity<?> downloadStudentCsv(
        @PathVariable String flow,
        @PathVariable String submissionId,
        HttpSession httpSession,
        HttpServletRequest request,
        Locale locale
    ) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        log.info("GET downloadCSV Student (url: {}): flow: {}, submissionId: {}", request.getRequestURI().toLowerCase(), flow, submissionId);

        return handleCsvGeneration(flow, request, submissionId, httpSession, locale, CsvType.STUDENT);
    }

    @GetMapping("{flow}/rel/{submissionId}")
    ResponseEntity<?> downloadRelCsv(
        @PathVariable String flow,
        @PathVariable String submissionId,
        HttpSession httpSession,
        HttpServletRequest request,
        Locale locale
    ) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        log.info("GET downloadCSV Relationship (url: {}): flow: {}, submissionId: {}", request.getRequestURI().toLowerCase(), flow, submissionId);

        return handleCsvGeneration(flow, request, submissionId, httpSession, locale, CsvType.RELATIONSHIP);

    }

    protected static void throwNotFoundError(String flow, String screen, String message) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("There was a problem with the request (flow: %s, screen: %s): %s",
                        flow, screen, message));

    }

    protected Boolean doesFlowExist(String flow) {
        return flowConfigurations.stream().anyMatch(
                flowConfiguration -> flowConfiguration.getName().equals(flow)
        );
    }

    private ResponseEntity handleCsvGeneration(String flow, HttpServletRequest request, String submissionId, HttpSession httpSession,
        Locale locale, CsvType csvType) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        log.info("GET downloadCSV (url: {}): flow: {}, submissionId: {}", request.getRequestURI().toLowerCase(), flow, submissionId);
        if (!doesFlowExist(flow)) {
            throwNotFoundError(flow, null, String.format("Could not find flow %s in your application's flow configuration.", flow));
        }
        // TODO: get list of submissions based on another column - like transmission?
        Optional<Submission> maybeSubmission = submissionRepositoryService.findById(UUID.fromString(submissionId));
        if (getSubmissionIdForFlow(httpSession, flow).toString().equals(submissionId) && maybeSubmission.isPresent()) {
            log.info("Downloading CSV with submission_id: " + submissionId);
            Submission submission = maybeSubmission.get();
            CsvDocument csvDoc = csvService.generateCsvFormattedData(List.of(submission), csvType);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=%s".formatted(csvService.generateCsvName(submission.getFlow(), csvType)));
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .headers(headers)
                    .body(csvDoc.getCsvData());
        } else {
            log.error("Attempted to download PDF with submission_id: " + submissionId + " but session_id was: "
                    + httpSession.getAttribute("id"));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageSource.getMessage("error.forbidden", null, locale));
        }
    }
}
