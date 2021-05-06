import java.io.IOException;
import java.text.ParseException;

/**
 * The ConferenceDemo class contains just one method that helps this program to create
 * a new ConferenceSystem and run.
 * @author Group_0112
 * @version 1.0
 * @since November 19th, 2020
 */
public class ConferenceDemo {
    public static void main(String[] args) throws IOException, ParseException {
        ConferenceSystem conferenceSystem = new ConferenceSystem();
        conferenceSystem.run();
    }
}