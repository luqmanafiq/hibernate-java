package csc1035.project2;

import csc1035.project2.DatabaseTables.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class DatabaseIO {
    private static Session _session = HibernateUtil.getSessionFactory().openSession();
    private static List<Object> HQLQueryDatabase(String hql) {
        _session.beginTransaction();
        Query query = _session.createQuery(hql);
        List<Object> results = query.list();
        _session.getTransaction().commit();
        return results;
    }

    private static List<Object[]> SQLQueryDatabase(String sql) {
        _session.beginTransaction();
        Query query = _session.createSQLQuery(sql);
        List<Object[]> results = query.list();
        _session.getTransaction().commit();
        return results;
    }

    private static void AddToDatabase(Object o) {
        _session.beginTransaction();
        _session.save(o);
        _session.getTransaction().commit();
    }

    private static void RemoveFromDatabase(Object o) {
        _session.beginTransaction();
        _session.delete(o);
        _session.getTransaction().commit();
    }

    /**
     * Checks if a user exists in tblUser with a given username.
     * @param username : String representing the username (case-insensitive)
     * @return : User object if user exists, null if user does not exist
     * **/
    public static User CheckUserExists(String username) {
        User newUser = new User(username.toLowerCase());
        List<User> currentUsers = (List<User>)(Object)HQLQueryDatabase("FROM User");
        for (User usr: currentUsers) {
            if(newUser.getUsername().equalsIgnoreCase(usr.getUsername())) {
                return usr;
            }
        }
        return null;
    }

    /**
     * Checks whether a username (case-insensitive) is unique (valid) then adds the user to the table
     * (username will be lowercase added to the database).
     * Returns TblUser object of the new user if successful or null if the name was invalid.
     * @param username : String representation of the username
     * @return : TblUser object representing all user table information
     * **/
    public static User AddUser(String username) {
        User newUser = CheckUserExists(username);
        if(newUser == null) {
            User usr = new User(username.toLowerCase());
            AddToDatabase(usr);
            return usr;
        }
        return null;
    }

    /**
     * Removes a given user from the tblUser by the username of the user (case-insensitive).
     * @param username : String representing the username entity attribute
     * @return int : 1 if user does not exist, 0 if user existed and was removed successfully
     * **/
    public static int RemoveUser(String username) {
        User usr = CheckUserExists(username);
        if (usr == null) {
            return 1;
        }
        RemoveFromDatabase(usr);
        return 0;
    }

    /**
     * Adds a new quiz to the database for a given user and quiz name as long as the user exists and a quiz of the
     * same name for a user does not exist.
     * @param quizName String: name of the quiz (case-insensitive)
     * @param username String: name of the user (case-insensitive)
     * @return Int: 0 if successful, 1 if user does not exist, 2 if quiz already exists
     * **/
    public static int AddQuiz(String username, String quizName) {
        List<TblQuiz> quizzes = (List<TblQuiz>)(Object) HQLQueryDatabase("FROM TblQuiz");
        User usr = CheckUserExists(username);
        if(usr == null) {return 1;}
        for(TblQuiz quiz : quizzes) {
            if(quiz.getQuizName().equalsIgnoreCase(quizName) &&
                    quiz.getUsername() == usr) {
                return 2;
            }
        }
        TblQuiz q = new TblQuiz(usr, quizName.toLowerCase());
        AddToDatabase(q);
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(AddQuiz("bob", "Science"));
        // Query query = session.createQuery(hql String);
        // List<ItemEntity> results = query.list();
        // save item to the database: session.save();
    }
}
