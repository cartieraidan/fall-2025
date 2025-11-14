import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AddressBookTest {
    private AddressBook book;
    private BuddyInfo buddy1;
    private BuddyInfo buddy2;

    @Before
    public void setUp() {
        book = new AddressBook("Friends");
        buddy1 = new BuddyInfo("Alice", "123 Main St", "555-1111");
        buddy2 = new BuddyInfo("Bob", "456 Oak St", "555-2222");
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
}
