package Controller;

import Entities.Event;
import Gateway.KeyboardInput;
import Presenter.TextPresenter;
import UseCases.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * AttendeeSystem controller implements various actions that can be done for an attendee, including
 * see all Events, Sign up for Events /cancel an event, check Schedule for Signed Up Events,
 * add or Remove Attendee in Contact, message other Users, logout the account, shutdown the system.
 * @author Group_0112
 * @version 1.0
 * @since November 19th, 2020
 */

public class AttendeeSystem {
    private TextPresenter output;
    private KeyboardInput input;
    private AttendeeManager attendeeManager;
    private OrganizerManager organizerManager;
    private SpeakerManager speakerManager;
    private ChatManager chatManager;
    private MessageSystem messageSystem;
    private EventSystem eventSystem;
    private RoomManager roomManager;
    private EventManager eventManager;

    /**
     * Constructor
     * @param speakerManager   The speaker manager implements by SpeakerManager use case
     * @param organizerManager The organizer manager implements by OrganizerManager use case
     * @param chatManager      The chat manager implements by ChatManager use case
     * @param attendeeManager  The attendee manager implements by AttendeeManager use case
     * @param messageSystem    The message system implements by MessageSystem Controller
     * @param eventSystem      The event system implements by EventSystem Controller
     * @param roomManager      The room manager implements by RoomManager use case
     * @param eventManager     The event manager implements by EventManager use case
     */
    public AttendeeSystem(SpeakerManager speakerManager, OrganizerManager organizerManager, ChatManager chatManager,
                          AttendeeManager attendeeManager, MessageSystem messageSystem, EventSystem eventSystem, RoomManager roomManager, EventManager eventManager) {
        this.speakerManager = speakerManager;
        this.organizerManager = organizerManager;
        this.attendeeManager = attendeeManager;
        this.chatManager = chatManager;
        this.messageSystem = messageSystem;
        this.eventSystem = eventSystem;
        this.roomManager = roomManager;
        this.eventManager = eventManager;
        this.input = new KeyboardInput();
        this.output = new TextPresenter();
    }

