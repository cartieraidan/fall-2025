import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BuddyInfoTest {

    @Test
    public void testToString() {
        BuddyInfo buddy = new BuddyInfo("Aidan", "123 street", "223-334-1123");
        String text = buddy.toString();
        Assert.assertEquals("Name: Aidan#Address: 123 street#Phone: 223-334-1123", text);
    }
}
