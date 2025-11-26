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
    private int bestCard;

    public AiPlayer(String name) {
        super(name);
        uiHand = new ArrayList<>();
        playableCards = new ArrayList<>();
        bestCard = -1;
    }

    public AiPlayer() {
        this("AI Player");
    }

    @Override
    public void drawCard(Deck deck) {
        Card c = deck.drawCard();
        if (c != null){
            gethand().add(c);
        }

    }

    //gets called after AI gets called to draw
    //other ai functions should have been called to initialize all current data
    //function will only be called when AI has draw card and it able to play it
    public JButton tryToPlay() {
        return uiHand.get(playableCards.get(bestCard));
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
        if (!(playableCards.isEmpty())) { //finding best card if you have cards to play
            this.findBestCard();
        }

        JPanel cardContainer = view.getPlayerCards(); //where UI buttons stored in view
        Component[] comp = cardContainer.getComponents(); //get all buttons

        for (Component component : comp) {
            if (component instanceof JButton) {
                uiHand.add((JButton) component);
            }
        }
    }

    /**
     * Gets the best hand out of players hand
     *
     * @return Card it will play.
     */
    public Card getBestCard() {
        if (bestCard == -1) {
            return null;
        } else {
            return gethand().get(playableCards.get(bestCard));
        }
    }

    /**
     * Finds the best card value in playable cards. Updates an int value, which is the card index
     * in players hand.
     */
    private void findBestCard() {
        if (playableCards.size() == 1) {
            bestCard = 0;
        } else {
            bestCard = 0;

            for (int i = 1; i < playableCards.size(); i++) {
                int bestCardIndex = playableCards.get(bestCard);
                int currentCardIndex = playableCards.get(i);

                if (gethand().get(bestCardIndex).getValue() < gethand().get(currentCardIndex).getValue()) {
                    bestCard = i;
                }
            }
        }
    }

    /**
     * Update the Arraylist to specify which cards are playable to AI player.
     *
     * @param discardedCard Top card on the discard pile.
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

    public void testBestCard() {
        System.out.println("best card to play: " + gethand().get(playableCards.get(bestCard)).toString());
    }
}
