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
     * Checks whether a record exists in the database on a specified table by matching a search term with a record.
     * @param attribute String: Attribute in the table you with to search
     * @param entity String: Name of the database entity
     * @param searchTerm String: Term to see if record attribute matches search term
     * @return Object: null of object does not exist, object if it does exist
     * **/
    public static Object CheckExists (String searchTerm, String attribute, String entity) {
        List<Object> response = HQLQueryDatabase(String.format("FROM %s WHERE %s = %s", entity, attribute,
                searchTerm));
        if(response.size() != 0) {return response.get(0);}
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
        User newUser = (User)CheckExists(String.format("'%s'", username.toLowerCase()),
                "Username", "User");
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
        User usr = (User)CheckExists(String.format("'%s'", username.toLowerCase()),
                "Username", "User");
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
        User usr = (User)CheckExists(String.format("'%s'",username), "Username", "User");
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
        // Query query = session.createQuery(hql String);
        // List<ItemEntity> results = query.list();
        // save item to the database: session.save();
    }
}
