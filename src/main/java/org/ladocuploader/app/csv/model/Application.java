package org.ladocuploader.app.csv.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Application {
    @CsvBindByName(column="Student Id", required=true)
    public String studentId;

    @CsvBindByName(column="Application Status", required=true)
    public String applicationStatus;

    @CsvBindByName(column="Hide Application From Parent", required=true)
    public String hideApplicationFromParent;

    @CsvBindByName(column="School Id")
    public String schoolId;

    @CsvBindByName(column="School Rank")
    public String schoolRank;

    @CsvBindByName(column="Admin Notes")
    public String adminNotes;

    @CsvBindByName(column="Admin Last Reviewed Date")
    public String adminLastReviewedDate;

    @CsvBindByName(column="Admin Reviewer Name")
    public String adminReviewerName;

    @CsvBindByName(column="Current phone number")
    public String phoneNumber;

    @CsvBindByName(column="Do you want to receive text communication from NOLA Public Schools?")
    public String NolaTextComms;

    @CsvBindByName(column="Current email address")
    public String emailAddress;


    // TODO: how does this look like for an application with multiple children?
    @CsvBindByName(column="When was your child born?")
    public String childBirthDate;

    @CsvBindByName(column="Select the option that best describes where you live.")
    public String livingOption;

    @CsvBindByName(column="This questionnaire is intended to address the McKinney-Vento Act. Your child may be eligible for additional educational services. Did the student receive McKinney Vento (Homeless) Services in a previous school district?")
    public String receiveServices;

    @CsvBindByName(column="Is the student’s address a temporary living arrangement?")
    public String temporaryLivingArrangement;

    @CsvBindByName(column="Is the temporary living arrangement due to loss of housing or economic hardship?")
    public String economicHardship;

    @CsvBindByName(column="Does the student have a disability or receive any special education-related services?")
    public String disabilityServices;

    @CsvBindByName(column="Where is the student currently living?")
    public String livingWhere;

}
