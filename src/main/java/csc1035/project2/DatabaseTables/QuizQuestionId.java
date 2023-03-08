package csc1035.project2.DatabaseTables;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class QuizQuestionId implements Serializable {
    @Column(name = "QuizID", nullable = false)
    private Integer quizID;

    @Column(name = "QuestionID", nullable = false)
    private Integer questionID;

    public Integer getQuizID() {
        return quizID;
    }

    public void setQuizID(Integer quizID) {
        this.quizID = quizID;
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
        QuizQuestionId entity = (QuizQuestionId) o;
        return Objects.equals(this.questionID, entity.questionID) &&
                Objects.equals(this.quizID, entity.quizID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionID, quizID);
    }

}