package Controller;

import Gateway.KeyboardInput;
import Gateway.Serialization;
import Presenter.ExportPresenter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import UseCases.*;
import Entities.*;

import java.util.ArrayList;

/**
 * The Export System Class implements methods to allow users to export conference information as a text file.
 * @author Group_0112
 * @version 1.0
 * @since December 1st, 2020
 */
public class ExportSystem {
    private KeyboardInput input;
    private SpeakerManager speakerManager;
    private OrganizerManager organizerManager;
    private EventManager eventManager;
    private AttendeeManager attendeeManager;
    private RoomManager roomManager;
    private ExportPresenter exportOutput;
    private Serialization saveFile;

    /**
     * Constructor
     * @param speakerManager The speaker manager implements by SpeakerManager use case
     * @param organizerManager The organizer manager implements by OrganizerManager use case
     * @param eventManager The event manager implements by EventManager use case
     * @param attendeeManager The attendee manager implements by AttendeeManager use case
     * @param roomManager The room manager implements by RoomManager use case
     */
    public ExportSystem(SpeakerManager speakerManager, OrganizerManager organizerManager, EventManager eventManager, AttendeeManager attendeeManager, RoomManager roomManager) {
        this.speakerManager = speakerManager;
        this.organizerManager = organizerManager;
        this.eventManager = eventManager;
        this.attendeeManager = attendeeManager;
        this.input = new KeyboardInput();
        this.roomManager = roomManager;
        this.exportOutput = new ExportPresenter();
        this.saveFile = new Serialization();
    }

    public boolean run() throws IOException {
        while (true){
            exportOutput.exportOption();
            String option = input.getKeyboardInput();
            if (option.equals("y")){
                exportEvent();
                exportOutput.exportedFile();
                return true;
            }
            else if (option.equals("n")){
                exportOutput.notexportingFile();
                return true;
            }
            else {
                exportOutput.invalidInput();
            }
        }
    }


    /**
     * Gets the info of the whole conference
     * @return StringBuilder with info of the whole conference
     */
    public StringBuilder getInfo(){
        StringBuilder info = new StringBuilder("Full Schedule:\n");
        ArrayList<String> roomLocList = roomManager.getAllRoomLocs();
        for (String roomLoc : roomLocList){
            info.append(getRoomInfo(roomLoc));

        }
        return info;
    }

    /**
     * Gets the info of a single room
     * @return String info of a single room
     */
    public String getRoomInfo(String roomLocation){
        ArrayList<Event> eventList = eventManager.getListOfEvents();

        StringBuilder roomInfo = new StringBuilder("Room "+roomLocation+":\n");
        for (Event event : eventList){
            if (event.getLocation().equals(roomLocation)){
                roomInfo.append(getEventInfo(event.getID())).append("\n");
            }
        }
        return roomInfo.toString();
    }

    /**
     * Gets the info of an event
     * @return String info of an event
     */
    public String getEventInfo(int eventId){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Event event = eventManager.getEvent(eventId);
        String strDate = dateFormat.format(event.getTime());

        StringBuilder eventInfo = new StringBuilder("\t" + strDate + "\n");

        eventInfo.append("\t\tEvent:").append(event.getTitle()).append("\n");
        // get speaker info string
        ArrayList<String> speakers = event.getSpeakerID();
        eventInfo.append("\t\t\tSpeaker: " + "\n");

        for (String speaker : speakers){
            eventInfo.append("\t\t\t\t").append(speakerManager.getSpeaker(speaker).getName()).append("\n");
        }
        // get attendee info string
        eventInfo.append("\t\t\tAttendees: " + "\n");
        ArrayList<String> attendees = event.getAttendees();
        for (String attendee : attendees){
            eventInfo.append("\t\t\t\t").append(attendeeManager.getAttendee(attendee).getName()).append("\n");
        }
        // get organizer info string
        String organizer = organizerManager.getOrganizer(event.getOrganizerID()).getName();
        eventInfo.append("\t\t\tOrganizer: " + "\n");
        eventInfo.append("\t\t\t\t").append(organizer).append("\n");

        return eventInfo.toString();
    }

    /**
     * Make the presenter export info of the conference
     */
    public void exportEvent() throws IOException {
        String info = getInfo().toString();
        saveFile.exportToFile(info);
    }
}
