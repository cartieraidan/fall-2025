import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author Aidan Cartier
 * @version November 25, 2025
 */
public class AiPlayer extends Player {

    private ArrayList<JButton> uiHand;

    public AiPlayer(String name) {
        super(name);
        uiHand = new ArrayList<>();
    }

    public AiPlayer() {
        this("AI Player");
    }

    /**
     * Method clears its previous hand and updates it from getting the JPanel in view
     * and getting all the components (JButtons).
     * Can get actual card class from using .getClientProperty("index") which is the int index
     * in the player hand Card list.
     *
     * @param view Current game view.
     */
    public void updateUIHand(UnoView view) {
        uiHand.clear(); //clear current hand

        JPanel cardContainer = view.getPlayerCards(); //where UI buttons stored in view
        Component[] comp = cardContainer.getComponents(); //get all buttons

        for (Component component : comp) {
            if (component instanceof JButton) {
                uiHand.add((JButton) component);
            }
        }
    }

    public void testHand() {
        for (JButton button : uiHand) {
            System.out.println(button.getText() + " and card object toString: " + gethand().get((Integer)button.getClientProperty("index")).toString());
        }
    }
}
