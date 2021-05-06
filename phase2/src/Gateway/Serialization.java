package Gateway;

import UseCases.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Serialization {

    final String directory = "phase2";
    public Serialization(){}


    /**
     * Saves states of all managers.
     * @throws IOException throw IOException to avoid errors that might occur
     */
    public void saveState(Object obj, String type) throws IOException {
        String fileName = directory + File.separator + "src" + File.separator + type + ".ser";
        OutputStream file = new FileOutputStream(fileName);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        if (type.equals("AttendeeManager")){
            AttendeeManager attendeeManager = (AttendeeManager) obj;
            output.writeObject(attendeeManager);
            output.close();
        }
        else if (type.equals("ChatManager")){
            ChatManager chatManager = (ChatManager) obj;
            output.writeObject(chatManager);
            output.close();
        }
        else if (type.equals("AdminManager")){
            AdminManager adminManager = (AdminManager) obj;
            output.writeObject(adminManager);
            output.close();
        }
        else if (type.equals("EventManager")){
            EventManager eventManager = (EventManager) obj;
            output.writeObject(eventManager);
            output.close();
        }
        else if (type.equals("OrganizerManager")){
            OrganizerManager organizerManager = (OrganizerManager) obj;
            output.writeObject(organizerManager);
            output.close();
        }
        else if (type.equals("RoomManager")){
            RoomManager roomManager = (RoomManager) obj;
            output.writeObject(roomManager);
            output.close();
        }
        else if (type.equals("SpeakerManager")){
            SpeakerManager speakerManager = (SpeakerManager) obj;
            output.writeObject(speakerManager);
            output.close();
        }
        else if (type.equals("RequestManger")){
            RequestManager requestManager = (RequestManager) obj;
            output.writeObject(requestManager);
            output.close();
        }
    }

    /**
     * Imports ser files.
     * @return Object returns an implement of this use case (all the managers)
     */
    public Object importState(String type) {
        String fileName = directory + File.separator + "src" + File.separator + type + ".ser";
        try {
            InputStream file = new FileInputStream(fileName);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            if (type.equals("AttendeeManager")){
                AttendeeManager attendeeManager = (AttendeeManager) input.readObject();
                input.close();
                return attendeeManager;
            }
            else if (type.equals("ChatManager")){
                ChatManager chatManager = (ChatManager) input.readObject();
                input.close();
                return chatManager;
            }
            else if (type.equals("AdminManager")){
                AdminManager adminManager = (AdminManager) input.readObject();
                input.close();
                return adminManager;
            }
            else if (type.equals("EventManager")){
                EventManager eventManager = (EventManager) input.readObject();
                input.close();
                return eventManager;
            }
            else if (type.equals("OrganizerManager")){
                OrganizerManager organizerManager = (OrganizerManager) input.readObject();
                input.close();
                return organizerManager;
            }
            else if (type.equals("RoomManager")){
                RoomManager roomManager = (RoomManager) input.readObject();
                input.close();
                return roomManager;
            }
            else if (type.equals("RequestManager")){
                RequestManager requestManager = (RequestManager) input.readObject();
                input.close();
                return requestManager;
            }
            else {
                SpeakerManager speakerManager = (SpeakerManager) input.readObject();
                input.close();
                return speakerManager;
            }
        }

        catch (ClassNotFoundException | IOException e) {
            if (type.equals("AttendeeManager")){
                return new AttendeeManager();
            }
            else if (type.equals("ChatManager")){
                return new ChatManager();
            }
            else if (type.equals("AdminManager")){
                return new AdminManager();
            }
            else if (type.equals("EventManager")){
                return new EventManager();
            }
            else if (type.equals("OrganizerManager")){
                return new OrganizerManager();
            }
            else if (type.equals("RoomManager")){
                return new RoomManager();
            }
            else if (type.equals("RequestManager")){
                return new RequestManager();
            }
            else {
                return new SpeakerManager();
            }
        }
    }

    /**
     * The Export string as a text file.
     * @param info info of the conference
     */
    public void exportToFile(String info) throws IOException {
        try {
            OutputStream output = new FileOutputStream(directory + File.separator + "eventinfo.txt");
            output.write(info.getBytes(StandardCharsets.UTF_8));
            output.close();
        }catch(Exception IOException){
            System.out.println("Error export file.");
            throw new IOException();
        }
    }
}
