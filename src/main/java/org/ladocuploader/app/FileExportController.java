package org.ladocuploader.app;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.config.FlowConfiguration;
import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepositoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.csv.CsvService;
import org.ladocuploader.app.csv.model.ParentGuardian;
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

    private final ParentGuardian parentGuardian;


    public FileExportController(MessageSource messageSource,
                                SubmissionRepositoryService submissionRepositoryService,
                                List<FlowConfiguration> flowConfigurations, CsvService csvService, ParentGuardian parentGuardian) {
        this.submissionRepositoryService = submissionRepositoryService;
        this.flowConfigurations = flowConfigurations;
        this.messageSource = messageSource;
        this.csvService = csvService;
        this.parentGuardian = parentGuardian;
    }

    @GetMapping("{flow}/{submissionId}")
    ResponseEntity<?> downloadCsv(
            @PathVariable String flow,
            @PathVariable String submissionId,
            HttpSession httpSession,
            HttpServletRequest request,
            Locale locale
//            @PathVariable String csvName
    ) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        log.info("GET downloadCSV (url: {}): flow: {}, submissionId: {}", request.getRequestURI().toLowerCase(), flow, submissionId);
        if (!doesFlowExist(flow)) {
            throwNotFoundError(flow, null, String.format("Could not find flow %s in your application's flow configuration.", flow));
        }
        // TODO: get list of submissions based on another column - like transmission?
        Optional<Submission> maybeSubmission = submissionRepositoryService.findById(UUID.fromString(submissionId));
        if (getSubmissionIdForFlow(httpSession, flow).toString().equals(submissionId) && maybeSubmission.isPresent()) {
            log.info("Downloading CSV with submission_id: " + submissionId);
            Submission submission = maybeSubmission.get();
            HttpHeaders headers = new HttpHeaders();
            Map<String, Object> inputData = submission.getInputData();
            Map<String,String> inputDataMap = inputData.entrySet().
                    stream().
                    filter(entry -> entry.getValue() instanceof String )
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue()));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
            CSVWriter writer = new CSVWriter(streamWriter);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            Writer writer = Files.newBufferedWriter(Paths.get(csvService.generateCsvName(submission)));
//            ParentGuardian pg = new ParentGuardian();
            HeaderColumnNameTranslateMappingStrategy<ParentGuardian> mappingStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
            mappingStrategy.setType(ParentGuardian.class);
//            mappingStrategy.setColumnMapping(pg.getColumnMappings());
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withMappingStrategy(mappingStrategy)
                    .withSeparator(',')
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            beanToCsv.write(inputDataMap);
            streamWriter.flush();
            byte [] data = stream.toByteArray();
//            byte[] data = csvService.generateCsv(maybeSubmission.get(), flow);
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=%s".formatted(csvService.generateCsvName(submission)));
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .headers(headers)
                    .body(data);
        } else {
            log.error(
                    "Attempted to download PDF with submission_id: " + submissionId + " but session_id was: "
                            + httpSession.getAttribute(
                            "id"));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageSource.getMessage("error.forbidden", null, locale));
        }
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
}
