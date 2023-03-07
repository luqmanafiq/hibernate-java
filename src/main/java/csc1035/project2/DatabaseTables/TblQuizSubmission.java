package csc1035.project2.DatabaseTables;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tblQuizSubmission")
public class TblQuizSubmission {
    @Id
    @Column(name = "SubmissionID", nullable = false)
    private Integer id;

    @Column(name = "DateOfSubmission", nullable = false)
    private Instant dateOfSubmission;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Username", nullable = false)
    private TblUser username;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuizID", nullable = false)
    private TblQuiz quizID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getDateOfSubmission() {
        return dateOfSubmission;
    }

    public void setDateOfSubmission(Instant dateOfSubmission) {
        this.dateOfSubmission = dateOfSubmission;
    }

    public TblUser getUsername() {
        return username;
    }

    public void setUsername(TblUser username) {
        this.username = username;
    }

    public TblQuiz getQuizID() {
        return quizID;
    }

    public void setQuizID(TblQuiz quizID) {
        this.quizID = quizID;
    }

}