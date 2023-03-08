package csc1035.project2.DatabaseTables;

import javax.persistence.*;

@Entity
@Table(name = "tblQuestionOption")
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QuestionOptionID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuestionID", nullable = false)
    private TblQuestion questionID;

    @Column(name = "QuestionOption", nullable = false)
    private String questionOption;

    public QuestionOption() {
    }

    public QuestionOption(TblQuestion questionID, String questionOption) {
        this.questionID = questionID;
        this.questionOption = questionOption;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TblQuestion getQuestionID() {
        return questionID;
    }

    public void setQuestionID(TblQuestion questionID) {
        this.questionID = questionID;
    }

    public String getQuestionOption() {
        return questionOption;
    }

    public void setQuestionOption(String questionOption) {
        this.questionOption = questionOption;
    }

}