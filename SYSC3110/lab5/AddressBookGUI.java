import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddressBookGUI extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private JPanel buddyDisplay;


    private JMenuBar menuBar;
    private JMenu buddyOps;
    private JMenu AddressBook;

    private ArrayList<JMenu> createdAddress; //need to implement function for when making new address book

    private JMenuItem add;
    private JMenuItem remove;
    private JMenuItem display;
    private JMenuItem create;
    private JMenuItem select;

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
    }

    private void displayInfo() {
        buddyDisplay.setBackground(Color.RED); //test
        setContentPane(buddyDisplay);
        revalidate();  
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == display) {
            this.displayInfo();
        }
    }

    public static void main(String[] args) {

        AddressBookGUI obj = new AddressBookGUI();
    }
}
