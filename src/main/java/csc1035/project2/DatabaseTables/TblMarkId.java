package csc1035.project2.DatabaseTables;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

public class TblMarkId implements Serializable {
    @Column(name = "SubmissionID", nullable = false)
    private Integer submissionID;

    @Column(name = "QuestionID", nullable = false)
    private Integer questionID;

    public Integer getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(Integer submissionID) {
        this.submissionID = submissionID;
    }

    public Integer getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Integer questionID) {
        this.questionID = questionID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TblMarkId entity = (TblMarkId) o;
        return Objects.equals(this.submissionID, entity.submissionID) &&
                Objects.equals(this.questionID, entity.questionID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(submissionID, questionID);
    }

}