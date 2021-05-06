import Controller.*;
import Gateway.Serialization;
import UseCases.*;
import UseCases.AdminManager;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * The ConferenceSystem class stores all controllers, import/save states,
 * and it allows the entire program to work.
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */

public class ConferenceSystem {

    private SpeakerManager speakerManager;
    private RoomManager roomManager;
    private OrganizerManager organizerManager;
    private EventManager eventManager;
    private ChatManager chatManager;
    private AttendeeManager attendeeManager;
    private AdminManager adminManager;
    private RequestManager requestManager;

    private OrganizerSystem organizerSystem;
    private MessageSystem messageSystem;
    private LogInAndRegistrationSystem logInAndRegistrationSystem;
    private EventSystem eventSystem;
    private AttendeeSystem attendeeSystem;
    private SpeakerSystem speakerSystem;
    private RequestSystem requestSystem;
    private AdminSystem adminSystem;

    /**
     * Constructor
     */
    public ConferenceSystem(){
        importState();// this function will bring back the managers and initialize them
        initializeControllers();// this function will initialize the controllers
    }

    /**
     * Initializes all controllers
     */
    private void initializeControllers() {
        logInAndRegistrationSystem = new LogInAndRegistrationSystem(attendeeManager, organizerManager, speakerManager, adminManager);
        requestSystem = new RequestSystem(requestManager, attendeeManager, organizerManager);
        messageSystem = new MessageSystem(speakerManager,organizerManager, eventManager, chatManager, attendeeManager);
        eventSystem = new EventSystem(speakerManager, roomManager, organizerManager, eventManager, attendeeManager);
        organizerSystem = new OrganizerSystem(speakerManager, roomManager,organizerManager, eventManager, chatManager, attendeeManager, messageSystem, eventSystem, requestSystem);
        attendeeSystem = new AttendeeSystem(speakerManager, organizerManager, chatManager,attendeeManager, messageSystem, eventSystem, roomManager, eventManager, requestSystem);
        speakerSystem = new SpeakerSystem(speakerManager, organizerManager, chatManager, attendeeManager, messageSystem, eventSystem);
        adminSystem = new AdminSystem(chatManager, eventManager, speakerManager, organizerManager, roomManager);

    }

    /**
     * Serializes all managers (use cases)
     */
    private void importState() {
        Serialization io = new Serialization();
        this.adminManager = (AdminManager) io.importState("AdminManager");
        this.speakerManager = (SpeakerManager) io.importState("SpeakerManager");
        this.roomManager = (RoomManager) io.importState("RoomManager");
        this.organizerManager = (OrganizerManager) io.importState("OrganizerManager");
        this.eventManager = (EventManager) io.importState("EventManager");
        this.chatManager = (ChatManager) io.importState("ChatManager");
        this.attendeeManager = (AttendeeManager) io.importState("AttendeeManager");
        this.requestManager = (RequestManager) io.importState("RequestManager");

    }

    /**
     * Saves all states of use cases.
     * @throws IOException  throw IOException to avoid errors that might occur while running the program
     */
    private void saveState() throws IOException {
        Serialization io = new Serialization();
        io.saveState(speakerManager, "SpeakerManager");
        io.saveState(roomManager, "RoomManager");
        io.saveState(adminManager, "AdminManager");
        io.saveState(organizerManager, "OrganizerManager");
        io.saveState(eventManager, "EventManager");
        io.saveState(chatManager, "ChatManager");
        io.saveState(attendeeManager, "AttendeeManager");
        io.saveState(requestManager, "RequestManager");
    }

    /**
     * Runs the program
     * @throws IOException throw IOException to avoid errors that might occur while running the program
     * @throws ParseException throw ParseException to avoid errors that might occur while running the program
     */
    public void run () throws IOException, ParseException {
        deletePastEvents();
        boolean shutdown = false;
        Debugger debugger = new Debugger();
        debugger.printStateofSystem(organizerManager,speakerManager,attendeeManager,eventManager,roomManager, requestManager, adminManager, chatManager);
        while (!shutdown) {
            String userID = logInAndRegistrationSystem.start();
            if (userID.equals("SHUTDOWN")){
                shutdown = true;
            }
            else if (organizerManager.userExist(userID)) {
                shutdown = organizerSystem.start(userID);
            }
            else if (attendeeManager.userExist(userID)) {
                shutdown = attendeeSystem.start(userID);
            }
            else if (adminManager.adminExist(userID)){
                shutdown = adminSystem.start();
            }
            else {
                shutdown = speakerSystem.start(userID);
            }
            saveState();
        }
        saveState();
    }

    /**
     * Deletes past event before the program starts running.
     */
    private void deletePastEvents() {
        Date now = new Date();  //gets the current date
        ArrayList<Integer> listofEventIDS = eventManager.getListOfEventIDs();   //gets list of all eventIDs in the system
        //checks every eventID, and if its in the past, then deletes everything related to that event
        for (Integer eventID: listofEventIDS) {
            //if the event happened before right now, delete the event, and remove all attendees that are attending that event, organizer, speaker and free up the room.
            if (eventManager.getEvent(eventID).getTime().before(now)){
                ArrayList<String> peopleAttending = eventManager.getEvent(eventID).getAttendees();  //list of userid of all people attending

                for (String attendeeID: peopleAttending){
                    //check if attendee is a organizer attending the event
                    if (organizerManager.userExist(attendeeID)){
                        organizerManager.removeEvent(eventID, attendeeID);
                    }
                    //if its not a organizer, it must be a attendee
                    else {
                        attendeeManager.removeEvent(eventID,attendeeID);
                    }
                }
                ArrayList<String> speakers = eventManager.getEvent(eventID).getSpeakerID();
                for (String speaker:speakers) {
                    speakerManager.getSpeaker(speaker).removeAssignEvent(eventID);
                }

                String organizerOfThisEvent = eventManager.getEvent(eventID).getOrganizerID();  //gets the userid of the organizer of this event
                //in the organizer's list of events that he/she created, this event is removed.
                organizerManager.removeEvent(eventID, organizerOfThisEvent);
                //removes the event from the room. NOTE THIS IS NOT NECESSARY AS YOU WILL NEVER BE ABLE TO ASSIGN A ROOM IN THE PAST, BUT JUST AS A SAFETY MEASURE.
                roomManager.removeEventFromRoom(eventManager.getEvent(eventID).getLocation(), eventManager.getEvent(eventID), eventManager.getTime(eventID));
                // and finally removes the event object itself from the event manager.
                eventManager.removeEvent(eventID);
            }
        }
    }
}