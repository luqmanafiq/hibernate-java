package csc1035.project2;

import csc1035.project2.DatabaseTables.Quiz;
import csc1035.project2.DatabaseTables.Topic;
import csc1035.project2.DatabaseTables.User;
import org.hibernate.dialect.Database;

import java.util.List;
import java.util.Random;

public class Testing {
    public static void AddUsers() {
        String[] users = new String[]{"pam","jim","michael","dwight","angela","creed","kevin","kelly","oscar", "oScAr"};
        for(String username: users){
            System.out.println(String.format("Add user to database test: adding user '%s'. Result: '%s'",
                    username, DatabaseIO.AddUser(username)));
        }
    }

    public static void AddTopics() {
        String[] topics = new String[]{"Biology", "Computer Science", "History", "Math", "English", "EnGliSh"};
        for(String topic: topics) {
            System.out.println(String.format("Add topic to database test: adding topic '%s'. Result: '%s'",
                    topic, DatabaseIO.AddTopic(new Topic(topic, "dummy description for testing."))));
        }
    }

    public static void AddQuizzes() {
        String[] quizNames = new String[]{"Quiz 1", "Quiz 2", "Quiz 3", "Quiz 3"};
        List<User> users = DatabaseIO.GetAllUsers();
        for(String quizName: quizNames) {
            User u = users.get(new Random().nextInt(users.size()));
            System.out.println(String.format("Add quiz to database test: adding quiz '%s' for user '%s'. Result: '%s'",
                    quizName, u, DatabaseIO.AddQuiz(new Quiz(u, quizName))));
        }
    }

    public static void main(String[] args) {
        AddUsers();
        AddTopics();
        AddQuizzes();
    }
}
