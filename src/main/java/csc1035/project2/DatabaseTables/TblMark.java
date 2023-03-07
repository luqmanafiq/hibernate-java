package csc1035.project2.DatabaseTables;

import javax.persistence.*;

@Entity
@Table(name = "tblMark")
public class TblMark {
    @EmbeddedId
    private TblMarkId id;

    @MapsId("submissionID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SubmissionID", nullable = false)
    private TblQuizSubmission submissionID;

    @MapsId("questionID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuestionID", nullable = false)
    private TblQuestion questionID;

    @Column(name = "Score", nullable = false)
    private Integer score;

    public TblMarkId getId() {
        return id;
    }

    public void setId(TblMarkId id) {
        this.id = id;
    }

    public TblQuizSubmission getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(TblQuizSubmission submissionID) {
        this.submissionID = submissionID;
    }

    public TblQuestion getQuestionID() {
        return questionID;
    }

    public void setQuestionID(TblQuestion questionID) {
        this.questionID = questionID;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

}