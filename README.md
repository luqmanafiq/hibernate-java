CSC1035 Project 2 (Team Project)
================================

Details on how to connect with your database can be found on your Canvas group.

Get overwritten part 2 - overwritten - Denis Dysen

## Relevant Links
- https://ncl.instructure.com/courses/48552/pages/csc1035-project-2-team-project?module_item_id=2645028
- https://ncl.instructure.com/courses/48552/pages/project-2-faqs-and-hints?module_item_id=2645037
- https://ncl.instructure.com/courses/48552/pages/connecting-to-your-database-using-intellij-ultimate-on-campus-cluster-room?module_item_id=2645033
- https://ncl.instructure.com/groups/44138/pages/resources

## Database Structure
- Question Table
  - QuestionType (string)
  - ID (int) (automatic + unique)
  - QuestionString (string)
  - AnswerString (string)
  - MaximumMark (int)
  - Topic (string)
    - Stored initially as String in Question table, but can be changed to be a separate table for normalisation
- Mark table
  - Quiz
  - Username
  - DateTime
    - Recorded when the entire quiz finishes
    - Two quizzes cannot be submitted at the samee time by the same user
    - Is a composite key with Username and Quiz
  - Question
    - Foreign Key to ID in Question table 
  - Mark
- Quiz table
  - NameOfQuiz
  - Username (user that made the quiz)
- QuizQuestion
  - Quiz
  - Question
  - Order (int)  

FooterNote:
Essay-style questions can be added in the future via displaying the answer to the user and asking them to self-mark, but as of right now, Single Answer and Multiple Choice Questions are the priority.

### Olis suggested Database Modification
tblQuiz
-    UserID (Auto Number) (foreign key)
-    QuizID (primary key) (Number)
-    QuizName (String)
     Explanation: Quizzes can be created by multiple users so the userID needs to be stored so that we can identify which quiz is related to which user. QuizID used to represent a quiz as the quiz name may be the same (user1 could have a quiz called science but so could user2 so the quizname can’t be the primary key).

tblQuestion
-	QuestionID (Number) (Primary Key)
-	Question (String)
-	Answer (String)
-	MaximumMarks (Number)
-	QuestionType (String)
-	TopicName (foreign key) (String)
     Explanation: Stores the questions and answers for quizzes. QuestionID uniquely identifies a question, topicName is a foreign key to the tblTopic.

tblQuizQuestion
-    QuizID (foreign key) (Number)
-    QuestionID (foreign key) (Number)
-    QuestionIndex (Number)
     Explanation: Quiz question links a question to a specific quiz. Composite primary key created by the two foreign keys. Order is used to determine the index of the question in a quiz.

tblTopic
-    TopicName (String) (Primary key)
-    Topic Description (String)
     Explanation: Used to reference a topic (used in question table). Thought it should be in a separate class so that if someone was to alter a topic name, they could do so by altering this table only (not having to loop through all values in the question table).

tblUser
-    UsersName (String) (Primary key)
-    UserID (Auto Number) (Primary key)
     Explanation: Defines a user. Two users may share the same name so a UserID should be used to uniquely identify each user.

tblQuizSubmission
-    SubmissionID (Auto Number) (Primary key)
-    DateOfCompletion (Date/Time)
-    UserID (foreign key) (Number)
-    QuizID (foreign key) (Number)
     Explanation: Quiz Submission represents when a user submits a quiz. It has a submissionID field to identify the quizzes submission uniquely. The userID represents the user that took the quiz (QuizID) at the DateOfCompletion.

tblMark
-    submissionID (foreign key) (Number)
-    QuestionID (foreign key) (Number)
-    Score (number)
     Explanation: Stores the mark for each question submitted. Query can be made with the submissionID (representing the quiz submitted) to get all marks for questions taken in that quiz.


