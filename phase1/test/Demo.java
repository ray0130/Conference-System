import Entities.Attendee;
import Entities.User;
import Entities.Organizer;
public class Demo {
    public static void main(String[] args) {
        Organizer org1 = new Organizer("1", "Nat", "123");
        System.out.println(org1.getContacts());
        Organizer org2 = new Organizer("2", "N", "123");
        org1.addContact("2");
        System.out.println(org1.getContacts());;
    }
}
