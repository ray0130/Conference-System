package Controller;

import Entities.Event;
import Gateway.KeyboardInput;
import Presenter.TextPresenter;
import UseCases.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * EventSystem controller implements various actions that can be done for the users, including
 *  see all Events, create/delete Events, Sign up for Events /cancel an event, check Schedule for Signed Up and Assigned Events.
 *  This is all dependent on who is signed into the system.
 *  @author Group_0112
 *  @version 2.0
 *  @since December 1st, 2020
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
            String schedule = "Title: " + event.getTitle() + "\nEvent type: " + eventType(event.getSpeakerID().size())
                    + "\nLocation: " + event.getLocation() + "\nTime: " + formatter.format(event.getTime()) +
                    "\nEvent ID: " + event.getID() + "\nEvent Capacity: " + event.getMaxCapacity() + "\nEvent Length: "
                    + event.getLength() + " Hours";
            if (eventManager.hasSpeaker(event.getID())){
                schedule +=  "\nSpeaker: ";
                ArrayList<String> speakerNames = new ArrayList<>();
                for (String speaker: event.getSpeakerID()) {
                    speakerNames.add(speakerManager.getSpeaker(speaker).getName());
                }
                schedule += speakerNames;

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
                StringBuilder schedule = new StringBuilder("Title: " + event.getTitle() + "\nEvent type: "+
                eventType(event.getSpeakerID().size()) + "\nLocation: " + event.getLocation() + "\n"
                        + "Time: " + formatter.format(event.getTime()) + "\n" + "Event Length (In Hours): " + event.getLength());
                if (eventManager.hasSpeaker(event.getID())){
                    schedule.append("Speaker: ");
                    ArrayList<String> speakerNames = new ArrayList<>();
                    for (String speaker: event.getSpeakerID()) {
                        speakerNames.add(speakerManager.getSpeaker(speaker).getName());
                    }
                    schedule.append(speakerNames);
                }
                listOfEventSchedule.add(schedule.toString());
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

    /**
     * Returns the type of the event
     * @param speakerSize The number of speakers in the event
     * @return String Returns "Party", "Talk", or "Panel discussion"
     */
    public String eventType( int speakerSize){
        if (speakerSize == 0){
            return "Party";
        }
        else if (speakerSize == 1){
            return "Talk";
        }
        else {
            return "Panel discussion";
        }
    }

    public void newJoinLeaveEvent(String userid){
        boolean validInput = false;
        while (!validInput){
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
            else if (joinLeaveInt==1){                                               //user wants to join event
                int helperInt = helperJoinEvent(userid);
                if (helperInt==1){
                    validInput=true;
                    try {
                        Thread.sleep(1000);
                    }
                    catch (Exception e){
                        output.couldntSleep();
                    }
                }
            }
            else if (joinLeaveInt==2){                                               //user wants to leave event
                int helperInt = helperLeaveEvent(userid);
                if (helperInt==1){
                    validInput=true;
                    try {
                        Thread.sleep(1000);
                    }
                    catch (Exception e){
                        output.couldntSleep();
                    }
                }
            }
            else{
                output.invalidInput();
            }
        }
    }

    public ArrayList<Integer> helperJoinAvailableEventRetriever(String userid) {
        ArrayList<Integer> listOfAllEventIDs = eventManager.getListOfEventIDs();                            //gets list of all events
        ArrayList<Integer> listOfCurrentlyAttendingEventIds;
        if (attendeeManager.userExist(userid)) {
            listOfCurrentlyAttendingEventIds = attendeeManager.getSignedUpEvents(userid);    //gets list of all events this user is already attending
        } else {                                                                            //userid has to be an organizer
            listOfCurrentlyAttendingEventIds = organizerManager.getSignedUpEvents(userid);
        }
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
            int capacity = eventManager.eventCapacity(eventid);                                       //gets the capacity of the  event
            int numExistingAttendees = actualEvent.getAttendees().size();                                   //gets the number of attendees that are attending this event
            if (capacity - numExistingAttendees == 0) {                                                           //if the number of attendees attending this event has reached the max capacity of the room,
                listOfEventsThatNeedToBeRemoved.add(eventid);                                                     //the organizer cannot join this room. Remove it from the list.
            }
        }
        listOfAllEventIDs.removeAll(listOfEventsThatNeedToBeRemoved);
        return listOfAllEventIDs;
    }

    public int helperJoinEvent(String userid){
        while (true){
            if (!(attendeeManager.userExist(userid)||organizerManager.userExist(userid))){
                output.ActionFailed(); // if the user is not a organizer or a attendee, then he shouldnt be joining/leaving events
                return 0;
            }
            ArrayList<Integer> listOfJoinableEventIDs = new ArrayList<Integer>();
            listOfJoinableEventIDs = helperJoinAvailableEventRetriever(userid);
            if (listOfJoinableEventIDs.isEmpty()) {
                output.noEventAvailableToJoin();
                return 0;
            }
            ArrayList<Event> listOfJoinableEvents = new ArrayList<>();                                          //list of all events this Attendee can join
            for (Integer eventid : listOfJoinableEventIDs) {
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
                return 0;
            }
            else if (1 <= eventSelectedInt && eventSelectedInt <= listOfJoinableEvents.size()) {
                if (attendeeManager.userExist(userid)){
                    attendeeManager.addEventToAttendee(listOfJoinableEventIDs.get(eventSelectedInt - 1), userid);         //add eventid to the attendees list of events.
                }
                else if (organizerManager.userExist(userid)){
                    organizerManager.addEventToOrganizer(listOfJoinableEventIDs.get(eventSelectedInt - 1), userid);         //add eventid to the attendees list of events.
                }
                eventManager.addAttendee(listOfJoinableEventIDs.get(eventSelectedInt - 1), userid);                     //add attendee to events list of attendees for this event.
                output.ActionDone();
                return 1;
            } else {
                output.joinLeaveInvalidResponse();
            }
        }
    }

    public int helperLeaveEvent(String userID){
        while(true){
            ArrayList<Integer> listOfAttendingEventIds;
            if (attendeeManager.userExist(userID)){
                listOfAttendingEventIds = attendeeManager.getSignedUpEvents(userID);            //get the list of signed up eventids
            }
            else if (organizerManager.userExist(userID)){
                listOfAttendingEventIds = organizerManager.getSignedUpEvents(userID);
            }
            else {
                output.ActionFailed();
                return 0;
            }
            ArrayList<Event> listofAttendingEvents = new ArrayList<>();
            for (Integer eventid : listOfAttendingEventIds) {                                                      //get the list of events
                listofAttendingEvents.add(eventManager.getEvent(eventid));
            }
            if (listofAttendingEvents.isEmpty()) {
                output.noSignedUpEvents();
                return 0;
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
                    return 0;
                } else if (1 <= eventSelectedInt && eventSelectedInt <= listofAttendingEvents.size()) {
                    int eventid = listOfAttendingEventIds.get(eventSelectedInt - 1);
                    eventManager.removeAttendee(eventid, userID);
                    if (attendeeManager.userExist(userID)){
                        attendeeManager.removeEvent(eventid, userID);
                    }
                    else if (organizerManager.userExist(userID)){
                        organizerManager.removeEvent(eventid, userID);
                    }
                    output.ActionDone();
                    return 1;
                } else {
                    output.joinLeaveInvalidResponse();
                }
            }
        }
    }

}

