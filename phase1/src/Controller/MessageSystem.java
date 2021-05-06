package Controller;

import Entities.Chat;
import Gateway.KeyboardInput;
import Presenter.TextPresenter;
import UseCases.ChatManager;

import UseCases.*;

import java.io.IOException;
import java.util.ArrayList;

/** MessageSystem controller implements various actions that can be done for the users, including
 *  sent message, get chat history, view contact list, add contact.
 *  @author Group_0112
 *  @version 1.0
 *  @since November 19th, 2020
 */

public class MessageSystem {
    private TextPresenter output;
    private KeyboardInput input;
    private ChatManager chatManager;
    private SpeakerManager speakerManager;
    private OrganizerManager organizerManager;
    private EventManager eventManager;
    private AttendeeManager attendeeManager;

    /**
     *Constructor
     * @param speakerManager The speaker manager implements by SpeakerManager use case
     * @param organizerManager The organizer manager implements by OrganizerManager use case
     * @param eventManager The event manager implements by EventManager use case
     * @param chatManager The chat manager implements by ChatManager use case
     * @param attendeeManager The attendee manager implements by AttendeeManager use case
     */
    public MessageSystem(SpeakerManager speakerManager, OrganizerManager organizerManager, EventManager eventManager, ChatManager chatManager, AttendeeManager attendeeManager) {
        this.speakerManager = speakerManager;
        this.organizerManager = organizerManager;
        this.eventManager = eventManager;
        this.chatManager = chatManager;
        this.attendeeManager = attendeeManager;
        this.output = new TextPresenter();
        this.input = new KeyboardInput();
    }

    /**
     * Gets the chat between two users (user1 and user2)
     * @param id1 The user_id of user1
     * @param id2 The user_id of user2
     * @return Chat The chat history between user1 and user2
     */
    public Chat getChat(String id1, String id2) {
        return chatManager.findChat(id1, id2);
    }

