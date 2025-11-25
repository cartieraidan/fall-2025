import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Controller class manages the state and flow of the GUI UNO game.
 * Controller most of the game flow from interactions with the GUI and handle the logic to handle the.
 *
 *
 * @author Aidan Cartier
 * @version November 10, 2025
 */
public class Controller implements MouseListener, MouseMotionListener, ActionListener {

    private UnoView view;
    private ArrayList<Player> players;
    private GameManager gameManager;

    private boolean gameOver;

    /**
     * Constructs a Controller that handles a view and model of UNO
     *
     */
    public Controller() {
        view = new UnoView();
        view.setController(this);
        players = new ArrayList<>();

        //setup game
        gameManager = new GameManager(players);
        gameManager.setView(view);
        gameManager.initializeControls();
        view.subscribe(gameManager);
        gameManager.startgame();

        view.setVisible(true);

        gameOver = false;


    }

    /**
     * Returns the controllers view
     *
     * @return the controllers view
     */
    public UnoView getView() {
        return view;
    }

    /**
     * Plays the card the player selected after pressing the play button.
     * Goes through different logic if it is a wild card as it requires user input for its color.
     */
    /*private void playCard() {

        if (selectedCard == null) {
            JOptionPane.showMessageDialog(null, "No card selected.");
        } else {
            Card card = currentPlayerHand.get((int) selectedCard.getClientProperty("index")); //get card from button hidden index

            //wild requires input so controller has to take care of it
            if (card.getType() == CardType.WILD || card.getType() == CardType.WILD_DRAW_TWO) {
                handleWildCard(card);
                currentPlayerHand.remove(card);
            } else {
                gameManager.playCard(card);
                currentPlayerHand.remove(card); //remove card played from list, never implemented in game manager?
            }
        }

    }*/

    /**
     * When cursor leaves area over card/JButton.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent event) {
        gameManager.resetHover();
    }

    /**
     * Implemented for whenever a UNO/JButton Card is pressed, sends logic to handleCardPressed method.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        JButton buttonCard = (JButton) event.getSource(); //get button source

        gameManager.handleCardPressed(buttonCard);

    }

    /**
     * Mouse event for when cursor goes over the button.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent event) {
        //get current card hovering over
        JButton buttonCard = (JButton) event.getSource();

        gameManager.handleHover(buttonCard);
    }

    /**
     * Not required so not implemented.
     *
     * @param event the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent event) {

    }

    /**
     * Not required so not implemented.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent event) {

    }

    /**
     * Not required so not implemented.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent event) {

    }

    /**
     * Not required so not implemented.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent event) {

    }

    /**
     * Action listener for the game control buttons like quit, play, draw
     *
     * @param event the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();

        if (button.getText().equals("Quit")) {
            System.exit(0);

        } else if (button.getText().equals("Play")) {
            System.out.println("play called");
            gameManager.playCard();

            gameManager.updateView();

        } else if (button.getText().equals("Draw")) {
            System.out.println("draw called");
            Player player = gameManager.getCurrentPlayer();

            CardColour loopColour = gameManager.getDrawLoopColour();
            if (gameManager.getWildDrawLoop()) {
                Deck deck = gameManager.getDeck();
                Card drawCard = deck.drawCard();

                player.addCardtoHand(drawCard);
                if (drawCard.getColour() == loopColour) {
                    gameManager.setWildDrawLoop(false); //exit draw loop
                    gameManager.setPlayButton(true); //enable play card button
                    gameManager.setButtonBool(false); //disable draw button
                } else {
                    JOptionPane.showMessageDialog(null, "Keep drawing");
                }

            } else {
                // Draw one card
                gameManager.drawCard();
                gameManager.setBool(true);
                //gameManager.updatePlayerCards();
            }


            // After drawing, check if they can play now
            if (!gameManager.getWildDrawLoop()) { //does not break draw colour loop
                if (!player.hasPlayableCard(gameManager.topDiscard())) {
                    JOptionPane.showMessageDialog(null, "Still no playable cards. Turn skipped.");
                    gameManager.setBool(false);
                    gameManager.setButtonBool(false);
                    gameManager.nextTurn();
                    //gameManager.updateView();
                } else {
                    JOptionPane.showMessageDialog(null, "You may now play your new card if possible.");
                    gameManager.setButtonBool(false);
                }
            }

            gameManager.updateView();
        }
    }

    public static void main(String[] args) {
        UnoView unoView = new UnoView();
        Controller controller = new Controller();
    }
}
