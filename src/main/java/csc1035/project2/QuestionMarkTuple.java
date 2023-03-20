package csc1035.project2;

import csc1035.project2.DatabaseTables.Question;

public class QuestionMarkTuple {
    private Question _question;
    private int _marksReceived;
    private String _userAnswer;

    public QuestionMarkTuple(Question question, int marksReceived, String userAnswer) {
        this._question = question;
        this._marksReceived = marksReceived;
        this._userAnswer = userAnswer;
    }

    public Question GetQuestion() {
        return _question;
    }

    public void SetQuestion(Question _question) {
        this._question = _question;
    }

    public int GetMarksReceived() {
        return _marksReceived;
    }

    public void SetMarksReceived(int _marksReceived) {
        this._marksReceived = _marksReceived;
    }

    public String get_userAnswer() {
        return _userAnswer;
    }

    public void set_userAnswer(String _userAnswer) {
        this._userAnswer = _userAnswer;
    }
}
