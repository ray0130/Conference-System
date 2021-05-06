package UseCases;

import Entities.Speaker;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * The SpeakerManager program implements an application that records all speakers from Speaker and the contact list
 * and verify LogIn. The users in the contact list can be added or removed.
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */

public class SpeakerManager implements Serializable{
    private Hashtable<String, Speaker> tableOfSpeakers;

    /**
     * Constructor that initializes a hashtable to contains all speakers.
     */
    public SpeakerManager (){
        tableOfSpeakers = new Hashtable<>();
    }

    /**
     * Adds an attendee to the hashtable of all attendees.
     * @param user_id the id of user that we wished to add
     * @param passwords the passwords of the user that we wished to add
     * @param name the name of the user that we wished to add
     * @param organizerID the organizer who created this Speaker
     */
    public void addSpeaker(String user_id, String passwords, String name, String organizerID){
        Speaker newSpeaker = new Speaker(user_id,  passwords, name, organizerID);
        tableOfSpeakers.put(newSpeaker.getUser_id(), newSpeaker);
    }

    /**
     * Verifies speaker's login based on the inputted credentials. The user will be logged in if the
     * inputted information is correct.
     * @param inputUserId the user id entered by the user
     * @param inputUserPassword the password entered by the user
     * @return boolean return true when the inputted credential is correct, return false otherwise
     */
    public boolean verifyLogIn(String inputUserId, String inputUserPassword){
        if (userExist(inputUserId)) {
            return tableOfSpeakers.get(inputUserId).getPasswords().equals(inputUserPassword);
        }
        return false;
    }


    /**
     * Gets the arraylist of all user ids in the contact.
     * @param userID the user id of this user
     * @return ArrayList returns a list of contacts of the given user
     */
    public ArrayList<String> contactList(String userID){
        return getSpeaker(userID).getContacts();
    }

    /**
     * Gets the arraylist of all speaker's user ids.
     * @return ArrayList a list list of user_ids
     */
    public ArrayList<String> getUserIDs(){
        return new ArrayList<String>(tableOfSpeakers.keySet());
    }

    /**
     * Checks if the speaker with its given user_id is in SpeakerManager.
     * @param userId the user id of this speaker
     * @return boolean return true if SpeakerManager contains this speaker
     */
    public boolean userExist(String userId){ return tableOfSpeakers.containsKey(userId);
    }

    /**
     * Gets a speaker refer to it's given user_id.
     * @param userId the user id of speaker
     * @return Speaker the speaker refer to the given user_id
     */
    public Speaker getSpeaker(String userId){
        return tableOfSpeakers.get(userId);
    }

    /**
     * Assigns an event to a speaker with its given user_id.
     * @param EventId the event id of the event which is assigned to speaker
     * @param userId the user id of this speaker
     */
    public void addEventToSpeaker (Integer EventId, String userId){
        getSpeaker(userId).setAssignEvent(EventId);
    }

    /**
     * Removes the assigned event from a speaker with its given user_id.
     * @param EventId the event id of the event which is assigned to speaker
     * @param userId the user id of this speaker
     */
    public void removeEvent(Integer EventId, String userId) {
        getSpeaker(userId).removeAssignEvent(EventId);
    }

    /**
     * Checks if this speaker has the contact of another user with its given user_id.
     * @param userId the user id of this speaker
     * @param otherUserId the user id which needs to be checked
     * @return boolean returns true if otherUserId is in the ContactList of this speaker.
     */
    public boolean contactExists(String userId, String otherUserId){
        return getSpeaker(userId).checkContact(otherUserId);
    }

    /**
     * Adds an user with its given user_id to this speaker's ContactList.
     * @param userId the user id of this speaker
     * @param otherUserId the user id which would be added
     */
    public void addContact (String userId, String otherUserId){
        getSpeaker(userId).addContact(otherUserId);
    }

    /**
     * Removes an user with its given user_id from this speaker's ContactList.
     * @param userId the user id of this speaker
     * @param otherUserId the user id which would be removed
     */
    public void removeContact (String userId, String otherUserId){
        getSpeaker(userId).removeContact(otherUserId);
    }

    /**
     * Returns an ArrayList of all AssignEvents.
     * @param userId the user id of this speaker
     * @return ArrayList a list of AssignEvents
     */
    public ArrayList<Integer> getAssignedEvent (String userId){
        return getSpeaker(userId).getAssignEvents();
    }

}
