package Entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Admin extends User implements Serializable{

    /**
     * Constructor
     * @param user_id the user id of this user. User id is an unique integer for each user.
     * @param name    the registered name of this user.
     * @param passwords the registered passwords of this user.
     */
    public Admin(String user_id, String name, String passwords){
        super(user_id, name, passwords);
    }
}
