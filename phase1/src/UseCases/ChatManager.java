package UseCases;
import Entities.Chat;

import java.io.*;
import java.util.ArrayList;

/**
 * The ChatManager class stores all chats in an arraylist,
 * and implements various actions that are relevant to the chats.
 * Actions include adding message, creating chats, verifying chats,
 * finding chats, finding contacts, and deleting chats.
 * @author Group_0112
 * @version 1.0
 * @since November 19th, 2020
 */

public class ChatManager implements Serializable{

    //list of chats. this will be a 2 dimensional list.
    private ArrayList<Chat> chats;

    /**
     * Constructor that creates a new arraylist of chats.
     */
    public ChatManager(){
        chats = new ArrayList<>();
    }

    /**
     * Adds message to chat.
     * @param sender the name of user who sends this message
     * @param recipient the name of user who receives this message
     * @param context the text of the message
     */
    public void addMessageToChat(String sender, String recipient, String context){
        if (chatExists(sender, recipient)) {
            Chat chat = findChat(sender, recipient);
            chat.addMessages(sender, recipient, context);
        }
    }

    /**
     * Creates a new chat between 2 users.
     * @param id1 the id of user1
     * @param id2 the id of user2
     */
    public void createChat(String id1, String id2){
        if (!chatExists(id1, id2)) {
            chats.add(new Chat(id1, id2));
        }
    }

    /**
     * Checks if the chat already exists in the system.
     * @param id1 the id of user1
     * @param id2 the id of user2
     * @return boolean returns true if the chat exists,
     * returns false otherwise
     */
    public boolean chatExists(String id1, String id2){
        for (Chat chat : chats) {
            if ((chat.getId1().equals(id1)) && (chat.getId2().equals(id2))) {
                return true;
            }
            else if ((chat.getId1().equals(id2)) && (chat.getId2().equals(id1))){
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the chat between 2 users (given their user ids).
     * @param id1 the id of user1
     * @param id2 the id of user2
     * @return Chat the Chat object between the two users
     */
    public Chat findChat(String id1, String id2){

        for (Chat chat : chats) {
            if ((chat.getId1().equals(id1)) && (chat.getId2().equals(id2))) {
                return chat;
            }
            else if ((chat.getId1().equals(id2)) && (chat.getId2().equals(id1))){
                return chat;
            }
        }
        return null;
    }


    /**
     * Finds all the contacts this person has a chat with and stores it in an arraylist.
     * @param id1 the id of user who wants to perform this action
     * @return ArraryList </Sting> a list containing all usernames of its contact
     */
    public ArrayList<String> getContactsWithChat(String id1){
        ArrayList<String> listOfChatsPeople = new ArrayList<>();
        for (Chat chat : chats) {
            if (chat.getId1().equals(id1)) {
                listOfChatsPeople.add(chat.getId2());
            }
            else if (chat.getId2().equals(id1)){
                listOfChatsPeople.add(chat.getId1());
            }
        }
        return listOfChatsPeople;
    }


    /**
     * Deletes the chat between 2 users (given their user ids).
     * @param id1 the id of user1
     * @param id2 the id of user2
     */
    public void deleteChat(String id1, String id2){
        for (int i = 0; i<chats.size();i++){
            if (chats.get(i).getId1().equals(id1)&&chats.get(i).getId2().equals(id2)){
                chats.remove(i);
                return;
            }
            else if (chats.get(i).getId1().equals(id2)&&chats.get(i).getId2().equals(id1)){
                chats.remove(i);
                return;
            }
        }
    }

    /**
     * Saves states of chat manager.
     * @throws IOException throw IOException to avoid errors that might occur
     */
    public void saveState() throws IOException {
        OutputStream file = new FileOutputStream("phase1/src/ChatManager.ser");
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(this);
        output.close();
    }

    /**
     * Imports ser files.
     * @return ChatManager returns an implement of this use case
     */

    public ChatManager importState() {
        try {
            InputStream file = new FileInputStream("phase1/src/ChatManager.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            ChatManager chatManager = (ChatManager) input.readObject();
            input.close();
            return chatManager;

        } catch (ClassNotFoundException | IOException e) {
            return new ChatManager();
        }
    }

}