package org.ladocuploader.app.data;

import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class TransmissionRepositoryService {

    final SubmissionRepository submissionRepository;

    final TransmissionRepository transmissionRepository;

    public TransmissionRepositoryService(SubmissionRepository submissionRepository, TransmissionRepository transmissionRepository) {
        this.submissionRepository = submissionRepository;
        this.transmissionRepository = transmissionRepository;
    }

    public boolean transmissionExists(Submission submission, String transmissionType) {
        return transmissionRepository.getTransmissionBySubmissionAndType(submission, transmissionType) != null;
    }

    public Transmission createTransmissionRecord(Submission submission, String transmissionType) {
        if (!submission.getFlow().equals("laDigitalAssister")) {
            throw new RuntimeException("Non-laDigitalAssister object passed to createTransmissionRecord");
        }

        var transmission = Transmission.fromSubmission(submission);
        transmission.setTransmissionType(transmissionType);

        this.submissionRepository.save(submission);
        this.transmissionRepository.save(transmission);

        return transmission;
    }

    public Transmission save(Transmission transmission){
        return transmissionRepository.save(transmission);
    }
}
