package org.ladocuploader.app.data;

import formflow.library.data.Submission;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransmissionRepository extends JpaRepository<Transmission, UUID> {
    @Query(value = "SELECT s FROM Submission s JOIN Transmission t ON t.submission = s WHERE s.submittedAt IS NOT NULL AND t.timeSent IS NULL ORDER BY s.updatedAt ASC ")
    List<Submission> submissionsToTransmit(Sort sort);

    @Query(value = "SELECT t FROM Transmission t WHERE t.submission = :submission")
    Transmission getTransmissionBySubmission(Submission submission);
}
