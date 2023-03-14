package csc1035.project2;

import csc1035.project2.DatabaseTables.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Check;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.xml.crypto.Data;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.RunnableScheduledFuture;

public abstract class DatabaseIO {
    private static Session _session = HibernateUtil.getSessionFactory().openSession();

    //region Database Interaction
    public static List<Object> HQLQueryDatabase(String hql) {
        _session.beginTransaction();
        Query query = _session.createQuery(hql);
        List<Object> results = query.list();
        _session.getTransaction().commit();
        return results;
    }

    public static List<Object[]> SQLQueryDatabase(String sql) {
        _session.beginTransaction();
        Query query = _session.createSQLQuery(sql);
        List<Object[]> results = query.list();
        _session.getTransaction().commit();
        return results;
    }

    public static void AddToDatabase(Object o) {
        _session.beginTransaction();
        _session.save(o);
        _session.getTransaction().commit();
    }

    public static void RemoveFromDatabase(Object o) {
        _session.beginTransaction();
        _session.delete(o);
        _session.getTransaction().commit();
    }
    //endregion

    //region privateDatabaseIO
    private static <T> T GetObject(Class<T> clazz, String queryString) {
        if(!queryString.contains("FROM")) {
            queryString = CreateQueryString(clazz, queryString);
        }
        List<T> response = (List<T>) (Object) DatabaseIO.HQLQueryDatabase(queryString);
        if (response.size() == 0) {
            return null;
        }
        return response.get(0);
    }

    private static boolean CheckObjectExists(Class<?> clazz, String queryString) {
        if(!queryString.contains("FROM")) {
            queryString = CreateQueryString(clazz, queryString);
        }
        Object obj = GetObject(clazz, queryString);
        return obj != null;
    }

    private static <T> T AddObject(Class<T> clazz, String checkQueryString, Object objectToAdd) {
        if(!checkQueryString.contains("FROM")) {
            checkQueryString = CreateQueryString(clazz, checkQueryString);
        }

        if(!CheckObjectExists(clazz, checkQueryString)) {
            DatabaseIO.AddToDatabase(objectToAdd);
            return (T) objectToAdd;
        }
        return null;
    }

