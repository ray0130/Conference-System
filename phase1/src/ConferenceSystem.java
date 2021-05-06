import Controller.*;
import UseCases.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * The ConferenceSystem class stores all controllers, import/save states,
 * and it allows the entire program to work.
 * @author Group_0112
 * @version 1.0
 * @since November 19th, 2020
 */

public class ConferenceSystem {

    private SpeakerManager speakerManager;
    private RoomManager roomManager;
    private OrganizerManager organizerManager;
    private EventManager eventManager;
    private ChatManager chatManager;
    private AttendeeManager attendeeManager;
    private AdminManager adminManager;
    private OrganizerSystem organizerSystem;
    private MessageSystem messageSystem;
    private LogInAndRegistrationSystem logInAndRegistrationSystem;
    private EventSystem eventSystem;
    private AttendeeSystem attendeeSystem;
    private SpeakerSystem speakerSystem;
    private AdminSystem adminSystem;

    /**
     * Constructor
     */
    public ConferenceSystem(){
        importState();// this function will bring back the managers and initialize them
        logInAndRegistrationSystem = new LogInAndRegistrationSystem(attendeeManager, organizerManager, speakerManager);
        messageSystem = new MessageSystem(speakerManager,organizerManager, eventManager, chatManager, attendeeManager);
        eventSystem = new EventSystem(speakerManager, roomManager, organizerManager, eventManager, attendeeManager);
        organizerSystem = new OrganizerSystem(speakerManager, roomManager,organizerManager, eventManager, chatManager, attendeeManager, messageSystem, eventSystem);
        attendeeSystem = new AttendeeSystem(speakerManager, organizerManager, chatManager,attendeeManager, messageSystem, eventSystem, roomManager, eventManager);
        speakerSystem = new SpeakerSystem(speakerManager, organizerManager, chatManager, attendeeManager, messageSystem, eventSystem);
        adminSystem = new AdminSystem(speakerManager, organizerManager, chatManager, attendeeManager, messageSystem, eventSystem, eventManager, roomManager, adminManager);
    }

    /**
     * Serialize all managers (use cases)
     */
    private void importState() {
        SpeakerManager s = new SpeakerManager();
        this.speakerManager = s.importState();

        RoomManager r = new RoomManager();
        this.roomManager = r.importState();

        OrganizerManager o = new OrganizerManager();
        this.organizerManager = o.importState();

        EventManager e = new EventManager();
        this.eventManager = e.importState();

        ChatManager c = new ChatManager();
        this.chatManager = c.importState();

        AttendeeManager a = new AttendeeManager();
        this.attendeeManager = a.importState();


    }

    /**
     * Saves all states of use cases.
     * @throws IOException  throw IOException to avoid errors that might occur while running the program
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
     * Runs the program
     * @throws IOException throw IOException to avoid errors that might occur while running the program
     * @throws ParseException throw ParseException to avoid errors that might occur while running the program
     */
    public void run () throws IOException, ParseException {
        deletePastEvents();
        boolean shutdown = false;
        listofIDPass();
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
            else if (speakerManager.userExist(userID)){
                shutdown = speakerSystem.start(userID);
            }
            else if (adminManager.userExist(userID)){
                //shutdown = adminSystem.start(userID);
            }
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
                //NOTE: THIS WORKS RIGHT NOW BECAUSE WE ONLY HAVE 1 SPEAKER, IN PHASE 2 WE WILL HAVE MULTIPLE SPEAKERS
                speakerManager.getSpeaker(eventManager.getEvent(eventID).getSpeakerID()).removeAssignEvent(eventID);
                String organizerOfThisEvent = eventManager.getEvent(eventID).getOrganizerID();  //gets the userid of the organizer of this event
                //in the organizer's list of events that he/she created, this event is removed.
                organizerManager.removeEvent(eventID, organizerOfThisEvent);
                //removes the event from the room. NOTE THIS IS NOT NECESSARY AS YOU WILL NEVER BE ABLE TO ASSIGN A ROOM IN THE PAST, BUT JUST AS A SAFETY MEASURE.
                roomManager.removeEventFromRoom(eventManager.getEvent(eventID).getLocation(), eventID, eventManager.getTime(eventID));
                // and finally removes the event object itself from the event manager.
                eventManager.removeEvent(eventID);
            }
        }
    }

    private void listofIDPass() {
        System.out.println("\n\n\nNOTE: this is a debug feature to assist our TA. Here is the current state of the system:");
        System.out.println("\n\nAll the organizers in the system:");
        ArrayList<String> organizersList = organizerManager.getUserIDs();
        for (String organizer: organizersList){
            System.out.println(organizerManager.getOrganizer(organizer).toString());
        }
        System.out.println("\n\nAll the speakers in the system:");
        ArrayList<String> speakers = speakerManager.getUserIDs();
        for (String speaker: speakers){
            System.out.println(speakerManager.getSpeaker(speaker).toString());
        }
        System.out.println("\n\nAll the attendees in the system:");
        ArrayList<String> attendees = attendeeManager.getUserIDs();
        for (String attendee: attendees){
            System.out.println(attendeeManager.getAttendee(attendee).toString());
        }
        System.out.println("\n\nAll the events in the system:");
        ArrayList<Integer> eventIDS = eventManager.getListOfEventIDs();
        for (Integer event: eventIDS){
            System.out.println(eventManager.getEvent(event).toString());
        }
        System.out.println("\n\nAll the rooms in the system");
        ArrayList<String> roomLOCS = roomManager.getAllRoomLocs();
        for (String room: roomLOCS){
            System.out.println(roomManager.getRoom(room).toString());
        }
        System.out.println("\n\n");
    }
}


