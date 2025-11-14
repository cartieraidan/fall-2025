import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class AddressBook extends DefaultListModel {

    private JList<BuddyInfo> buddyList;

    private DefaultListModel<BuddyInfo> buddyListModel;

    private String name;

    public AddressBook(String name) {
        this.name = name;
        buddyListModel = new DefaultListModel<>();
        buddyList = new JList<>(buddyListModel);
    }

    public void addBuddyInfo(String name, String address, String phoneNumber) {
        BuddyInfo buddyInfo = new BuddyInfo(name, address, phoneNumber);
        buddyListModel.addElement(buddyInfo);
    }

    public void addBuddyInfo(BuddyInfo buddyInfo) {
        buddyListModel.addElement(buddyInfo);
    }

    public void removeBuddyInfo(BuddyInfo buddyInfo) {
        buddyListModel.removeElement(buddyInfo);
    }

    public JList<BuddyInfo> getBuddyList() {
        //buddyList = new JList<>(buddyListModel);
        return buddyList;
    }

    public int getSize() {
        return buddyList.getModel().getSize();
    }

    public String getName() {
        return name;
    }

    public void save(String filename) {
        //requires full file name ex. file.txt
        try {
            FileWriter file = new FileWriter(filename);

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

    public void export(String filename) {
        String fileName = filename.trim() + ".txt";
        try {
            File file = new File(fileName);

            if (file.exists()) {
                System.out.println("overwriting existing file");
            }else if (file.createNewFile()) { //try to create file
                System.out.println("File created");
            }

            this.save(fileName);

        } catch (IOException e) {
            System.out.println("error creating file");
            e.printStackTrace();
        }
    }

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
}
