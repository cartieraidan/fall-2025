import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
}
