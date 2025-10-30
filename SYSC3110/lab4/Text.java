public class Text extends Elements {

    private String text;

    /**
     * Construct Elements object for text
     * @param text string value of element
     */
    public Text(String text) {
        this.text = text;
    }

    /**
     * Gets value of text
     * @return String text of object
     */
    public String getText() {
        return text;
    }

    /**
     * abstract class that implements print function
     * @param recursion recursion number for the amount of \t
     * @return String concatenation of output
     */
    @Override
    public String print(int recursion) {
        //System.out.println(getText());

        return "\t".repeat(recursion) + getText();
    }
}
