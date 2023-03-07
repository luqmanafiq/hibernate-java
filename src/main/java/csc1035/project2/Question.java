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
    private ArrayList<String> optionList = new ArrayList<>();

    public ArrayList <String> getOptionList() {
        return this.optionList;
    }
    public void setOptionList(ArrayList questionList) {
        this.optionList = questionList;
    }
    public void createQuestionOption(String option) {
        this.optionList.add(option);
    }
    public String readQuestionOption(int index) {
        return this.optionList.get(index);
    }
    public void deleteQuestionOption(int index) {
        this.optionList.remove(index);
    }
    public void updateQuestionOption(int index, String option) {
        this.optionList.set(index, option);
    }
}
class SAQ extends Question {

}