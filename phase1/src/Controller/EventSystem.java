package Controller;

import Entities.Event;
import Gateway.KeyboardInput;
import Presenter.TextPresenter;
import UseCases.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/** EventSystem controller implements various actions that can be done for the users, including
 *  see all Events, create/delete Events, Sign up for Events /cancel an event, check Schedule for Signed Up and Assigned Events.
 *  This is all dependent on who is signed into the system.
 *  @author Group_0112
 *  @version 1.0
 *  @since November 19th, 2020
 */

public class EventSystem {
    private TextPresenter output;
    private KeyboardInput input;
    private SpeakerManager speakerManager;
    private OrganizerManager organizerManager;
    private EventManager eventManager;
    private AttendeeManager attendeeManager;
    private RoomManager roomManager;

    /**
     * Constructor
     * @param speakerManager The speaker manager implements by SpeakerManager use case
     * @param roomManager The room manager implements by RoomManager use case
     * @param organizerManager The organizer manager implements by OrganizerManager use case
     * @param eventManager The event manager implements by EventManager use case
     * @param attendeeManager The attendee manager implements by AttendeeManager use case
     */
    public EventSystem(SpeakerManager speakerManager, RoomManager roomManager, OrganizerManager organizerManager,
                       EventManager eventManager, AttendeeManager attendeeManager) {
        this.attendeeManager = attendeeManager;
        this.roomManager = roomManager;
        this.organizerManager = organizerManager;
        this.eventManager = eventManager;
        this.speakerManager = speakerManager;
        this.input = new KeyboardInput();
        this.output = new TextPresenter();
    }

    /**
     * Show all the Events to the user with its title, location, time, event_id, and speaker
     */
    public void checkAllEvents(){
        ArrayList<String> listOfEventSchedule = new ArrayList<>();
        ArrayList<Event> listOfEvents = eventManager.getListOfEvents();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("EST"));
        for (Event event :listOfEvents){
            String schedule = event.getTitle() + "\n" + "Location: " + event.getLocation() + "\n"
                    + "Time: " + formatter.format(event.getTime()) + "\n" + "Event ID: " + event.getID();
            if (eventManager.hasSpeaker(event.getID())){
                schedule +=  "\n" + "Speaker: " + event.getSpeakerID();
            }

            listOfEventSchedule.add(schedule);
        }
        output.eventsCheckAll(listOfEventSchedule);
        boolean rtn = false;
        while(!rtn){
            output.pressAnyKeyToContinue();
            String in = input.getKeyboardInput();
            if (!in.equals("")){
                rtn = true;
            }
        }
    }

    /**
     * Sign up for an event
     * @param UserId The user_id of the user who wants to sign up the event
     * @param EventId The event that the user wants to sign up to
     */
    public void signUpEvent(String UserId, Integer EventId){
        if (attendeeManager.SignedUp(EventId, UserId)){
            output.ActionFailed();
        }
        else {
            attendeeManager.addEventToAttendee(EventId, UserId);
            eventManager.addAttendee(EventId, UserId);
            output.ActionDone();
        }
    }

    /**
     * Cancel an event from the Signup-events of that user.
     * @param UserId The user_id of the user who wants to cancel the event
     * @param EventId The event that the user wants to cancel
     */
    public void cancelSignedUpEvent(String UserId, Integer EventId) {
        if (attendeeManager.SignedUp(EventId, UserId)) {
            attendeeManager.removeEvent(EventId, UserId);
            output.ActionDone();
        } else {
            output.ActionFailed();
        }
    }

    /**
     * Show all the Events that the user signed up to with the event's title, location, time, event_id, and speaker.
     * @param UserId The user_id of the user who wants to check the signed up events
     */
    public void checkSignedUpEvent(String UserId){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("EST"));
        ArrayList<Event> listOfEvents = new ArrayList<>();
        ArrayList<String> listOfEventSchedule = new ArrayList<>();
        ArrayList<Integer> listOfEventsId = new ArrayList<>();
        if (attendeeManager.userExist(UserId)) {
            listOfEventsId = attendeeManager.getSignedUpEvents(UserId);
        }
        else if (organizerManager.userExist(UserId)){
            listOfEventsId = organizerManager.getSignedUpEvents(UserId);
        }
        for(Integer i: listOfEventsId){
            listOfEvents.add(eventManager.getEvent(i));
        }
        if (!listOfEventsId.isEmpty()){
            for (Event event :listOfEvents){
                String schedule = event.getTitle() + "\n" + "Location: " + event.getLocation() + "\n"
                        + "Time: " + formatter.format(event.getTime()) + "\n";
                if (eventManager.hasSpeaker(event.getID())){
                    schedule +=  "Speaker: " + speakerManager.getSpeaker(event.getSpeakerID()).getName();
                }
                listOfEventSchedule.add(schedule);
            }
            output.eventsAttendeeAndOrganizer(listOfEventSchedule);
        }
        else{
            output.noSignedUpEvents();
        }
        boolean rtn = false;
        while(!rtn){
            output.pressAnyKeyToContinue();
            String in = input.getKeyboardInput();
            if (!in.equals("")){
                rtn = true;
            }
        }
    }

    /**
     * Show all the Events that the user is assigned to with the event's title, location, time, event_id, and attendees.
     * @param UserId The user_id of the user who wants to check the assigned events
     */
    public void checkAssignedEvent(String UserId){
        ArrayList<Event> listOfEvents = new ArrayList<>();
        ArrayList<String> listOfEventSchedule = new ArrayList<>();
        ArrayList<Integer> listOfEventsId = speakerManager.getAssignedEvent(UserId);
        if(!listOfEventsId.isEmpty()){
            for(Integer i: listOfEventsId){
                listOfEvents.add(eventManager.getEvent(i));
            }
            for (Event event :listOfEvents){
                String schedule = event.getTitle() + "\n" + "Location: " + event.getLocation() + "\n"
                        + "Time: " + event.getTime() + "\n"
                        + "Attendees: ";
                if(event.getAttendees().isEmpty()){
                    schedule += "There are no attendees yet!";
                }
                else{
                    schedule += eventManager.getEventAttendees(event.getID());
                }
                /*
                if (attendeeManager.getAttendee(event.getAttendees().toString()) != null){
                    schedule += attendeeManager.getAttendee(event.getAttendees().toString());
                }
                else {
                    schedule += "There are no attendees yet!";
                }*/
                listOfEventSchedule.add(schedule);
            }
            output.eventsSpeaker(listOfEventSchedule);
        }
        else{
            output.noAssignedEvents();
        }

        boolean rtn = false;
        while(!rtn){
            output.pressAnyKeyToContinue();
            String in = input.getKeyboardInput();
            if (!in.equals("")){
                rtn = true;
            }
        }
    }
}

