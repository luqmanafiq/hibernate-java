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
        + "10. Review incorrectly answered questions");
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
}