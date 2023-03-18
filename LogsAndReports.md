# First Tuesday session 28/2/23
- 3/5 showed up. Denis, Oli and Kai
- Got to know each other, some icebreakers and banter to build team morale 
- Began ER diagram
- Tested how push and commits work as a group project
- planned DB structure (Denis and Oli)
- Looked at SQL for creating the table 
- A layout was created on the classes, variables, what methods to use

# Second Tuesday Session 7/3/23
- Denis struggled with pushes from intelliJ (not reliable)
- 5/5 showed up. Full group!
- Denis created Question class abstract (getters and setters too) and child class MCQ
- Decided Oli is mainly in charge of the DB (most experienced)
- Oli- creating classes for table entities for DB
- Dinoshan- started FrontEndUserIO (doing methods)
- Luke- generating report from answers
- Kai- began logging what everyone is doing, making sure no is duplicating work and began quiz class (possibly working with Denis.

Anyone is welcome to add and edit anything they have done to benefit them for the personal report 

# 8/3/23
- Denis
    - Merging parent question class to TblQuestion.java to remove duplicate code
    - Also making MCQ and SAQ subclasses of TblQuestion
    - made returnMark() case-insensitive
    - Quiz class now uses TblQuestion objects instead of Question

# 10/3/23
- Denis
  - Renamed Question.java to QuestionTypes to remove confusion with the Question.java in DatabaseTables folder

# 14/3/23
- Denis
  - UserIO
    - built menu() method with 11 options
    - built listSubmenu() method for options for listing questions
    - built promptUsername() to get the user's username and store it statically for the entire runtime of main()
    - built helper methods
      - printQuestion() takes a question input and neatly prints the attributes
      - stringValidInput() prompts for a string input until one is given by checking if the length is greater than 0
      - menuValidInput() prompts for an integer until one in the given range of minimum integer to maximum integer is given - this is useful for menus

# 18/3/23
- Denis
  - UserIO
    - added randomQuizGenSubmenu() for option 8 in main menu
    - Refactored some of the code in listSubmenu() to chooseTopic() and chooseType() to allow reuse of the code for randomQuizGenSubmenu()
    - completed randomQuizGenSubmenu() method that asks for each filter for the generated quiz and calls generateQuiz()
    - started generateQuiz()