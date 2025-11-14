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
        return "Name: " + getName() + " Address: " + getAddress() + " Phone: " + getPhoneNumber();
    }

    public static void main(String[] args) {
        BuddyInfo obj = new BuddyInfo("Homer", "Donut", "888-888-8888");
        System.out.println("Hello " + obj.getName());
    }
}