    /**
     * Sends message to other users, the options are different for different kinds of user.
     * @param sender The user_id of the user who wants to send the message
     * @throws IOException Throw IOException to avoid errors that might occur
     */
    public void sendMessage(String sender) throws IOException {
        String context;
        int role = userType(sender);
        // Organizer
        if (role == 1) {
            output.sendMsgOptions(role);
            String action = input.getKeyboardInput();

            while (!(action.equals("1") || action.equals("2") || action.equals("3") || action.equals("4"))) {
                // may need to double check if user wants to quit
                output.msgOptionInvalid();
                action = input.getKeyboardInput();
            }
            // send to all speakers, organizers, attendees
            if (action.equals("1") || action.equals("2") || action.equals("3")) {
                // prompt context
                output.promptContext();
                context = input.getKeyboardInput();
                if (context.equals("0")) {
                    return;
                }
                // add the message to everyone
                ArrayList<String> ids;
                if (action.equals("1")) {
                    ids = speakerManager.getUserIDs();
                } else if (action.equals("2")) {
                    ids = organizerManager.getUserIDs();
                } else {
                    ids = attendeeManager.getUserIDs();
                }
                for (String id : ids) {
                    if (sender.equals(id)) {
                        continue;
                    }
                    if (!(chatManager.chatExists(sender, id))) {
                        chatManager.createChat(sender, id);
                        addContact(sender, id);
                        addContact(id, sender);

                    }
                    chatManager.addMessageToChat(sender, id, context);
                }
                output.messageSentToEveryone();
            }
            // send to one user in contact list
            else {
                singleUserMessageHelper(sender);
            }
        }
        // Attendee
        else if (role == 2) {
            singleUserMessageHelper(sender);
        }
        // Speaker
        else if (role == 3) {
            String in;
            boolean validInput1 = false;
            while (!validInput1) {
                output.replyOrAutomessage();
                in = input.getKeyboardInput();

                if (in.equals("0")) {
                    validInput1 = true;
                } else if (in.equals("1")) {
                    // Select an event
                    ArrayList<ArrayList<String>> eventIDsandTitle = eventManager.getListofEventsBySpeaker(sender);
                    if (eventIDsandTitle.size() == 0) {
                        output.youHaveNoEvents();
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            System.out.println("couldnt not sleep thread");
                        }
                        return;
                    }
                    output.promptEvents(eventIDsandTitle, false);

                    String getInput = input.getKeyboardInput();
                    boolean validInput = false;
                    int eventIDChoosen = -1;// will guarantee change, -1 is a place holder
                    for (ArrayList<String> stringArrayList : eventIDsandTitle) {
                        if (stringArrayList.get(0).equals(getInput)) {
                            validInput = true;
                            eventIDChoosen = Integer.parseInt(getInput);
                            break;
                        }
                    }

                    while (!validInput) {
                        output.promptEvents(eventIDsandTitle, true);
                        getInput = input.getKeyboardInput();
                        for (ArrayList<String> strings : eventIDsandTitle) {
                            if (strings.get(0).equals(getInput)) {
                                validInput = true;
                                eventIDChoosen = Integer.parseInt(getInput);
                                break;
                            }
                        }
                    }
                    output.promptContextEvent(eventManager.getEvent(eventIDChoosen).getTitle());
                    context = input.getKeyboardInput();

                    ArrayList<String> attendeesOfEvent = eventManager.getEventAttendees(eventIDChoosen);
                    for (String id : attendeesOfEvent) {
                        if (!(chatManager.chatExists(sender, id))) {
                            chatManager.createChat(sender, id);
                            addContact(sender, id);
                            addContact(id, sender);
                        }
                        chatManager.addMessageToChat(sender, id, context);
                    }
                    output.messageSentToEveryone();
                } else if (in.equals("2")) {
                    singleUserMessageHelper(sender);
                } else {
                    output.invalidInputSelection();
                }
            }
        }
        saveState();
    }

    private void singleUserMessageHelper(String sender) {
        int role = userType(sender);
        ArrayList<String> contactList;
        if (role == 1) {
            contactList = organizerManager.contactList(sender);
        } else if (role == 2) {
            contactList = attendeeManager.contactList(sender);
        } else {
            contactList = speakerManager.contactList(sender);
        }
        if (contactList.size() == 0) {
            output.youHaveNoContacts();
            return;
        }
        //shows user their contact list
        output.promptRecipient(contactList, false);
        //tells them to choose 1 contact
        int personNumber;
        try {
            personNumber = Integer.parseInt(input.getKeyboardInput());
        } catch (NumberFormatException e) {
            personNumber = -1;
        }
        while (!(0 <= personNumber && personNumber <= contactList.size())) {
            output.promptRecipient(contactList, true);
            try {
                personNumber = Integer.parseInt(input.getKeyboardInput());
            } catch (NumberFormatException e) {
                personNumber = -1;
            }
        }
        if (personNumber == 0) {
            return;
        }
        String contactID = contactList.get(personNumber - 1);
        Chat conversation = getChat(sender, contactID);
        //prints the chat of the user
        output.printChat(conversation);
        //ask user to type a message
        output.promptContext();
        String context = input.getKeyboardInput();
        if (context.equals("0")) {
            return;
        }
        // add message
        chatManager.addMessageToChat(sender, contactID, context);
        output.messageSent();
    }


    /**
     *Saves states of the entire system.
     * @throws IOException Throw IOException to avoid errors that might occur
     */
    private void saveState() throws IOException {
        speakerManager.saveState();
        organizerManager.saveState();
        eventManager.saveState();
        chatManager.saveState();
        attendeeManager.saveState();

    }

    /**
     * See all contacts of the user
     * @param id The user_id of the user who wants to see its contacts
     */
    public void viewContacts(String id) {
        chatManager.getContactsWithChat(id);
    }

    /**
     * Check the type of user, such as 1.organizer 2.attendee, or 3.speaker. Return -1 if the user cannot be defined.
     * @param id The user_id of the user we are checking
     * @return int Integer that refer to different type of user
     */
    public int userType(String id) {
        if (organizerManager.userExist(id)) {
            return 1;
        } else if (attendeeManager.userExist(id)) {
            return 2;
        } else if (speakerManager.userExist(id)) {
            return 3;
        } else {
            System.out.println("Current User DNE Error");
            return -1;
        }
    }

    /**
     * Adds the user into the contact list
     * @param current The type of user who is the own of this contact list
     * @param id The user_id of the user we want to add
     */
    public void addContact(String current, String id) {
        int current_role = userType(current);
        int id_role = userType(id);
        boolean success = true;
        // adding contact based on their authority
        // organizer
        if (current_role == 1) {
            // adding contacts that are attendee, organizer, or speaker
            if (!organizerManager.contactExists(current, id)) {
                organizerManager.addContact(current, id);
            } else {
                success = false;
            }
        }
        // attendee
        else if (current_role == 2) {
            if (!attendeeManager.contactExists(current, id)) {
                attendeeManager.addContact(current, id);
            } else {
                success = false;
            }
        }
        // speaker
        else if (current_role == 3) {
            if (!speakerManager.contactExists(current, id)) {
                speakerManager.addContact(current, id);
            } else {
                success = false;
            }
        }
        if (!success) {
            output.addContactFailed();
        }
    }

}
