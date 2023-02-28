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
  - Score (int)
  - Topic (string)
    - Stored initially as String in Question table, but can be changed to be a separate table for normalisation
- Results table
  - Username (string)
  - QuestionID (int)
  - Mark (int)
  - MaximumMark (int)

FooterNote:
Essay-style questions can be added in the future via displaying the answer to the user and asking them to self-mark, but as of right now, Single Answer and Multiple Choice Questions are the priority.

## Planning
### Oli

- 
### Kai

- 
### Denis 

- 
