package Entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;

/**
 * Room entity records basic information of an room, including location, capacity, bookedTime.,
 * @author Group_0112
 * @version 1.0
 * @since November 19th, 2020
 */

public class Room implements Serializable {
    //capacity of the room
    private Integer capacity = 2;
    //location of the room
    private String roomLocation;
    //the key is the  time, and the value is the eventid
    private Hashtable<Date, Integer> bookedTime;

    /**
     * Constructor
     * @param roomLoc The location of the room
     */
    public Room(String roomLoc){
        this.roomLocation = roomLoc;
        this.bookedTime = new Hashtable<>();
    }

    /**
     * Gets the Location of the room
     * @return String The room's location
     */
    public String getRoomLocation() {
        return roomLocation;
    }

    /**
     * Gets the capacity of the room
     * @return Integer The capacity of the room
     */
    public Integer getCapacity(){
        return this.capacity;
    }

    /**
     * Gets the dictionary of the date of events booked
     * @return Hashtable The dictionary of the date of events booked
     */
    public Hashtable<Date, Integer> getBookedTime(){
        return this.bookedTime;
    }

    /**
     * Checks if the room is booked at a given date/time
     * @param date
     * @return boolean Returns true if the room is booked, false otherwise
     */
    public boolean isBooked(Date date){
        return bookedTime.containsKey(date);
    }

    /**
     * Adds an event to the given time in our dictionary
     * @param date The date of the event
     * @param EventId The event_id of the event
     * @return boolean Returns ture if the room is booked successfully
     */
    public boolean addBookedTime(Date date, Integer EventId){
        //if the room is already booked, you cant add a new event!
        if (bookedTime.containsKey(date)){
            return false;
        }
        //if it isnt booked, add the new event
        bookedTime.put(date, EventId);
        return true;
    }

    /**
     * Removes event at given time from our dictionary
     * @param date The date of the event
     * @param EventId The event_id of the event
     * @return boolean Returns ture if the room is removed from booking list successfully
     */
    public boolean removeBookedTime(Date date, Integer EventId){
        //if the event exists at the time inour dictionary, then delete the event
        if (bookedTime.containsKey(date) && bookedTime.get(date).equals(EventId)){
            bookedTime.remove(date, EventId);
            return true;
        }
        //otherwise return false, ie. the event wasnt deleted from the time
        return false;
    }

    /**
     * Present the String of Room in the following form: Room{capacity, roomLocation, bookedTime}
     * @return String The String of the Room
     */
    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("EST"));
        StringBuilder ret = new StringBuilder("Room Location = " + roomLocation + " | Capacity = " + capacity + " | booked Time: \n");
        ArrayList<Date> dateKeys = new ArrayList<Date>(bookedTime.keySet());
        for (Date d:dateKeys) {
            ret.append("\tTime: ");
            ret.append(formatter.format(d));
            ret.append(" | Event ID: ").append(bookedTime.get(d)).append("\n");
        }
        return ret.toString();
    }
}
