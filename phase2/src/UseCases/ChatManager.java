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
 * @version 2.0
 * @since December 1st, 2020
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
     * Returns the chat between 2 users in the String form
     * @param id1 the id of user1
     * @param id2 the id of user2
     * @return String the chat between 2 users
     */
    public String chatToString(String id1, String id2){
        for (Chat chat : chats) {
            if ((chat.getId1().equals(id1)) && (chat.getId2().equals(id2))) {
                return chat.toString();
            }
            else if ((chat.getId1().equals(id2)) && (chat.getId2().equals(id1))){
                return chat.toString();
            }
        }
        return null;
    }

    /**
     *
     * @return a list of chat titles. A chat title is the people who the chat is between.
     * Ex. if the chat is between bob123 and dora123, then their chat title is: "1. Chat between bob123 and dora123"
     */
    public ArrayList<String> chatTitle(){
        ArrayList<String> chatString = new ArrayList<>();
        for (int i = 0; i < chats.size(); i++){
            String s = i+1 + ". Chat between " + chats.get(i).getId1() + " and " + chats.get(i).getId2();
            chatString.add(s);
        }
        return chatString;
    }
    /**
     * Returns the chat object of a given index
     * @param i the index of the chat
     * @return The chat object of the given index
     */
    public Chat getChatAtIndex(int i) {
        return chats.get(i);
    }
}