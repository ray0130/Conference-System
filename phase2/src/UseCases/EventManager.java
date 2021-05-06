package UseCases;
import Entities.Event;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * The EventManager class stores all events in an arraylist,
 * and implements various of actions that are relevant to the Event object.
 * Actions includes
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */
public class EventManager implements Serializable{
    private ArrayList<Event> listOfEvents;

    /**
     * Constructor
     */
    public EventManager(){
        listOfEvents = new ArrayList<>();
    }

    /**
     * Gets an event list.
     * @return ArrayList </Event> a list that contains all event objects.
     */
    public ArrayList<Event> getListOfEvents(){
        return listOfEvents;
    }

    /**
     * Removes an event from the list.
     * @param eventID the id of event that we wish to remove.
     */
    public void removeEvent(int eventID){
        listOfEvents.remove(getEvent(eventID));
    }

    /**
     * Gets an arraylist of event ids.
     * @return ArrayList </Integer> a list of event ids.
     */

    public ArrayList<Integer> getListOfEventIDs(){
        ArrayList<Integer> listOfEventIDs = new ArrayList<>();
        for(Event event: listOfEvents){
            listOfEventIDs.add(event.getID());
        }
        return listOfEventIDs;
    }

    /**
     * Gets an event object.
     * @param eventId the id of event that we wish to return
     * @return Event event object
     * @see Event
     */
    public Event getEvent(int eventId){
        for (Event e:listOfEvents){
            if (eventId == e.getID()){
                return e;
            }
        }
        return null;
    }

    /**
     * Gets the time of an event.
     * @param eventID the id of event that we wish to return
     * @return Date an object that records the timing of the event
     * @see Date
     */
    public Date getTime(int eventID){
        return getEvent(eventID).getTime();
    }

    public Integer getID(Event event){return event.getID();}
    /**
     * Gets an arraylist of events, given the infomration of speaker id.
     * @param speakerID the id of speaker who are involved in the events
     * @return ArrayList <ArrayList </String>>
     */
    public ArrayList<ArrayList<String>> getListofEventsBySpeaker(String speakerID){
        ArrayList<ArrayList<String>> listofEventsbySpeaker = new ArrayList<>();
        for (Event listOfEvent : listOfEvents) {
            if (listOfEvent.getSpeakerID().contains(speakerID)) {
                ArrayList<String> eventInfo = new ArrayList<>();
                eventInfo.add(String.valueOf(listOfEvent.getID()));
                eventInfo.add(listOfEvent.getTitle());
                listofEventsbySpeaker.add(eventInfo);
            }
        }
        return listofEventsbySpeaker;
    }

    /**
     * Adds an event to the existing event arraylist, given the event information.
     * @param title the name of the event
     * @param time the time of the event
     * @param location the location (room) of the event
     * @param organizerID the id of the organizer who hold the event
     * @param id the id of the event
     * @return int the id of the added event
     */
    public int addEvent(String title, Date time, String location, String organizerID, int id, int maxCapacity, String length) {
        // check time (speaker??? room???)
        Event event = new Event(title, time, location, organizerID, id, maxCapacity, length);
        listOfEvents.add(event);
        return event.getID();
    }

    public int eventCapacity(int id){
        return getEvent(id).getMaxCapacity();
    }

    /**
     * Cancels an event.
     * @param id the id of the event that is wished to be cancelled
     */
    public void cancelEvent(int id){
        int index = -1;
        for (int i = 0; i < listOfEvents.size(); i++){
            if (listOfEvents.get(i).getID() == id){
                index = i;
                break;
            }
        }
        listOfEvents.remove(index);
    }

    /**
     * Checks if the event has a speaker.
     * @param eventID the id of the event that we want to check
     * @return boolean return true if the event has speaker,
     * return false otherwise
     */
    public boolean hasSpeaker(int eventID){
        for (Event event: listOfEvents){
            if (event.getID()==eventID){
                return !event.noSpeaker();
            }
        }
        return false;
    }

    /**
     * Gets the ids of the speakers in an event.
     * @param eventID the id of event that we want to check
     * @return String the id of speaker
     */
    public ArrayList<String> getSpeakerID(Integer eventID){
        for (Event event: listOfEvents){
            if (event.getID()==eventID){
                return event.getSpeakerID();
            }
        }
        return null;
    }

    /**
     * Adds an attendee to the event.
     * @param eventID the id of added event
     * @param userID the id of user who wants to perform this task
     */
    public void addAttendee(int eventID,String userID){
        for (Event e:listOfEvents){
            if (eventID == e.getID()){
                e.addAttendee(userID);
            }
        }
    }

    /**
     * Gets a list of attendees in an event.
     * @param eventID the id of event that we want to look into
     * @return ArrayList </String> a list of all attendees
     */
    public ArrayList<String> getEventAttendees(int eventID){
        if (getEvent(eventID).getAttendees() != null){
            return getEvent(eventID).getAttendees();
        }
        else {
            return new ArrayList<>();
        }
    }

    /**
     * Removes an attendee from the event.
     * @param eventID the id of event that is wished to be removed
     * @param userID the id of user who wants to perform this task
     */
    public void removeAttendee(int eventID,String userID){
        for (Event e:listOfEvents){
            if (eventID == e.getID()){
                e.removeAttendee(userID);
            }
        }
    }

    /**
     * Gets the location of an event.
     * @param eventID the id of event that we want to know the location of
     * @return String the room name
     */
    public String getLocation(int eventID){
        return getEvent(eventID).getLocation();
    }

    /**
     * Sets a list of events.
     * @param listOfEvents a list that contains all events in the system.
     */
    public void setListOfEvents(ArrayList<Event> listOfEvents){
        this.listOfEvents.addAll(listOfEvents);
    }


    /**
     * Gets a list of events that do not have speakers.
     * @return ArrayList </Event> a list that contains all Event objects
     * @see Event
     */
    public ArrayList<Event> listOfEventsWithoutSpeaker(){
        return listOfEvents;
    }

    public void renameEvent(int eventID, String newTitle){
        for (Event event:listOfEvents){
            if (event.getID()==eventID){
                event.setTitle(newTitle);
            }
        }
    }

    /**
     * Gets the time of the event from the start to the end
     * @param eventId the event we want to check
     * @return ArrayList<Date> The time of the event in hours
     */
    public ArrayList<Date> getAllTimesForEvent(int eventId) {
        ArrayList<Date> returnList = new ArrayList<>();
        Event eventItself = getEvent(eventId);
        String eventLength = eventItself.getLength();
        Date eventDate = eventItself.getTime();
        int eventLengthInt = Integer.parseInt(eventLength);
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < eventLengthInt; i++) {
            c.setTime(eventDate);
            c.add(Calendar.HOUR, i);
            returnList.add(c.getTime());
        }
        return returnList;
    }
}