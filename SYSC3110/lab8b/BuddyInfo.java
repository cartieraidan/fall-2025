import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Basic class that holds information for an element in address book.
 * Has a static method to retrieve an ArrayList of BuddyInfo from a .txt file.
 *
 * @author Aidan Cartier
 * @version November 21, 2025
 */
public class BuddyInfo implements Serializable {
    private static final long serialVersionUID = 1L; //ID
    private String name;
    private String address;
    private String phoneNumber;

    /**
     * Constructor for initializing all required info.
     * All String var.
     *
     * @param name Name of buddy.
     * @param address Address of buddy.
     * @param phoneNumber Phone number of buddy.
     */
    public BuddyInfo(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Constructor chaining just in case there was a case requiring nothing initialized.
     * Calls over constructor.
     */
    public BuddyInfo() {
        this("N/A", "N/A", "N/A");
    }

    /**
     * Used for test cases, determines if same objects are the same by comparing name and
     * address of each object instead of location in memory.
     *
     * @param buddy Instance of BuddyInfo.
     * @return Boolean true if they're the same buddy,
     */
    public boolean equals(BuddyInfo buddy) {
        return (this.getName().equals(buddy.getName())) && ((this.getAddress()).equals(buddy.getAddress()));
    }

    /**
     * Returns name of BuddyInfo.
     *
     * @return String name of BuddyInfo.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns address of BuddyInfo.
     *
     * @return String address of BuddyInfo.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns phone number of BuddyInfo.
     *
     * @return Sting phone number of BuddyInfo.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Method that returns name, address and phone number of BuddyInfo
     * in one string seperated by #.
     *
     * @return String output of all BuddyInfo.
     */
    @Override
    public String toString() {
        return getName() + "#" + getAddress() + "#" + getPhoneNumber();
    }

    /**
     * Static method that creates a list of BuddyInfo from a .txt file and returns
     * an ArrayList.
     *
     * @param filename Full file name with extension .txt.
     * @return ArrayList of BuddyInfo
     */
    public static ArrayList<BuddyInfo> importBuddyInfo(String filename) {
        ArrayList<BuddyInfo> buddyInfos = new ArrayList<>();

        File file = new File(filename); //file object from filename

        try (Scanner myReader = new Scanner(file)) { //try reading file
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine(); //need to consume a line
                String[] dataArray = data.split("#"); //split by special character
                BuddyInfo buddy = new BuddyInfo(dataArray[0], dataArray[1], dataArray[2]);
                buddyInfos.add(buddy);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }

        return buddyInfos;
    }

    public String toXML() {
        return "<buddyInfo>\n\t\t\t\t" +
                "<name>" + this.getName() +"</name>\n\t\t\t\t" +
                "<address>" + this.getAddress() + "</address>\n\t\t\t\t" +
                "<phoneNumber>" + this.getPhoneNumber() + "</phoneNumber>\n\t\t\t</buddyInfo>";
    }

    public static void main(String[] args) {
        BuddyInfo obj = new BuddyInfo("Homer", "Donut", "888-888-8888");
        System.out.println("Hello " + obj.getName());
    }
}
