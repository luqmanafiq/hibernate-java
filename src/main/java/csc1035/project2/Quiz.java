package csc1035.project2;

import java.util.ArrayList;

public class Quiz {
    private String quizName;
    private ArrayList<Question> questionList;

    public String getQuizName() {
        return quizName;
    }
    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }
    public ArrayList<Question> getQuestionList() {
        return getQuestionList();
    }
    public void setQuestionList(ArrayList<Question> list) {
        this.questionList = list;
    }
    public Question getQuestion(int index) {
        return questionList.get(index);
    }
}
