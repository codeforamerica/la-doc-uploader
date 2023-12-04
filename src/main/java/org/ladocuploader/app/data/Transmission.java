package org.ladocuploader.app.data;

import formflow.library.data.Submission;
import lombok.Data;
import org.springframework.stereotype.Component;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Data
@Table(name="transmissions")
@Component
public class Transmission {

    @Id
    @GeneratedValue
    private UUID transmission_id;

    @Id
    private UUID batchId;

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

    public static Transmission fromSubmission(Submission submission) {
        var transmission = new Transmission();
        transmission.setSubmission(submission);
//        transmission.setFlow(submission.getFlow());
        return transmission;
    }

}
