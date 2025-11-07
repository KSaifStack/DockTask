NOTE: THIS WILL NOT WORK IN VS CODE RIGHT OFF THE BAT
YOU WILL NEED: JavaFX, IntelliJ(not really, but recommend for this)
Once everything is downloaded to set up JavaFX, you can watch the YouTube video below, and everything should run.
TO SET THE VM: THE MAIN FILE FOR INTELLIJ IS THE "LoginUi.java" FILE__

LINKS:
https://gluonhq.com/products/javafx/ (Version 21)
https://www.jetbrains.com/idea/download/?section=windows (Community Edition)
https://www.youtube.com/watch?app=desktop&v=IvsvjUq38Jc (IntelliJ Video more easier but does require you to get IntelliJ)
---------------------------------------------------------
Version updt:
- --- 
- NOTE: Project does take around 400mb-600mb(can lead up to the 1300mb or more)(Done)
- Redo CreateTaskUi(Done)
- Calander Updates better or have refresh button(Done)
- KeyBinds(Done)
- Refactored some old code(Done)
- Have update Uitask button update calander display(Done)
- Update the due date for the UpdateTaskUi(Done)**
- --- v0.2
- PluginUi - Uses internet connection to connect to a server or a github filled with downloadable plugins in which can be used
- EX: A StopWatch user can download from plugin section and will download
- EX: Motavational Quotes
- EX: Note Taker
- EX: Prayer Reminder
- can track amount of users made
- --- v1.0
- Settings-Delete all data,Export data, Import data,refresh button,KeyBinds changes?
- Auto Saving or conformation when you hit back without saving data(Done)
- Saved Dark Mode
- Useable Links
- FullScreened Task Desc
- Custom Window border(Notfations,color chan8ges with theme)
- Imports(Something idk what im saying here)
- --- v0.5
- when creating task your given the option to make a reg task or Reccuring Tasks
- Reccuring tasks are tasks that will atuomatcily appare every certain amount of days depending on the user can be removed through the task menu
- --- v 1.5
---------------------------------------------------------
AS OF 8/13/25:
NOTE: School is within a week or less so this current version will be the base but I will list every change I would like here after testing
- New tray Menu as JavaFx system integration is not the best(make your own Pane or use a third Party Tray Library)
- dorkbox SystemTray
- dorkbox Notify 
- have task be grayed out when mouse hovers over.
- more after testing...
- also everything in "TO DO" list below
- this project was kinda stressful but fun at the same time 8 out of 10..
---------------------------------------------------------
T0 DO:
NOTE: This project is reaching its end so everything that needs to be done will be listed here.
- Add Sun and Moon by the clock on top of the calander(Done)
- Trash functions(Done)
- Arrows for calander actions.(Done)
- See the tasks for each day on the calander.(Done)
- Settings window(overlaying window) Dark/Light Mode,Delete all data,Resart App
- Plugin window(overlaying window)
- Plugin Box in main gui 
- Color coded groups(Done)
- Red overdue task(Done)
- Enter Keybind/New task Keybinds(future up)
- System tray icon and system notifications for tasks(Done)
- Change all task timers that are over 48 hours to the day that its due in MM/DD/YR format(future up)
- (EXTRA) Make data encrypted 
---------------------------------------------------------
AS OF 7/17/25:
- Fixed login ui Case issues and Errors meg.
- Refind and add functionally to "UpdateTaskUI"
- Added overlaying windows(these windows overlay in the main window while providing different functions).
- UpdateTask ui implementations is complete.
- Main Ui task page is done(at last)
- NEW CLASS (Yes I cant spell :c)CalanderUi(can tell you the current day and shows a calander layout of tasks)
- UpdateTask Done
- Fixed fonts(Lexo and Latto)
- Used SubStrings to upper case 
---------------------------------------------------------
AS OF 7/4/25:
- Remade the homescreen menu and added a plugin button(features will be implemented soon).((Biggest lie))
- Added error messages on the login screen when the user data is not aligned with the chosen action.
- Functionality for task listing is done.
- Updated UserData with new function "ReturnData," which returns data in a string array.
- I added shadows when the  button hovers over a box option.
- Create Task Done
- Live clock implemented
- (New class) CustomDatePicker Class allows the user to pick a date instead of making them type it in one by one
- Updated Error messages within classes so the error message will remove the old one and get replaced by the new one without stacking
---------------------------------------------------------
Name ideas: To-Do, Listy, DueList, StudyDo, PlanForge, TaskSpark, StudyDo, FocusPilot
"Smarter Tasks. Sharper Mind"
Ui Color Scheme: Orange and Black/Blue and White
Logo: Orange and Black Calendar/Blue and White Calendar

Intended goal/idea: A list of the user's daily tasks, assessments, and daily activities. Unlike regular calendar apps and trackers, this app will give you the option to sort out stuff(ex, if it's a homework assignment, it will see it's a homework assignment and remind you differently because it's a homework assignment). Also, unlike other calendar apps and trackers, it will be much more detailed and focused, like you'll have the option of adding a date when the assignment is due(yes, even within a day) and stuff like the ability to study and practice questions, much like Quizlet, but focused more on active recall, motavial quotes, plugin like features.

To Do list flyout
A Windows flyout(or app) that will add a to-do list using Java
This project should give you an idea of how to make things work
DO NOT USE CHATGPT

Do you know how this will be done? - Java, JavaFX
