import javax.print.Doc;
import java.util.ArrayList;

public class Document {
    private String root;
    private ArrayList<Elements> elements;

    /**
     * Constructs a XML document
     * @param root is the main tag of document
     */
    public Document(String root) {
        this.root = root;
        elements = new ArrayList<>();

    }

    /**
     * Add elements to list
     * @param element contain text and tags
     */
    public void addElement(Elements element) {
        elements.add(element);
    }

    /**
     * prints output of document
     */
    public void print() {
        System.out.println("<" + root + ">");
        for (Elements element : elements) {
            System.out.println(element.print());
        }
        System.out.println("</" + root + ">");
    } //just need to change to create root as tag and add everything to that

    static void main() {
        Document d = new Document("course");

        Tag t1 = new Tag("class");
        Tag t2 = new Tag("student");
        Tag t3 = new Tag("student");
        Text tx1 = new Text("Mich");
        Text tx2 = new Text("Anna");

        t2.addSubElement(tx1);
        t1.addSubElement(t2);

        t3.addSubElement(tx2);
        t1.addSubElement(t3);

        Tag t4 = new Tag("code");
        Text tx4 = new Text("SYSC3110");
        t4.addSubElement(tx4);

        Tag t5 = new Tag("prof");
        Text tx5 = new Text("Wafa");
        t5.addSubElement(tx5);

        d.addElement(t4);
        d.addElement(t5);
        d.addElement(t1);

        d.print();
    }
}
