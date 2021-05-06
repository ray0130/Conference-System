import Presenter.TextPresenter;
import UseCases.*;

import java.util.ArrayList;

/**
 * The Debugger class contains a method that can print state of the system,
 * which is designed to help our TA to understand our program.
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */
public class Debugger {
    public Debugger(){}

    public void printStateofSystem(OrganizerManager organizerManager, SpeakerManager speakerManager, AttendeeManager attendeeManager, EventManager eventManager, RoomManager roomManager, RequestManager requestManager, AdminManager adminManager, ChatManager chatManager){
        System.out.println("\n\n\nNOTE: this is a debug feature to assist our TA. Here is the current state of the system:");
        System.out.println("\n\nAll the organizers in the system:");
        ArrayList<String> organizersList = organizerManager.getUserIDs();
        for (String organizer: organizersList){
            System.out.println(organizerManager.getOrganizer(organizer).toString());
        }
        System.out.println("\n\nAll the speakers in the system:");
        ArrayList<String> speakers = speakerManager.getUserIDs();
        for (String speaker: speakers){
            System.out.println(speakerManager.getSpeaker(speaker).toString());
        }
        System.out.println("\n\nAll the attendees in the system:");
        ArrayList<String> attendees = attendeeManager.getUserIDs();
        for (String attendee: attendees){
            System.out.println(attendeeManager.getAttendee(attendee).toString());
        }
        System.out.println("\n\nAll the admins in the system:");
        ArrayList<String> admins = adminManager.getAllAdmins();
        for (String admin: admins){
            System.out.println(adminManager.getAdmin(admin).toString());
        }
        System.out.println("\n\nAll the events in the system:");
        ArrayList<Integer> eventIDS = eventManager.getListOfEventIDs();
        for (Integer event: eventIDS){
            System.out.println(eventManager.getEvent(event).toString());
        }
        System.out.println("\n\nAll the rooms in the system");
        ArrayList<String> roomLOCS = roomManager.getAllRoomLocs();
        for (String room: roomLOCS){
            System.out.println(roomManager.getRoom(room).toString());
        }
        System.out.println("\n\nAll the requests in the system and their status:");
        TextPresenter output = new TextPresenter();
        output.requestList(requestManager.getListOfRequests());

        System.out.println("\n\nAll the chats in the system:");
        ArrayList<String> chattitles = chatManager.chatTitle();
        for (String chattitle: chattitles){
            System.out.println(chattitle);
        }
        System.out.println("\n\n\n");
    }
}
