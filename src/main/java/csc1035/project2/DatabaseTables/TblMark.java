package csc1035.project2.DatabaseTables;

import javax.persistence.*;

@Entity
@Table(name = "tblMark")
@IdClass(TblMarkId.class)
public class TblMark {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SubmissionID", nullable = false)
    private TblQuizSubmission submissionID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuestionID", nullable = false)
    private TblQuestion questionID;

    @Column(name = "Score", nullable = false)
    private Integer score;

    public TblMark() {
    }

    public TblMark(TblQuizSubmission submissionID, TblQuestion questionID, int score) {
        this.submissionID = submissionID;
        this.questionID = questionID;
        this.score = score;
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