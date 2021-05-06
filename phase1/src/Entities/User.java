package Entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User is an Abstract class of Attendee, Organizer, Speaker
 * @author Group_0112
 * @version 1.0
 * @since November 16th, 2020
 */

public abstract class User implements Serializable {
    /* this is an abstract class as all types of people are users, and have the following basic properties*/

    //userid of the user
    protected String user_id;

    //password of the user
    protected String password;

    //name of the user
    protected String name;

    //list of contacts of the user
    private ArrayList<String> contacts;

    /**
     * Constructor
     * @param user_id the user id of this user. User id is an unique integer for each user.
     * @param name the registered name of this user.
     * @param password the registered passwords of this user.
     */
    public User(String user_id, String name, String password) {
        this.user_id = user_id;
        this.name = name;
        this.password = password;
        this.contacts = new ArrayList<>();
    }

    /**
     * Gets the user id of the person
     * @return String The user's id
     */
    public String getUser_id() {
        return user_id;
    }

    //returns the password of the person

    /**
     * Gets the password of the person
     * @return String The user's password
     */
    public String getPasswords() {
        return password;
    }

    /**
     * Gets the name of the person
     * @return String The name of the person
     */
    public String getName() {
        return name;
    }

    //sets the name of the person

    /**
     * Sets the name of the person
     * @param name The name of the person
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the password of the person
     * @param password The password of the person
     */
    public void setPasswords(String password) {
        this.password = password;
    }

    /**
     * Gets the list of contacts of this user
     * @return ArrayList The list of user_ids in contact
     */
    public ArrayList<String> getContacts() {
        return contacts;
    }

    /**
     * Adds contact by id to the users list of contacts
     * @param user_id The user_id we want to add in the contact
     */
    public void addContact(String user_id){
        this.contacts.add(user_id);
    }

    /**
     * Checks if the user is already in the contact
     * @param user_id The user_id of the user we want to check in contact
     * @return boolean Returns true if the user is in contact, false otherwise
     */
    public boolean checkContact(String user_id){
        return this.contacts.contains(user_id);
    }

    /**
     * Removes the contact by id from the users list of contacts.
     * @param user_id The user_id of the user we want to remove from contact
     * @return boolean Returns true if removing was successful, false otherwise
     */
    public boolean removeContact(String user_id){
        return this.contacts.remove(user_id);
    }

    /**
     * Present the String of User in the following form: User{Name, UserID, Password, Contacts}
     * @return String The String of User
     */
    @Override
    public String toString() {
        return "User -> Name: " + getName() + " | UserID: " + getUser_id() + " | Password: " + getPasswords() + " \n\tContacts: " + getContacts();
    }

}