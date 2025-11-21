import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * View class that creates a JFrame to display address books.
 *
 * @author Aidan Cartier
 * @version November 21, 2025
 */
public class AddressBookGUI extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private JPanel buddyDisplay; //used to display BuddyInfo

    private AddressBook addressSelected;

    private JMenuBar menuBar;
    private JMenu buddyOps;
    private JMenu addressMenu;

    private ArrayList<AddressBook> createdAddress; //need to implement function for when making new address book

    private JMenuItem add;
    private JMenuItem remove;
    private JMenuItem display;
    private JMenuItem create;
    //private JMenuItem select;
    private JMenuItem importBook;
    private JMenuItem importSerial;
    private JMenuItem importXML;

    /**
     *Constructor that creates JFrame initializes everything needed.
     */
    public AddressBookGUI() {
        setTitle("AddressBook");

        mainPanel = new JPanel();
        buddyDisplay = new JPanel();

        setContentPane(mainPanel);

        //all menu bar objects
        menuBar = new JMenuBar();
        buddyOps = new JMenu("Buddy");
        addressMenu = new JMenu("Address");
        createdAddress = new ArrayList<>();
        add = new JMenuItem("Add");
        remove = new JMenuItem("Remove");
        display = new JMenuItem("Display");
        create = new JMenuItem("Create");
        importBook = new JMenuItem("Import");
        importSerial = new JMenuItem("ImportSerial");
        importXML = new JMenuItem("ImportXML");

        //adding to parent objects
        buddyOps.add(add);
        buddyOps.add(remove);
        buddyOps.add(display);
        addressMenu.add(create);
        addressMenu.add(importBook);
        addressMenu.add(importSerial);
        addressMenu.add(importXML);

        //adding parent objects to menu bar
        menuBar.add(buddyOps);
        menuBar.add(addressMenu);

        //set menu bar for JFrame
        setJMenuBar(menuBar);

        //JFrame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        setResizable(false);
        setVisible(true);

        //Adding action listeners for menu items
        display.addActionListener(this);
        create.addActionListener(this);
        add.addActionListener(this);
        remove.addActionListener(this);
        importBook.addActionListener(this);
        importSerial.addActionListener(this);
        importXML.addActionListener(this);
    }

    /**
     * Method that display JList of BuddyInfo, calls addDisplayPane to handle updating
     * the current panel.
     */
    private void displayInfo() {
        //buddyDisplay.setBackground(Color.RED); //test

        JList<BuddyInfo> list = addressSelected.getBuddyList();
        addDisplayPane(list);
    }

    /**
     * Method that removes all components from display pane then adds the
     * JList to it then updates the view for the user to see it.
     *
     * @param list JList of current selected address book.
     */
    private void addDisplayPane(JList<BuddyInfo> list) {
        buddyDisplay.removeAll(); //remove all components

        JScrollPane scrollPane = new JScrollPane(list); //adds JList

        buddyDisplay.add(scrollPane); //add to panel
        setContentPane(buddyDisplay); //set as current view

        //update for user
        revalidate();
        repaint();
    }

    /**
     * Method for initializing all functions when creating an address book,
     * like adding a new menu file and items that are separate for each address book
     * and adding action listeners.
     *
     * @param book Address book that was newly created by user input.
     */
    private void addressFunctionality(AddressBook book) {
        addressSelected = book; //updates current selected address book
        createdAddress.add(book); //add to list to track
        JMenu address = new JMenu(book.getName()); //name menu of address book name

        //all menu items
        JMenuItem select = new JMenuItem("Select");
        JMenuItem export = new JMenuItem("Export");
        JMenuItem exportS = new JMenuItem("Export Serial");
        JMenuItem exportXML = new JMenuItem("Export XML");

        //adding to parent
        address.add(select);
        address.add(export);
        address.add(exportS);
        address.add(exportXML);

        //adding menu parent to menu bar
        menuBar.add(address);

        //adding all action listeners
        export.addActionListener(this);
        select.addActionListener(this);
        exportS.addActionListener(this);
        exportXML.addActionListener(this);

        //for testing
        //book.addBuddyInfo("Adina", "123mm", "2265543");
        //book.addBuddyInfo("Afjdj", "12344m", "223345543");

        //update view for user
        revalidate();
        repaint();
    }

    /**
     * Method for creating new address book. Gets input from user on the name
     * then call addressFunctionality to initialize address operations.
     */
    private void createAddress() {
        String name = JOptionPane.showInputDialog("Enter Name: "); //user input

        if (name != null && !name.trim().isEmpty()) { //ensure not empty input
            AddressBook book = new AddressBook(name);
            this.addressFunctionality(book);
        }

    }

    /**
     * Method that implemented for menu item to import an address book from a .txt file.
     *
     * @param filename Input name of file with full extension like file.txt.
     * @param bookName String user inputs.
     */
    private void importAddress(String filename, String bookName) {
        AddressBook book = AddressBook.loadAddress(filename, bookName); //calls static method
        addressFunctionality(book); //initializes all operations
        displayInfo(); //update view
    }

    /**
     * Method that implemented for menu item to import a serialized version of
     * an address book.
     *
     * @param fileName full file name of serialized file.
     * @param bookName String user input for name of address book.
     */
    private void importSerialAddress(String fileName, String bookName) {
        try {
            AddressBook book = AddressBook.loadAddressSerial(fileName, bookName); //calls static method
            addressFunctionality(book); //initializes all operations
            displayInfo(); //update view
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     *
     *
     * @param filename name of file must have full extension name i.e. file.xml
     */
    private void importFromXmlFile(String filename) {
        AddressBookXMLParser parser = new AddressBookXMLParser();
        try {
            AddressBook temp = parser.readXMLFileOutAddress(filename);

            addressFunctionality(temp); //initializes all operations
            displayInfo(); //update view
        } catch (IOException e) {
            e.getMessage();
        }
    }

    /**
     * All implementations for all the actions listeners for menu item.
     * For add, remove, display, create, select, export, import, import serial, export serial.
     *
     * @param event the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == display) {
            this.displayInfo();
        } else if (event.getSource() == create) {
            this.createAddress();
        } else if (event.getSource() == add) {
            String name = JOptionPane.showInputDialog("Enter Name: ");
            String address = JOptionPane.showInputDialog("Enter Address: ");
            String phone = JOptionPane.showInputDialog("Enter Phone Number: ");
            addressSelected.addBuddyInfo(name, address, phone);
            this.displayInfo();
        } else if (event.getSource() == remove) {
            this.remove();
        } else if (event.getSource() == importBook) {
            String bookName = JOptionPane.showInputDialog("Enter Book Name: ");
            String fileName = JOptionPane.showInputDialog("Enter File Name(full name with .txt): ");

            importAddress(fileName, bookName);
        } else if (event.getSource() == importSerial) {
            String bookName = JOptionPane.showInputDialog("Enter Book Name: ");
            String fileName = JOptionPane.showInputDialog("Enter File Name: ");

            importSerialAddress(fileName, bookName);
        } else if (event.getSource() == importXML) {
            String input = JOptionPane.showInputDialog("Enter File Name(full name with .xml): ");
            importFromXmlFile(input.trim());
        } else if (event.getSource() instanceof JMenuItem item) {
            if (item.getText().equals("Select")) {
                //JMenuItem selected = (JMenuItem) event.getSource();
                JMenu parent = (JMenu) ((JPopupMenu) item.getParent()).getInvoker();
                //System.out.println(parent.getText());


                for (AddressBook book : createdAddress) {
                    if (book.getName().equals(parent.getText())) {
                        addressSelected = book;
                    }
                }
            } else if (item.getText().equals("Export")) {
                String name = JOptionPane.showInputDialog("Enter Name of file(don't include .txt): ");
                addressSelected.export(name);
            } else if (item.getText().equals("Export Serial")) {
                String name = JOptionPane.showInputDialog("Enter Name of file(don't include extension name): ");
                try {
                    AddressBook.serializeToFile(addressSelected.getBuddyListModel(), name);
                } catch (IOException e) {
                    System.out.println("Error while saving file");
                    throw new RuntimeException(e);
                }
            } else if (item.getText().equals("Export XML")) {
                String fileName = JOptionPane.showInputDialog("Enter File Name(full name with .xml): ");
                try {
                    AddressBook.exportToXmlFile(addressSelected, fileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Method implemented for the remove menu item to remove the selected BuddyInfo
     * from the JList then updates view.
     */
    private void remove() {
        JList<BuddyInfo> list = addressSelected.getBuddyList();
        BuddyInfo selected = (BuddyInfo) list.getSelectedValue();

        System.out.println("Removing " + selected.getName());

        addressSelected.removeBuddyInfo(selected);

        addDisplayPane(list);
    }

    public static void main(String[] args) {

        AddressBookGUI obj = new AddressBookGUI();
    }
}
