package csc1035.project2;

import csc1035.project2.DatabaseTables.Question;

public class QuestionMarkTuple {
    private Question _question;
    private int _marksReceived;

    public QuestionMarkTuple(Question question, int marksReceived) {
        this._question = question;
        this._marksReceived = marksReceived;
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
}
