package Entities;

import java.util.ArrayList;
import java.io.*;

/**
 * The Attendee is a  user. It has its list of signed up events.
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */

public class Attendee extends User implements Serializable{

    private ArrayList<Integer> signedUpEvents;

    /**
     * Constructor
     * @param user_id the user id of this user. User id is an unique integer for each user.
     * @param name    the registered name of this user.
     * @param passwords the registered passwords of this user.
     */
    public Attendee(String user_id, String name, String passwords){
        super(user_id, name, passwords);
        this.signedUpEvents = new ArrayList<>();
    }

    /**
     * Returns an arraylist of ids of signed-up event by the user.
     * @return ArrayList of all IDs of signed-up events
     */
    public ArrayList<Integer> getSignedUpEvents() {
        return signedUpEvents;
    }

    /**
     * Adds an event by event id to the list of signed-up events of the attendee.
     * @param EventId  the id of an event that is wished to be added
     */
    public void addEvent(Integer EventId){
        this.signedUpEvents.add(EventId);
    }

    /**
     * Removes an event by event id from the list of signed-up events of the attendee.
     * @param EventId  the id of an event that is wished to be removed
     * @return boolean  returns true if the event has been successfully removed, false otherwise.
     */
    public boolean removeEvent(Integer EventId){
        return this.signedUpEvents.remove(EventId);
    }
    }