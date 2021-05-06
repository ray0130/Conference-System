package UseCases;
import Entities.Event;
import Entities.Room;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

/** The RoomManager class stores all room in a hashtable,
 * and implements various functions thar are relevant to the Room object.
 * Major functions include creating room, adding/removing event to room,
 * verifying available location, getting room/location, etc.
 * @author Group_0112
 * @version 1.0
 * @since November 19th, 2020
 */
public class RoomManager implements Serializable{
    //dictionary of all rooms (key is the roomlocation)
    private Hashtable<String, Room> tableOfRooms;

    /**
     * Constructor. It creates 5 room with different room locations.
     */
    public RoomManager(){
        this.tableOfRooms = new Hashtable<>();
    }

    /**
     * Creates a room.
     * @param location the location of the room.
     */
    public void createRoom(String location){
        Room room = new Room(location);
        tableOfRooms.put(location, room);
    }

    /**
     * Checks if the location is not currently booked in the existing system (still available for use).
     * @param location the location of the room
     * @return boolean returns true if the location is available and add the room to it,
     * returns false otherwise
     */
    public boolean locationIDAvailable(String location){
        return !tableOfRooms.containsKey(location);
    }
    //room manager should be able to pull up a list of rooms that are free at a given time
    public ArrayList<String> getAvailableRooms(Date date){
        ArrayList<String> availableRooms = new ArrayList<String>();
        for (Room roomValue: tableOfRooms.values()){
            if (!roomValue.isBooked(date)){
                availableRooms.add(roomValue.getRoomLocation());
            }
        }
        return availableRooms;
    }

    /**
     * Adds an event to a given room at a given time.
     * @param roomLocation the location of the room
     * @param eventID the id of the selected event
     * @param date the date of the selected event
     */
    public void addEventToRoom(String roomLocation, Integer eventID, Date date){
        tableOfRooms.get(roomLocation).addBookedTime(date, eventID);
    }


    /**
     * Removes an event from the room at a given time.
     * @param roomLocation the location of the room
     * @param eventID the id of the selected event
     * @param date the date of the selected event
     */
    public void removeEventFromRoom(String roomLocation, Integer eventID, Date date){
        tableOfRooms.get(roomLocation).removeBookedTime(date, eventID);
    }

    /**
     * Gets the location of a room.
     * @param roomloc the location name of a room
     * @return Room a room object
     * @see Room
     */
    public Room getRoom(String roomloc){
        for (String loc : tableOfRooms.keySet()){
            if (loc.equals(roomloc)){
                return tableOfRooms.get(loc);
            }
        }
        return null;
    }

    /**
     * Gets the location of all rooms in the system.
     * @return ArrayList </String> contains location name of all rooms.
     */
    public ArrayList<String> getAllRoomLocs() {
        return new ArrayList<>(tableOfRooms.keySet());
    }

    /**
     * Saves states of room manager.
     * @throws IOException throw IOException to avoid errors that might occur
     */
    public void saveState() throws IOException{
        OutputStream file = new FileOutputStream("phase1/src/RoomManager.ser");
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(this);
        output.close();
    }

    /**
     * Imports ser files.
     * @return RoomManager returns an implement of this use case
     */
    public RoomManager importState() {
        try {
            InputStream file = new FileInputStream("phase1/src/RoomManager.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            RoomManager roomManager = (RoomManager) input.readObject();
            input.close();
            return roomManager;

        } catch (ClassNotFoundException | IOException e) {
            return new RoomManager();
        }
    }
}


//unused method
//    public int getSpots(String roomloc){
//        Room r = getRoom(roomloc);
//        return r.getCapacity();
//    }
