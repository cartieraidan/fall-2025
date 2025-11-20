import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Address book model for carrying a interactive list of BuddyInfo
 *
 * @author Aidan Cartier
 * @version November 20, 2025
 */
public class AddressBook extends DefaultListModel implements Serializable {

    //used for displaying and operations
    private JList<BuddyInfo> buddyList;

    //for maintaining UI list
    private DefaultListModel<BuddyInfo> buddyListModel;

    //name of address book
    private String name;

    /**
     * Constructor for class that initials a list and JList for GUI.
     *
     * @param name is referencing name of address book.
     */
    public AddressBook(String name) {
        this.name = name;
        buddyListModel = new DefaultListModel<>(); //backend list
        buddyList = new JList<>(buddyListModel); //interactive list
    }

    /**
     * Adds a BuddyInfo to address book list.
     *
     * @param name Name of new BuddyInfo.
     * @param address Address of new BuddyInfo.
     * @param phoneNumber Phone number of new BuddyInfo.
     */
    public void addBuddyInfo(String name, String address, String phoneNumber) {
        BuddyInfo buddyInfo = new BuddyInfo(name, address, phoneNumber); //create new instance
        buddyListModel.addElement(buddyInfo); //add to DefaultListModel
    }

    /**
     * Method overloading to accept already initialized BuddyInfo. Used for Testing.
     *
     * @param buddyInfo Fully initialized BuddyInfo object.
     */
    public void addBuddyInfo(BuddyInfo buddyInfo) {
        buddyListModel.addElement(buddyInfo); //add to DefaultListModel
    }

    /**
     * Removes object from list maintained by address book.
     *
     * @param buddyInfo BuddyInfo object that wants to be removed.
     */
    public void removeBuddyInfo(BuddyInfo buddyInfo) {
        buddyListModel.removeElement(buddyInfo); //removes from DefaultListModel
    }

    /**
     * Method that returns the JList for the view.
     *
     * @return JList of BuddyInfo.
     */
    public JList<BuddyInfo> getBuddyList() {
        return buddyList;
    }

    /**
     * Method that gets the size of JList of how many elements that are currently in it.
     *
     * @return int of the size of JList.
     */
    public int getSize() {
        return buddyList.getModel().getSize();
    }

    /**
     * Method to get name of address book.
     *
     * @return String of name of address book.
     */
    public String getName() {
        return name;
    }

    /**
     * Saves output of address book to an already existing .txt file.
     * May attempt to create a new file.
     *
     * @param filename name of .txt file so file.txt.
     */
    public void save(String filename) {
        try {
            FileWriter file = new FileWriter(filename); //opens file

            String text = ""; //what we want to write
            for (int i = 0; i < buddyListModel.getSize(); i++) { //loop through all elements in list
                BuddyInfo temp = (BuddyInfo) buddyListModel.get(i); //get element
                text += temp.toString() + "\n"; //using toString() to get full description
            }

            file.write(text); //write to file
            file.close();
            System.out.println("Output saved to file");

        } catch (IOException e) { //if could not open or create file
            System.out.println("error getting file");
            e.printStackTrace();
        }
    }

    /**
     * Method called from the view to export current address book.
     * Create a file then saves all list of BuddyInfo calling "save" method.
     *
     * @param filename name of file you're creating, does not include extension name .txt
     */
    public void export(String filename) {
        String fileName = filename.trim() + ".txt"; //get rid of any whitespace and add .txt
        try {
            File file = new File(fileName); //create file instance

            if (file.exists()) { //file already created
                System.out.println("overwriting existing file");

            }else if (file.createNewFile()) { //try to create file
                System.out.println("File created");
            }

            this.save(fileName); //calling save method that writes in file

        } catch (IOException e) {
            System.out.println("error creating file");
            e.printStackTrace();
        }
    }

    /**
     * 
     *
     * @param filename
     * @param bookName
     * @return
     */
    public static AddressBook loadAddress(String filename, String bookName) {
        ArrayList<BuddyInfo> buddyInfos = new ArrayList<>();
        AddressBook addressBook = new AddressBook(bookName);

        buddyInfos = BuddyInfo.importBuddyInfo(filename);

        for (BuddyInfo buddyInfo : buddyInfos) {
            addressBook.addBuddyInfo(buddyInfo);
        }

        return addressBook;
    }

    public DefaultListModel<BuddyInfo> getBuddyListModel() {
        return buddyListModel;
    }

    public static void serializeToFile(Object list, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
        }
    }

    //annotation to ignore casting safety
    @SuppressWarnings("unchecked")
    public static AddressBook loadAddressSerial(String filename, String name) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            DefaultListModel<BuddyInfo> list = (DefaultListModel<BuddyInfo>) ois.readObject();
            AddressBook book = new AddressBook(name);

            for (int i = 0; i < list.getSize(); i++) {
                book.addBuddyInfo(list.getElementAt(i));
            }

            return book;

        }
    }
}
