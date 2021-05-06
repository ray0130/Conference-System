package Gateway;
import java.util.Scanner;

/**
 * KeyboardInput receives the input that users' end.
 *  @author Group_0112
 *  @version 2.0
 *  @since December 1st, 2020
 */

public class KeyboardInput {

    /**
     * Returns the input that users enter to the program.
     * @return String The alphabets from users
     */
    public String getKeyboardInput(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}