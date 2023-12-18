package org.ladocuploader.app.data;

import formflow.library.data.Submission;
import formflow.library.data.UserFile;
import org.ladocuploader.app.data.enums.TransmissionType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransmissionRepository extends JpaRepository<Transmission, UUID> {
    @Query(value = "SELECT s FROM Submission s " +
            "JOIN Transmission t ON t.submission = s " +
            "WHERE s.submittedAt IS NOT NULL " +
            "AND t.transmissionType = :transmissionType " +
            "AND (t.status = 'Queued') OR (t.status = 'Failed') " +
            "ORDER BY s.updatedAt ASC ")
    List<Submission> submissionsToTransmit(Sort sort, TransmissionType transmissionType);

    Transmission findBySubmissionAndTransmissionType(Submission submission, TransmissionType transmissionType);

    @Query(value = "SELECT u FROM UserFile u WHERE u.submission_id = :submissionId ORDER BY u.createdAt")
    List<UserFile> userFilesBySubmission(UUID submissionId);
}
