package csc1035.project2;

import java.util.Scanner;

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
//                listQuestions();
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
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

    private static void listSubmenu() {
        System.out.println("List menu\nSelect an option:\n"
        + "1. List all questions"
        + "2. List all questions by type"
        + "3. List all questions by topic");
        switch(menuValidInput(1, 3)) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
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