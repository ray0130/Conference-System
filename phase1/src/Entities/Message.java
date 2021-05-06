package Entities;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Message entity records basic information of a single message, including sender, recipient, text and date.
 *  @author Group_0112
 *  @version 1.0
 *  @since November 19th, 2020
 */

public class Message implements Serializable {
    //sender of the message
    private String sender;
    //receipt of the message
    private String recipient;
    //the actual text of the message
    private String context;
    //the date that the message was sent
    private Date time;
    //date formatter for formatting the date
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /** Constructor
     * @param sender sender's user id of this message
     * @param recipient user id of the account receiving this message
     * @param context context of the message
     */
    public Message(String sender, String recipient, String context){
        this.sender = sender;
        this.recipient = recipient;
        this.context = context;
        this.time = new Date();
    }

    /** Returns a context of the message as a String
     * @return String of the message
     */

    public String getContext() {
        return context;
    }

    /** Returns time of the message as a Date object
     * @return Date The time of message (Date object)
     */
    public Date getTime() {
        return time;
    }

    /** Converts the message to a String, including information of sender, send time and context
     * @return String Message as a String
     */
    @Override
    public String toString() {
        return "[" + formatter.format(getTime()) + "] " + sender + ": " + context;
    }

}