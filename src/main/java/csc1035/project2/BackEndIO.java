package csc1035.project2;

import csc1035.project2.DatabaseTables.Mark;
import csc1035.project2.DatabaseTables.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BackEndIO {

    public static void main(String[] args) {

        //Ask the user for the number of questions
        System.out.print("Please enter the number of questions: ");
        Scanner scanner = new Scanner(System.in);
        int totalQuestions = scanner.nextInt();

        //Ask the user for the correct answers
        System.out.println("Please enter the correct answers:");
        Object[] correctAnswers = new Object[totalQuestions];
        for (int i = 0; i < totalQuestions; i++) {
            correctAnswers[i] = scanner.next();
        }

        //Ask the user for the user's answers
        System.out.println("Please enter your answers:");
        Object[] userAnswers = new Object[totalQuestions];
        for (int i = 0; i < totalQuestions; i++) {
            userAnswers[i] = scanner.next();
        }

        //Loop through the questions and answers to check correctness
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        List<String> userAnswersList = new ArrayList<>();
        for (int i = 0; i < totalQuestions; i++) {
            String question = "Question " + (i + 1);
            String answer = correctAnswers[i].toString();
            String userAnswer = userAnswers[i].toString();
            questions.add(question);
            answers.add(answer);
            userAnswersList.add(userAnswer);
        }

        //Calculate number of correct answers
        int numCorrect = 0;
        for (int i = 0; i < questions.size(); i++) {
            String question = questions.get(i);
            String answer = answers.get(i);
            String userAnswer = userAnswersList.get(i);

            if (answer.equals(userAnswer)) {
                numCorrect++;
            }
        }

        //Calculate the final mark
        double finalMark = ((double) numCorrect / totalQuestions) * 100;

        //Print out the quiz results
        System.out.println("Quiz Results:");
        System.out.println("Number of Correct Questions: " + numCorrect);
        System.out.println("Number of Incorrect Questions: " + (totalQuestions - numCorrect));
        System.out.println("Incorrect Answers: ");
        for (int i = 0; i < questions.size(); i++) {
            String question = questions.get(i);
            String answer = answers.get(i);
            String userAnswer = userAnswersList.get(i);
            if (!answer.equals(userAnswer)) {
                System.out.println("Question " + (i + 1) + ": " + question + " (Correct answer: " + answer + ", Your answer: " + userAnswer + ")");
            }
        }
        System.out.println("Final Mark: " + finalMark);


        int submissionID = 0; // Replace with the ID of the quiz submission
        List<Mark> marks = DatabaseIO.getMarksFromSubmission(submissionID);

        //Print out the details of each mark
        System.out.println("Marks:");
        for (Mark mark : marks) {
            System.out.println("Mark ID: " + mark.getUserAnswer());
            System.out.println("Quiz Submission ID: " + mark.getSubmissionID());
            System.out.println("Question ID: " + mark.getQuestionID());
            System.out.println("Mark: " + mark.getScore());
            System.out.println("----------------");
        }
    }
}



/*

COMMUNICATE WITH FRONT END USER

To produce a short report of the quiz results,
you will need to write code to read the quiz questions and user responses from the database. You will then need to compare the user responses to the correct answer and count the number of correct and incorrect answers.
Finally, you will need to calculate the user's final mark based on the number of correct and incorrect answers.

To log the user's quiz result,
you will need to write code to store the user's quiz result in the database. This should include the user's responses to each question, the correct answer, and the user's final mark.

To allow the user to review questions they have answered incorrectly in the past,
you will need to write code to retrieve the user's results from the database. You will then need to compare the user's responses to the correct answer and display the questions that were answered incorrectly.

 */
