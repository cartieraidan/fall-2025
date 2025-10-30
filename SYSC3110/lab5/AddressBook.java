import javax.swing.*;

public class AddressBook {

    private JList<BuddyInfo> buddyList;

    public AddressBook() {
        buddyList = new JList<>();
    }

    public int getSize() {
        return buddyList.getModel().getSize();
    }


}
