package csc1035.project2.DatabaseTables;

import javax.persistence.*;

@Entity
@Table(name = "tblQuizQuestion")
@IdClass(QuizQuestionId.class)
public class QuizQuestion {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuizID", nullable = false)
    private TblQuiz quizID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuestionID", nullable = false)
    private TblQuestion questionID;

    @Column(name = "OrderIndex", nullable = false)
    private Integer orderIndex;

    public QuizQuestion() {
    }

    public QuizQuestion(TblQuiz quizID, TblQuestion questionID, Integer orderIndex) {
        this.quizID = quizID;
        this.questionID = questionID;
        this.orderIndex = orderIndex;
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