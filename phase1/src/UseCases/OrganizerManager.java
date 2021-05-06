package UseCases;

import Entities.Attendee;
import Entities.Organizer;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * The OrganizerManager class stores all organizers in a hashtable,
 * and implements various functions that can be done for an organizer.
 * Functions include getting/ adding organizer, verifying login, adding/removing contacts,
 * setting events, etc.
 * @author Group_0112
 * @version 1.0
 * @since November 19th, 2020
 */
public class OrganizerManager implements Serializable{

    private Hashtable<String, Organizer> tableOfOrganizers;

    /**
     * Constructor
     */
    public OrganizerManager (){
        tableOfOrganizers = new Hashtable<>();
    }

    /**
     * Adds an organizer to the existing table of organizer.
     * @param user_id the id of user that we want to add
     * @param username the name of the user that we want to add
     * @param passwords the passwords of the user that we want to add
     */
    public void addOrganizer(String user_id, String username, String passwords){
        Organizer newOrganizer = new Organizer(user_id, username, passwords);
        tableOfOrganizers.put(newOrganizer.getUser_id(), newOrganizer);
    }

    /**
     * Verifies organizer's login based on the inputted credential. The user will be
     * logged in if the inputted information is correct.
     * @param inputUserId the user id inputted by the user
     * @param inputUserPassword the passwords inputted by the user
     * @return boolean returns true if the the login information is correct,
     * returns false otherwise
     */
    public boolean verifyLogIn(String inputUserId, String inputUserPassword){
        if (userExist(inputUserId)) {
            return tableOfOrganizers.get(inputUserId).getPasswords().equals(inputUserPassword);
        }
        return false;
    }

    /**
     * Gets the ids of all organizers.
     * @return ArrayList </String> a list that contains all ids for all organizers
     */
    public ArrayList<String> getUserIDs(){
        return new ArrayList<String>(tableOfOrganizers.keySet());
    }

    /**
     * Checks if an organizer exists in the organizers arraylist.
     * @param userId the id of the organizer that we want to look into
     * @return boolean returns true if the organizer exists,
     * returns false otherwise
     */
    public boolean userExist(String userId){
        return tableOfOrganizers.containsKey(userId);
    }

    /**
     * Gets the id of an organizer.
     * @param userId the id of the organizer that we want to look into
     * @return Organizer an organizer object
     * @see Organizer
     */
    public Organizer getOrganizer(String userId){
        return tableOfOrganizers.get(userId);
    }

    //An attendee action

    /**
     * Adds an event to the organizer.
     * @param EventId the id of event that is requested to be added
     * @param userId the id of organizer who wants to perform this task
     */
    public void addEventToOrganizer (Integer EventId, String userId){
        getOrganizer(userId).addEvent(EventId);
    }


    //An attendee action

    /**
     * Remove an event for an organizer.
     * @param EventId the id of event that is requested to be removed
     * @param userId the id of organizer who wants to perform this task
     */
    public void removeEvent(Integer EventId, String userId) {
        getOrganizer(userId).removeEvent(EventId);
    }

    /**
     * Checks if the user already exists in the organizer's contact list.
     * @param userId the id of user who wants to perform this task
     * @param otherUserId the id of another user the person wants to look into
     * @return boolean returns true if the other user is already in the contact list of this user,
     * returns false otherwise
     */
    public boolean contactExists(String userId, String otherUserId){
        return getOrganizer(userId).checkContact(otherUserId);
    }

    //An attendee action

    /**
     * Adds an user to the contact list.
     * @param userId the id of user who wants to perform this task
     * @param otherUserId the id of another user the person wants to look into
     */
    public void addContact (String userId, String otherUserId){
        // assume both users exists (checked in controller) and userid is added to otherUserID's contacts in controller as well.
        getOrganizer(userId).addContact(otherUserId);
    }


    /**
     * Returns a list of contacts of the given user.
     * @param userID the id of the user who wants to perform this task
     * @return ArrayList </String> a list containing string of all contacts of the given user
     */
    public ArrayList<String> contactList(String userID){
        return getOrganizer(userID).getContacts();
    }
    //An attendee action

    /**
     * Removes a contact from the contact list of the given user.
     * @param userId the id of user who wants to perform this task
     * @param otherUserId the id of another user that this person wants to look into
     */
    public void removeContact (String userId, String otherUserId){
        //assume both users exists (checked in controller) and userid is removed from otherUserID's contacts in controller as well.
        getOrganizer(userId).removeContact(otherUserId);
    }

    /**
     * Gets a list of all signed-up events.
     * @param userId the id of user who wants to perform this task
     * @return ArrayList </Integer> a list containing all ids of signed-up events.
     */
    public ArrayList<Integer> getSignedUpEvents (String userId){ return getOrganizer(userId).getSignedUpEvents(); }

    /*but we need to actually create the speaker!!!*/
    //Following are Organizer ONLY actions

    /**
     * Creates a speaker in the system.
     * @param user_id the id of user who wants to perform this task
     * @param speaker_user_id the id of speaker that is wished to be set
     */
    public void setAddSpeakerCreated(String user_id, String speaker_user_id){
        getOrganizer(user_id).addSpeakerCreated(speaker_user_id);
    }

    //but we actually have to add event to event system stuff!!!
    //this only tells that this user made the event, doesnt acc make the event

    /**
     * Creates an event in the system.
     * @param user_id the id of user who wants to perform this task
     * @param event_id the id of event that is requested to be added
     */
    public void setAddEventCreated(String user_id, Integer event_id){
        getOrganizer(user_id).addEventCreated(event_id);
    }

    //but we acc have to make a event!!
    //this only deletes it from the organizers list!!!

    /**
     * Deletes an event in the system.
     * @param user_id the id of user who wants to perform this task
     * @param event_id the id of event that is requested to be deleted
     */
    public void setDeleteEventCreated(String user_id, Integer event_id){
        getOrganizer(user_id).deleteEventCreated(event_id);
    }

    /**
     * Saves states of organizer manager.
     * @throws IOException throw IOException to avoid errors that might occur
     */
    public void saveState() throws IOException {
        OutputStream file = new FileOutputStream("phase1/src/OrganizerManager.ser");
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(this);
        output.close();
    }

    /**
     * Imports ser files.
     * @return OrganizerManager returns an implement of this use case
     */
    public OrganizerManager importState() {
        try {
            InputStream file = new FileInputStream("phase1/src/OrganizerManager.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            OrganizerManager organizerManager = (OrganizerManager) input.readObject();
            input.close();
            return organizerManager;

        } catch (ClassNotFoundException | IOException e) {
            return new OrganizerManager();
        }
    }

}
