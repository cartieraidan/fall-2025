import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class AddressBookXMLParser extends DefaultHandler {

    private ArrayList<BuddyInfo> buddyInfos;
    private StringBuilder elementContent;

    /**
     * Called when document starts
     */
    @Override
    public void startDocument() {
        buddyInfos = new ArrayList<>();
    }

    /**
     * Every time a new tag is formed it calls this function creating a new string builder.
     * If tag buddyInfo passes create an empty BuddyInfo and add to ArrayList.
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @param attributes The attributes attached to the element.  If
     *        there are no attributes, it shall be an empty
     *        Attributes object.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(qName.equalsIgnoreCase("buddyInfo")) {
            BuddyInfo currentBuddy = new BuddyInfo();
            buddyInfos.add(currentBuddy);

        }

        elementContent = new StringBuilder();
    }

    



}
