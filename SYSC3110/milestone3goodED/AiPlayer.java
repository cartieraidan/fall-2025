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
    private ArrayList<Integer> playableCards;

    public AiPlayer(String name) {
        super(name);
        uiHand = new ArrayList<>();
        playableCards = new ArrayList<>();
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
    public void updateUIHand(UnoView view, Card card) {
        uiHand.clear(); //clear current hand
        playableCards.clear(); //clear playable cards

        this.playableCards(card);

        JPanel cardContainer = view.getPlayerCards(); //where UI buttons stored in view
        Component[] comp = cardContainer.getComponents(); //get all buttons

        for (Component component : comp) {
            if (component instanceof JButton) {
                uiHand.add((JButton) component);
            }
        }
    }

    /**
     * Update the Arraylist to specify which cards are playable to AI player
     *
     * @param discardedCard Top card on the discard pile
     */
    private void playableCards(Card discardedCard) {
        for (Card card : gethand()) {
            if (card.matches(discardedCard)) {
                playableCards.add(gethand().indexOf(card)); //adding index of card
            }
        }

    }

    public void testHand() {
        for (JButton button : uiHand) {
            System.out.println(button.getText() + " and card object toString: " + gethand().get((Integer)button.getClientProperty("index")).toString());
        }
    }

    public void testPlayableCards() {
        for (int i : playableCards) {
            System.out.println("playble cards: " + gethand().get(i).toString());
        }
    }
}
