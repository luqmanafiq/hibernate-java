package csc1035.project2.DatabaseTables;

import javax.persistence.*;

@Entity
@Table(name = "tblQuiz")
public class TblQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QuizID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Username", nullable = false)
    private TblUser username;

    @Column(name = "QuizName", nullable = false, length = 50)
    private String quizName;

    public TblQuiz() {
    }

    public TblQuiz(TblUser username, String quizName) {
        this.username = username;
        this.quizName = quizName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TblUser getUsername() {
        return username;
    }

    public void setUsername(TblUser username) {
        this.username = username;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

}