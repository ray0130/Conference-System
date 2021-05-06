package Entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Event entity records basic information of an event, including event ids, title, time, location,
 * speakerID, attendees, organizerID.
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */

public class Event implements Serializable {

    private int id;
    private String title;
    private Date time;
    private String location;
    private ArrayList<String> speakerID;
    private ArrayList<String> attendees;
    private String organizerID;
    private int maxCapacity;
    private String length;


    /**
     * Constructor
     * @param title the title of this event
     * @param time the starting time of this event
     * @param location the location of this event
     * @param organizerID the user_id of organizer who hosts this event
     */

    public Event(String title, Date time, String location, String organizerID, int id, int maxCapacity, String length) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.location = location;
        this.organizerID = organizerID;
        this.attendees = new ArrayList<>();
        this.speakerID = new ArrayList<>();
        this.maxCapacity = maxCapacity;
        this.length = length;
    }

    /**
     * Gets the id of the event
     * @return int The id of the event
     */
    public int getID() {
        return this.id;
    }

    /**
     * Gets the title of the event
     * @return String The title of the event
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the starting time of the event
     * @return Data The starting time of the event
     */
    public Date getTime() { return this.time; }

    /**
     * Gets the location of the event
     * @return String The location of the event
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Get the speaker of the event
     * @return String The user_id of the speaker of the event
     */
    public ArrayList<String> getSpeakerID() {
        return this.speakerID;
    }

    /**
     * Gets a list of all user_id of the attendees in the event
     * @return ArrayList<String> A list of attendees' user_id
     */
    public ArrayList<String> getAttendees() {
        return this.attendees;
    }

    /**
     * Gets the organizer who hosts the event
     * @return String The user_id of the organizer
     */
    public String getOrganizerID() {
        return this.organizerID;
    }

    /**
     * Sets title of the event
     * @param title The title of the event
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets time of the event
     * @param time The starting time of the event
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Sets location of the event
     * @param location The location of the event
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets Speaker of the event by its user_id
     * @param speaker The user_id of the speaker
     */
    public void setSpeaker(String speaker) {
        this.speakerID.add(speaker);
    }

    /**
     * Sets Organizer to the event
     * @param organizer The organizer who hosts the event
     */
    public void setOrganizer(String organizer) {
        this.organizerID = organizer;
    }

    /**
     * Adds Attendee to the event by its user_id
     * @param attendeeID The user_id of the attendee
     */
    public void addAttendee(String attendeeID) {
        this.attendees.add(attendeeID);
    }

    /**
     * Removes Attendee from the event by its user_id
     * @param attendeeID The user_id of the attendee
     * @return boolean Returns true if it removed the attendee, false otherwise
     */
    public boolean removeAttendee(String attendeeID) {
        return this.attendees.remove(attendeeID);
    }

    /**
     * Checks if there's a Speaker for the event
     * @return boolean Returns true if there's a Speaker for the event, false otherwise
     */
    public boolean noSpeaker(){
        return this.speakerID.isEmpty();
    }

    /**
     * Override the toString method for event
     * @return String Event{id, title, time, location, speakerID, attendees, organizerID}
     */
    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("EST"));
        return "Event ->  Id = "  + id + " | Title = " + title +" | Time = " + formatter.format(time) + " | Location = " + location + " | SpeakerID(s) = " + speakerID + " | Attendees = " + attendees + " | OrganizerID = " + organizerID + " | Max Capacity = " + maxCapacity + " | Length: " + length + " hours";
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getLength() {
        return length;
    }
}