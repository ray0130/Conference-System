package Controller;

import Gateway.KeyboardInput;
import Presenter.TextPresenter;
import UseCases.AttendeeManager;
import UseCases.OrganizerManager;
import UseCases.SpeakerManager;

/** LoginAndRegistrationSystem controller implements multiple options that are useful when the user
 * start the system. Such as verifying the user's login and registration for new users.
 *  @author Group_0112
 *  @version 1.0
 *  @since November 19th, 2020
 */

public class LogInAndRegistrationSystem {
    private TextPresenter output;
    private KeyboardInput input;
    private AttendeeManager attendeeManager;
    private OrganizerManager organizerManager;
    private SpeakerManager speakerManager;

    /**
     * Constructor
     * @param a The attendee manager implements by AttendeeManager use case
     * @param b The organizer manager implements by OrganizerManager use case
     * @param c The speaker manager implements by SpeakerManager use case
     */
    public LogInAndRegistrationSystem(AttendeeManager a, OrganizerManager b, SpeakerManager c) {
        this.output = new TextPresenter();
        this.input = new KeyboardInput();
        this.attendeeManager = a;
        this.organizerManager = b;
        this.speakerManager = c;
    }


    /**
     * You can do either of the following options: 1.register as a new user. 2.LogIn the user account
     * 3. shutdown the system
     * @return object Returns userID if they logged in or registered. If they choose to shutdown, then it returns "SHUTDOWN" -
     * a keyword for the ConferenceSystem to know to shutdown automatically.
     */
    public String start() {
        String in;
        String userID = "false";
        while (true) {
            output.loginOrRegister();
            in = input.getKeyboardInput();
            if (in.equals("1")) {
                userID = registerUser();
            } else if (in.equals("2")) {
                userID = loginUser();
            } else if (in.equals("3")) {
                return "SHUTDOWN";
            } else {
                output.invalidInput();
            }
            if (!userID.equals("false")){
                return userID;
            }
        }

    }

    /**
     * Register as a new user
     * @return String The user_id of this new user
     */
    public String registerUser() {
        boolean selectType = false;
        while (!selectType){
            output.enterType(true);
            String inputType = input.getKeyboardInput();
            if (!(inputType.equals("1") || inputType.equals("2")||inputType.equals("0"))){
                output.invalidInput();
            }
            else if (inputType.equals("0")){
                return "false";
            }
            else{
                boolean nameBack = false;
                while (!nameBack){
                    output.enterName();
                    String inputName = input.getKeyboardInput();
                    if (inputName.equals("0")){
                        nameBack = true;
                    }
                    else{
                        boolean untilCorrect = true;
                        boolean correct = true;
                        String inputID = "";
                        while (untilCorrect) {
                            output.enterID(correct);
                            inputID = input.getKeyboardInput();
                            if (attendeeManager.userExist(inputID) || organizerManager.userExist(inputID) || speakerManager.userExist(inputID)) {
                                correct = false;
                            }
                            else if(inputID.equals("0")) {
                                untilCorrect = false;
                            }
                            else{
                                boolean untilCorrect1 = true;
                                boolean correct1= true;
                                String inputPass = "";
                                while (untilCorrect1) {
                                    output.enterPassword(correct1);
                                    inputPass = input.getKeyboardInput();
                                    if(inputPass.equals("0")) {
                                        untilCorrect1 = false;
                                    }
                                    else if (inputPass.length() > 14 || inputPass.length() < 8) {
                                        correct1 = false;
                                    }
                                    else{
                                        if (inputType.equals("1")){
                                            organizerManager.addOrganizer(inputID, inputName, inputPass);
                                        }
                                        else {
                                            attendeeManager.addAttendee(inputID, inputName, inputPass);
                                        }
                                        return inputID;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return "false";
    }

    /**
     * Log in the user account
     * @return object Returns the String user_id if verified (LogIn successfully), false otherwise
     */
    public String loginUser() {
        output.loginEnterID(true);
        String inputID = "";
        inputID = input.getKeyboardInput();
        output.loginPassword(true);
        String inputPassword = "";
        inputPassword = input.getKeyboardInput();
        if (attendeeManager.verifyLogIn(inputID, inputPassword)) {
            return inputID;
        } else if (organizerManager.verifyLogIn(inputID, inputPassword)) {
            return inputID;
        } else if (speakerManager.verifyLogIn(inputID, inputPassword)) {
            return inputID;
        } else {
            output.loginEnterID(false);
            return "false";
        }
    }
}

