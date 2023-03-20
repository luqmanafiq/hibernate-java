package csc1035.project2;

import csc1035.project2.DatabaseTables.*;
import org.hibernate.dialect.Database;

import java.util.ArrayList;
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
        List<Quiz> addedQuizzes = new ArrayList<>();
        for(String quizName: quizNames) {
            User u = users.get(new Random().nextInt(users.size()));
            Quiz addedQuiz = DatabaseIO.AddQuiz(new Quiz(u, quizName));
            System.out.println(String.format("Add quiz to database test: adding quiz '%s' for user '%s'. Result: '%s'",
                    quizName, u.getUsername(), addedQuiz));
            if(addedQuiz != null) {
                addedQuizzes.add(addedQuiz);
            }
        }
    }

    public static void AddSCQ() {
        List<Question> questions = new ArrayList<>();
        List<Topic> topics = DatabaseIO.GetAllTopics();
        questions.add(new Question("What is the capital of France?", "Paris", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the highest mountain in the world?", "Mount Everest", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("Who is the current president of the United States?", "Joe Biden", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the symbol for sodium in the periodic table?", "Na", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the largest mammal in the world?", "Blue whale", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the boiling point of water in Celsius?", "100", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the currency of Japan?", "Yen", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the highest grossing movie of all time?", "Avatar", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the name of the longest river in South America?", "Amazon River", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the speed of light in meters per second?", "299792458", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        for(var question: questions) {
            System.out.println(String.format("Add single choice questions to database test: " +
                    "question: '%s', answer: '%s', marks: %s, questionType: '%s', topic: '%s', RESPONSE: '%s'",
                    question.getQuestion(), question.getAnswer(), question.getMaximumMarks(), question.getQuestionType(),
                    question.getTopicName().getId(), DatabaseIO.AddQuestion(question)));
        }
    }

    public static void AddMCQWithOptions() {
        List<Topic> topics = DatabaseIO.GetAllTopics();
        List<QuestionOption> questionOptions = new ArrayList<>();
        Question q = DatabaseIO.AddQuestion(new Question("What is the capital of France?", "Paris", 1, "MCQ", topics.get(new Random().nextInt(topics.size()))));
        if(q != null) {
            questionOptions.add(DatabaseIO.AddQuestionOption(new QuestionOption(q, "Paris")));
            questionOptions.add(DatabaseIO.AddQuestionOption(new QuestionOption(q, "London")));
            questionOptions.add(DatabaseIO.AddQuestionOption(new QuestionOption(q, "Rome")));
        }
        q = DatabaseIO.AddQuestion(new Question("What is the largest organ in the human body?", "Skin", 1, "MCQ", topics.get(new Random().nextInt(topics.size()))));
        if(q != null) {
            questionOptions.add(DatabaseIO.AddQuestionOption(new QuestionOption(q, "Skin")));
            questionOptions.add(DatabaseIO.AddQuestionOption(new QuestionOption(q, "Liver")));
            questionOptions.add(DatabaseIO.AddQuestionOption(new QuestionOption(q, "Brain")));
            questionOptions.add(DatabaseIO.AddQuestionOption(new QuestionOption(q, "Heart")));
        }

        q = DatabaseIO.AddQuestion(new Question("Which planet in our solar system is closest to the sun?", "Mercury", 1, "MCQ", topics.get(new Random().nextInt(topics.size()))));
        if(q != null) {
            questionOptions.add(DatabaseIO.AddQuestionOption(new QuestionOption(q, "Mars")));
            questionOptions.add(DatabaseIO.AddQuestionOption(new QuestionOption(q, "Mercury")));
            questionOptions.add(DatabaseIO.AddQuestionOption(new QuestionOption(q, "Venus")));
            questionOptions.add(DatabaseIO.AddQuestionOption(new QuestionOption(q, "Jupiter")));
        }
    }

    public static void AddQuizQuestions() {
        List<Quiz> quizzes = DatabaseIO.GetAllQuizzes();
        List<Question> questions = DatabaseIO.GetAllQuestions();
        for(Quiz quiz: quizzes) {
            for(int i = 0; i < 3; i++) {
                Question randQuestion = questions.get(new Random().nextInt(questions.size()));
                System.out.println(String.format("Add QuizQuestion to database test: Quiz: '%s', Question: '%s'," +
                                " Response: '%s'", quiz.getQuizName(), randQuestion.getQuestion(),
                        DatabaseIO.AddQuizQuestion(new QuizQuestion(quiz, randQuestion, -1))));
            }
        }
    }

    public static void main(String[] args) {
        AddUsers();
        AddTopics();
        AddQuizzes();
        AddSCQ();
        AddMCQWithOptions();
        AddQuizQuestions();
    }
}
