package Controller;

import Gateway.KeyboardInput;
import Gateway.Serialization;
import Presenter.TextPresenter;
import UseCases.*;

import java.io.IOException;

/**
 * AttendeeSystem controller implements various actions that can be done for an attendee, including
 * see all Events, Sign up for Events /cancel an event, check Schedule for Signed Up Events,
 * add or remove Attendee in Contact, message other Users, logout the account, shutdown the system.
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */

public class AttendeeSystem {
    private TextPresenter output;
    private KeyboardInput input;
    private AttendeeManager attendeeManager;
    private OrganizerManager organizerManager;
    private SpeakerManager speakerManager;
    private ChatManager chatManager;
    private MessageSystem messageSystem;
    private EventSystem eventSystem;
    private RoomManager roomManager;
    private EventManager eventManager;
    private RequestSystem requestSystem;

    /**
     * Constructor
     * @param speakerManager   The speaker manager implements by SpeakerManager use case
     * @param organizerManager The organizer manager implements by OrganizerManager use case
     * @param chatManager      The chat manager implements by ChatManager use case
     * @param attendeeManager  The attendee manager implements by AttendeeManager use case
     * @param messageSystem    The message system implements by MessageSystem Controller
     * @param eventSystem      The event system implements by EventSystem Controller
     * @param roomManager      The room manager implements by RoomManager use case
     * @param eventManager     The event manager implements by EventManager use case
     * @param requestSystem    The system to use to send requests or see your requests
     */
    public AttendeeSystem(SpeakerManager speakerManager, OrganizerManager organizerManager, ChatManager chatManager,
                          AttendeeManager attendeeManager, MessageSystem messageSystem, EventSystem eventSystem, RoomManager roomManager, EventManager eventManager, RequestSystem requestSystem) {
        this.speakerManager = speakerManager;
        this.organizerManager = organizerManager;
        this.attendeeManager = attendeeManager;
        this.chatManager = chatManager;
        this.messageSystem = messageSystem;
        this.eventSystem = eventSystem;
        this.roomManager = roomManager;
        this.eventManager = eventManager;
        this.input = new KeyboardInput();
        this.output = new TextPresenter();
        this.requestSystem = requestSystem;
    }

    /**
     * Attendee is allowed to do the following options: 1.see all Events. 2.Sign up for Events /cancel an event
     * 3.check Schedule for an Signed Up Event. 4.add or Remove Attendee in Contact. 5.message other Users
     * 6.log out the account. 7.shutdown the system
     * @param userID The user_id of the attendee who is logged in
     * @return object Returns true if user wants to shutdown, false if it wants to logout
     * @throws IOException Throw IOException to avoid errors that might occur
     */
    public boolean start(String userID) throws IOException {
        while (true) {
            String i;
            output.AttendeeMenu();
            i = input.getKeyboardInput();
            //1. see Events
            if (i.equals("1")) {
                eventSystem.checkAllEvents();
            }
            //2. Sign up for Events / leave an event
            else if (i.equals("2")) {
                eventSystem.newJoinLeaveEvent(userID);
            }
            //3. Check Schedule for an Signed Up Event
            else if (i.equals("3")) {
                eventSystem.checkSignedUpEvent(userID);
            }
            //4. Add or Remove Attendee in Contact
            else if (i.equals("4")) {
                messageSystem.newAddRemoveContact(userID);
            }
            //5. Message Other Users
            else if (i.equals("5")) {
                messageSystem.newSendMessage(userID);
            }
            //6. requests
            else if (i.equals("6")) {
                requestSystem.attendeeOptions(userID);
            }
            //7. logout
            else if (i.equals("7")) {
                return false;
            }
            //8. shutdown
            else if (i.equals("8")) {
                return true;
            }
            else {
                output.invalidInput();
            }
            saveState();
        }
    }

    /**
     * Saves states of the entire system system.
     *
     * @throws IOException Throw IOException to avoid errors that might occur
     */
    private void saveState() throws IOException {
        Serialization io = new Serialization();
        io.saveState(speakerManager, "SpeakerManager");
        io.saveState(eventManager, "EventManager");
        io.saveState(roomManager, "RoomManager");
        io.saveState(organizerManager, "OrganizerManager");
        io.saveState(chatManager, "ChatManager");
        io.saveState(attendeeManager, "AttendeeManager");

    }
}