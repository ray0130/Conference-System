package UseCases;
import Entities.Attendee;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * The AttendeeManager class stores all attendees in an arraylist, and implements various actions that
 * can be done for an attendee, including verifying login/ if user exists/ event sign-up/ if contact exists,
 * adding/removing events, adding/ removing contacts, and getting a list of signed-up event and contacts.
 * @author Group_0112
 * @version 1.0
 * @since November 19th, 2020
 */

public class  AttendeeManager implements Serializable{

    //this dictionary contains all attendees in the system
    private Hashtable<String, Attendee> tableOfAttendees;

    /**
     * Constructor that initializes a hashtable to contains all attendees.
     */
    public AttendeeManager(){tableOfAttendees = new Hashtable<>();}

    /**
     * Adds an attendee to the hashtable of all attendees.
     * @param user_id the id of user that we wish to add
     * @param username the name of the user that we wish to add
     * @param passwords the passwords of the user that we wish to add
     */
    public void addAttendee(String user_id, String username, String passwords){
        Attendee newAttendee = new Attendee(user_id, username, passwords);
        tableOfAttendees.put(newAttendee.getUser_id(), newAttendee);
    }

    /**
     * Verifies attendee's login based on the inputted credentials. The user will be logged in if the
     * inputted information is correct.
     * @param inputUserId the user id entered by the user
     * @param inputUserPassword the password entered by the user
     * @return boolean returns true when the inputted credential is correct,
     * returns false otherwise
     */
    public boolean verifyLogIn(String inputUserId, String inputUserPassword){
        if (userExist(inputUserId)) {
            return tableOfAttendees.get(inputUserId).getPasswords().equals(inputUserPassword);
        }
        return false;
    }

    /**
     * Gets the arraylist of all user ids.
     * @return ArrayList </String> that contains all user ids.
     */
    public ArrayList<String> getUserIDs(){
        return new ArrayList<String>(tableOfAttendees.keySet());
    }

    /**
     * Checks if a particular user exists in the arraylist of attendees.
     * @param userId the id of user that we'd like to look into
     * @return boolean returns true if the user already registered in the system,
     * returns false otherwise
     */
    public boolean userExist(String userId){
        return tableOfAttendees.containsKey(userId);
    }

    /**
     * Returns a particular attendee.
     * @param userId the id of user that we'd like to return
     * @return Attendee
     * @see Attendee
     */
    public Attendee getAttendee(String userId){
        return tableOfAttendees.get(userId);
    }

    /**
     * Adds an event to the an attendee's event list.
     * @param EventId the id of event that we wish to add
     * @param userId the id of user who wants to perform this task
     */
    public void addEventToAttendee (Integer EventId, String userId){
        getAttendee(userId).addEvent(EventId);

    }

    /**
     * Checks if the user already signed up for an event.
     * @param EventId the id of event that the user wants to sign up
     * @param userId the id of user who wants to perform this task
     * @return boolean returns true if the user already signed up for this event,
     * returns false otherwise
     */
    public boolean SignedUp(Integer EventId, String userId) {
        if(getAttendee(userId).getSignedUpEvents().contains(EventId)){
            return true;
        }
        return false;
    }

    /**
     * Removes an user from the event.
     * @param EventId the id of event that the user wants to remove
     * @param userId the id of user who wants to perform this task
     */
    public void removeEvent(Integer EventId, String userId) {
        getAttendee(userId).removeEvent(EventId);
    }

    /**
     * Checks if the contact already exists.
     * @param userId the id of user who wants to perform this task
     * @param otherUserId the id of contact that the user wants to look into
     * @return boolean returns true if the contact exists in the contact list,
     * returns false otherwise
     */
    public boolean contactExists(String userId, String otherUserId){
        return getAttendee(userId).checkContact(otherUserId);
    }

    /**
     * Adds a contact to the user's contact list
     * @param userId the id of user who wants to perform this task
     * @param otherUserId the id of contact that the user wants to add
     */
    public void addContact (String userId, String otherUserId){
        // assume both users exist (checked in controller) and userid is added to otherUserID's contacts in controller as well.
        getAttendee(userId).addContact(otherUserId);
        }

    /**
     * Removes a contact from the user's contact list
     * @param userId the id of user who wants to perform this task
     * @param otherUserId the id of contact that the user wants to remove
     */
    public void removeContact (String userId, String otherUserId){
        //assume both users exist (checked in controller) and userid is removed from otherUserID's contacts in controller as well.
        getAttendee(userId).removeContact(otherUserId);
    }

    /**
     * Gets the signed-up event list of an user.
     * @param userId the id of user who wants to perform this task
     * @return ArrayList </Integer> of all event ids
     */
    public ArrayList<Integer> getSignedUpEvents (String userId){
        return getAttendee(userId).getSignedUpEvents();
    }


    /**
     * Gets the contact list of an user.
     * @param userID the id of user who wants to perform this task
     * @return ArrayList </String> of all contact ids
     */
    public ArrayList<String> contactList(String userID){
        return getAttendee(userID).getContacts();
    }

    /**
     * Saves states of attendee manager.
     * @throws IOException throw IOException to avoid errors that might occur
     */
    public void saveState() throws IOException {
        OutputStream file = new FileOutputStream("phase1/src/AttendeeManager.ser");
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(this);
        output.close();
    }

    /**
     * Imports ser files.
     * @return AttendeeManager returns an implement of this use case
     */
    public AttendeeManager importState() {
        try {
            InputStream file = new FileInputStream("phase1/src/AttendeeManager.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            AttendeeManager attendeeManager = (AttendeeManager) input.readObject();
            input.close();
            return attendeeManager;

        } catch (ClassNotFoundException | IOException e) {
            return new AttendeeManager();
        }
    }

}
