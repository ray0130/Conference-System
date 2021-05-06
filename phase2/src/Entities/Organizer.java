package Entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Organizer entity implements an application that simply records users, the list
 * of event organizer created.
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */

public class Organizer extends Attendee implements Serializable {
    //list of all speakers created
    private ArrayList<String> speakersCreated;
    //list of all events created by this Organzier
    private ArrayList<Integer> eventsCreated;

    /**
     * Constructor
     * @param user_id the user id of the organizer.
     * @param name the registered name of the organizer
     * @param passwords the registered passwords of the organizer
     */
    public Organizer(String user_id, String name, String passwords) {
        super(user_id, name, passwords);
        this.speakersCreated = new ArrayList<>();
        this.eventsCreated = new ArrayList<>();
    }

    /**
     * Adds a speaker that this organizer created
     * @param user_id The user_id of the speaker created
     */
    public void addSpeakerCreated(String user_id){
        this.speakersCreated.add(user_id);
    }

    /**
     * Adds an event created by this organizer by its given event_id
     * @param event_id The id of the event
     */
    public void addEventCreated(Integer event_id){
        this.eventsCreated.add(event_id);
    }


    /**
     * Deletes an event created by this organizer from the list eventsCreated .
     * @param event_id The id of the event
     * @return boolean Returns true if the event is deleted, false otherwise
     */
    public boolean deleteEventCreated(Integer event_id){
        return this.eventsCreated.remove(event_id);
    }


}
