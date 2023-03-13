package csc1035.project2.DatabaseTables;

import javax.persistence.*;

@Entity
@Table(name = "tblQuestionOption")
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QuestionOptionID", nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuestionID", nullable = false)
    private int questionID;

    @Column(name = "QuestionOption", nullable = false)
    private String questionOption;

    public QuestionOption() {
    }

    public QuestionOption(int questionID, String questionOption) {
        this.questionID = questionID;
        this.questionOption = questionOption;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getQuestionOption() {
        return questionOption;
    }

    public void setQuestionOption(String questionOption) {
        this.questionOption = questionOption;
    }

}