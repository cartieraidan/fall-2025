import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BuddyInfoTest {

    @Test
    public void testToString() {
        BuddyInfo buddy = new BuddyInfo("Aidan", "123 street", "223-334-1123");
        String text = buddy.toString();
        Assert.assertEquals("Name: Aidan#Address: 123 street#Phone: 223-334-1123", text);
    }

    @Test
    public void testImportBuddyInfo() {
        ArrayList<BuddyInfo> buddyInfos = new ArrayList<BuddyInfo>();
        buddyInfos = BuddyInfo.importBuddyInfo("output.txt");

        assertEquals(3, buddyInfos.size());
        assertEquals("Adina", buddyInfos.get(0).getName());
        assertEquals("Afjdj", buddyInfos.get(1).getName());
        assertEquals("aidan", buddyInfos.get(2).getName());
    }
}
