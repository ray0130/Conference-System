package Controller;

import Entities.Event;
import Gateway.KeyboardInput;
import Presenter.TextPresenter;
import UseCases.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * OrganizerSystem controller implements various actions that can be done for an organizer, including
 * create a new speaker, schedule a speaker, message other Users, create/delete Events, create Rooms,
 * add or Remove users in Contact, Sign up for Events /cancel an event, see all Events,
 * check Schedule for Signed Up Events, logout the account, shutdown the system.
 *
 * @author Group_0112
 * @version 1.0
 * @since November 19th, 2020
 */

public class OrganizerSystem {
    private TextPresenter output;
    private KeyboardInput input;
    private AttendeeManager attendeeManager;
    private OrganizerManager organizerManager;
    private SpeakerManager speakerManager;
    private RoomManager roomManager;
    private EventManager eventManager;
    private ChatManager chatManager;
    private MessageSystem messageSystem;
    private EventSystem eventSystem;


    /**
     * Constructor
     *
     * @param speakerManager   The speaker manager implements by SpeakerManager use case
     * @param roomManager      The room manager implements by RoomManager use case
     * @param organizerManager The organizer manager implements by OrganizerManager use case
     * @param eventManager     The event manager implements by EventManager use case
     * @param chatManager      The chat manager implements by ChatManager use case
     * @param attendeeManager  The attendee manager implements by AttendeeManager use case
     * @param messageSystem    The message system implements by MessageSystem Controller
     * @param eventSystem      The event system implements by EventSystem Controller
     */

    public OrganizerSystem(SpeakerManager speakerManager, RoomManager roomManager, OrganizerManager organizerManager, EventManager eventManager, ChatManager chatManager, AttendeeManager attendeeManager, MessageSystem messageSystem, EventSystem eventSystem) {
        this.attendeeManager = attendeeManager;
        this.roomManager = roomManager;
        this.organizerManager = organizerManager;
        this.eventManager = eventManager;
        this.speakerManager = speakerManager;
        this.chatManager = chatManager;
        this.input = new KeyboardInput();
        this.output = new TextPresenter();
        this.messageSystem = messageSystem;
        this.eventSystem = eventSystem;
    }

    /**
     * Organizer is allowed to do the following options: 1.create a new speaker.
     * 2.schedule a speaker. 3.message other Users. 4.create/delete Events 5.create Rooms.
     * 6.add or Remove users in Contact. 7.Sign up for Events /cancel an event. 8.see all Events.
     * 9.check Schedule for Signed Up Events. 10.logout the account. 11.shutdown the system.
     *
     * @param userID The user_id of the attendee who logs in
     * @return object Returns different types depend on the action system takes.
     * @throws ParseException Throw ParseException to avoid errors that might occur
     * @throws IOException    Throw IOException to avoid errors that might occur
     */
    public boolean start(String userID) throws ParseException, IOException {
        while (true) {
            String o;
            output.organizationSystemStartOptions();
            o = input.getKeyboardInput();

            switch (o) {

                case "1":   //1. Create a speaker
                    createSpeaker(userID);
                    break;
                case "2":   //2. Schedule a speaker
                    scheduleASpeaker();
                    break;
                case "3":   //3. Message a speaker
                    message(userID);
                    break;
                case "4":   //4. Create/Delete an Event
                    createDeleteEvent(userID);
                    break;
                case "5":   //5. Create a room
                    createRoom();
                    break;
                case "6":   //6. Add or remove a contact
                    addRemoveContact(userID);
                    break;
                case "7":   //7. Join/Leave an Event
                    joinLeaveEvent(userID);
                    break;
                case "8":   //8. Check all events
                    eventSystem.checkAllEvents();
                    break;
                case "9":   //9. Check signed up events
                    eventSystem.checkSignedUpEvent(userID);
                    break;
                case "10":  //10. LOGOUT
                    return false;
                case "11":  //11. SHUTDOWN
                    return true;
            }
            saveState();
        }

    }

    /**
     * Saves states of the entire system system.
     *
     * @throws IOException Throw IOException to avoid errors that might occur
     */
    private void saveState() throws IOException {
        speakerManager.saveState();
        roomManager.saveState();
        organizerManager.saveState();
        eventManager.saveState();
        chatManager.saveState();
        attendeeManager.saveState();

    }

