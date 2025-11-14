import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BuddyInfo {
    private String name;
    private String address;
    private String phoneNumber;

    public BuddyInfo(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public BuddyInfo() {
        this("N/A", "N/A", "N/A");
    }

    public boolean equals(BuddyInfo buddy) {
        return (this.getName().equals(buddy.getName())) && ((this.getAddress()).equals(buddy.getAddress()));
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return getName() + "#" + getAddress() + "#" + getPhoneNumber();
    }

    public static ArrayList<BuddyInfo> importBuddyInfo(String filename) {
        ArrayList<BuddyInfo> buddyInfos = new ArrayList<>();

        File file = new File(filename);
        System.out.println(file.exists());
        System.out.println(filename);


        try (Scanner myReader = new Scanner(file)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine(); //need to consume a line
                String[] dataArray = data.split("#");
                BuddyInfo buddy = new BuddyInfo(dataArray[0], dataArray[1], dataArray[2]);
                buddyInfos.add(buddy);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }

        return buddyInfos;
    }

    public static void main(String[] args) {
        BuddyInfo obj = new BuddyInfo("Homer", "Donut", "888-888-8888");
        System.out.println("Hello " + obj.getName());
    }
}
