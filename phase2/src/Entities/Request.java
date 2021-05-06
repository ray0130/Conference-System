package Entities;

import java.io.Serializable;


/**
 * Request Entities simply records the id of request, the attendee who made the request, the request context and the status.
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */

public class Request implements Serializable {


    private int id;
    private String user_id;
    private String req;
    private String status;  //status of the req

    /**
     * Constructor
     * @param id the id of the request
     * @param user_id the userid of the attendee who made the request
     * @param req the request string
     */
    public Request( int id, String user_id, String req){
        this.id = id;
        this.user_id = user_id;
        this.req = req;
        this.status = "Pending";
    }

    /**
     * Gets the id of the request
     * @return int The id of the request
     */
    public int getId() {
        return id;
    }

    /**
     *Sets the id of the request
     * @param id the id of the request we want to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the user_id of the attendee who made the request
     * @return String user_id
     */
    public String getUser_id() {
        return user_id;
    }

    /**
     * Sets the user_id of the attendee who made the request
     * @param user_id the user_id of the attendee who made the request
     */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    /**
     * Gets the request context
     * @return String the request context
     */
    public String getReq() {
        return req;
    }

    /**
     * Sets the request context
     * @param req the request context
     */
    public void setReq(String req) {
        this.req = req;
    }

    /**
     * Gets the status of the request
     * @return String the status of the request
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the request
     * @param status the status of the request
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the request in String form, following by request id, attendee who made the request, the request context and status
     * @return String the request in String form
     */
    @Override
    public String toString() {
        return "Request ID: " + id + " | Attendee that made request: " + user_id + " | Request: " + req + " | Status: " + status;
    }
}
