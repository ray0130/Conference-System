package Presenter;

/**
 * The Export Presenter Class implements methods to export string as a text file.
 * @author Group_0112
 * @version 1.0
 * @since December 1st, 2020
 */
public class ExportPresenter {

    public void exportOption(){
        System.out.println("Do you want to export? (y/n)");
    }


    /**
     * Prints: Invalid input, please try again!
     */
    public void invalidInput() {
        System.out.println("Invalid input, please try again!");
    }

    /**
     * Prints: Exported File!
     */
    public void exportedFile(){
        System.out.println("Exported File!");
    }

    /**
     * Prints: Not exporting File!
     */
    public void notexportingFile(){
        System.out.println("Not Exporting File!");
    }

}
