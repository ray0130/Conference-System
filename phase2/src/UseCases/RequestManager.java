package UseCases;

import Entities.Request;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The RequestManager class stores all Requests in an arraylist, and implements various actions that
 * can be done for users, including add request, remove request, and show the list of all requests.
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */

public class RequestManager implements Serializable {

    private ArrayList<Request> listOfRequests;

    /**
     * Constructor
     */
    public RequestManager(){
        listOfRequests = new ArrayList<>();
    }

    /**
     * Adds a request to list of requests
     * @param id the id of the request
     * @param user_id the userid of the attendee who made the request
     * @param req the req string
     */
    public void addRequest(int id, String user_id, String req){
        Request request = new Request( id,  user_id,  req);
        listOfRequests.add(request);
    }

    /**
     * Removes a request from list of requests
     * @param id the id of the request
     */
    public void removeRequest(int id){
        listOfRequests.removeIf(request -> request.getId() == id);
    }

    /**
     *Changes the status of the request
     * @param id the id of the request
     * @param status the status of the request, will be 2 options: "Addressed" or "Pending"
     */
    public void changeStatus(int id, String status){
        for (Request request: listOfRequests){
            if (request.getId() == id){
                request.setStatus(status);
            }
        }
    }

    /**
     *Gets a list of all requests
     * @return ArrayList<Request> The list of all requests
     */
    public ArrayList<Request> getListOfRequests() {
        return listOfRequests;
    }

    /**
     * Gets a list of requests of an attendee
     * @param userid the user_id of the attendee
     * @return ArrayList<Request> The list of requests from the attendee
     */
    public ArrayList<Request> getListOfRequestsofAttendee(String userid) {
        ArrayList<Request> requestArrayList = new ArrayList<>();
        for (Request request: listOfRequests){
            if (request.getUser_id().equals(userid)){
                requestArrayList.add(request);
            }
        }
        return requestArrayList;
    }

    /**
     * Check if the id is valid
     * @param id the id we want to check
     * @return boolean Return true if the id is available, false otherwise.
     */
    public boolean idIsAvailable(int id){
        for (Request request: listOfRequests){
            if (request.getId() == id){
                return false;
            }
        }
        return true;
    }
}
