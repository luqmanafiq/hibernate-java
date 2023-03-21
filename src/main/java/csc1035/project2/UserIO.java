package csc1035.project2;

import java.util.Scanner;
import java.util.Random;

import csc1035.project2.DatabaseTables.*;
import org.hibernate.dialect.Database;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class UserIO {
    static Scanner scan = new Scanner(System.in);
    static User user;
    public static void main(String[] args) {
        user = promptUsername();
        while (true) {
            menu();
        }
    }
    
    private static User promptUsername() {
        User userToReturn = null;
        System.out.println("What is your username:");
        String username = stringValidInput();
        if(DatabaseIO.CheckUserExists(username.trim())) {
            return DatabaseIO.GetUser(username.trim());
        }
        else {
            System.out.println("This user does not currently exist, would you like to create a new user with this name (Y)es or (N)o?");
            Boolean validAnswer = false;
            while (!validAnswer) {
                String userAnswer = scan.nextLine();
                if(userAnswer.equalsIgnoreCase("Y")) {
                    User addedUser = DatabaseIO.AddUser(username.trim());
                    if(addedUser != null) {
                        System.out.println("Added user to the database!");
                        validAnswer = true;
                        userToReturn = addedUser;
                    }
                    else {
                        System.out.println("Error adding user to the database, try again.");
                        promptUsername();
                    }
                }
                else if(userAnswer.equalsIgnoreCase("N")) {
                    promptUsername();
                }
                else {
                    System.out.println("invalid option choice!");
                    System.out.println("Would you like to create a new user with this name (Y)es or (N)o?");
                }
            }
            return userToReturn;
        }
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
                //Search for a quiz to play
                PlayQuiz();
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
                crudQuizSubmenu();
                break;
            case 7:
                crudQuestionSubmenu();
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

    private static int GetUserOption(String[] userOptions, String question) {
        Boolean isValidChoice = false;
        int userChoiceToReturn = 0;
        while(!isValidChoice) {
            System.out.println(question);
            for (int i = 0; i < userOptions.length; i++) {
                System.out.println(String.format("%s - %s", i, userOptions[i]));
            }
            String userInput = scan.nextLine();
            try {
                int userMenuChoice = Integer.parseInt(userInput);
                if(userMenuChoice >= 0 && userMenuChoice < userOptions.length) {
                    userChoiceToReturn = userMenuChoice;
                    isValidChoice = true;
                }
                else {
                    System.out.println("That menu choice is invalid! Please try again.");
                }
            }
            catch(Exception e) {
                System.out.println("That menu choice is invalid! Please try again.");
            }
        }
        return userChoiceToReturn;
    }

    private static void PlayQuiz() {
        int userQuizOption = GetUserOption(new String[]{"quizzes created by you.", "quizzes created by everyone."},
                String.format("Would you like to select quizzes created by you ('%s') or from all quizzes " +
                        "in the database?", user.getUsername()));
        List<Quiz> quizSelection = new ArrayList<>();
        switch(userQuizOption) {
            case(0):
                //list only users quizzes
                List<Quiz> allQuizzes = DatabaseIO.GetAllQuizzes();
                for(var quiz: allQuizzes) {
                    if(quiz.getUsername().equals(user)) {
                        quizSelection.add(quiz);
                    }
                }
                break;
            case(1):
                //list all quizzes
                quizSelection = DatabaseIO.GetAllQuizzes();
                break;
        }
        String[] quizOptions = new String[quizSelection.size()];
        for(int i = 0; i < quizSelection.size(); i++) {
            quizOptions[i] = String.format("'%s' - '%s'", quizSelection.get(i).getUsername().getUsername(),
                    quizSelection.get(i).getQuizName());
        }
        if(quizOptions.length == 0) {
            System.out.println("No quizzes could be found!");
        }
        else {
            int quizChoice = GetUserOption(quizOptions, "Please select a quiz to play:");
            Quiz quizToPlay = quizSelection.get(quizChoice);
            PlayAndSubmitQuiz(quizToPlay);
        }
     }

    private static List<QuestionMarkTuple> QuestionAnswerLoop(List<Question> questionsToAnswer) {
         List<QuestionMarkTuple> questionMarkTuples = new ArrayList<>();
        for(Question question: questionsToAnswer) {
            if(question.getQuestionType().equalsIgnoreCase("SAQ")) {
                System.out.println(String.format("%s", question.getQuestion()));
                String userAnswer = scan.nextLine();
                questionMarkTuples.add(DatabaseIO.MarkQuestionAnswer(question, userAnswer));
            }
            else if(question.getQuestionType().equalsIgnoreCase("MCQ")) {
                List<QuestionOption> optionsForQuestion = DatabaseIO.GetQuestionOptionsForQuestion(question);
                String[] questionOptions = new String[optionsForQuestion.size()];
                for(int i = 0; i < optionsForQuestion.size(); i++) {
                    questionOptions[i] = optionsForQuestion.get(i).getQuestionOption();
                }
                int userOption = GetUserOption(questionOptions,question.getQuestion());
                String userAnswer = questionOptions[userOption];
                questionMarkTuples.add(DatabaseIO.MarkQuestionAnswer(question, userAnswer));
            }
        }
        return questionMarkTuples;
    }

    private static void DisplayMarks(List<QuestionMarkTuple> marks, List<Question> questions) {
        int totalScore = 0;
        int totalPossibleMarks = 0;
        for(int i = 0; i < marks.size(); i++) {
            System.out.println(String.format("Question %s - '%s'\nYour answer: '%s'\nCorrect answer: " +
                    "'%s'\nScore: %s/%s", i, questions.get(i).getQuestion(), marks.get(i).get_userAnswer(),
                    questions.get(i).getAnswer(), marks.get(i).GetMarksReceived(), questions.get(i).getMaximumMarks()));
            totalPossibleMarks += questions.get(i).getMaximumMarks();
            totalScore += marks.get(i).GetMarksReceived();
        }
        System.out.println(String.format("Your final score for that quiz was %s/%s",
                totalScore, totalPossibleMarks));
    }


    /**
     *
     * @param quizToPlay
     * @return null if no questions in a quiz or a quiz does not exist.
     */
    private static QuizSubmission PlayAndSubmitQuiz(Quiz quizToPlay) {
        List<QuestionMarkTuple> questionsAndMarks = new ArrayList<>();
        System.out.println(String.format("You are now playing '%s' created by '%s'.",
                quizToPlay.getQuizName(), quizToPlay.getUsername().getUsername()));
        if(!DatabaseIO.CheckQuizExists(String.valueOf(quizToPlay.getId()))) {
            System.out.println("Error! it seems the chosen quiz does not exist.");
            return null;
        }
        List<Question> questionsToAnswer = DatabaseIO.GetQuestionsFromQuiz(quizToPlay.getId());
        if(questionsToAnswer.isEmpty()) {
            System.out.println("This quiz is empty.");
            return null;
        }
        questionsAndMarks = QuestionAnswerLoop(questionsToAnswer);
        QuizSubmission submission = DatabaseIO.SubmitQuizResults(questionsAndMarks, user, quizToPlay);
        System.out.println("Quiz complete and submitted!");
        DisplayMarks(questionsAndMarks, questionsToAnswer);
        //TEST THIS
        return submission;
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

    private static void crudQuestionSubmenu() {
        System.out.println("[Question CRUD submenu]\nSelect an option:\n"
        + "1. Create a question"
        + "2. Read a question"
        + "3. Update a question"
        + "4. Delete a question");

        switch(menuValidInput(1, 4)) {
            case 1:
                createQuestion();
                break;
            case 2:
                System.out.println("Enter a question ID:");
                printQuestion(DatabaseIO.GetQuestion(String.valueOf(positiveIntegerValidInput())));
                break;
            case 3:
                break;
            case 4:
                System.out.println("Enter a question ID:");
                DatabaseIO.PurgeQuestionFromDatabase(positiveIntegerValidInput());
                System.out.println("Question has been deleted");
                break;
        }
    }

    private static void createQuestion() {
        System.out.println("[Creating a question]");
        System.out.println("Question string:");
        String quString = stringValidInput();
        String quType = chooseType();
        String quTopic;
        System.out.println("Use an existing topic? (Y/N)");
        if (scan.nextLine().toLowerCase().equals("y")) {
            quTopic = chooseTopic();
        }
        System.out.println("Enter a new topic name:");
        quTopic = stringValidInput();
        System.out.println("Enter a description for the new topic");
        Topic topic = DatabaseIO.AddTopic(quTopic, scan.nextLine());

        System.out.println("Score:");
        int quScore = positiveIntegerValidInput();

        System.out.println("Answer string:");
        String quAnswer = stringValidInput();
        Question question = new Question(quString, quAnswer, quScore, quType, topic);
        Question dbQuestion = DatabaseIO.AddQuestion(question);

        if (quType.equals("MCQ")) {
            DatabaseIO.AddQuestionOption(new QuestionOption(dbQuestion, quAnswer));
            System.out.println("How many extra options do you want to add?");
            for (int i = 0; i < positiveIntegerValidInput(); i++) {
                System.out.println("Enter option string:");
                DatabaseIO.AddQuestionOption(new QuestionOption(dbQuestion, stringValidInput()));
                System.out.println("Option added to database");
            }
        }

        System.out.println("To add this question to a quiz, use the CRUD quiz menu option");
                
    }

    private static int positiveIntegerValidInput() { // prompts for user input until an integer greater than 0 is entered
        boolean success = false;
        int number = 0;
        while (!success) {
            try {
                number = scan.nextInt();
                if (number > 0) {
                    System.out.println("Input accepted");
                    scan.nextLine();
                    success = true;
                } else {
                    scan.nextLine();
                    System.out.println("Error - integer must be greater than 0 \nTry again: ");
                }
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("Error - input was not an integer \nTry again: ");
            }
        }
        return number;
    }
    
    private static void crudQuizSubmenu() {
        System.out.println("[Quiz CRUD submenu]\nSelect an option:\n"
        + "1. Create a quiz"
        + "2. Read a quiz"
        + "3. Update a quiz"
        + "4. Delete a quiz");

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
                    if (i.getTopicName().getId().toLowerCase() == topicInput.toLowerCase()) {
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
        String topic = "ALL";
        if (response.toLowerCase().equals("y")) {
            topic = chooseTopic();
        }

        System.out.println("Choose a specific question type?: (Y/N)");
        response = scan.nextLine();
        String type = "ALL";
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
        Quiz generator = DatabaseIO.AddQuiz(new Quiz(user, "RNG:"+user+"|QUcount:"+questionCount+"|Topic:"+topic+"|Type:"+type+"|HistoricalBadAnswer:"+wronglyAnsweredQus));
        ArrayList<Question> validQuestions = new ArrayList<Question>();
        List<Question> possibleQuestions;
        
        if (wronglyAnsweredQus) {
            possibleQuestions = DatabaseIO.GetAllQuestionsUserIncorrectlyAnsweredEver(user);
        }
        else {
            possibleQuestions = DatabaseIO.GetAllQuestions();
        }

        for (Question i : possibleQuestions) {
            if ( (i.getTopicName().getId() == topic || topic == "ALL") && (i.getQuestionType() == type || type == "ALL") ) {
                validQuestions.add(i);
            }
        }

        Random rng = new Random();
        for (int i = 0; i < questionCount; i++)
        {
            int random = rng.nextInt(validQuestions.size());
            DatabaseIO.AddQuizQuestion(new QuizQuestion(generator, validQuestions.get(random), i));          
            validQuestions.remove(random);
        }

        return generator;
    }
    
    private static String chooseTopic() {
        System.out.println("Choose a topic:");
        List<Topic> TopicList = DatabaseIO.GetAllTopics();
        for (Topic i : TopicList) {
            System.out.println(i.getId());
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