package org.ladocuploader.app.submission.enums;

public enum SubmissionErrorTypes {
    INCOMPLETE_RECORD("INCOMPLETE_RECORD"),

    OTHER("OTHER");

    private final String name;
    SubmissionErrorTypes(String name) {
        this.name = name;
    }
}
