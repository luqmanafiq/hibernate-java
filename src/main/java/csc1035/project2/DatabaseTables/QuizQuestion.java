package csc1035.project2.DatabaseTables;

import javax.persistence.*;

@Entity
@Table(name = "tblQuizQuestion")
@IdClass(QuizQuestionPK.class)
public class QuizQuestion {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuizID", nullable = false)
    private TblQuiz quizID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuestionID", nullable = false)
    private Question questionID;

    @Column(name = "OrderIndex", nullable = false)
    private int orderIndex;

    public QuizQuestion() {
    }

    public QuizQuestion(TblQuiz quiz, Question question, Integer orderIndex) {
        this.quizID = quiz;
        this.questionID = question;
        this.orderIndex = orderIndex;
    }

    public TblQuiz getQuizID() {
        return quizID;
    }

    public void setQuizID(TblQuiz quizID) {
        this.quizID = quizID;
    }

    public Question getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Question questionID) {
        this.questionID = questionID;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}