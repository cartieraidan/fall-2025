import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Address book model for carrying an interactive list of BuddyInfo
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

    public void setName(String name) {
        this.name = name;
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

        } catch (IOException e) { //if you could not open or create file
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
     * Static method that loads an address book from a .txt. Calls static method from BuddyInfo
     * that returns a ArrayList of BuddyInfo which converts the toString output in instances.
     *
     * @param filename full file name of address book i.e. file.txt.
     * @param bookName name you want to give to address book.
     * @return instance of address book filed with BuddyInfo objects from file.
     */
    public static AddressBook loadAddress(String filename, String bookName) {
        AddressBook addressBook = new AddressBook(bookName); //returning this object

        ArrayList<BuddyInfo> buddyInfos = BuddyInfo.importBuddyInfo(filename); //all BuddyInfo from text file

        for (BuddyInfo buddyInfo : buddyInfos) { //adding BuddyInfo to address book
            addressBook.addBuddyInfo(buddyInfo);
        }

        return addressBook;
    }

    /**
     * Method returns the actual list contributing to JList.
     *
     * @return DefaultListModel carried by address book.
     */
    public DefaultListModel<BuddyInfo> getBuddyListModel() {
        return buddyListModel;
    }

    /**
     * Method to serialize the address book. Pass the DefaultListModel to method to
     * be serialized and creates a binary file.
     *
     * @param list is the DefaultListModel of address book you want to export.
     * @param filename Name of file you want it to output, no extension name needed.
     * @throws IOException Exception if unable to create file.
     */
    public static void serializeToFile(Object list, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
        }
    }

    /**
     * Method has same function as loadAddress except it has one extract feature to extract
     * from a serialized file instead of a .txt.
     * SuppressWarnings("unchecked") is for ignoring casting safety since we're assuming
     * all serialized files we're passing are DefaultListModel of BuddyInfo.
     *
     * @param filename Full name of serialized file.
     * @param name Name of address book.
     * @return Instance of address book with BuddyInfo loaded from serialized file.
     * @throws IOException Exception.
     * @throws ClassNotFoundException Exception.
     */
    @SuppressWarnings("unchecked")
    public static AddressBook loadAddressSerial(String filename, String name) throws IOException, ClassNotFoundException {
        //try reading serialized file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            //casting object in serial file
            DefaultListModel<BuddyInfo> list = (DefaultListModel<BuddyInfo>) ois.readObject();
            AddressBook book = new AddressBook(name);

            for (int i = 0; i < list.getSize(); i++) { //adding elements to address book
                book.addBuddyInfo(list.getElementAt(i));
            }

            return book;

        }
    }

    public static void exportToXmlFile(AddressBook book, String filename) throws IOException {
        String fileName = filename.trim(); //get rid of any whitespace and add .txt

        if (!fileName.toLowerCase().endsWith(".xml")) {
            fileName += ".xml";
        }

        File file = new File(fileName); //create file instance

        if (file.exists()) { //file already created
            System.out.println("overwriting existing file");

        }else if (file.createNewFile()) { //try to create file
            System.out.println("File created");
        }

        try (FileWriter out = new FileWriter(fileName)) {
            out.write(book.toXML());
        }


    }

    private String getBuddyXML() {
        StringBuilder buddyXML = new StringBuilder();
        for (int i = 0; i < buddyListModel.getSize(); i++) {
            BuddyInfo buddy = (BuddyInfo) buddyListModel.get(i);
            if (i == buddyListModel.getSize() - 1) {
                buddyXML.append(buddy.toXML()).append("\n\t\t");
            } else {
                buddyXML.append(buddy.toXML()).append("\n\t\t\t");
            }
        }

        return buddyXML.toString();
    }

    public String toXML() {
        return "<addressBook>\n\t" +
                "<addressBookName>" + this.getName() + "</addressBookName>\n\t\t" +
                "<buddyInfos>\n\t\t\t" +
                this.getBuddyXML() + "</buddyInfos>\n</addressBook>\n";
    }

    public static void main(String[] args) {
        AddressBook book = new AddressBook("testBook");
        book.addBuddyInfo("test1", "test2", "test3");
        book.addBuddyInfo("test4", "test5", "test6");

        //System.out.println(book.toXML());

        /*
        try {
            AddressBook.exportToXmlFile(book, "outputXML");
        } catch (IOException e) {
            e.printStackTrace();
        }

         */

        /*
        //this works
        AddressBookXMLParser parser = new AddressBookXMLParser();
        try {
            ArrayList<BuddyInfo> temp = parser.readXMLFile("outputXML.xml");
            for (BuddyInfo buddy: temp) {
                System.out.println(buddy.toString());
            }
        } catch (IOException e) {
            e.getMessage();
        }

         */

        AddressBookXMLParser parser = new AddressBookXMLParser();
        try {
            AddressBook temp = parser.readXMLFileOutAddress("outputXML.xml");

            System.out.println("Name of address " + temp.getName());

            DefaultListModel<BuddyInfo> list = temp.getBuddyListModel();
            for (int i =0; i < temp.getSize(); i++) {
                System.out.println(list.getElementAt(i).toString());
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
