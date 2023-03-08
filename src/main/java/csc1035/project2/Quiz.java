package csc1035.project2;

import java.util.ArrayList;

import csc1035.project2.DatabaseTables.TblQuestion;

public class Quiz {
    private String quizName;
    private ArrayList<TblQuestion> questionList;

    public String getQuizName() {
        return quizName;
    }
    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }
    public ArrayList<TblQuestion> getQuestionList() {
        return getQuestionList();
    }
    public void setQuestionList(ArrayList<TblQuestion> list) {
        this.questionList = list;
    }
    public TblQuestion getQuestion(int index) {
        return questionList.get(index);
    }
}
