package Controller;

import java.io.IOException;

import Gateway.KeyboardInput;
import Presenter.TextPresenter;
import UseCases.*;

/** SpeakerSystem controller implements various actions that can be done for a speaker, including
 *  see all Events, check Schedule for assigned-Events, message other Users, logout the account, shutdown the system.
 *  @author Group_0112
 *  @version 1.0
 *  @since November 19th, 2020
 */

public class SpeakerSystem {
    private TextPresenter output;
    private KeyboardInput input;
    private AttendeeManager attendeeManager;
    private OrganizerManager organizerManager;
    private SpeakerManager speakerManager;
    private ChatManager chatManager;
    private MessageSystem messageSystem;
    private EventSystem eventSystem;

    /**
     * Constructor
     * @param speakerManager The speaker manager implements by SpeakerManager use case
     * @param organizerManager The organizer manager implements by OrganizerManager use case
     * @param chatManager The chat manager implements by ChatManager use case
     * @param attendeeManager The attendee manager implements by AttendeeManager use case
     * @param messageSystem The message system implements by MessageSystem Controller
     * @param eventSystem The event system implements by EventSystem Controller
     */

    public SpeakerSystem (SpeakerManager speakerManager, OrganizerManager organizerManager, ChatManager chatManager,
                          AttendeeManager attendeeManager, MessageSystem messageSystem, EventSystem eventSystem){
        this.speakerManager = speakerManager;
        this.organizerManager = organizerManager;
        this.attendeeManager = attendeeManager;
        this.chatManager = chatManager;
        this.messageSystem = messageSystem;
        this.eventSystem = eventSystem;
        this.input = new KeyboardInput();
        this.output = new TextPresenter();
    }

    /**
     * Speaker is allowed to do the following options: 1.see all Events. 2.check Schedule for assigned-Events.
     * 3.message other Users. 4.logout the account. 5.shutdown the system
     * @param userID The user_id of the speaker who is logged in
     * @return object Returns true if user wants to shutdown system, or false if user wants to logout.
     * @throws IOException Throw IOException to avoid errors that might occur
     */
    public boolean start(String userID) throws IOException {
        while (true) {
            String i;
            boolean validInput = false;
            output.SpeakerMenu();
            i = input.getKeyboardInput();
            //1. see all Events.
            if (i.equals("1")) {
                eventSystem.checkAllEvents();
            }
            //2. see the Events they're assigned to.
            else if (i.equals("2")) {
                eventSystem.checkAssignedEvent(userID);
            }
            //3. Message Attendees
            else if (i.equals("3")){
                messageSystem.sendMessage(userID);
            }
            //4. logout
            else if (i.equals("4")){
                return false;
            }
            //5. shutdown
            else if (i.equals("5")){
                return true;
            }
            saveState();
        }
    }

    /**
     * Saves states of the system.
     * @throws IOException Throw IOException to avoid errors that might occur
     */
    private void saveState() throws IOException {
        speakerManager.saveState();
        organizerManager.saveState();
        chatManager.saveState();
        attendeeManager.saveState();
    }

    /**
     *Sends message to another user.
     * @param userID The user_id of the user that we send message to
     * @throws IOException Throw IOException to avoid errors that might occur
     */
    private void message(String userID) throws IOException {
        messageSystem.sendMessage(userID);
    }

    /**
     * Check if the user already registered in this system or not
     * @param userid The user_id of the user we want to check
     * @return boolean Returns true if the user already registered, false otherwise
     */
    private boolean userExists(String userid){
        return attendeeManager.userExist(userid) || organizerManager.userExist(userid) || speakerManager.userExist(userid);
    }

    /**
     *Add a user to the contact or remove a user from the contact by getting its user_id
     * @param userID The user_id of the user we want to add/ remove for. (logged in user)
     */
    private void addRemoveContact(String userID) {
        output.addRemoveContact();
        String option = input.getKeyboardInput();
        while (!(option.equals("1")||option.equals("2"))){
            output.addRemoveContact();
            option = input.getKeyboardInput();
        }
        if (option.equals("1")){
            output.enterContactUserid(false);
            String input = this.input.getKeyboardInput();
            while (!(organizerManager.userExist(input)||attendeeManager.userExist(input)||speakerManager.userExist(input))){
                output.enterContactUserid(true);
                input = this.input.getKeyboardInput();
            }
            organizerManager.addContact(userID, input);
            if (organizerManager.userExist(input)){
                organizerManager.addContact(input, userID);
            }
            else if (attendeeManager.userExist(input)){
                attendeeManager.addContact(input, userID);
            }
            else{
                speakerManager.addContact(input, userID);
            }
            chatManager.createChat(input, userID);
        }
        else {
            output.enterContactUserid(false);
            String input = this.input.getKeyboardInput();
            while (!(organizerManager.userExist(input)||attendeeManager.userExist(input)||speakerManager.userExist(input))){
                output.enterContactUserid(true);
                input = this.input.getKeyboardInput();
            }
            organizerManager.removeContact(userID, input);
            if (organizerManager.userExist(input)){
                organizerManager.removeContact(input, userID);
            }
            else if (attendeeManager.userExist(input)){
                attendeeManager.removeContact(input, userID);
            }
            else{
                speakerManager.removeContact(input, userID);
            }
            chatManager.deleteChat(input,userID);
        }
    }
}

