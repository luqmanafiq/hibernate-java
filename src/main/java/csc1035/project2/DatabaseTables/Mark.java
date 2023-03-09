package csc1035.project2.DatabaseTables;

import javax.persistence.*;

@Entity
@Table(name = "tblMark")
@IdClass(MarkId.class)
public class Mark {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SubmissionID", nullable = false)
    private QuizSubmission submissionID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuestionID", nullable = false)
    private Question questionID;

    @Column(name = "Score", nullable = false)
    private Integer score;

    public Mark() {
    }

    public Mark(QuizSubmission submissionID, Question questionID, int score) {
        this.submissionID = submissionID;
        this.questionID = questionID;
        this.score = score;
    }

    public QuizSubmission getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(QuizSubmission submissionID) {
        this.submissionID = submissionID;
    }

    public Question getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Question questionID) {
        this.questionID = questionID;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

}