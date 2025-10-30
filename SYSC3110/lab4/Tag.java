import java.util.ArrayList;

public class Tag extends Elements{

    private String name;

    private ArrayList<Elements> subElements;

    //private enum Content {TEXT, TAG}
    private Content type;

    /**
     * Constructs Tag with a tag name and initialize list for sub elements.
     * Acts as a container
     * @param name Is the tag name of Tag
     */
    public Tag(String name) {
        this.name = name;
        subElements = new ArrayList<>();
    }

    /**
     * Sets type of the sub elements for Tag once first element added to the list
     * from assumption where can only have one
     * @param element is the Elements object that gets added the subElement
     */
    private void setType(Elements element) {
        this.type = (element instanceof Tag) ? Content.TAG : Content.TEXT;
    }

    /**
     * Add Elements object to sub list
     * @param element is the Elements object that gets added the subElement
     */
    public void addSubElement(Elements element) {
        if (subElementEmpty()) {
            subElements.add(element);

            this.setType(element);
        } else {
            if (this.type == Content.TAG) {
                subElements.add(element);
            }
        }
    }

    private boolean subElementEmpty() {
        return subElements.isEmpty();
    }

    /**
     * abstract class that implements print function
     * @param recursion recursion number for the amount of \t
     * @return String concatenation of output
     */
    @Override
    public String print(int recursion) {
        StringBuilder output;

        if (subElementEmpty()) {

            output = new StringBuilder("\t".repeat(recursion) + "<" + name + "></" + name + ">");
        } else {

            output = new StringBuilder("\t".repeat(recursion) + "<" + name + ">");

            for (Elements element : subElements) {
                output.append("\n").append(element.print(recursion + 1));
            }

            output.append("\n").append("\t".repeat(recursion)).append("</").append(name).append(">");
        }

        return output.toString();

    }
}
