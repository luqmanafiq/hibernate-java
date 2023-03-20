package csc1035.project2;

import csc1035.project2.DatabaseTables.Topic;

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

    public static void main(String[] args) {
        AddUsers();
        AddTopics();
    }
}
