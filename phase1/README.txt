Phase1 | Group number: group_0112

How To Begin The System:
You can start the system up by opening ConferenceDemo.java and running the main method. First you will see the current
state of the system, that is the data that is currently stored. THIS IS A DEBUG FEATURE ONLY, MEANT TO ASSIST THE TA
IN KEEPING TRACK OF CHANGES MADE TO THE SYSTEM.
You will then be prompted to chose whether you want to
    1. Register
    2. Login
    3. SHUTDOWN
If you chose SHUTDOWN, the state of the system will be saved.
If you chose register, you will have the option to register as an organizer or attendee. If you accidentally press
register, you can press 0 to go back and login.
If you chose login, you will be asked for your credentials. Only after both username and password have been inputted,
will the system verify the credentials. If you have inputted the wrong credentials, a message will appear saying
"incorrect username or password" and you will be sent back to the main page. Hence, if you accidentally click login,
type a random invalid userid and password and you will be redirected to the main menu.

If you input the correct userid and password, you will be directed to the options menu for your user type.
Here you can access your account specific tasks, or LOGOUT or SHUTDOWN. If you chose LOGOUT, you will be logged out
and be sent back to the main page, where you can either Login/Register/SHUTDOWN.

Things To Note:
1) If you want to delete everything and start with a fresh empty system, delete all 6 of the following ser files:
    AttendeeManager.ser
    ChatManager.ser
    EventManager.ser
    OrganizerManager.ser
    RoomManager.ser
    SpeakerManager.ser
    These files must be deleted all at once. IF YOU FAIL TO DELETE ALL 6 TOGETHER, THE SYSTEM WILL NOT WORK!!!

2) Once the time for an event has passed, our program will automatically delete it from the system, and all attendees
   will be derolled from that event, and that event will be removed from the speakers assigned events. This is so that
   users cannot enroll in an event that has already passed. THIS IS ONE OF OUR OPTIONAL EXTENSIONS FOR PHASE 2.

Important Facts to Know About The System:
1) DO NOT abruptly stop the program, this will cause issues. Please use the dedicated SHUTDOWN option, as this will
   save the state of the program.
2) When an organizer creates an event, he/she does not need to select the room. This is done automatically by the
   program!
3) When an organizer or speaker sends an automatic message, every recipient of that message will be automatically
   added to the organizer/speakers contact list if they are not already in it, and they will be able to privately
   message them.
4) A speaker can only send an automatic message to the attendees of the selected event that they are speaking at. A
   speaker can only reply to messages of people who are in their contact list. A speaker cannot add individual contacts.
5) When an organizer tries to schedule a speaker, they will only be shown the events that currently do not have a
   speaker.
6) When a user tries to join an event, they can only see a list of events that they are applicable to join. This mean
   an event that they are already enrolled for, will not appear on this list. Any events whose time conflicts with
   an event they are already attending, will not be shown on this list. Any event that does not have the capacity to
   enroll one more attendee will also not be shown on this list.
7) When an organizer creates an event, they will be asked for a time. This event will only be created if there is a room
   that is not occupied at that given time.
8) When an organizer deletes an event, that event will be removed from the list of attending events for all attendees
   attending that event.
9) When a organizer or attendee wants to send an private message to someone, they will be presented with their contact
   list. Hence to message someone, the user first has to add them to their contact list.
10)When an organizer creates a speaker, they will both be added to each others contact lists.

IF YOU FACE ANY UNEXPECTED ERRORS AND REQUIRE CLARIFICATION, PLEASE EMAIL US.




