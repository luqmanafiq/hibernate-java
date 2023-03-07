package csc1035.project2.DatabaseTables;

import javax.persistence.*;

@Entity
@Table(name = "tblQuizQuestion")
public class TblQuizQuestion {
    @EmbeddedId
    private TblQuizQuestionId id;

    @MapsId("quizID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuizID", nullable = false)
    private TblQuiz quizID;

    @MapsId("questionID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuestionID", nullable = false)
    private TblQuestion questionID;

    @Column(name = "OrderIndex", nullable = false)
    private Integer orderIndex;

    public TblQuizQuestionId getId() {
        return id;
    }

    public void setId(TblQuizQuestionId id) {
        this.id = id;
    }

    public TblQuiz getQuizID() {
        return quizID;
    }

    public void setQuizID(TblQuiz quizID) {
        this.quizID = quizID;
    }

    public TblQuestion getQuestionID() {
        return questionID;
    }

    public void setQuestionID(TblQuestion questionID) {
        this.questionID = questionID;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

}