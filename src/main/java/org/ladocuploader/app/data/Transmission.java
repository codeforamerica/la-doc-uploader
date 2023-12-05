package org.ladocuploader.app.data;

import formflow.library.data.Submission;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Data
@Table(name="transmissions")
@Component
public class Transmission {

    @Id
    @GeneratedValue
    @Column(name="transmission_id")
    private UUID transmission_id;

    @Column(name="run_id")
    private UUID runId;


    @Column(name="batch_id")
    private Integer batchId;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @Temporal(TIMESTAMP)
    @Column(name="time_sent")
    private Date timeSent;

    @Column(name="status")
    private String status;

    @Column(name="transmission_type")
    private String transmissionType;

    @Type(JsonType.class)
    @Column(name="submission_errors")
    private Map<String, Object> submissionErrors;

    public static Transmission fromSubmission(Submission submission) {
        var transmission = new Transmission();
        transmission.setSubmission(submission);
//        transmission.setFlow(submission.getFlow());
        return transmission;
    }

}
