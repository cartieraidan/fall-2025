import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AddressBookXMLParser extends DefaultHandler {

    private ArrayList<BuddyInfo> buddyInfos;
    private StringBuilder elementContent;
    private AddressBook book;

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

        } else if (qName.equalsIgnoreCase("addressBook")) {
            book = new AddressBook("N/A");
        }

        elementContent = new StringBuilder();
    }

    /**
     * Appends the content in between the tag.
     *
     * @param ch The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the
     *               character array.
     */
    @Override
    public void characters(char[] ch, int start, int length) {
        elementContent.append(ch, start, length);
    }

    /**
     * Updates the last BuddyInfo in array list after a tag has closed.
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     */
    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("name")) {
            buddyInfos.getLast().setName(elementContent.toString());

        } else if (qName.equalsIgnoreCase("address")) {
            buddyInfos.getLast().setAddress(elementContent.toString());
        } else if (qName.equalsIgnoreCase("phoneNumber")) {
            buddyInfos.getLast().setPhoneNumber(elementContent.toString());
        } else if (qName.equalsIgnoreCase("addressBookName")) {
            book.setName(elementContent.toString());
        }
    }

    public ArrayList<BuddyInfo> readXMLFile(String fileName) throws IOException {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            File file = new File(fileName);
            parser.parse(file, this);
            return buddyInfos;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }

    public AddressBook readXMLFileOutAddress(String fileName) throws IOException {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            File file = new File(fileName);
            parser.parse(file, this);

            addBuddies();

            return book; //replace
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }

    private void addBuddies() {
        for (BuddyInfo buddyInfo : buddyInfos) {
            book.addBuddyInfo(buddyInfo);
        }
    }


}
