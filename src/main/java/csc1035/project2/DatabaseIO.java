package csc1035.project2;

import csc1035.project2.DatabaseTables.*;
import org.hibernate.Session;
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

    //region User
    /**
     * Gets user related to the given username
     * @param username : String of the username of the user (case-insensitive)
     * @return User : User object representing the user. Null if not user associated with the username
     * **/
    public static User GetUser(String username) {
        List<User> userResponse = (List<User>)(Object)DatabaseIO.HQLQueryDatabase(String.format("FROM User WHERE " +
                "Username = '%s'", username.toLowerCase()));
        if(userResponse.size() == 0) {return null;}
        return userResponse.get(0);
    }

    /**
     * Checks if a user exists based on a given username (String)
     * @param username : String (case-insensitive)
     * @return Boolean: True if user exists, false if not
     * **/
    public static Boolean CheckUserExists(String username) {
        User u = GetUser(username);
        if (u == null) {return false;}
        return true;
    }

    /**
     * Checks whether a username (case-insensitive) is unique (valid) then adds the user to the table
     * (username will be lowercase added to the database).
     * Returns TblUser object of the new user if successful or null if the name was invalid.
     * @param username : String representation of the username
     * @return : TblUser object representing all user table information
     * **/
    public static User AddUser(String username) {
        if(!CheckUserExists(username)) {
            User usr = new User(username.toLowerCase());
            DatabaseIO.AddToDatabase(usr);
            return usr;
        }
        return null;
    }

    /**
     * Removes a given user from the tblUser by the username of the user (case-insensitive).
     * @param username : String representing the username entity attribute
     * @return int : 1 if user does not exist, 2 if user can't be removed (foreign key dependencies),
     * 0 if user existed and was removed successfully
     * **/
    public static int RemoveUser(String username) {
        if (!CheckUserExists(username)) {
            return 1;
        }
        try {
            DatabaseIO.RemoveFromDatabase(GetUser(username));
        }
        catch (Exception e){return 2;}
        return 0;
    }
    //endregion

    //region Topic
    /**
     * Gets topic related to the given topicName
     * @param topicName : String of the topic's name (case-insensitive)
     * @return User : Topic object representing the topic. Null if no topic associated with the topicName
     * **/
    public static Topic GetTopic(String topicName) {
        List<Topic> topicResponse = (List<Topic>)(Object)DatabaseIO.HQLQueryDatabase(String.format("FROM Topic WHERE " +
                "TopicName = '%s'", topicName.toLowerCase()));
        if(topicResponse.size() == 0) {return null;}
        return topicResponse.get(0);
    }

    /**
     * Checks if a topic exists based on a given topicName (String)
     * @param topicName : String (case-insensitive)
     * @return Boolean: True if topic exists, false if not
     * **/
    public static Boolean CheckTopicExists(String topicName) {
        Topic t = GetTopic(topicName);
        if (t == null) {return false;}
        return true;
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
        if(!CheckTopicExists(topicName)) {
            Topic topic = new Topic(topicName.toLowerCase(), topicDescription);
            DatabaseIO.AddToDatabase(topic);
            return topic;
        }
        return null;
    }

    /**
     * Removes a given topic from the tblTopic by the topicName of the topic (case-insensitive).
     * @param topicName : String representing the topicName entity attribute
     * @return int : 1 if topic does not exist, 2 if topic can't be removed (foreign key dependencies),
     * 0 if topic existed and was removed successfully
     * **/
    public static int RemoveTopic(String topicName) {
        if (!CheckTopicExists(topicName)) {
            return 1;
        }
        try {
            DatabaseIO.RemoveFromDatabase(GetTopic(topicName));
        }
        catch (Exception e){return 2;}
        return 0;
    }
    //endregion

    public static void main(String[] args) {
        // Query query = session.createQuery(hql String);
        // List<ItemEntity> results = query.list();
        // save item to the database: session.save();
    }
}
