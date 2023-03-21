package csc1035.project2;

import csc1035.project2.DatabaseTables.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.query.Query;
import java.io.File;
import java.io.FileWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public abstract class DatabaseIO {
    private static Session _session = HibernateUtil.getSessionFactory().openSession();

    //region Database Interaction

    /**
     * Connects and sends HQL query to the database.
     * @param hql String representing the HQL that should be sent to the database.
     * @return List of objects representing the response.
     */
    public static List<Object> hqlquerydatabase(String hql) {
        _session.beginTransaction();
        Query query = _session.createQuery(hql);
        List<Object> results = query.list();
        _session.getTransaction().commit();
        return results;
    }

    /**
     * Connects and sends SQL query to the database.
     * @param sql String representing the SQL that should be sent to the database.
     * @return List of objects representing the response.
     */
    public static List<Object[]> sqlquerydatabase(String sql) {
        _session.beginTransaction();
        Query query = _session.createSQLQuery(sql);
        List<Object[]> results = query.list();
        _session.getTransaction().commit();
        return results;
    }

    /**
     * Adds an object of any type to the database.
     * @param o Object to add to the database.
     */
    public static void addToDatabase(Object o) {
        _session.beginTransaction();
        _session.save(o);
        _session.getTransaction().commit();
    }

    /**
     * Removes an object of any type from the database.
     * @param o Object to remove from the database.
     */
    public static void removeFromDatabase(Object o) {
        _session.beginTransaction();
        _session.delete(o);
        _session.getTransaction().commit();
    }
    //endregion

    //region privateDatabaseIO

    /**
     * Gets a record (object) of any type from any entity in the database.
     * @param clazz Type of class representing the entity the object (record) belongs to.
     * @param queryString The HQL query string to identify what record to get from the database.
     * @return The object retrieved from the database (null if no record could be found).
     * @param <T> Type of object to be returned.
     */
    private static <T> T getObject(Class<T> clazz, String queryString) {
        if(!queryString.contains("FROM")) {
            queryString = createQueryString(clazz, queryString);
        }
        List<T> response = (List<T>) (Object) DatabaseIO.hqlquerydatabase(queryString);
        if (response.size() == 0) {
            return null;
        }
        return response.get(0);
    }

    /**
     * Checks if an object in the database exists.
     * @param clazz Type of class representing the entity the object (record) belongs to.
     * @param queryString The HQL query string to identify what record to check in the database.
     * @return True if object exists, false if it does not.
     */
    private static boolean checkObjectExists(Class<?> clazz, String queryString) {
        if(!queryString.contains("FROM")) {
            queryString = createQueryString(clazz, queryString);
        }
        Object obj = getObject(clazz, queryString);
        return obj != null;
    }

    /**
     * Adds an object of any type to the database.
     * @param clazz Type of class representing the entity the object (record) belongs to.
     * @param checkQueryString The HQL query string used to check that the object being added to the database does not already exist.
     * @param objectToAdd Object that should be added to the database (must be of the same type as the clazz param).
     * @return The object added to the database (null if there was an error).
     * @param <T> Type of object to be returned.
     */
    private static <T> T addObject(Class<T> clazz, String checkQueryString, Object objectToAdd) {
        if(!checkQueryString.contains("FROM")) {
            checkQueryString = createQueryString(clazz, checkQueryString);
        }

        if(!checkObjectExists(clazz, checkQueryString)) {
            DatabaseIO.addToDatabase(objectToAdd);
            return (T) objectToAdd;
        }
        return null;
    }

    /**
     * Gets a list of all objects from a specific database entity.
     * @param clazz Type of class representing the entity the object (record) belongs to.
     * @return List of objects from the database.
     * @param <T>
     */
    private static <T> List<T> getAllObjects(Class<T> clazz) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ClassMetadata classMetadata = sessionFactory.getClassMetadata(clazz);
        return (List<T>) hqlquerydatabase(String.format("FROM %s", classMetadata.getEntityName()));
    }

    /**
     * Removes a given object from the database by the parametrized value (case-insensitive).
     * @param clazz Type of class representing the entity the object (record) belongs to.
     * @param queryString HQL query string to find the value that should be removed from the database.
     * @return 1 if does not exist, 2 if can't be removed (foreign key dependencies), 0 if successful
     */
    private static int removeObject(Class clazz , String queryString) {
        if(!queryString.contains("FROM")) {
            queryString = createQueryString(clazz, queryString);
        }
        if (!checkObjectExists(clazz,queryString)) {
            return 1;
        }
        try {
            DatabaseIO.removeFromDatabase(getObject(clazz, queryString));
            return 0;
        }
        catch (Exception e){return 2;}
    }

    /**
     * Creates a HQL query based on the entity (class type) and primary key value you wish to search for (searchTerm).
     * @param clazz Class type representing a database entity.
     * @param searchTerm String representation of the primary key value of the database entity.
     * @return HQL query string
     */
    private static String createQueryString(Class clazz, String searchTerm) {
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
    public static User getUser(String username) {
        return getObject(User.class, username.toLowerCase());
    }

    /**
     * Checks if a user exists based on a given username (String)
     * @param username : String (case-insensitive)
     * @return Boolean: True if user exists, false if not
     * **/
    public static Boolean checkUserExists(String username) {
        return checkObjectExists(User.class, username.toLowerCase());
    }

    public static List<User> getAllUsers() {
        return getAllObjects(User.class);
    }

    /**
     * Checks whether a username (case-insensitive) is unique (valid) then adds the user to the table
     * (username will be lowercase added to the database).
     * Returns TblUser object of the new user if successful or null if the name was invalid.
     * @param username : String representation of the username (case-insensitive)
     * @return : User object representing added user
     * **/
    public static User addUser(String username) {
        return addObject(User.class, username.toLowerCase(),
                new User(username.toLowerCase()));
    }

    public static User addUser(User user) {
        return addObject(User.class, user.getUsername(), user);
    }

    public static int removeUser(String username) {
        return removeObject(User.class, username.toLowerCase());
    }
    //endregion

    //region Topic
    /**
     * Gets topic related to the given topicName
     * @param topicName : String of the topic's name (case-insensitive)
     * @return User : Topic object representing the topic. Null if no topic associated with the topicName
     * **/
    public static Topic getTopic(String topicName) {
        return getObject(Topic.class, topicName.toLowerCase());
    }

    public static List<Topic> getAllTopics() {
        return getAllObjects(Topic.class);
    }

    /**
     * Checks if a topic exists based on a given topicName (String)
     * @param topicName : String (case-insensitive)
     * @return Boolean: True if topic exists, false if not
     * **/
    public static Boolean checkTopicExists(String topicName) {
        return checkObjectExists(Topic.class, topicName.toLowerCase());
    }

    /**
     * Checks whether a topicName (case-insensitive) is unique (valid) then adds the topic to the table
     * (topicName will be lowercase added to the database).
     * Returns Topic object of the new topic if successful or null if the name was invalid.
     * @param topicName : String representation of the topicName (case-insensitive)
     * @param topicDescription : String topic description (case-sensitive)
     * @return : Topic object added to database or null if operation was unsuccessful
     * **/
    public static Topic addTopic(String topicName, String topicDescription) {
        return addObject(Topic.class, topicName.toLowerCase(), new Topic(topicName.toLowerCase(), topicDescription));
    }

    public static Topic addTopic(Topic topic) {
        return addObject(Topic.class, topic.getId(), topic);
    }

    /**
     * Removes a given topic from the tblTopic by the topicName of the topic (case-insensitive).
     * @param topicName : String representing the topicName entity attribute
     * @return int : 1 if topic does not exist, 2 if topic can't be removed (foreign key dependencies),
     * 0 if topic existed and was removed successfully
     * **/
    public static int removeTopic(String topicName) {
        return removeObject(Topic.class, topicName.toLowerCase());
    }
    //endregion

    //region Question
    public static Question getQuestion(String questionID) {
        return getObject(Question.class, questionID);
    }

    public static List<Question> getAllQuestions() {
        return getAllObjects(Question.class);
    }

    public static boolean checkQuestionExists(String questionID) {
        return checkObjectExists(Question.class, questionID);
    }

    public static Question addQuestion(Question newQuestion) {
        _session.persist(newQuestion);
        addToDatabase(newQuestion);
        return newQuestion;
    }

    public static int removeQuestion(String questionID) {
        return removeObject(Question.class, questionID);
    }

    /**
     * Updates (replaces) the question by its ID with the object (updatedQuestion) passed.
     * @param updatedQuestion Question used to replace (update) the question. It's ID should be the same as the
     *                       question you wish to update.
     * @return Updated Question (null if there was an error updating the question).
     */
    public static Question updateQuestion(Question updatedQuestion) {
        if(!checkQuestionExists(String.valueOf(updatedQuestion.getId()))) {return null;}
        try {
            Transaction transaction = _session.beginTransaction();
            _session.update(updatedQuestion);
            transaction.commit();
            return updatedQuestion;
        }
        catch(Exception e) {
            return null;
        }
    }
    //endregion

    //region Quiz
    public static Quiz getQuiz(String quizID) {
        return getObject(Quiz.class, quizID);
    }

    public static Quiz overrideQuiz(Quiz updatedQuiz) {
        if(!checkQuizExists(String.valueOf(updatedQuiz.getId()), updatedQuiz.getUsername().getUsername(),
                updatedQuiz.getQuizName())) {return null;}
        try {
            Transaction transaction = _session.beginTransaction();
            _session.update(updatedQuiz);
            transaction.commit();
            return updatedQuiz;
        }
        catch(Exception e) {
            return null;
        }
    }

    public static Boolean checkQuizExists(String quizID) {
        return checkObjectExists(Quiz.class, quizID);
    }

    public static Boolean checkQuizExists(String quizID, String username, String quizName) {
        List<Quiz> quizzes = getAllQuizzes();
        for(Quiz q: quizzes) {
            if(q.getUsername().getUsername().equalsIgnoreCase(username.toLowerCase()) && q.getQuizName()
                    .equalsIgnoreCase(quizName.toLowerCase())) {
                return true;
            }
        }
        if(checkObjectExists(Quiz.class, quizID)) {
            return true;
        }
        return false;
    }

    public static List<Quiz> getAllQuizzes() {
        return getAllObjects(Quiz.class);
    }

    public static Boolean checkQuizExists(String username, String quizName) {
        List<Quiz> quizzes = getAllQuizzes();
        for(Quiz q: quizzes) {
            if(q.getUsername().getUsername().equalsIgnoreCase(username.toLowerCase()) && q.getQuizName()
                    .equalsIgnoreCase(quizName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static Quiz addQuiz(Quiz quiz) {
        if(checkQuizExists(quiz.getUsername().getUsername(), quiz.getQuizName())) {
            return null;
        }
        addToDatabase(quiz);
        return quiz;
    }

    public static int removeQuiz(Quiz quiz) {
        if(checkQuizExists(quiz.getId().toString(), quiz.getUsername().getUsername(), quiz.getQuizName())) {
            removeFromDatabase(quiz);
            return 0;
        }
        return 1;
    }

    public static int removeQuiz(String quizID) {
        return removeObject(Quiz.class, quizID);
    }
    //endregion

    //region Question Option
    public static boolean checkQuestionOptionExists(String questionOptionID) {
        return checkObjectExists(QuestionOption.class, questionOptionID);
    }

    public static QuestionOption getQuestionOption(String questionOptionID) {
        return getObject(QuestionOption.class, questionOptionID);
    }

    public static int removeQuestionOption(String questionOptionID) {
        return removeObject(QuestionOption.class, questionOptionID);
    }

    public static List<QuestionOption> getAllQuestionOptions() {
        return getAllObjects(QuestionOption.class);
    }

    public static QuestionOption addQuestionOption(QuestionOption questionOption) {
        if(hqlquerydatabase(String.format("FROM QuestionOption WHERE questionID = %s AND questionOption = '%s'"
                ,questionOption.getQuestionID().getId(), questionOption.getQuestionOption())).isEmpty()) {
            addToDatabase(questionOption);
            return questionOption;
        }
        return null;
    }
    //endregion

    //region Quiz Question
    public static Boolean checkQuizQuestionExists(int quizID, int questionID) {
        if(!(checkQuizExists(String.valueOf(quizID)) && checkQuestionExists(String.valueOf(questionID)))) {
            return false;
        }
        return checkObjectExists(QuizQuestion.class, String.format("FROM QuizQuestion WHERE QuizID = %s AND " +
                "QuestionID = %s", quizID, questionID));
    }

    public static QuizQuestion getQuizQuestion(int quizID, int questionID) {
        return getObject(QuizQuestion.class, String.format("FROM QuizQuestion WHERE QuizID = %s AND " +
                "QuestionID = %s", quizID, questionID));
    }

    public static List<QuizQuestion> getAllQuizQuestions() {
        return getAllObjects(QuizQuestion.class);
    }

    public static int removeQuizQuestion(int quizID, int questionID) {
        if(!checkQuizQuestionExists(quizID, questionID)) {return 1;}
        QuizQuestion questionToRemove = getQuizQuestion(quizID, questionID);
        int statusCode = removeObject(QuizQuestion.class, String.format("FROM QuizQuestion WHERE QuizID = %s AND " +
                "QuestionID = %s", quizID, questionID));
        if(statusCode == 0) {
            List<QuizQuestion> quizQuestions = (List<QuizQuestion>)(Object) hqlquerydatabase(
                    String.format("FROM QuizQuestion WHERE QuizID=%s", questionToRemove.getQuizID().getId()));
            for(QuizQuestion qq: quizQuestions) {
                QuizQuestion questionToUpdate = qq;
                if(qq.getOrderIndex() > questionToRemove.getOrderIndex()) {
                    questionToUpdate.setOrderIndex(questionToUpdate.getOrderIndex() - 1);
                    Transaction transaction = _session.beginTransaction();
                    _session.update(questionToUpdate);
                    transaction.commit();
                }
            }
        }
        return statusCode;
    }

    public static QuizQuestion addQuizQuestion(QuizQuestion quizQuestion) {
        List<QuizQuestion> quizQuestions = (List<QuizQuestion>)(Object) hqlquerydatabase(String.format(
                "FROM QuizQuestion WHERE QuizID = %s", quizQuestion.getQuizID().getId()));
        int amountOfQuestions = quizQuestions.size();
        if(quizQuestion.getOrderIndex() > amountOfQuestions - 1) {
            quizQuestion.setOrderIndex(amountOfQuestions - 1);
        }
        else if(quizQuestion.getOrderIndex() < 0) {
            quizQuestion.setOrderIndex(amountOfQuestions);
        }
        QuizQuestion addedObject =  addObject(QuizQuestion.class, String.format("FROM QuizQuestion WHERE QuizID = %s AND " +
                "QuestionID = %s", quizQuestion.getQuizID().getId(), quizQuestion.getQuestionID().getId()), quizQuestion);
        if(addedObject != null) {
            for(QuizQuestion qq: quizQuestions) {
                QuizQuestion questionToUpdate = qq;
                if(questionToUpdate.getOrderIndex() >= quizQuestion.getOrderIndex()) {
                    questionToUpdate.setOrderIndex(questionToUpdate.getOrderIndex() + 1);
                    Transaction transaction = _session.beginTransaction();
                    _session.update(questionToUpdate);
                    transaction.commit();
                }
            }
        }
        return addedObject;
    }
    //endregion

    //region Quiz Submission
    public static Boolean checkQuizSubmissionExists(int submissionID) {
        return checkObjectExists(QuizSubmission.class, String.valueOf(submissionID));
    }

    public static QuizSubmission addQuizSubmission(QuizSubmission quizSubmission) {
        return addObject(QuizSubmission.class,String.valueOf(quizSubmission.getId()),quizSubmission);
    }

    public static int removeQuizSubmission(QuizSubmission quizSubmission) {
        return removeObject(QuizSubmission.class, String.valueOf(quizSubmission.getId()));
    }

    public static int removeQuizSubmission(int submissionID) {
        return removeObject(QuizSubmission.class, String.valueOf(submissionID));
    }

    public static QuizSubmission getQuizSubmission(int submissionID) {
        return getObject(QuizSubmission.class, String.valueOf(submissionID));
    }

    //endregion

    //region Mark
    private static String _markQueryString = "FROM Mark WHERE submissionID = %s AND questionID = %s";
    public static boolean checkMarkExists(int submissionID, int questionID) {
        return checkObjectExists(Mark.class, String.format(_markQueryString, submissionID, questionID));
    }

    public static List<Mark> getAllMarks() {
        return getAllObjects(Mark.class);
    }

    public static Mark getMark(int submissionID, int questionID) {
        return getObject(Mark.class, String.format(_markQueryString, submissionID, questionID));
    }

    public static int removeMark(Mark mark) {
        return removeObject(Mark.class, String.format(_markQueryString, mark.getSubmissionID().getId(),
                mark.getQuestionID().getId()));
    }

    public static int removeMark(int submissionID, int questionID) {
        return removeObject(Mark.class, String.format(_markQueryString, submissionID, questionID));
    }

    public static Mark addMark(Mark mark) {
        return addObject(Mark.class, String.format(_markQueryString, mark.getSubmissionID().getId(),
                mark.getQuestionID().getId()), mark);
    }

    public static Mark addMark(int submissionID, int questionID, int score, String userAnswer) {
        if(!(checkQuizSubmissionExists(submissionID) && checkQuestionExists(String.valueOf(questionID)))) {
            return null;
        }
        Mark newMark = new Mark(getQuizSubmission(submissionID), getQuestion(String.valueOf(questionID)),
                score, userAnswer);
        return addObject(Mark.class, String.format(_markQueryString, submissionID, questionID), newMark);
    }
    //endregion

    public static List<Question> getQuestionsFromQuiz(int quizID) {
        List<Question> returnList = new ArrayList<>();
        Quiz quiz = getQuiz(String.valueOf(quizID));
        if(quiz == null) {return returnList;}
        List<QuizQuestion> quizQuestions = (List<QuizQuestion>)(Object) hqlquerydatabase(
                String.format("FROM QuizQuestion WHERE quizID=%s", quiz.getId()));
        Question[] orderedQuestions = new Question[quizQuestions.size()];
        for (QuizQuestion quizQuestion:
             quizQuestions) {
            orderedQuestions[quizQuestion.getOrderIndex()] = getQuestion(String.valueOf(quizQuestion.getQuestionID().getId()));
        }
        returnList = Arrays.asList(orderedQuestions);
        return returnList;
    }

    public static List<Mark> getMarksFromSubmission(int submissionID) {
        List<Mark> returnList = new ArrayList<>();
        if(!checkQuizSubmissionExists(submissionID)) {return returnList;}
        QuizSubmission submission = getQuizSubmission(submissionID);
        returnList = (List<Mark>)(Object) hqlquerydatabase(
                String.format("FROM Mark WHERE submissionID = %s", submission.getId()));
        return returnList;
    }

    // returns list of questions if successful, null if unsuccessful
    public static List<Question> importQuestionsFromCSV(String filePath) {
        List<Question> questions = new ArrayList<>();
        if(!new File(filePath).isFile()) {
            return null;
        }
        try {
            Scanner s = new Scanner(new File(filePath));
            s.useDelimiter("\\n");
            boolean isFirstValue = true;
            while(s.hasNext()) {
                String value = s.next();
                if(isFirstValue == false) {
                    String[] question = value.split(",");
                    Question questionFromCSV = null;
                    try {
                        if(checkTopicExists(question[4])) {
                            questionFromCSV = new Question(question[0], question[1], Integer.parseInt(question[2]),
                                    question[3], getTopic(question[4]));
                        }
                    }
                    catch(Exception e){}
                    if(questionFromCSV != null) {
                        questions.add(questionFromCSV);
                    }
                }
                if(isFirstValue == true){isFirstValue = false;}
            }
            s.close();
        }
        catch (Exception e) {return null;}
        return questions;
    }

    //0 success, 1 if directory (folder) does not exist, 2 if failed writing to file
    public static int exportQuestionsToCSV(String fileDirectoryPath, String fileName, List<Question> questions) {
        if(!new File(fileDirectoryPath).isDirectory()) {
            return 1;
        }
        String fileContent = "question,answer,maximumMarks,type,topic\n";
        for(int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            fileContent += String.format("%s,%s,%s,%s,%s",question.getQuestion(), question.getAnswer(),
                    question.getMaximumMarks(), question.getQuestionType(), question.getTopicName().getId());
            if(i+1!=questions.size()){fileContent+="\n";}
        }
        if(!fileName.contains(".csv")) {fileName += ".csv";}
        String finalisedPath = fileDirectoryPath + fileName;
        try {
            FileWriter fileWriter = new FileWriter(finalisedPath);
            fileWriter.write(fileContent);
            fileWriter.close();
            return 0;
        }
        catch (Exception e){return 2;}
    }

    /**
     * Compares a question's answer to a given answer to see if it is correct (for MCQ the answer should be the answer
     * in the records answer field).
     * @param question Question answered by the user.
     * @param answer User's answer to the question (case-insensitive).
     * @return Question and score received for the question in the form of QuestionMarkTuple.
     */
    public static QuestionMarkTuple markQuestionAnswer(Question question, String answer) {
        if(question.getAnswer().trim().equalsIgnoreCase(answer.trim())) {
            return new QuestionMarkTuple(question, question.getMaximumMarks(), answer.toLowerCase().trim());
        }
        return new QuestionMarkTuple(question, 0, answer.toLowerCase().trim());
    }


    /**
     * Creates a quiz submission and adds marks to the database.
     * @param markedQuestions List of QuestionMarkTuples (all marked questions to be submitted).
     * @param user The user submitting the quiz.
     * @param quiz The quiz being submitted.
     * @return QuizSubmission that was submitted. Null if there was an error creating the submission.
     */
    public static QuizSubmission submitQuizResults(List<QuestionMarkTuple> markedQuestions, User user, Quiz quiz) {
        QuizSubmission submission = addQuizSubmission(new QuizSubmission(Instant.now(), user, quiz));
        if(submission == null) {return null;}
        for (QuestionMarkTuple qm: markedQuestions) {
            Mark mark = addMark(new Mark(submission, qm.getQuestion(), qm.getMarksReceived(), qm.get_userAnswer()));
            if(mark == null) {return null;}
        }
        return submission;
    }

    /**
     * Checks whether a user has answered a given question incorrectly in the past.
     * @param user The user to query.
     * @param question The question to query.
     * @return True or false depending on if the user has answered the question incorrectly in the past (null if there was an error).
     */
    public static Boolean hasUserAnsweredQuestionIncorrectlyPreviously(User user, Question question) {
        if(!(checkUserExists(user.getUsername()) || checkQuestionExists(String.valueOf(question.getId()))))
        {return null;}
        List<Mark> queriedMarks = (List<Mark>)(Object) hqlquerydatabase(String.format("FROM Mark WHERE QuestionID=%s",
                question.getId()));
        if(queriedMarks.isEmpty()){return false;}
        for(Mark qm: queriedMarks) {
            QuizSubmission sub = getQuizSubmission(qm.getSubmissionID().getId());
            if(sub.getUsername() == user) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a question from the database even if it is being used in a quiz.
     * @param questionID The question that should be removed from the database.
     * @return A boolean value depending on if the element was successfully removed from the database.
     */
    public static Boolean purgeQuestionFromDatabase(int questionID) {
        if(!checkQuestionExists(String.valueOf(questionID))) {
            return false;
        }
        int removalStatus = removeQuestion(String.valueOf(questionID));
        if(removalStatus == 2) {
            //foreign key error
            if(getQuestion(String.valueOf(questionID)).getQuestionType().equalsIgnoreCase("MCQ")) {
                List<QuestionOption> questionOptions =
                        (List<QuestionOption>)(Object) hqlquerydatabase(
                                String.format("FROM QuestionOption WHERE questionID=%s", questionID));
                for(QuestionOption option: questionOptions) {
                    removeFromDatabase(option);
                }
            }
            List<QuizQuestion> quizQuestions =
                    (List<QuizQuestion>)(Object) hqlquerydatabase(
                            String.format("FROM QuizQuestion WHERE questionID=%s", questionID));
            for(QuizQuestion question: quizQuestions) {
                removeFromDatabase(question);
            }
            List<Mark> marks =
                    (List<Mark>)(Object) hqlquerydatabase(
                            String.format("FROM Mark WHERE questionID=%s", questionID));
            for(Mark mark: marks) {
                removeFromDatabase(mark);
            }
            removeFromDatabase(getQuestion(String.valueOf(questionID)));
            return true;
        }
        else if(removalStatus == 1) {return false;}
        else if(removalStatus == 0) {return true;}
        return false;
    }

    /**
     * Gets all questions that a user has ever incorrectly answered as a list.
     * @param user User object of the user to query.
     * @return List of questions that the user has incorrectly answered in the past.
     */
    public static List<Question> getAllQuestionsUserIncorrectlyAnsweredEver(User user) {
        List<Question> questions = new ArrayList<>();
        List<Question> allQuestions = getAllQuestions();
        for(Question q: allQuestions) {
            if(hasUserAnsweredQuestionIncorrectlyPreviously(user, q)) {
                questions.add(q);
            }
        }
        return questions;
    }

    public static List<QuestionOption> getQuestionOptionsForQuestion(Question question) {
        if(!question.getQuestionType().equalsIgnoreCase("MCQ")) {
            return null;
        }
        List<QuestionOption> questionOptions = new ArrayList<>();
        questionOptions = (List<QuestionOption>)(Object) hqlquerydatabase(
                String.format("FROM QuestionOption WHERE questionID=%s", question.getId()));
        return questionOptions;
    }

    /**
     * Gets all the quizzes generated by a user.
     * @param user The user to query.
     * @return A list of quizzes. Null if the given user is invalid.
     */
    public static List<Quiz> getQuizzesBasedOnUser(User user) {
        if(!checkUserExists(user.getUsername())){return null;}
        List<Quiz> quizzes = (List<Quiz>)(Object) hqlquerydatabase(
                String.format("FROM Quiz WHERE username='%s'", user.getUsername()));
        return quizzes;
    }

    public static List<Question> getQuestionsBasedOnTopic(Topic topic) {
        if(!checkTopicExists(topic.getId())){return null;}
        List<Question> questions = (List<Question>)(Object) hqlquerydatabase(
                String.format("FROM Question WHERE topicName='%s'", topic.getId()));
        return questions;
    }

    public static void main(String[] args) {
    }
}
