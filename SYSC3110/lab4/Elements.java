import java.util.ArrayList;

public abstract class Elements {

    public enum Content {TEXT, TAG};

    /**
     * Not really supposed to be initialized
     */
    public Elements() {
    }

    /**
     * abstract class that implements print function
     * @param recursion recursion number for the amount of \t
     * @return String concatenation of output
     */
    public abstract String print(int recursion);

    /**
     * Method overload abstract print method.
     * input 1 for recursion value
     * @return string concatenation of output
     */
    public String print() {
        return print(1);
    }



}
