package Entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Speaker entity implements an application that simply records users, their list
 * of assigned-event and the organizer who creates this speaker. Events can be added or removed.
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */

public class Speaker extends User implements Serializable {

    //list of assigned events
    private ArrayList<Integer> assignEvents;
    //userid of the organizer who created this event
    private String createdOrganizer;

    /**
     * Constructor
     * @param user_id the user id of this user.
     * @param passwords the registered passwords of this user
     * @param name the registered name of this user
     * @param organizer_id the organizer who created this Speaker
     */
    public Speaker(String user_id, String passwords, String name,  String organizer_id){
        super(user_id, name, passwords);
        this.assignEvents = new ArrayList<>();
        this.createdOrganizer = organizer_id;
    }

    /**
     * Returns an arraylist of ids of assigned-event by the user.
     * @return ArrayList a list of all IDs of assigned-events
     */
    public ArrayList<Integer> getAssignEvents(){
        return assignEvents;
    }

    /**
     * Adds an event by event id to the list of assigned events of the speaker.
     * @param event_id the id of an event that is wished to be added
     */
    public void setAssignEvent(int event_id){
        assignEvents.add(event_id);
    }

    /**
     * Removes an event by event id from the list of assigned events of the speaker.
     * @param event_id  the id of an event that is wished to be removed
     */
    public void removeAssignEvent(Integer event_id){assignEvents.remove(event_id);}
}
