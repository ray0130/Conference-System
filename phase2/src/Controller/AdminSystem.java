package Controller;

import Entities.Event;
import Entities.Message;
import Gateway.KeyboardInput;
import Gateway.Serialization;
import Presenter.TextPresenter;
import UseCases.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * AdminSystem controller implements 2 major functions that can be done by an admin,
 * which are delete empty events, and delete selected chat.
 *
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */

public class AdminSystem {

    private TextPresenter output;
    private KeyboardInput input;
    private ChatManager chatManager;
    private EventManager eventManager;
    private SpeakerManager speakerManager;
    private OrganizerManager organizerManager;
    private RoomManager roomManager;

    /**
     * Constructor
     *
     * @param chatManager  The chat manager implements by ChatManager use case
     * @param eventManager The event manager implements by EventManager use case
     * @param organizerManager The organizer manager implemented by OrganizerManager
     * @param roomManager The room manager
     * @param speakerManager The speaker manager
     */
    public AdminSystem(ChatManager chatManager, EventManager eventManager, SpeakerManager speakerManager, OrganizerManager organizerManager, RoomManager roomManager) {
        this.chatManager = chatManager;
        this.eventManager = eventManager;
        this.organizerManager = organizerManager;
        this.speakerManager = speakerManager;
        this.roomManager = roomManager;
        this.input = new KeyboardInput();
        this.output = new TextPresenter();
    }

    /**
     * Returns an arraylist of event ids that are empty (without any attendee).
     *
     * @return ArrayList </Integer> list that contains all ids of empty events
     */
    private ArrayList<Integer> GetNoAttendeeEvent() {
        ArrayList<Integer> NoAttendee = new ArrayList<>();
        for (Event event : eventManager.getListOfEvents()) {
            if (event.getAttendees().size() == 0) {
                NoAttendee.add(event.getID());
            }
        }
        return NoAttendee;
    }

    /**
     * Removes the events that do not have any attendee.
     */
    public void RemoveEmptyEvent() {
        boolean goback = false;
        while (!goback){
            ArrayList<Integer> noAttendees = GetNoAttendeeEvent();
            ArrayList<Event> noAttendeeEvents = new ArrayList<>();
            for (Integer id : noAttendees) {
                noAttendeeEvents.add(eventManager.getEvent(id));
            }
            if (noAttendeeEvents.isEmpty()){
                output.allEventsHaveAttendees();
                return;
            }
            output.chooseNoAttendeeEvent(noAttendeeEvents);
            int inInt;
            try {
                inInt = Integer.parseInt(input.getKeyboardInput());
            }
            catch (Exception e){
                inInt = -1;
            }
            if (inInt == 0){ // if user chooses to go back, they pressed 0
                goback = true;
            }
            else if (inInt<= noAttendeeEvents.size() && inInt>=1){

                int eventID = noAttendeeEvents.get(inInt-1).getID();
                String eventLocation = eventManager.getLocation(eventID);
                Date eventTime = eventManager.getTime(eventID);
                Event eventItself = eventManager.getEvent(eventID);
                roomManager.removeEventFromRoom(eventLocation, eventItself, eventTime);      //the room no longer holds this event at that time.

                organizerManager.setDeleteEventCreated(noAttendeeEvents.get(inInt-1).getOrganizerID(), eventID);                //removes event from the list of events this organizer has created
                if (eventManager.hasSpeaker(eventID)) {                                 //if this event has a speaker, delete this event from that speakers list of assigned events.
                    ArrayList<String> speakers = eventManager.getSpeakerID(eventID);
                    for (String speaker:speakers) {
                        speakerManager.removeEvent(eventID, speaker);
                    }
                }
                eventManager.cancelEvent(eventID);                                     //removes from list of events
                output.thatEventHasBeenDeleted();
            }
            else {
                output.invalidInput();
            }
        }
    }

    /**
     * Removes the chat between 2 users
     */
    public void RemoveMessage() {

        boolean goback = false;
        while (!goback) {
            ArrayList<String> chatTitles = chatManager.chatTitle();
            if (chatTitles.isEmpty()) {
                output.thereAreNoChats();
            }
            output.chattitles(chatTitles);
            int inInt;
            try {
                inInt = Integer.parseInt(input.getKeyboardInput());
            } catch (Exception e) {
                inInt = -1;
            }
            if (inInt == 0) { // if user chooses to go back, they pressed 0
                goback = true;
            } else if (inInt <= chatTitles.size() && inInt >= 1) {
                boolean goback1 = false;
                while (!goback1){
                    ArrayList<Message> messages = chatManager.getChatAtIndex(inInt - 1).getMessages();
                    if (messages.isEmpty()) {
                        output.thereAreNoMessagesBetweenThese2();
                        return;
                    }
                    output.chooseMessage(messages);
                    int inInt1;
                    try {
                        inInt1 = Integer.parseInt(input.getKeyboardInput());
                    } catch (Exception e) {
                        inInt1 = -1;
                    }
                    if (inInt1 == 0) { // if user chooses to go back, they pressed 0
                        goback1 = true;
                    } else if (inInt1 <= messages.size() && inInt1 >= 1) {
                        messages.remove(inInt1 - 1);
                        output.ActionDone();
                    } else {
                        output.invalidInput();
                    }
                }
            }
            else {
                output.invalidInput();
            }
        }
    }

    /**
     * The start method of AdminSystem which can: 1.Delete Empty Event. 2.Delete Chat. 3.logout. 4.SHUTDOWN
     *
     * @return object Returns true or false based on the action taken
     * @throws IOException any errors that may occur while running
     */
    public boolean start() throws IOException {
        while (true) {
            String i;
            output.AdminMenu();
            i = input.getKeyboardInput();
            //1. Delete Empty Event
            switch (i) {
                case "1":
                    RemoveEmptyEvent();
                    break;
                //2. Delete Chat
                case "2":
                    RemoveMessage();
                    break;
                //3. logout
                case "3":
                    return false;
                //4. shutdown
                case "4":
                    return true;
                default:
                    output.invalidInput();
            }
            saveState();
        }
    }

    /**
     * Saves state for the system.
     *
     * @throws IOException any errors that may occur while running
     */
    private void saveState() throws IOException {
        Serialization io = new Serialization();
        io.saveState(eventManager, "EventManager");
        io.saveState(chatManager, "ChatManager");
    }

}
