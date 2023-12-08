package org.ladocuploader.app.data;

import formflow.library.data.Submission;
import org.ladocuploader.app.data.enums.TransmissionType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransmissionRepository extends JpaRepository<Transmission, UUID> {
    @Query(value = "SELECT s FROM Submission s JOIN Transmission t ON t.submission = s WHERE s.submittedAt IS NOT NULL AND (t.status = 'Queued') OR (t.status = 'Failed') ORDER BY s.updatedAt ASC ")
    List<Submission> submissionsToTransmit(Sort sort);

    Transmission findBySubmissionAndTransmissionType(Submission submission, TransmissionType transmissionType);
}
