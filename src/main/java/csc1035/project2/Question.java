package csc1035.project2;

import java.util.ArrayList;

/*
add constructor
add method to check if answer is correct
return marks
 */

public abstract class Question {
    private int questionID;
    private int maximumMark;
    private String questionTopic;
    private String questionTitle;
    private String answerString;
    public int getID() {
        return questionID;
    }
    public void setID(int ID) {
        this.questionID = ID;
    }
    public int getMaximumMark() {
        return maximumMark;
    }
    public void setMaximumMark(int mark) {
        this.maximumMark = mark;
    }
    public String getTopic() {
        return questionTopicopic;
    }
    public void setTopic(String topic) {
        this.questionTopic = topic;
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

    // checks if answer is correct; 0 is returned for incorrect; maximumMark of the question is returned for correct
    public int returnMark (String answer) {
        if (this.getAnswerString().equals(answer)) {
            return this.getMaximumMark();
        } else {
            return 0;
        }
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
    public void deleteAllQuestionOptions() {
        this.optionList.clear();
    }
    public void updateQuestionOption(int index, String option) {
        this.optionList.set(index, option);
    }
}
class SAQ extends Question {

}