    /**
     * Attendee is allowed to do the following options: 1.see all Events. 2.Sign up for Events /cancel an event
     * 3.check Schedule for an Signed Up Event. 4.add or Remove Attendee in Contact. 5.message other Users
     * 6.log out the account. 7.shutdown the system
     * @param userID The user_id of the attendee who is logged in
     * @return object Returns true if user wants to shutdown, false if it wants to logout
     * @throws IOException Throw IOException to avoid errors that might occur
     */
    public boolean start(String userID) throws IOException {
        while (true) {
            String i;
            boolean validInput = false;
            output.AttendeeMenu();
            i = input.getKeyboardInput();
            //1. see Events
            if (i.equals("1")) {
                eventSystem.checkAllEvents();
            }
            //2. Sign up for Events / leave an event
            else if (i.equals("2")) {
                joinLeaveEvent(userID);
            }
            //3. Check Schedule for an Signed Up Event
            else if (i.equals("3")) {
                eventSystem.checkSignedUpEvent(userID);
            }
            //4. Add or Remove Attendee in Contact
            else if (i.equals("4")) {
                addRemoveContact(userID);
            }
            //5. Message Other Users
            else if (i.equals("5")) {
                messageSystem.sendMessage(userID);
            }
            //6. logout
            else if (i.equals("6")) {
                return false;
            }
            //7. shutdown
            else if (i.equals("7")) {
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
        organizerManager.saveState();
        chatManager.saveState();
        attendeeManager.saveState();

    }

    /**
     * Check if the user already registered in this system or not
     *
     * @param userid The user_id of the user we want to check
     * @return boolean Returns true if the user already registered, false otherwise
     */
    private boolean userExists(String userid) {
        return attendeeManager.userExist(userid) || organizerManager.userExist(userid) || speakerManager.userExist(userid);
    }

    /**
     * Add a user to the contact or remove a user from the contact by given its user_id
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
                            if (!attendeeManager.contactExists(userID, user)) {
                                attendeeManager.addContact(userID, user);
                                if (organizerManager.userExist(user)) {
                                    organizerManager.addContact(user, userID);
                                } else if (attendeeManager.userExist(user)) {
                                    attendeeManager.addContact(user, userID);
                                } else {
                                    speakerManager.addContact(user, userID);
                                }
                                chatManager.createChat(user, userID);
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
                            if (attendeeManager.contactExists(userID, user)) {
                                attendeeManager.removeContact(userID, user);
                                if (organizerManager.userExist(user)) {
                                    organizerManager.removeContact(user, userID);
                                } else if (attendeeManager.userExist(user)) {
                                    attendeeManager.removeContact(user, userID);
                                } else {
                                    speakerManager.removeContact(user, userID);
                                }
                                chatManager.deleteChat(user, userID);
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
     * Sign up for an event or Cancel an event from the Signup-events.
     *
     * @param userID The user_id of the attendee who wants to Sign up or cancel an event
     */
    private void joinLeaveEvent(String userID) {
        boolean validInput = false;
        while (!validInput) {
            output.joinOrLeave();                                                     //checks whether attendee wants to join or leave an event, or wants to return back to menu
            String joinLeave = input.getKeyboardInput();
            int joinLeaveInt;
            try {
                joinLeaveInt = Integer.parseInt(joinLeave);
            }
            catch (Exception e){
                joinLeaveInt = -1;
            }
            if (joinLeaveInt == 0) {                                                  //if attendee wants to return to menu, we exit this loop, having done nothing.
                validInput = true;
            }
            else if (joinLeaveInt == 1) {                                           //in this case, org wants to join an event
                boolean validEventSelected = false;
                while (!validEventSelected) {
                    ArrayList<Integer> listOfAllEventIDs = eventManager.getListOfEventIDs();                            //gets list of all events
                    ArrayList<Integer> listOfCurrentlyAttendingEventIds = attendeeManager.getSignedUpEvents(userID);    //gets list of all events this attendee is already attending
                    listOfAllEventIDs.removeAll(listOfCurrentlyAttendingEventIds);                                      //now listOfAllEvents contains the events this organizer is NOT attending already
                    ArrayList<Integer> listOfEventsThatNeedToBeRemoved = new ArrayList<>();
                    for (Integer eventid : listOfAllEventIDs) {                                                            //goes through every event this organizer is not attending (list of events he can possible join)
                        Date newEventTime = eventManager.getTime(eventid);                                              //finds its time
                        for (Integer currenteventid : listOfCurrentlyAttendingEventIds) {
                            Date currentEventTime = eventManager.getTime(currenteventid);
                            if (newEventTime.equals(currentEventTime)) {                                                 //if this time is the same as any event the attendee is already attending,
                                listOfEventsThatNeedToBeRemoved.add(eventid);                                                      //remove that event from the event from list of event he can possible join (listOfAllEventIDs)
                            }
                        }
                        //by now, listOfAllEventIDs contains the eventIDs of events that the attendee has not joined already and whose timings do not
                        //overlap/interfere with events he/she is already going to! Now from these events, we want to remove those that do not have
                        //room (sufficient capacity) to support one more attendee.
                        Event actualEvent = eventManager.getEvent(eventid);
                        int capacity = roomManager.getRoom(actualEvent.getLocation()).getCapacity();                    //gets the capacity of the room this event is held in
                        int numExistingAttendees = actualEvent.getAttendees().size();                                   //gets the number of attendees that are attending this event
                        if (capacity - numExistingAttendees == 0) {                                                           //if the number of attendees attending this event has reached the max capacity of the room,
                            listOfEventsThatNeedToBeRemoved.add(eventid);                                                     //the organizer cannot join this room. Remove it from the list.
                        }
                    }
                    listOfAllEventIDs.removeAll(listOfEventsThatNeedToBeRemoved);
                    if (listOfAllEventIDs.isEmpty()){
                        output.noEventAvailableToJoin();
                        validEventSelected=true;

                    }
                    else{
                        ArrayList<Event> listOfJoinableEvents = new ArrayList<>();                                          //list of all events this Attendee can join
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
                            attendeeManager.addEventToAttendee(listOfAllEventIDs.get(eventSelectedInt - 1), userID);         //add eventid to the attendees list of events.
                            eventManager.addAttendee(listOfAllEventIDs.get(eventSelectedInt - 1), userID);                     //add attendee to events list of attendees for this event.
                            output.ActionDone();
                            validEventSelected = true;
                            validInput = true;
                        } else {
                            output.joinLeaveInvalidResponse();
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e){
                    System.out.println("Could not sleep");
                }
            } else if (joinLeaveInt == 2) {                                                                                   //we need to leave an event here
                boolean validEventSelected = false;
                while (!validEventSelected) {
                    ArrayList<Integer> listOfAttendingEventIds = attendeeManager.getSignedUpEvents(userID);            //get the list of signed up eventids
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
                        }
                        catch (Exception e){
                            eventSelectedInt = -1;
                        }

                        if (eventSelectedInt == 0) {
                            validEventSelected = true;
                        } else if (1 <= eventSelectedInt && eventSelectedInt <= listofAttendingEvents.size()) {
                            eventManager.removeAttendee(listOfAttendingEventIds.get(eventSelectedInt - 1), userID);
                            attendeeManager.removeEvent(listOfAttendingEventIds.get(eventSelectedInt - 1), userID);
                            output.ActionDone();
                            validEventSelected = true;
                            validInput = true;
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
}