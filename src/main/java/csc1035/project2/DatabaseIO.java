package csc1035.project2;

import csc1035.project2.DatabaseTables.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class DatabaseIO {
    private static Session _session = HibernateUtil.getSessionFactory().openSession();
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

    public static void main(String[] args) {

    }
}
