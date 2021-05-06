package UseCases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import Entities.Admin;

/**
 * The AdminManager class stores all admins in an arraylist, and implements various actions that
 * can be done for an admin, including verifying login/ if admin exists/ adding an admin
 * @author Group_0112
 * @version 2.0
 * @since December 1st, 2020
 */
public class AdminManager implements Serializable {

    private Hashtable<String, Admin> tableOfAdmin;
    /**
     * Constructor that initializes a hashtable to contains all admins.
     */
    public AdminManager(){tableOfAdmin = new Hashtable<>();}

    /**
     * Adds an admin to the hashtable of all admins.
     * @param user_id the id of user that we wish to add
     * @param username the name of the user that we wish to add
     * @param passwords the passwords of the user that we wish to add
     */
    public void addAdmin(String user_id, String username, String passwords){
        Admin newAdmin = new Admin(user_id, username, passwords);
        tableOfAdmin.put(newAdmin.getUser_id(), newAdmin);
    }

    /**
     * Verifies an admin's login based on the inputted credentials. The user will be logged in if the
     * inputted information is correct.
     * @param inputAdminId the user id entered by the admin
     * @param inputUserPassword the password entered by the admin
     * @return boolean returns true when the inputted credential is correct,
     * returns false otherwise
     */
    public boolean verifyLogIn(String inputAdminId, String inputUserPassword){
        if (adminExist(inputAdminId)) {
            return tableOfAdmin.get(inputAdminId).getPasswords().equals(inputUserPassword);
        }
        return false;
    }

    /**
     * Checks if a particular admin exists in the arraylist of admin.
     * @param userId the id of admin that we'd like to look into
     * @return boolean returns true if the admin already registered in the system,
     * returns false otherwise
     */
    public boolean adminExist(String userId){
        return tableOfAdmin.containsKey(userId);
    }

    /**
     * Returns a particular admin.
     * @param userId the id of admin that we'd like to return
     * @return Admin
     * @see Admin
     */
    public Admin getAdmin(String userId){
        return tableOfAdmin.get(userId);
    }

    public ArrayList<String> getAllAdmins() {
        return new ArrayList<String>(tableOfAdmin.keySet());
    }
}
