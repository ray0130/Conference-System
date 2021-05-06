package Controller;

import Entities.Request;
import Gateway.KeyboardInput;
import Gateway.Serialization;
import Presenter.TextPresenter;
import UseCases.AttendeeManager;
import UseCases.OrganizerManager;
import UseCases.RequestManager;

import java.io.IOException;
import java.util.ArrayList;

public class RequestSystem {


    private TextPresenter output;
    private KeyboardInput input;
    private RequestManager requestManager;
    private AttendeeManager attendeeManager;
    private OrganizerManager organizerManager;

    public RequestSystem(RequestManager requestManager, AttendeeManager attendeeManager, OrganizerManager organizerManager){
        this.requestManager = requestManager;
        this.attendeeManager = attendeeManager;
        this.organizerManager = organizerManager;
        this.input = new KeyboardInput();
        this.output = new TextPresenter();
    }

    /**
     * Saves all states of use cases.
     * @throws IOException throws IOException to avoid errors that might occur while running the program
     */
    private void saveState() throws IOException {
        Serialization io = new Serialization();
        io.saveState(organizerManager, "OrganizerManager");
        io.saveState(attendeeManager, "AttendeeManager");
        io.saveState(requestManager, "RequestManager");
    }

    public void organizerOptions() throws IOException {
        boolean goback = false;
        while (!goback){
            ArrayList<Request> requestArrayList = requestManager.getListOfRequests();
            if (requestArrayList.isEmpty()){
                noreqs();
                return;
            }
            output.chooseRequest();
            output.requestList(requestArrayList);
            output.enterZeroToContinue();
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
            else if ( inInt<= requestArrayList.size() && inInt>=1){
                boolean goback1 = false;
                while (!goback1){
                    output.changeStatus();
                    int status;
                    try {
                        status = Integer.parseInt(input.getKeyboardInput());
                    }
                    catch (Exception e){
                        status = -1;
                    }
                    if (status == 0){
                        goback1 = true;
                    }
                    else if (status == 1){
                        requestManager.changeStatus(requestArrayList.get(inInt-1).getId(), "Addressed");
                        goback1 = true;
                    }
                    else if (status == 2){
                        requestManager.changeStatus(requestArrayList.get(inInt-1).getId(), "Pending");
                        goback1 = true;
                    }
                    else {
                        output.invalidInput();
                    }
                }
            }
            else {
                output.invalidInput();
            }
            saveState();
        }
    }

    /**
     * The attendee options for request. (givew requests, and see status of requests
     * @param userid the userid of the attendee logged in
     * @throws IOException if there is an io error
     */
    public void attendeeOptions(String userid) throws IOException {
        boolean goback = false;
        while (!goback){
            output.attendeeRequestOptions();
            String in = input.getKeyboardInput();
            if (in.equals("0")){
                goback = true;
            }
            else if (in.equals("1")){ // attendee chooses to see list of requests
                ArrayList<Request> requestArrayList = requestManager.getListOfRequestsofAttendee(userid);
                if (requestArrayList.isEmpty()){
                    noreqs();
                }
                else {
                    output.requestList(requestArrayList);
                    output.pressAnyKeyToContinue();
                    String in1 = input.getKeyboardInput();
                }
            }
            else if (in.equals("2")){
                boolean goback1 = false;
                while (!goback1){
                    output.createUniqueIDforReq();
                    int reqID;
                    try {
                        reqID = Integer.parseInt(input.getKeyboardInput());
                    }
                    catch (Exception e){
                        reqID = -1;
                    }
                    if (reqID == 0){
                        goback1 = true;
                    }
                    else if (reqID>0 && requestManager.idIsAvailable(reqID)){
                        boolean goback2 = false;
                        while (!goback2){
                            output.writeYourRequest();
                            String req = input.getKeyboardInput();
                            if (!req.equals("0")) {
                                requestManager.addRequest(reqID, userid, req);
                            }
                            goback2 = true;
                            goback1 = true;
                        }
                    }
                    else{
                        output.invalidReqID();
                    }
                }
            }
            else {
                output.invalidInput();
            }
            saveState();
        }
    }

    private void noreqs() {
        output.thereAreNoRequests();
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            output.couldntSleep();
        }
    }
}