import javax.swing.*;

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


}
