import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddressBookGUI extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private JPanel buddyDisplay;

    private AddressBook addressSelected;

    private JMenuBar menuBar;
    private JMenu buddyOps;
    private JMenu AddressBook;

    private ArrayList<AddressBook> createdAddress; //need to implement function for when making new address book

    private JMenuItem add;
    private JMenuItem remove;
    private JMenuItem display;
    private JMenuItem create;
    //private JMenuItem select;

    public AddressBookGUI() {
        setTitle("AddressBook");

        mainPanel = new JPanel();
        buddyDisplay = new JPanel();

        setContentPane(mainPanel);

        menuBar = new JMenuBar();
        buddyOps = new JMenu("Buddy");
        AddressBook = new JMenu("Address");
        createdAddress = new ArrayList<>();
        add = new JMenuItem("Add");
        remove = new JMenuItem("Remove");
        display = new JMenuItem("Display");
        create = new JMenuItem("Create");

        buddyOps.add(add);
        buddyOps.add(remove);
        buddyOps.add(display);
        AddressBook.add(create);

        menuBar.add(buddyOps);
        menuBar.add(AddressBook);

        setJMenuBar(menuBar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        setResizable(false);
        setVisible(true);

        display.addActionListener(this);
        create.addActionListener(this);
        add.addActionListener(this);
        remove.addActionListener(this);
    }

    private void displayInfo() {
        //buddyDisplay.setBackground(Color.RED); //test

        JList<BuddyInfo> list = addressSelected.getBuddyList();
        addDisplayPane(list);
    }

    private void addDisplayPane(JList<BuddyInfo> list) {
        buddyDisplay.removeAll();

        JScrollPane scrollPane = new JScrollPane(list);

        buddyDisplay.add(scrollPane);
        setContentPane(buddyDisplay);
        revalidate();
        repaint();
    }

    private void createAddress() {
        //needs to prompt

        String name = JOptionPane.showInputDialog("Enter Name: ");
        if (name != null && !name.trim().isEmpty()) {
            AddressBook book = new AddressBook(name);

            addressSelected = book;
            createdAddress.add(book);
            JMenu address = new JMenu(book.getName());
            JMenuItem select = new JMenuItem("Select");
            address.add(select);
            menuBar.add(address);

            select.addActionListener(this);

            //for testing
            book.addBuddyInfo("Adina", "123mm", "2265543");
            book.addBuddyInfo("Afjdj", "12344m", "223345543");

            revalidate();
            repaint();
        }


    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == display) {
            this.displayInfo();
        } else if (event.getSource() == create) {
            this.createAddress();
        }  else if (event.getSource() == add) {
            String name = JOptionPane.showInputDialog("Enter Name: ");
            String address = JOptionPane.showInputDialog("Enter Address: ");
            String phone = JOptionPane.showInputDialog("Enter Phone Number: ");
            addressSelected.addBuddyInfo(name, address, phone);
            this.displayInfo();
        } else if (event.getSource() == remove) {
            this.remove();
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
            }
        }
    }

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
