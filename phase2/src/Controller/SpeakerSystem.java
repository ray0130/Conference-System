package Controller;

import java.io.IOException;

import Gateway.KeyboardInput;
import Gateway.Serialization;
import Presenter.TextPresenter;
import UseCases.*;

/**
 * SpeakerSystem controller implements various actions that can be done for a speaker, including
 *  see all Events, check Schedule for assigned-Events, message other Users, logout the account, shutdown the system.
 *  @author Group_0112
 *  @version 2.0
 *  @since December 1st, 2020
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
                messageSystem.newSendMessage(userID);
            }
            //4. logout
            else if (i.equals("4")){
                return false;
            }
            //5. shutdown
            else if (i.equals("5")){
                return true;
            }
            else {
                output.invalidInput();
            }
            saveState();
        }
    }

    /**
     * Saves states of the system.
     * @throws IOException Throw IOException to avoid errors that might occur
     */
    private void saveState() throws IOException {
        Serialization io = new Serialization();
        io.saveState(speakerManager, "SpeakerManager");
        io.saveState(organizerManager, "OrganizerManager");
        io.saveState(chatManager, "ChatManager");
        io.saveState(attendeeManager, "AttendeeManager");
    }

    /**
     * Check if the user already registered in this system or not
     * @param userid The user_id of the user we want to check
     * @return boolean Returns true if the user already registered, false otherwise
     */
    private boolean userExists(String userid){
        return attendeeManager.userExist(userid) || organizerManager.userExist(userid) || speakerManager.userExist(userid);
    }
}

