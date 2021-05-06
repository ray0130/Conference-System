package Controller;

import Gateway.KeyboardInput;
import Gateway.Serialization;
import Presenter.TextPresenter;
import UseCases.ChatManager;

import UseCases.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * MessageSystem controller implements various actions that can be done for the users, including
 * sent message, get chat history, view contact list, add contact.
 *
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
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
     * Constructor
     *
     * @param speakerManager   The speaker manager implements by SpeakerManager use case
     * @param organizerManager The organizer manager implements by OrganizerManager use case
     * @param eventManager     The event manager implements by EventManager use case
     * @param chatManager      The chat manager implements by ChatManager use case
     * @param attendeeManager  The attendee manager implements by AttendeeManager use case
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
     *
     * @param id1 The user_id of user1
     * @param id2 The user_id of user2
     * @return Chat The chat history between user1 and user2
     */
    public String getChat(String id1, String id2) {
        return chatManager.chatToString(id1, id2);
    }

    /**
     * Sends message to other users, the options are different for different kinds of user.
     *
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
                } else if (in.equals("1")) {                //mass message
                    // Select an event
                    ArrayList<ArrayList<String>> eventIDsandTitle = eventManager.getListofEventsBySpeaker(sender);
                    if (eventIDsandTitle.size() == 0) {
                        output.youHaveNoEvents();
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            output.couldntSleep();
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
                    context = input.getKeyboardInput();                                     //youve selected an event, now you write message.

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

    public void newSendMessage(String sender) throws IOException {
        int role = userType(sender);
        if (role == 1) {                               //organizer
            helperSendMessageOrganizer(sender);
        } else if (role == 2) {                          //attendee
            singleUserMessageHelper(sender);
        } else if (role == 3) {                           //speaker
            helperSendMessageSpeaker(sender);
        }
        saveState();
    }

    public void helperSendMessageOrganizer(String sender) {
        while (true) {
            output.sendMsgOptions(1);
            String action = input.getKeyboardInput();
            if (action.equals("0")) {
                return;
            } else if (action.matches("1|2|3")) {
                boolean contextGoBack = false;
                while (!contextGoBack) {
                    output.promptContext();
                    String context = input.getKeyboardInput();
                    if (context.equals("0")) {
                        contextGoBack = true;
                    } else {
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
                        return;
                    }
                }
            } else if (action.equals("4")) {
                singleUserMessageHelper(sender);
                return;
            } else {
                output.msgOptionInvalid();
            }
        }
    }

    public void helperSendMessageSpeaker(String sender) {
        String in;
        boolean validInput1 = false;
        while (!validInput1) {
            output.replyOrAutomessage();
            in = input.getKeyboardInput();
            if (in.equals("0")) {
                validInput1 = true;
            } else if (in.equals("1")) {                //mass message
                // Select an event
                ArrayList<ArrayList<String>> eventIDsandTitle = eventManager.getListofEventsBySpeaker(sender);
                if (eventIDsandTitle.size() == 0) {
                    output.youHaveNoEvents();
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        output.couldntSleep();
                    }
                    return;
                }
                else{
                    boolean validInput = false;
                    while(!validInput){
                        output.promptEvents(eventIDsandTitle, false);
                        String getInput = input.getKeyboardInput();
                        if (getInput.equals("0")){
                            validInput=true;
                        }
                        else {
                            int eventIDChosen = -1;
                            for (ArrayList<String> stringArrayList : eventIDsandTitle) {
                                if (stringArrayList.get(0).equals(getInput)) {
                                    eventIDChosen = Integer.parseInt(getInput);
                                }
                            }
                            if (eventIDChosen==-1){
                                output.promptEvents(eventIDsandTitle, true);
                            }
                            else{
                                output.promptContextEvent(eventManager.getEvent(eventIDChosen).getTitle());
                                String context = input.getKeyboardInput();                                     //youve selected an event, now you write message.
                                if (context.equals("0")){
                                    continue;
                                }
                                ArrayList<String> attendeesOfEvent = eventManager.getEventAttendees(eventIDChosen);
                                for (String id : attendeesOfEvent) {
                                    if (!(chatManager.chatExists(sender, id))) {
                                        chatManager.createChat(sender, id);
                                        addContact(sender, id);
                                        addContact(id, sender);
                                    }
                                    chatManager.addMessageToChat(sender, id, context);
                                }
                                output.messageSentToEveryone();
                                return;
                            }
                        }
                    }
                }
            } else if (in.equals("2")) {
                singleUserMessageHelper(sender);
            } else {
                output.invalidInputSelection();
            }
        }
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
        //prints the chat of the user
        output.printChat(getChat(sender, contactID));
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
     * Saves states of the entire system.
     *
     * @throws IOException Throw IOException to avoid errors that might occur
     */
    private void saveState() throws IOException {
        Serialization io = new Serialization();
        io.saveState(speakerManager, "SpeakerManager");
        io.saveState(eventManager, "EventManager");
        io.saveState(organizerManager, "OrganizerManager");
        io.saveState(chatManager, "ChatManager");
        io.saveState(attendeeManager, "AttendeeManager");

    }

    /**
     * Check the type of user, such as 1.organizer 2.attendee, or 3.speaker. Return -1 if the user cannot be defined.
     *
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
     *
     * @param current The type of user who is the own of this contact list
     * @param id      The user_id of the user we want to add
     */
    public void addContact(String current, String id) {
        int current_role = userType(current);
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


    /**
     * Add a user to the contact or remove a user for the logged in organizer by getting its user_id
     *
     * @param userID The user_id of the user we want to add/ remove
     */
    public void addRemoveContact(String userID) {
        boolean goBack = false;
        while (!goBack) {
            boolean validAddRemove = false;
            while (!validAddRemove) {
                output.addRemoveContact();
                String option = input.getKeyboardInput();
                if (option.equals("0")) {
                    validAddRemove = true;
                    goBack = true;
                } else if (option.equals("1")) {
                    boolean validUserID = false;
                    while (!validUserID) {

                        ArrayList<String> listOfContactsID = findAllUsersThatArentContacts(userID);
                        if (listOfContactsID.isEmpty()) {
                            output.allUsersAreYourContact();
                            validUserID = true;
                            continue;
                        }
                        output.enterContactUserid(false);
                        output.showContacts(listOfContactsID); // new
                        int userIndex;
                        try {
                            userIndex = Integer.parseInt(input.getKeyboardInput());
                        } catch (Exception e) {
                            userIndex = -1;
                        }
                        if (userIndex == 0) {
                            validUserID = true;
                        } else if (1 <= userIndex && userIndex <= listOfContactsID.size()) {
                            String user = listOfContactsID.get(userIndex - 1);
                            if (organizerManager.userExist(userID) && !organizerManager.contactExists(userID, user)) {
                                organizerManager.addContact(userID, user);
                                addContact(user, userID);
                                chatManager.createChat(user, userID);
                                output.ActionDone();
                                validUserID = true;
                                validAddRemove = true;
                                goBack = true;
                            } else if (attendeeManager.userExist(userID) && !attendeeManager.contactExists(userID, user)) {
                                attendeeManager.addContact(userID, user);
                                addContact(user, userID);
                                chatManager.createChat(user, userID);
                                output.ActionDone();
                                validUserID = true;
                                validAddRemove = true;
                                goBack = true;
                            } else {
                                output.userAlreadyInYourContacts();
                            }
                        } else {
                            output.enterContactUserid(true);
                        }
                    }
                } else if (option.equals("2")) {
                    boolean validUserID = false;
                    while (!validUserID) {
                        ArrayList<String> contactList;
                        if (organizerManager.userExist(userID)) {
                            contactList = organizerManager.contactList(userID);
                        } else {
                            contactList = attendeeManager.contactList(userID);
                        }
                        if (contactList.isEmpty()) {
                            output.youHaveNoContactstoRemove();
                            validUserID = true;
                            continue;
                        }
                        output.enterContactUseridtoRemove();
                        output.showContacts(contactList);
                        int userIndex;
                        try {
                            userIndex = Integer.parseInt(input.getKeyboardInput());
                        } catch (Exception e) {
                            userIndex = -1;
                        }
                        if (userIndex == 0) {
                            validUserID = true;
                        } else if (1 <= userIndex && userIndex <= contactList.size()) {
                            String user = contactList.get(userIndex - 1);
                            if (organizerManager.userExist(userID) && organizerManager.contactExists(userID, user)) {
                                organizerManager.removeContact(userID, user);

                            } else if (attendeeManager.userExist(userID) && attendeeManager.contactExists(userID, user)) {
                                attendeeManager.removeContact(userID, user);
                            }
                            if (organizerManager.userExist(user)) {
                                organizerManager.removeContact(user, userID);
                            } else if (attendeeManager.userExist(user)) {
                                attendeeManager.removeContact(user, userID);
                            } else {
                                speakerManager.removeContact(user, userID);
                            }
                            chatManager.deleteChat(user, userID);
                            output.ActionDone();
                            validUserID = true;
                            validAddRemove = true;
                            goBack = true;
                        } else {
                            output.invalidInput();
                        }
                    }
                } else {
                    output.invalidInputSelection();
                }
            }
        }
    }

    public void newAddRemoveContact(String userid) throws IOException {
        boolean main = false;
        while (!main) {
            output.addRemoveContact();
            String option = input.getKeyboardInput();
            if (option.equals("0")) {
                return;
            } else if (option.equals("1")) {
                int helper = helperAddContact(userid);
                if (helper == 1) {
                    return;
                }
            } else if (option.equals("2")) {
                int helper = helperRemoveContact(userid);
                if (helper == 1) {
                    return;
                }
            } else {
                output.invalidInputSelection();
            }
        }
        saveState();
    }

    private int helperAddContact(String userid) {
        while (true) {
            ArrayList<String> listOfContactsID = findAllUsersThatArentContacts(userid);
            if (listOfContactsID.isEmpty()) {
                output.allUsersAreYourContact();
                return 0;
            }
            output.enterContactUserid(false);
            output.showContacts(listOfContactsID); // new
            int userIndex;
            try {
                userIndex = Integer.parseInt(input.getKeyboardInput());
            } catch (Exception e) {
                userIndex = -1;
            }
            if (userIndex == 0) {
                return 0;
            } else if (1 <= userIndex && userIndex <= listOfContactsID.size()) {
                String user = listOfContactsID.get(userIndex - 1);
                if (organizerManager.userExist(userid) && !organizerManager.contactExists(userid, user)) {
                    organizerManager.addContact(userid, user);
                    addContact(user, userid);
                    chatManager.createChat(user, userid);
                    output.ActionDone();
                    return 1;
                } else if (attendeeManager.userExist(userid) && !attendeeManager.contactExists(userid, user)) {
                    attendeeManager.addContact(userid, user);
                    addContact(user, userid);
                    chatManager.createChat(user, userid);
                    output.ActionDone();
                    return 1;
                } else {
                    output.userAlreadyInYourContacts();
                }
            } else {
                output.enterContactUserid(true);
            }

        }
    }

    private int helperRemoveContact(String userid) {
        while (true) {
            ArrayList<String> contactList;
            if (organizerManager.userExist(userid)) {
                contactList = organizerManager.contactList(userid);
            } else {
                contactList = attendeeManager.contactList(userid);
            }
            if (contactList.isEmpty()) {
                output.youHaveNoContactstoRemove();
                return 0;
            }
            output.enterContactUseridtoRemove();
            output.showContacts(contactList);
            int userIndex;
            try {
                userIndex = Integer.parseInt(input.getKeyboardInput());
            } catch (Exception e) {
                userIndex = -1;
            }
            if (userIndex == 0) {
                return 0;
            } else if (1 <= userIndex && userIndex <= contactList.size()) {
                String user = contactList.get(userIndex - 1);
                if (organizerManager.userExist(userid) && organizerManager.contactExists(userid, user)) {
                    organizerManager.removeContact(userid, user);

                } else if (attendeeManager.userExist(userid) && attendeeManager.contactExists(userid, user)) {
                    attendeeManager.removeContact(userid, user);
                }
                if (organizerManager.userExist(user)) {
                    organizerManager.removeContact(user, userid);
                } else if (attendeeManager.userExist(user)) {
                    attendeeManager.removeContact(user, userid);
                } else {
                    speakerManager.removeContact(user, userid);
                }
                chatManager.deleteChat(user, userid);
                output.ActionDone();
                return 1;
            } else {
                output.invalidInput();
            }
        }
    }

    private ArrayList<String> findAllUsersThatArentContacts(String userID) {
        ArrayList<String> listOfContactsID;
        ArrayList<String> listofAllIDs = new ArrayList<>();
        listofAllIDs.addAll(organizerManager.getUserIDs());
        listofAllIDs.addAll(speakerManager.getUserIDs());
        listofAllIDs.addAll(attendeeManager.getUserIDs());

        if (organizerManager.userExist(userID)) {
            listOfContactsID = organizerManager.contactList(userID); // new
        } else {
            listOfContactsID = attendeeManager.contactList(userID);
        }
        listofAllIDs.removeAll(listOfContactsID);
        listofAllIDs.remove(userID);
        return listofAllIDs;
    }
}