    /**
     * Creates a new Speaker and add the Speaker into SpeakerManager.
     *
     * @param userID The user_id of the organizer who creates this Speaker
     */
    private void createSpeaker(String userID) {
        boolean goBack = false;
        while (!goBack) {
            output.enterSpeakerName();
            String inputName = input.getKeyboardInput();
            if (inputName.equals("0")) {
                goBack = true;
            } else {
                boolean untilCorrect = true;
                boolean correct = true;
                while (untilCorrect) {
                    output.enterSpeakerID(correct);
                    String inputID = input.getKeyboardInput();
                    if (inputID.equals("0")) {
                        untilCorrect = false;
                    } else if (attendeeManager.userExist(inputID) || organizerManager.userExist(inputID) || speakerManager.userExist(inputID)) {
                        correct = false;
                    } else {
                        boolean untilCorrectNew = true;
                        boolean correctNew = true;
                        while (untilCorrectNew) {
                            output.enterPassword(correctNew);
                            String inputPass = input.getKeyboardInput();
                            if (inputPass.equals("0")) {
                                untilCorrectNew = false;
                            }
                            if (inputPass.length() > 14 || inputPass.length() < 8) {
                                correctNew = false;
                            } else {
                                speakerManager.addSpeaker(inputID, inputPass, inputName, userID);
                                organizerManager.setAddSpeakerCreated(userID, inputID);
                                messageSystem.addContact(inputID, userID);
                                messageSystem.addContact(userID, inputID);
                                chatManager.createChat(userID, inputID);
                                output.ActionDone();
                                untilCorrectNew = false;
                                untilCorrect = false;
                                goBack = true;
                            }
                        }
                    }
                }
            }
        }

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Was not able to sleep");
        }

    }

    //WE WANT THE ORGANIZER TO BE ABLE TO RETURN TO THE PREVIOUS STEP AT ANY TIME. TO DO THIS, EVERY TIME THE ORGANIZER HAS TO INPUT
    //SOMETHING, IF THEY INPUT 0, IT WILL BREAK OUT OF THE LOOP, AND THEREBY RETURN TO THE PREVIOUS STEP.

    /**
     * Schedules a Speaker to some specific Events.
     */
    private void scheduleASpeaker() {
        ArrayList<Event> listOfEvents = eventManager.listOfEventsWithoutSpeaker();

        if (listOfEvents.isEmpty()) {                                               //if all events already have speakers.
            output.scheduleSpeakerNoSpeakerlessEvents();                            //there's no choice but to return to the previous menu
        } else {                                                                      //there is at least one event which does not have an assigned speaker
            boolean loopVariable = true;
            boolean invalidEvent = false;
            while (loopVariable) {
                if (invalidEvent) {
                    output.scheduleSpeakerInvalidEventID();                         //WE NEED TO COME UP WITH A WAY FOR THIS MESSAGE TO STAY FOR AT LEAST 5-10 SECONDS
                }
                output.joinDeleteEventSelector(listOfEvents);                    //Gives list of events without a speaker //joinDeleteEventSelector does the same as scheduleSpeakerSelectEvent.
                String eventID = input.getKeyboardInput();
                int eventIDint = -1;
                try {
                    eventIDint = Integer.parseInt(eventID);
                } catch (Exception e) {
                    eventIDint = -2;
                }
                if (eventIDint == 0) {                                                 //If the user wishes to return to the previous menu, they press 0. Exit loop.
                    loopVariable = false;
                } else if (0 < eventIDint && eventIDint <= listOfEvents.size()) {         //if they entered a valid int to select an event
                    boolean validSpeakerID = false;
                    while (!validSpeakerID) {                                         //loop repeats until user inputs a valid speakerID
                        output.scheduleSpeaker();
                        String speakerID = input.getKeyboardInput();                                     // gets speakerid
                        if (speakerID.equals("0")) {                                                      // if organizer wants to select a different event, they press 0. Exit loop.
                            validSpeakerID = true;
                        } else if (speakerManager.userExist(speakerID)) {                                   //if speaker exists
                            ArrayList<Integer> listOfEnrolledEventIDs = speakerManager.getSpeaker(speakerID).getAssignEvents(); //gets list of eventid's this speaker is talking at
                            Date newEventDateTime = listOfEvents.get(eventIDint - 1).getTime();         //gets the time of this new event
                            boolean speakerBusy = false;
                            for (Integer event : listOfEnrolledEventIDs) {                                 // gets time of every event this speaker is talking at.
                                Date existingEventTime = eventManager.getEvent(event).getTime();
                                if (existingEventTime.equals(newEventDateTime)) {                          //if the speaker is talking at the same time at another existing event,
                                    output.scheduleSpeakerSpeakerBusy();
                                    speakerBusy = true;                                                  // then speakerBusy = true
                                    break;
                                }
                            }
                            if (speakerBusy) {                                       //if the speaker is busy, then we want the organizer to be able to select a different event
                                validSpeakerID = true;                                 //breaks out of the inner while loop that checks for speaker, but not the outer while loop
                            }                                                        //that checks for event.
                            else {                                                                      //if speaker is not busy, then
                                Event event = listOfEvents.get(eventIDint - 1);
                                event.setSpeaker(speakerID);                                            //gets the event and sets the speaker.
                                speakerManager.addEventToSpeaker(event.getID(), speakerID);             //gets the speaker and adds this new event to its list of assigned events.
                                output.ActionDone();
                                validSpeakerID = true;                                 //Now we want to exit both loops, since our job is done successfully.
                                loopVariable = false;
                            }
                        } else {                                                        //if speakerid does not exist.
                            output.scheduleSpeakerInvalidSpeakerID();                // validSpeakerID stays false, hence this inner loop will repeat and ask for a speakerID again
                        }
                    }
                } else {                                                                //if the event id (int) they inputted was not valid, we loop back
                    invalidEvent = true;
                }
            }
        }
    }

    /**
     * Sends message to another user. Uses the help of the messageSystem to send the message
     *
     * @param userID The user_id of the user that we send message to
     * @throws IOException Throw IOException to avoid errors that might occur
     */
    private void message(String userID) throws IOException {
        messageSystem.sendMessage(userID);
    }

    /**
     * Create an new Event or Delete an Event that already exists
     *
     * @param userID The user_id of the Organizer who is creating/ deleting the event.
     * @throws ParseException Throw ParseException to avoid errors that might occur
     */
    private void createDeleteEvent(String userID) throws ParseException {
        boolean createDelete = false;
        while (!createDelete) {
            output.createDeleteEvent();
            String createDeleteInput = input.getKeyboardInput();
            int createDeleteInt = Integer.parseInt(createDeleteInput);
            if (createDeleteInt == 1) {                                                    //Create event
                boolean validTime = false;
                while (!validTime) {
                    output.createEnterTime();
                    String inputTime = input.getKeyboardInput();
                    if (verifyDateTimeEntered(inputTime)) {                                //they entered a valid date time
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        formatter.setTimeZone(TimeZone.getTimeZone("EST"));
                        Date d1 = formatter.parse(inputTime);
                        ArrayList<String> availableRooms = roomManager.getAvailableRooms(d1);
                        if (availableRooms.isEmpty()) {
                            output.createNoRoomAvailable();                                 //MAKE SURE THIS STAYS FOR SOME TIME
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                                System.out.println("couldnt sleep");
                            }
                        } else {                                                               //there is a room available
                            output.createProvideEventTitle();
                            String eventTitle = input.getKeyboardInput();                   //gets title for event.
                            String locationSelected = availableRooms.get(0);
                            int eventID = -1;
                            boolean validID = false;
                            while (!validID) {
                                output.enterCreatingEventID();
                                try {
                                    eventID = Integer.parseInt(input.getKeyboardInput());
                                } catch (Exception e) {
                                    eventID = -1;
                                }
                                if (eventID >= 0 && !eventManager.getListOfEventIDs().contains(eventID)) {
                                    validID = true;
                                } else {
                                    output.invalidEventID();
                                }
                            }
                            eventManager.addEvent(eventTitle, d1, locationSelected, userID, eventID);
                            roomManager.addEventToRoom(locationSelected, eventID, d1);
                            organizerManager.setAddEventCreated(userID, eventID);           //adds to the list of events this organizer has created
                            output.ActionDone();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                System.out.println("couldnt sleep");
                            }
                            validTime = true;
                            createDelete = true;
                        }
                    } else if (inputTime.equals("0")) {
                        validTime = true;
                    } else {                                                                  //they entered an invalid date time
                        output.createEnterTimeInvalidTime();
                    }
                }
            } else if (createDeleteInt == 2) {                                                   //Deletes Event
                boolean validEventSelected = false;
                while (!validEventSelected) {
                    ArrayList<Event> events = eventManager.getListOfEvents();
                    output.joinDeleteEventSelector(events);
                    String eventSelected = input.getKeyboardInput();
                    int eventSelectedInt = Integer.parseInt(eventSelected);
                    if (eventSelectedInt == 0) {
                        validEventSelected = true;
                    } else if (0 < eventSelectedInt && eventSelectedInt <= events.size()) {
                        int eventID = events.get(eventSelectedInt - 1).getID();
                        String eventLocation = eventManager.getLocation(eventID);
                        Date eventTime = eventManager.getTime(eventID);
                        roomManager.removeEventFromRoom(eventLocation, eventID, eventTime);      //the room no longer holds this event at that time.

                        organizerManager.setDeleteEventCreated(userID, eventID);                //removes event from the list of events this organizer has created
                        ArrayList<String> listofAttendees = eventManager.getEventAttendees(eventID);
                        for (String attendeeID : listofAttendees) {                                   //removes this event from all attendees list of attending events
                            if (organizerManager.userExist(attendeeID)) {
                                organizerManager.removeEvent(eventID, attendeeID);
                            } else if (attendeeManager.userExist(attendeeID)) {
                                attendeeManager.removeEvent(eventID, attendeeID);
                            }
                        }
                        if (eventManager.hasSpeaker(eventID)) {                                 //if this event has a speaker, delete this event from that speakers list of assigned events.
                            String speakerID = eventManager.getSpeakerID(eventID);
                            speakerManager.removeEvent(eventID, speakerID);
                        }
                        eventManager.cancelEvent(eventID);                                     //removes from list of events
                        output.ActionDone();
                        validEventSelected = true;
                        createDelete = true;
                    } else {
                        output.invalidInputSelection();
                    }
                }
            } else if (createDeleteInt == 0) {
                createDelete = true;
            } else {
                output.invalidInputSelection();         //MAKE SURE THIS MESSAGE STAYS ON FOR A COUPLE SECONDS
            }
        }
    }

    //This helper method checks if the date entered by the user follows the appropriate format. If it doesn't, then return false,
    //and we get the user to re-enter the date.
    private boolean verifyDateTimeEntered(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("EST"));
        Date currentDateTime = new Date();                  //current dateandtime
        //checks if the string date provided fits the format and is after the current date. else, returns false. HOWCOME .BEFORE IS IGNORED?
        Date d1;
        try {
            d1 = formatter.parse(date);
        } catch (ParseException e) {
            return false;
        }

        if (currentDateTime.after(d1)) {
            return false;
        }
        SimpleDateFormat formatter2 = new SimpleDateFormat("EEE-dd-MMM-yyyy HH:mm:ss");
        formatter2.setTimeZone(TimeZone.getTimeZone("EST"));
        String day1 = formatter2.format(d1).substring(0, 1);
        if (day1.equals("S")) {
            return false;
        }
        //if the month isn't between 01-12, return false
        String month = date.substring(3, 5);
        if (!month.matches("01|02|03|04|05|06|07|08|09|10|11|12")) {
            return false;
        }
        //if the day is greater than 31 in months with at max 31 days, return false
        String day = date.substring(0, 2);
        int dayInt = Integer.parseInt(day);
        if (month.matches("01|03|05|07|08|10|12")) {
            if (0 > dayInt || dayInt > 31) {
                return false;
            }
        }
        //if the month is feb, and date is greater than 28, return false
        if (month.equals("02")) {
            if (0 > dayInt || dayInt > 28) {
                return false;
            }
        }
        //if the month has at max 30 days, and they entered something more, return false.
        if (month.matches("04|06|09|11")) {
            if (0 > dayInt || dayInt > 30) {
                return false;
            }
        }
        //checks if hours is between 09-16
        String hour = date.substring(11, 13);
        if (!hour.matches("09|10|11|12|13|14|15|16")) {
            return false;
        }

        //checks if minutes and seconds are both 00
        String minutes = date.substring(14, 16);
        String seconds = date.substring(17, 19);
        if (!minutes.equals("00") || !seconds.equals("00")) {
            return false;
        }
        return true;
    }

    /**
     * Add a user to the contact or remove a user for the logged in organizer by getting its user_id
     *
     * @param userID The user_id of the user we want to add/ remove
     */
    private void addRemoveContact(String userID) {
        boolean goBack = false;
        while (!goBack) {
            boolean validAddRemove = false;
            while (!validAddRemove) {
                output.addRemoveContact();
                String option = input.getKeyboardInput();
                if (option.equals("0")) {
                    validAddRemove = true;
                    goBack = true;
                } else if (option.equals("1")) {
                    boolean validUserID = false;
                    while (!validUserID) {
                        output.enterContactUserid(false);
                        String user = input.getKeyboardInput();
                        if (user.equals("0")) {
                            validUserID = true;
                        } else if ((organizerManager.userExist(user) || attendeeManager.userExist(user) || speakerManager.userExist(user))) {
                            if (!organizerManager.contactExists(userID, user)) {
                                organizerManager.addContact(userID, user);
                                if (organizerManager.userExist(user)) {
                                    organizerManager.addContact(user, userID);
                                } else if (attendeeManager.userExist(user)) {
                                    attendeeManager.addContact(user, userID);
                                } else {
                                    speakerManager.addContact(user, userID);
                                }
                                chatManager.createChat(user, userID);
                                output.ActionDone();
                                validUserID = true;
                                validAddRemove = true;
                                goBack = true;
                            } else {
                                output.userAlreadyInYourContacts();
                            }
                        } else {
                            output.enterContactUserid(true);
                        }
                    }
                } else if (option.equals("2")) {
                    boolean validUserID = false;
                    while (!validUserID) {
                        output.enterContactUserid(false);
                        String user = input.getKeyboardInput();
                        if (user.equals("0")) {
                            validUserID = true;
                        } else if ((organizerManager.userExist(user) || attendeeManager.userExist(user) || speakerManager.userExist(user))) {
                            if (organizerManager.contactExists(userID, user)) {
                                organizerManager.removeContact(userID, user);
                                if (organizerManager.userExist(user)) {
                                    organizerManager.removeContact(user, userID);
                                } else if (attendeeManager.userExist(user)) {
                                    attendeeManager.removeContact(user, userID);
                                } else {
                                    speakerManager.removeContact(user, userID);
                                }
                                chatManager.deleteChat(user, userID);
                                output.ActionDone();
                                validUserID = true;
                                validAddRemove = true;
                                goBack = true;
                            } else {
                                output.userNotInYourContacts();
                            }
                        } else {
                            output.enterContactUserid(true);
                        }
                    }
                } else {
                    output.invalidInputSelection();
                }
            }
        }
    }


    /**
     * Sign up for an event or Leave an event from the Signup-events.
     *
     * @param userID The user_id of the organizer who wants to Sign up or cancel an event
     */
    private void joinLeaveEvent(String userID) {
        boolean validInput = false;
        while (!validInput) {
            output.joinOrLeave();                                                     //checks whether org wants to join or leave an event, or wants to return back to menu
            String joinLeave = input.getKeyboardInput();
            int joinLeaveInt;
            try {
                joinLeaveInt = Integer.parseInt(joinLeave);
            }
            catch (Exception e){
                joinLeaveInt = -1;
            }
            if (joinLeaveInt == 0) {                                                  //if org wants to return to menu, we exit this loop, having done nothing.
                validInput = true;
            } else if (joinLeaveInt == 1) {                                           //in this case, org wants to join an event
                boolean validEventSelected = false;
                while (!validEventSelected) {
                    ArrayList<Integer> listOfAllEventIDs = eventManager.getListOfEventIDs();                            //gets list of all events
                    ArrayList<Integer> listOfCurrentlyAttendingEventIds = organizerManager.getSignedUpEvents(userID);    //gets list of all events this organizer is already attending
                    listOfAllEventIDs.removeAll(listOfCurrentlyAttendingEventIds);                                      //now listOfAllEvents contains the events this organizer is NOT attending already
                    ArrayList<Integer> listOfAllEventsThatNeedToBeRemoved = new ArrayList<>();
                    for (Integer eventid : listOfAllEventIDs) {                                                            //goes through every event this organizer is not attending (list of events he can possible join)
                        Date newEventTime = eventManager.getTime(eventid);                                              //finds its time
                        for (Integer currenteventid : listOfCurrentlyAttendingEventIds) {
                            Date currentEventTime = eventManager.getTime(currenteventid);
                            if (newEventTime.equals(currentEventTime)) {                                                 //if this time is the same as any event the organizer is already attending,
                                listOfAllEventsThatNeedToBeRemoved.add(eventid);                                                      //remove that event from the event from list of event he can possible join (listOfAllEventIDs)
                            }
                        }
                        //by now, listOfAllEventIDs contains the eventIDs of events that the organizer has not joined already and whose timings do not
                        //overlap/interfere with events he/she is already going to! Now from these events, we want to remove those that do not have
                        //room (sufficient capacity) to support one more attendee.
                        Event actualEvent = eventManager.getEvent(eventid);
                        int capacity = roomManager.getRoom(actualEvent.getLocation()).getCapacity();                    //gets the capacity of the room this event is held in
                        int numExistingAttendees = actualEvent.getAttendees().size();                                   //gets the number of attendees that are attending this event
                        if (capacity - numExistingAttendees == 0) {                                                           //if the number of attendees attending this event has reached the max capacity of the room,
                            listOfAllEventsThatNeedToBeRemoved.add(eventid);                                                         //the organizer cannot join this room. Remove it from the list.
                        }
                    }
                    listOfAllEventIDs.removeAll(listOfAllEventsThatNeedToBeRemoved);
                    if (listOfAllEventIDs.isEmpty()) {
                        output.noEventAvailableToJoin();
                        validEventSelected=true;
                    } else {
                        ArrayList<Event> listOfJoinableEvents = new ArrayList<>();                                          //list of all events this organizer can join
                        for (Integer eventid : listOfAllEventIDs) {
                            listOfJoinableEvents.add(eventManager.getEvent(eventid));
                        }
                        output.joinDeleteEventSelector(listOfJoinableEvents);
                        String eventSelected = input.getKeyboardInput();
                        int eventSelectedInt;
                        try {
                            eventSelectedInt = Integer.parseInt(eventSelected);
                        }
                        catch (Exception e){
                            eventSelectedInt = -1;
                        }
                        if (eventSelectedInt == 0) {
                            validEventSelected = true;
                        } else if (1 <= eventSelectedInt && eventSelectedInt <= listOfJoinableEvents.size()) {
                            organizerManager.addEventToOrganizer(listOfAllEventIDs.get(eventSelectedInt - 1), userID);         //add eventid to the organizers list of events.
                            eventManager.addAttendee(listOfAllEventIDs.get(eventSelectedInt - 1), userID);                     //add organizer to events list of attendees for this event.
                            validEventSelected = true;
                            validInput = true;
                            output.ActionDone();
                        } else {
                            output.joinLeaveInvalidResponse();
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println("couldnt sleep!");
                }
            } else if (joinLeaveInt == 2) {                                                                                   //we need to delete an event here
                boolean validEventSelected = false;
                while (!validEventSelected) {
                    ArrayList<Integer> listOfAttendingEventIds = organizerManager.getSignedUpEvents(userID);            //get the list of signed up eventids
                    ArrayList<Event> listofAttendingEvents = new ArrayList<>();
                    for (Integer eventid : listOfAttendingEventIds) {                                                      //get the list of events
                        listofAttendingEvents.add(eventManager.getEvent(eventid));
                    }
                    if (listofAttendingEvents.isEmpty()) {
                        output.noSignedUpEvents();
                        validEventSelected = true;
                        validInput = true;
                    } else {
                        output.joinDeleteEventSelector(listofAttendingEvents);                                              //select which event they want to leave
                        String eventSelected = input.getKeyboardInput();
                        int eventSelectedInt;
                        try {
                            eventSelectedInt = Integer.parseInt(eventSelected);
                        } catch (Exception e) {
                            eventSelectedInt = -1;
                        }

                        if (eventSelectedInt == 0) {
                            validEventSelected = true;
                        } else if (1 <= eventSelectedInt && eventSelectedInt <= listofAttendingEvents.size()) {
                            organizerManager.removeEvent(listOfAttendingEventIds.get(eventSelectedInt - 1), userID);
                            eventManager.removeAttendee(listOfAttendingEventIds.get(eventSelectedInt - 1), userID);
                            validEventSelected = true;
                            validInput = true;
                            output.ActionDone();
                        } else {
                            output.joinLeaveInvalidResponse();
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println("couldnt sleep!");
                }
            }
            else{
                output.invalidInput();
            }
        }
    }

    /**
     * Create a new Room
     */
    public void createRoom() {
        boolean validLocation = false;
        while (!validLocation) {
            output.createRoom();
            String roomLocation = input.getKeyboardInput();
            if (roomLocation.equals("0")) {
                validLocation = true;
            } else if (roomManager.locationIDAvailable(roomLocation)) {
                roomManager.createRoom(roomLocation);
                output.ActionDone();
                validLocation = true;
            } else {
                output.createRoomUnavailable();
            }
        }
    }

    /**
     * Check if the user already registered in this system or not
     *
     * @param userid The user_id of the user we want to check
     * @return boolean Returns true if the user already registered, false otherwise
     */
    private boolean userExists(String userid) {
        if (attendeeManager.userExist(userid) || organizerManager.userExist(userid) || speakerManager.userExist(userid)) {
            return true;
        }
        return false;
    }
}


