package csc1035.project2;

import java.util.ArrayList;

public abstract class Question {
    private int questionID;
    private String questionTitle;
    private String answerString;
    public int getID() {
        return questionID;
    }
    public void setID(int ID) {
        this.questionID = ID;
    }
    public String getTitle() {
        return questionTitle;
    }
    public void setTitle(String title) {
        this.questionTitle = title;
    }
    public String getAnswerString() {
        return answerString;
    }
    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }
}
class MCQ extends Question {
    //new ArrayList;
}
