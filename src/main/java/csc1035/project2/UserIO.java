package csc1035.project2;

import java.util.Scanner;

import com.mysql.fabric.xmlrpc.base.Data;

import csc1035.project2.DatabaseTables.*;

import java.util.ArrayList;
import java.util.List;

public class UserIO {
    static Scanner scan = new Scanner(System.in);
    static String userName;
    public static void main(String[] args) {
        userName = promptUsername();
        while (true) {
            menu();
        }
    }
    
    private static String promptUsername() {
        System.out.println("What is your username:");
        String username = stringValidInput();
        return username;
    }

    private static void menu() {
        System.out.println("Main menu\nSelect an option:\n"
        + "1. Search for a quiz to play\n" //submenu
        + "2. Choose a topic of questions to play\n"
        + "3. List questions\n" //submenu
        + "4. Import a list of questions\n"
        + "5. Export a list of questions\n"
        + "6. CRUD a quiz\n"
        + "7. CRUD a question\n"
        + "8. Randomly generate a quiz\n" //submenu
        + "9. Produce a short report\n"
        + "10. Review incorrectly answered questions\n"
        + "11. Exit\n");
  
        switch (menuValidInput(1, 11)) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                listSubmenu();
                break;
            case 4:
                importFromCSV();
                break;
            case 5:
            exportToCSV();
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                randomQuizGenSubmenu();
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                System.exit(0);;
                break;
            default:
                System.out.println("This is not a valid option\n");
                break;
        }

    }

    private static void importFromCSV() {
        // unsure of necessary syntax for filepath, if it should be absolute or relative
        System.out.println("What is the name of the file you want to import?\n"
        + "It should be a csv file and include the .csv extension. The filename is case-insensitive");
        String filePath = scan.nextLine();
        List<Question> questionList = DatabaseIO.ImportQuestionsFromCSV(filePath);
        if (questionList != null) {
            for (Question i : questionList) {
                DatabaseIO.AddQuestion(i);
            }
            System.out.println("File read, and the questions were added to the database");
        }
        else {
            System.out.println("File does not exist, or was not read");
        }
    }

    private static void exportToCSV() {
        System.out.println("What is the name of the file you want to create? (.csv extension is not necessary):");
        String fileName = stringValidInput();
        System.out.println("What directory do you want to store the file in?");
        String filePath = stringValidInput();
        System.out.println("Add a question to the file using its ID. You will be repeatedly prompted until you enter nothing");
        List<Question> questionList = new ArrayList<Question>();
        while (true) {
            System.out.println("Enter ID");
            String input = scan.nextLine();
            if (input.length() == 0) {
                break;
            }
            questionList.add(DatabaseIO.GetQuestion(input));
        }
        System.out.println("Question list has been created");
        System.out.println("Exporting to CSV");
        int status = DatabaseIO.ExportQuestionsToCSV(filePath, fileName, questionList);
        switch (status) {
            case 0:
                System.out.println("Success!");
                break;
            case 1:
                System.out.println("Directory does not exist");
                break;
            case 2:
                System.out.println("Writing to file failed");
                break;
        }          
    }
    
    private static void listSubmenu() {
        System.out.println("[Question Lister submenu]\nSelect an option:\n"
        + "1. List all questions"
        + "2. List all questions by type"
        + "3. List all questions by topic");
        switch(menuValidInput(1, 3)) {
            case 1:
                List<Question> QuestionList1 = DatabaseIO.GetAllQuestions();
                for (Question i : QuestionList1) {
                    printQuestion(i);
                }      
                break;
            case 2:               
                String typeInput = chooseType();

                List<Question> QuestionList2 = DatabaseIO.GetAllQuestions();
                for (Question i : QuestionList2) {
                    if (i.getQuestionType().toLowerCase() == typeInput.toLowerCase())
                    printQuestion(i);
                }   
                break;
            case 3:
                String topicInput = chooseTopic();

                List<Question> QuestionList3 = DatabaseIO.GetAllQuestions();
                for (Question i : QuestionList3) {
                    if (i.getTopicName().getTopicDescription().toLowerCase() == topicInput.toLowerCase()) {
                        printQuestion(i);
                    }               
                }   
                break;
        }
    }

    private static void randomQuizGenSubmenu() {
        System.out.println("[Random Quiz Generator submenu]\n"
        + "How many questions?:\n"
        + "1. 5\n"
        + "2. 10\n"
        + "3. 15\n"
        + "4. 20\n");
        
        int questionCount = menuValidInput(1, 4) * 5;

        System.out.println("Choose a specific topic?: (Y/N)");
        String response = scan.nextLine();
        String topic = "";
        if (response.toLowerCase().equals("y")) {
            topic = chooseTopic();
        }

        System.out.println("Choose a specific question type?: (Y/N)");
        response = scan.nextLine();
        String type = "";
        if (response.toLowerCase().equals("y")) {
            type = chooseType();
        }

        System.out.println("Use only questions you have answered incorrectly before? (Y/N)");
        response = scan.nextLine();
        boolean wronglyAnsweredQus = false;
        if (response.toLowerCase().equals("y")) {
            wronglyAnsweredQus = true;
        }
        Quiz generatedQuiz = generateQuiz(questionCount, topic, type, wronglyAnsweredQus);
        System.out.println("Quiz generated!\n"
        + "Name: " + generatedQuiz.getQuizName() + "\n"
        + "Username: " + generatedQuiz.getUsername() + "\n"
        + "ID: " + generatedQuiz.getId());
    }

    private static Quiz generateQuiz(int questionCount, String topic, String type, boolean wronglyAnsweredQus) {
        Quiz generater = new Quiz();
        ArrayList<Question> validQuestions = new ArrayList<Question>();




        return generater;
    }
    
    private static String chooseTopic() {
        System.out.println("Choose a topic:");
        List<Topic> TopicList = DatabaseIO.GetAllTopics();
        for (Topic i : TopicList) {
            System.out.println(i.getTopicDescription());
        }
        return scan.nextLine();
    }

    private static String chooseType() {
        System.out.println("Choose a question type: \nMCQ \nSAQ");
        return scan.nextLine();
    }

    private static void printQuestion(Question qu) {
        System.out.println("ID: [" +qu.getId()+ "] \n"
                    + "Question: [" +qu.getQuestion()+ "]\n"
                    + "Type: [" +qu.getQuestionType()+ "]\n"
                    + "Topic: [" +qu.getTopicName()+ "]\n"
                    + "Marks: [" +qu.getMaximumMarks()+ "]\n"
                    + "Answer: [" +qu.getAnswer()+ "]");
    }

    private static String stringValidInput() { // prompts for string input, accepts any non-empty string
        String text = "";
        boolean success = false;
        while (!success) {
            text = scan.nextLine();
            if (text.length() > 0) {
                success = true;
            } else {
                System.out.println("Error - no characters typed \nTry again: ");
            }
        }
        return text;
    }

    private static int menuValidInput(int minInput, int maxInput) { // prompts for user input until an integer greater than 0 is entered
        boolean success = false;
        int number = -1;
        while (!success) {
            try {
                number = scan.nextInt();
                if (number >= minInput && number <= maxInput) {
                    System.out.println("Input accepted");
                    scan.nextLine();
                    success = true;
                } else {
                    scan.nextLine();
                    System.out.println("Error - integer must be greater than "+minInput+" and less than "+maxInput+"\nTry again: ");
                }
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("Error - input was not an integer \nTry again: ");
            }
        }
        return number;
    }

}