    private static <T> List<T> GetAllObjects(Class<T> clazz) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ClassMetadata classMetadata = sessionFactory.getClassMetadata(clazz);
        return (List<T>) HQLQueryDatabase(String.format("FROM %s", classMetadata.getEntityName()));
    }

    /**
     * Removes a given object from the database by the parametrized value (case-insensitive).
     * @return int : 1 if does not exist, 2 if can't be removed (foreign key dependencies),
     * 0 if existed and was removed successfully
     * **/
    private static int RemoveObject(Class clazz ,String queryString) {
        if(!queryString.contains("FROM")) {
            queryString = CreateQueryString(clazz, queryString);
        }
        if (!CheckObjectExists(clazz,queryString)) {
            return 1;
        }
        try {
            DatabaseIO.RemoveFromDatabase(GetObject(clazz, queryString));
            return 0;
        }
        catch (Exception e){return 2;}
    }

    private static String CreateQueryString(Class clazz, String searchTerm) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ClassMetadata classMetadata = sessionFactory.getClassMetadata(clazz);
        String queryString = "";
        try {
            Integer.parseInt(searchTerm);
            queryString = String.format("FROM %s WHERE %s = %s", classMetadata.getEntityName(),
                    classMetadata.getIdentifierPropertyName(), searchTerm);
            return queryString;
        }
        catch (Exception e){
            queryString = String.format("FROM %s WHERE %s = '%s'", classMetadata.getEntityName(),
                    classMetadata.getIdentifierPropertyName(), searchTerm);
            return queryString;
        }
    }
    //endregion

    //region User
    /**
     * Gets user related to the given username
     * @param username : String of the username of the user (case-insensitive)
     * @return User : User object representing the user. Null if not user associated with the username
     * **/
    public static User GetUser(String username) {
        return GetObject(User.class, username.toLowerCase());
    }

    /**
     * Checks if a user exists based on a given username (String)
     * @param username : String (case-insensitive)
     * @return Boolean: True if user exists, false if not
     * **/
    public static Boolean CheckUserExists(String username) {
        return CheckObjectExists(User.class, username.toLowerCase());
    }

    public static List<User> GetAllUsers() {
        return GetAllObjects(User.class);
    }

    /**
     * Checks whether a username (case-insensitive) is unique (valid) then adds the user to the table
     * (username will be lowercase added to the database).
     * Returns TblUser object of the new user if successful or null if the name was invalid.
     * @param username : String representation of the username (case-insensitive)
     * @return : User object representing added user
     * **/
    public static User AddUser(String username) {
        return AddObject(User.class, username.toLowerCase(),
                new User(username.toLowerCase()));
    }

    public static User AddUser(User user) {
        return AddObject(User.class, user.getUsername(), user);
    }

    public static int RemoveUser(String username) {
        return RemoveObject(User.class, username.toLowerCase());
    }
    //endregion

    //region Topic
    /**
     * Gets topic related to the given topicName
     * @param topicName : String of the topic's name (case-insensitive)
     * @return User : Topic object representing the topic. Null if no topic associated with the topicName
     * **/
    public static Topic GetTopic(String topicName) {
        return GetObject(Topic.class, topicName.toLowerCase());
    }

    public static List<Topic> GetAllTopics() {
        return GetAllObjects(Topic.class);
    }

    /**
     * Checks if a topic exists based on a given topicName (String)
     * @param topicName : String (case-insensitive)
     * @return Boolean: True if topic exists, false if not
     * **/
    public static Boolean CheckTopicExists(String topicName) {
        return CheckObjectExists(Topic.class, topicName.toLowerCase());
    }

    /**
     * Checks whether a topicName (case-insensitive) is unique (valid) then adds the topic to the table
     * (topicName will be lowercase added to the database).
     * Returns Topic object of the new topic if successful or null if the name was invalid.
     * @param topicName : String representation of the topicName (case-insensitive)
     * @param topicDescription : String topic description (case-sensitive)
     * @return : Topic object added to database or null if operation was unsuccessful
     * **/
    public static Topic AddTopic(String topicName, String topicDescription) {
        return AddObject(Topic.class, topicName.toLowerCase(), new Topic(topicName.toLowerCase(), topicDescription));
    }

    public static Topic AddTopic(Topic topic) {
        return AddObject(Topic.class, topic.getId(), topic);
    }

    /**
     * Removes a given topic from the tblTopic by the topicName of the topic (case-insensitive).
     * @param topicName : String representing the topicName entity attribute
     * @return int : 1 if topic does not exist, 2 if topic can't be removed (foreign key dependencies),
     * 0 if topic existed and was removed successfully
     * **/
    public static int RemoveTopic(String topicName) {
        return RemoveObject(Topic.class, topicName.toLowerCase());
    }
    //endregion

    //region Question
    public static Question GetQuestion(String questionID) {
        return GetObject(Question.class, questionID);
    }

    public static List<Question> GetAllQuestions() {
        return GetAllObjects(Question.class);
    }

    public static boolean CheckQuestionExists(String questionID) {
        return CheckObjectExists(Question.class, questionID);
    }

    public static Question AddQuestion(Question newQuestion) {
        _session.persist(newQuestion);
        AddToDatabase(newQuestion);
        return newQuestion;
    }

    public static int RemoveQuestion(String questionID) {
        return RemoveObject(Question.class, questionID);
    }
    //endregion

    //region Quiz
    public static Quiz GetQuiz(String quizID) {
        return GetObject(Quiz.class, quizID);
    }

    public static Boolean CheckQuizExists(String quizID) {
        return CheckObjectExists(Quiz.class, quizID);
    }

    public static Boolean CheckQuizExists(String quizID, String username, String quizName) {
        List<Quiz> quizzes = GetAllQuizzes();
        for(Quiz q: quizzes) {
            if(q.getUsername().getUsername().equalsIgnoreCase(username.toLowerCase()) && q.getQuizName()
                    .equalsIgnoreCase(quizName.toLowerCase())) {
                return true;
            }
        }
        if(CheckObjectExists(Quiz.class, quizID)) {
            return true;
        }
        return false;
    }

    public static List<Quiz> GetAllQuizzes() {
        return GetAllObjects(Quiz.class);
    }

    public static Boolean CheckQuizExists(String username, String quizName) {
        List<Quiz> quizzes = GetAllQuizzes();
        for(Quiz q: quizzes) {
            if(q.getUsername().getUsername().equalsIgnoreCase(username.toLowerCase()) && q.getQuizName()
                    .equalsIgnoreCase(quizName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static Quiz AddQuiz(Quiz quiz) {
        if(CheckQuizExists(quiz.getUsername().getUsername(), quiz.getQuizName())) {
            return null;
        }
        AddToDatabase(quiz);
        return quiz;
    }

    public static int RemoveQuiz(Quiz quiz) {
        if(CheckQuizExists(quiz.getId().toString(), quiz.getUsername().getUsername(), quiz.getQuizName())) {
            RemoveFromDatabase(quiz);
            return 0;
        }
        return 1;
    }

    public static int RemoveQuiz(String quizID) {
        return RemoveObject(Quiz.class, quizID);
    }
    //endregion

    //region Question Option
    public static boolean CheckQuestionOptionExists(String questionOptionID) {
        return CheckObjectExists(QuestionOption.class, questionOptionID);
    }

    public static QuestionOption GetQuestionOption(String questionOptionID) {
        return GetObject(QuestionOption.class, questionOptionID);
    }

    public static int RemoveQuestionOption(String questionOptionID) {
        return RemoveObject(QuestionOption.class, questionOptionID);
    }

    public static List<QuestionOption> GetAllQuestionOptions() {
        return GetAllObjects(QuestionOption.class);
    }

    public static QuestionOption AddQuestionOption(QuestionOption questionOption) {
        if(HQLQueryDatabase(String.format("FROM QuestionOption WHERE questionID = %s AND questionOption = '%s'"
                ,questionOption.getQuestionID().getId(), questionOption.getQuestionOption())).isEmpty()) {
            AddToDatabase(questionOption);
            return questionOption;
        }
        return null;
    }
    //endregion

    //region Quiz Question
    public static Boolean CheckQuizQuestionExists(int quizID, int questionID) {
        if(!(CheckQuizExists(String.valueOf(quizID)) && CheckQuestionExists(String.valueOf(questionID)))) {
            return false;
        }
        return CheckObjectExists(QuizQuestion.class, String.format("FROM QuizQuestion WHERE QuizID = %s AND " +
                "QuestionID = %s", quizID, questionID));
    }

    public static QuizQuestion GetQuizQuestion(int quizID, int questionID) {
        return GetObject(QuizQuestion.class, String.format("FROM QuizQuestion WHERE QuizID = %s AND " +
                "QuestionID = %s", quizID, questionID));
    }

    public static List<QuizQuestion> GetAllQuizQuestions() {
        return GetAllObjects(QuizQuestion.class);
    }

    public static int RemoveQuizQuestion(int quizID, int questionID) {
        return RemoveObject(QuizQuestion.class, String.format("FROM QuizQuestion WHERE QuizID = %s AND " +
                "QuestionID = %s", quizID, questionID));
    }

    public static QuizQuestion AddQuizQuestion(QuizQuestion quizQuestion) {
        return AddObject(QuizQuestion.class, String.format("FROM QuizQuestion WHERE QuizID = %s AND " +
                "QuestionID = %s", quizQuestion.getQuizID().getId(), quizQuestion.getQuestionID().getId()), quizQuestion);
    }
    //endregion

    //region Quiz Submission
    public static Boolean CheckQuizSubmissionExists(int submissionID) {
        return CheckObjectExists(QuizSubmission.class, String.valueOf(submissionID));
    }

    public static QuizSubmission AddQuizSubmission(QuizSubmission quizSubmission) {
        return AddObject(QuizSubmission.class,String.valueOf(quizSubmission.getId()),quizSubmission);
    }

    public static int RemoveQuizSubmission(QuizSubmission quizSubmission) {
        return RemoveObject(QuizSubmission.class, String.valueOf(quizSubmission.getId()));
    }

    public static int RemoveQuizSubmission(int submissionID) {
        return RemoveObject(QuizSubmission.class, String.valueOf(submissionID));
    }

    public static QuizSubmission GetQuizSubmission(int submissionID) {
        return GetObject(QuizSubmission.class, String.valueOf(submissionID));
    }

    //endregion

    //region Mark
    private static String _markQueryString = "FROM Mark WHERE submissionID = %s AND questionID = %s";
    public static boolean CheckMarkExists(int submissionID, int questionID) {
        return CheckObjectExists(Mark.class, String.format(_markQueryString, submissionID, questionID));
    }

    public static List<Mark> GetAllMarks() {
        return GetAllObjects(Mark.class);
    }

    public static Mark GetMark(int submissionID, int questionID) {
        return GetObject(Mark.class, String.format(_markQueryString, submissionID, questionID));
    }

    public static int RemoveMark(Mark mark) {
        return RemoveObject(Mark.class, String.format(_markQueryString, mark.getSubmissionID().getId(),
                mark.getQuestionID().getId()));
    }

    public static int RemoveMark(int submissionID, int questionID) {
        return RemoveObject(Mark.class, String.format(_markQueryString, submissionID, questionID));
    }

    public static Mark AddMark(Mark mark) {
        return AddObject(Mark.class, String.format(_markQueryString, mark.getSubmissionID().getId(),
                mark.getQuestionID().getId()), mark);
    }

    public static Mark AddMark(int submissionID, int questionID, int score) {
        if(!(CheckQuizSubmissionExists(submissionID) && CheckQuestionExists(String.valueOf(questionID)))) {
            return null;
        }
        Mark newMark = new Mark(GetQuizSubmission(submissionID), GetQuestion(String.valueOf(questionID)), score);
        return AddObject(Mark.class, String.format(_markQueryString, submissionID, questionID), newMark);
    }
    //endregion

    public static List<Question> GetQuestionsFromQuiz(int quizID) {
        List<Question> returnList = new ArrayList<>();
        Quiz quiz = GetQuiz(String.valueOf(quizID));
        if(quiz == null) {return returnList;}
        //Get all questions
        //check if quiz ID matches
        return returnList;
    }

    public static List<Mark> GetMarksFromSubmission(int submissionID) {
        System.out.println();
        return null;
    }



    public static void main(String[] args) {
        System.out.println(GetQuestionsFromQuiz(3).stream().toArray());
    }
}