package csc1035.project2.DatabaseTables;

import csc1035.project2.DatabaseIO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "tblUser")
public class User {
    @Id
    @Column(name = "Username", nullable = false, length = 50)
    private String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { this.username = username; }

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
}