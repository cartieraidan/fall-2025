import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;


public class AddressBook extends DefaultListModel {

    private JList<BuddyInfo> buddyList;

    private DefaultListModel<BuddyInfo> buddyListModel;

    private String name;

    public AddressBook(String name) {
        this.name = name;
        buddyListModel = new DefaultListModel<>();
    }

    public void addBuddyInfo(String name, String address, String phoneNumber) {
        BuddyInfo buddyInfo = new BuddyInfo(name, address, phoneNumber);
        buddyListModel.addElement(buddyInfo);
    }

    public void addBuddyInfo(BuddyInfo buddyInfo) {
        buddyListModel.addElement(buddyInfo);
    }

    public JList<BuddyInfo> getBuddyList() {
        buddyList = new JList<>(buddyListModel);
        return buddyList;
    }

    public int getSize() {
        return buddyList.getModel().getSize();
    }

    public String getName() {
        return name;
    }

    public void save(String filename) {
        try {
            FileWriter file = new FileWriter("output.txt");

            String text = "";
            for (int i = 0; i < buddyListModel.getSize(); i++) {
                BuddyInfo temp = (BuddyInfo) buddyListModel.get(i);
                text += temp.toString() + "\n";
            }

            file.write(text);
            file.close();
            System.out.println("Output saved to file");

        } catch (IOException e) {
            System.out.println("error getting file");
            e.printStackTrace();
        }
    }


}
