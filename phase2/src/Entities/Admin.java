package Entities;

import java.io.*;

/**
 *  Admin class implements an application that simply records an admin.
 *  * @author Group_0112
 *  * @version 2.0
 *  * @since December 1st, 2020
 */
public class Admin extends User implements Serializable{

    /**
     * Constructor
     * @param user_id the user id of this admin. User id is an unique integer for each user.
     * @param name    the registered name of this admin.
     * @param passwords the registered passwords of this admin.
     */
    public Admin(String user_id, String name, String passwords){
        super(user_id, name, passwords);
    }

}

