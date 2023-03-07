package csc1035.project2.DatabaseTables;

import javax.persistence.*;

@Entity
@Table(name = "tblQuestion")
public class TblQuestion {
    @Id
    @Column(name = "QuestionID", nullable = false)
    private Integer id;

    @Column(name = "Question", nullable = false)
    private String question;

    @Column(name = "Answer", nullable = false)
    private String answer;

    @Column(name = "MaximumMarks", nullable = false)
    private Integer maximumMarks;

    @Column(name = "QuestionType", nullable = false, length = 15)
    private String questionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TopicName")
    private TblTopic topicName;

    public TblQuestion() {
    }

    public TblQuestion(String question, String answer, Integer maximumMarks, String questionType, TblTopic topicName) {
        this.question = question;
        this.answer = answer;
        this.maximumMarks = maximumMarks;
        this.questionType = questionType;
        this.topicName = topicName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getMaximumMarks() {
        return maximumMarks;
    }

    public void setMaximumMarks(Integer maximumMarks) {
        this.maximumMarks = maximumMarks;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public TblTopic getTopicName() {
        return topicName;
    }

    public void setTopicName(TblTopic topicName) {
        this.topicName = topicName;
    }

}