import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class AddressBookTest {
    private AddressBook book;
    private BuddyInfo buddy1;
    private BuddyInfo buddy2;
    private BuddyInfo buddy3;

    @Before
    public void setUp() {
        book = new AddressBook("Friends");
        buddy1 = new BuddyInfo("Alice", "123 Main St", "555-1111");
        buddy2 = new BuddyInfo("Bob", "456 Oak St", "555-2222");
        buddy3 = new BuddyInfo("auda", "2 street", "222-222-2222");
    }

    @Test
    public void testGetName() {
        assertEquals("Friends", book.getName());
    }

    @Test
    public void testBuddyToString() {
        String result = buddy1.toString();
        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("555-1111"));
    }

    @Test
    public void testSave() {
        book.addBuddyInfo(buddy1);
        book.addBuddyInfo(buddy2);

        book.save("output.txt");

        File file = new File("output.txt");

        int count = 0;
        try (Scanner myReader = new Scanner(file)) {
            while (myReader.hasNextLine()) {
                count++;
                myReader.nextLine(); //need to consume a line
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }

        //myReader does not count final empty lines
        assertEquals(2, count);
    }

    @Test
    public void testImportExport() {
        book.addBuddyInfo(buddy1);
        book.addBuddyInfo(buddy2);
        book.addBuddyInfo(buddy3);

        //String path = System.getProperty("user.dir") + "/testOutput.txt"; //JUnit has no brain

        book.export("testOutput");


        AddressBook book2 = AddressBook.loadAddress("testOutput.txt", "book2");

        DefaultListModel<BuddyInfo> book1List = book.getBuddyListModel();
        DefaultListModel<BuddyInfo> book2List = book2.getBuddyListModel();

        //System.out.println(book.getName());

        assertEquals(book1List.size(), book2List.size());
        for (int i = 0; i < book1List.size(); i++) {

            assertTrue(book1List.getElementAt(i).equals(book2List.getElementAt(i)));
        }
    }

    @Test
    public void testSerial() throws IOException, ClassNotFoundException {
        book.addBuddyInfo(buddy1);
        book.addBuddyInfo(buddy2);
        book.addBuddyInfo(buddy3);


        AddressBook.serializeToFile(book.getBuddyListModel(), "testSerial");

        AddressBook book2 = AddressBook.loadAddressSerial("testSerial", "friends");

        DefaultListModel<BuddyInfo> book1List = book.getBuddyListModel();
        DefaultListModel<BuddyInfo> book2List = book2.getBuddyListModel();

        assertEquals(book1List.size(), book2List.size());
        for (int i = 0; i < book1List.size(); i++) {

            assertTrue(book1List.getElementAt(i).equals(book2List.getElementAt(i)));
        }
    }
}
