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

    private Map<JButton, Integer> prevCardZ; //used for tracking button prev z
    private UnoView view;
    private int playerCount;
    private ArrayList<Player> players;
    private GameManager gameManager;
    private List<Card> currentPlayerHand;

    private JButton hoveredButton = null;
    private JButton selectedCard = null;
    private JButton play;
    private JButton draw;

    private boolean gameOver;
    private boolean roundOver;
    private boolean drawCard = false;

    /**
     * Constructs a Controller that handles a view and model of UNO
     *
     */
    public Controller() {
        view = new UnoView();
        players = new ArrayList<>();
        prevCardZ = new HashMap<>();

        getNumberPlayers();
        getPlayerNames();

        //setup game
        gameManager = new GameManager(players);
        gameManager.startgame();
        updateView();

        initializeControls();

        view.setVisible(true);

        gameOver = false;
        roundOver = false;


    }

    /**
     * Initializes all the buttons at the top right of the screen
     * quit, draw, play
     */
    private void initializeControls() {
        JPanel panel = view.getRightPanel();

        //quit
        JButton quit = new JButton("Quit");
        quit.addActionListener(this);
        panel.add(quit);

        //play
        play = new JButton("Play");
        play.addActionListener(this);
        panel.add(play);

        //draw
        draw = new JButton("Draw");
        draw.addActionListener(this);
        draw.setEnabled(false);
        panel.add(draw);

        view.repaint();
    }

    /**
     * Handles logic for when a wild card is played. If wild card is played check if valid move
     * then ask user for input on colour then push card to discard pile.
     *
     * @param card is the current card the player is trying to play.
     */
    public void handleWildCard(Card card) {
        if (gameManager.checkvalidMove(card)) {

            String input;
            CardColour colour = null;

            while (true) {
                input = JOptionPane.showInputDialog("choose a color (RED, BLUE, GREEN, YELLOW): ");
                try {
                    colour = CardColour.valueOf(input.trim().toUpperCase());
                    break;
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, "Invalid color");
                }
            }

            card.setColour(colour);
            gameManager.pushToDiscardPile(card);

            gameManager.nextTurn();
            //case for wild draw two cards
            if (card.getType() == CardType.WILD_DRAW_TWO) {
                gameManager.getCurrentPlayer().drawCard(gameManager.getDeck());
                gameManager.getCurrentPlayer().drawCard(gameManager.getDeck());
                gameManager.nextTurn();
            }
        }

    }

    /**
     * roundLoop() handles the flags for drawing a card. If flag not set and there are no valid moves
     * draw button enabled. If draw flag set and no valid moves, reset flags and nextTurn()
     */
    private void roundLoop() {
        //player can't play must draw
        if (!drawCard && (!(gameManager.getCurrentPlayer().hasPlayableCard(gameManager.topDiscard())))) {
            System.out.println("play has no moves");
            draw.setEnabled(true);
        }

        //player has no next turn
        if (drawCard && (!(gameManager.getCurrentPlayer().hasPlayableCard(gameManager.topDiscard())))) {
            System.out.println("play has no moves after draw");

            //probably centralize?

            drawCard = false; //reset var
            draw.setEnabled(false);

            gameManager.nextTurn();

            updateView();
        }

    }

    /**
     * Updates the JLabel in view for which player is playing
     */
    private void updateCurrentPlayer() {
        String name = gameManager.getCurrentPlayer().getName();
        view.currentPlayerDisplay(name);
    }

    //updates the top discard pile after every state change

    /**
     * Updates discard pile View by creating new card UI/button from top of stack then adding to JPanel in View.
     */
    private void updateDiscardPile() {
        Card topCard = gameManager.topDiscard();
        JButton discard = new JButton();
        discard.setEnabled(false);
        discard.setFocusPainted(false);

        discard = setCardStyle(discard, topCard);

        //System.out.println(topCard.toString());

        discard.setBounds(
                300,
                100,
                130,
                200
        );


        view.addCenterCard(discard);
    }

    /**
     * Everytime updatePlayerCards() is called these variables need to be reset to avoid null pointers.
     */
    private void resetVars() {
        prevCardZ.clear(); //reset map for updated hand
        hoveredButton = null; //reset var
        selectedCard = null; //reset var
    }

    /**
     * Main method for the UNO cards UI setup, each turn it clears the JPanel then updates hands
     * with dynamic x offsets and styling the button based on the Card class attributes.
     * Adds MouseListener to buttons and sends final JPanel to view to be added to JFrame.
     */
    private void updatePlayerCards() {
        Player currentPlayer = gameManager.getCurrentPlayer();
        currentPlayerHand = currentPlayer.gethand();

        JPanel playerCards = getPlayerCards();

        //need to move out function
        playerCards.removeAll(); //clear old components
        resetVars();

        int offset = (playerCards.getPreferredSize().width - 180) / currentPlayerHand.size();

        for (int i = 0; i < currentPlayerHand.size(); i++) {
            //JButton buttonCard = new JButton(String.valueOf(currentPlayerHand.get(i).getValue())); //testing
            JButton buttonCard = new JButton();

            buttonCard.setBounds(
                    (i == 0) ? 50 : 50 + offset * i,
                    30,
                    130,
                    200
            );
            buttonCard.setFocusPainted(false);

            buttonCard.putClientProperty("index", i);
            buttonCard.addMouseMotionListener(this);
            buttonCard.addMouseListener(this);

            buttonCard = setCardStyle(buttonCard, currentPlayerHand.get(i));


            playerCards.add(buttonCard);
            playerCards.setComponentZOrder(buttonCard, i);
            addButtonZ(buttonCard, i);


        }

        roundLoop();

        //add panel to view
        view.addPanel(playerCards, BorderLayout.SOUTH);
        view.repaint(); //just in case missed something
    }

    //sets color and type of card

    /**
     * setCardStyle determines what the card will look like in the UI based on CardColour and CardType and value.
     * Sets colour, sets numerical or special card, adjusts font, set styling visible
     *
     * @param buttonCard is the button we're styling based on card
     * @param card used to grab styling attributes
     * @return finalized style of the JButton as UNO card
     */
    private JButton setCardStyle(JButton buttonCard, Card card) {
        //set color
        switch (card.getColour()) {
            case RED -> buttonCard.setBackground(new Color(156, 24, 9));
            case BLUE -> buttonCard.setBackground(new Color(80, 139, 235));
            case GREEN -> buttonCard.setBackground(new Color(29, 161, 31));
            case YELLOW ->  buttonCard.setBackground(new Color(201, 196, 26));
            case WILD -> buttonCard.setBackground(Color.WHITE);
        }

        //set card type/value
        if (card.getType() == CardType.NUMBER) {
            buttonCard.setText(String.valueOf(card.getValue()));
        } else { //everything that's not a number
            buttonCard.setText(card.getType().toString().replace('_', ' '));
        }

        //setting style for text
        buttonCard.setForeground(
                (card.getColour() == CardColour.WILD) ? Color.BLACK : Color.WHITE
        );

        String text = card.getType().toString().replace('_', ' ');
        buttonCard.setFont(
                (text.length() > 8) ? new Font("Arial", Font.BOLD, 12) : new Font("Arial", Font.BOLD, 18)
        );

        //set border to be more visible
        buttonCard.setBorder(new LineBorder(Color.BLACK, 6));

        //ensure button style is visible
        buttonCard.setOpaque(true);
        buttonCard.setContentAreaFilled(true);
        buttonCard.setBorderPainted(true);

        return buttonCard;
    }

    /**
     * method to add the UNO card/JButton to a map with its z index.
     * Z index for keeping track of its layer order in view as it dynamically changes off of cursor position.
     * Use of Map to recall and needs to be cleared before every updatePlayerCards().
     *
     * @param button is the card/key
     * @param z the z index for the view layer order
     */
    public void addButtonZ(JButton button, int z) {
        prevCardZ.put(button, z);
    }

    /**
     * Initializes player classes for the GameManager, requiring player name.
     */
    private void getPlayerNames() {
        for(int i = 0; i < playerCount; i++){
            String name = JOptionPane.showInputDialog("Enter the name of the player " + (i + 1) + ": ");

            players.add(new Player(name));
        }
    }

    //get player count

    /**
     * To get initialize list of players for GameManager, require number of players before names.
     */
    private void getNumberPlayers() {
        playerCount = Integer.parseInt(JOptionPane.showInputDialog("Input number of players(2-4): "));
        while (playerCount < 2 || playerCount > 4){
            JOptionPane.showMessageDialog(null,"Invalid number. We have 2-4 players.");
            playerCount = Integer.parseInt(JOptionPane.showInputDialog("Input number of players(2-4): "));
        }
    }

    /**
     * Allows player to select a card and attempt to play it. Card will be automatically assessed.
     * If card selected is not a valid move the play button will not be enabled.
     * As well as makes the card more visible and stand out from the interactive GUI.
     *
     * @param button event handler passed from event, i.e. card/JButton pressed.
     */
    private void handleCardPressed(JButton button) {
        JPanel playerCards = getPlayerCards(); //get player panel

        if (selectedCard == null) { //no card select (keep raised)
            selectedCard = button;
            playerCards.setComponentZOrder(selectedCard, 0);
            button.setLocation(button.getX(), button.getY() - 20);

        } else if (selectedCard == button) { //same card (deselect)
            playerCards.setComponentZOrder(selectedCard, prevCardZ.get(selectedCard));
            selectedCard.setLocation(selectedCard.getX(), selectedCard.getY() + 20);

            selectedCard = null;

        } else { //another card selected (deselect current then select other)
            playerCards.setComponentZOrder(button, prevCardZ.get(selectedCard));
            selectedCard.setLocation(selectedCard.getX(), selectedCard.getY() + 20);

            selectedCard = button;

            playerCards.setComponentZOrder(selectedCard, 0);
            button.setLocation(button.getX(), button.getY() - 20);
        }

        if (selectedCard != null) {
            playableCard(selectedCard);
        }


        playerCards.repaint();
    }

    /**
     * Extra logic for handleCardPressed method for enabling or disabling play button if card is playable.
     *
     * @param selectedCard selected button from event.
     */
    private void playableCard(JButton selectedCard) {
        Card card = currentPlayerHand.get((int) selectedCard.getClientProperty("index")); //make this a function to call for all others
        if (gameManager.checkvalidMove(card)) {
            play.setEnabled(true);
        } else {
            play.setEnabled(false);
        }
        view.repaint();
    }

    /**
     * Handles when cursor is above card/JButton, called when mouse listener.
     * Will reset previous card/JButton to original state then update the newest one
     * with the highest z order for layers and shift it up.
     *
     * @param buttonCard the player will see interacting with.
     */
    private void handleHover(JButton buttonCard) {
        if (hoveredButton != buttonCard) {
            resetHover();
            hoveredButton = buttonCard;

            JPanel playerCards = getPlayerCards(); //get player panel
            playerCards.setComponentZOrder(buttonCard, 0);

            buttonCard.setLocation(buttonCard.getX(), buttonCard.getY() - 10);
            playerCards.repaint();
        }
    }

    /**
     * Opposite of handleHover method. Put previous hovered card back to original state and z order for the
     * component layer.
     */
    private void resetHover() {
        if (hoveredButton != null) {
            JPanel playerCards = getPlayerCards();
            hoveredButton.setLocation(hoveredButton.getX(), hoveredButton.getY() + 10);

            Integer z = prevCardZ.get(hoveredButton);
            if (z != null) {
                playerCards.setComponentZOrder(hoveredButton, z);
            }

            hoveredButton = null;
            playerCards.repaint();
        }
    }

    /**
     * Gets the player JPanel for the interactive card/JButton view.
     *
     * @return JPanel to edit/remove/add components.
     */
    private JPanel getPlayerCards() {
        return view.getPlayerCards();
    }

    /**
     * Plays the card the player selected after pressing the play button.
     * Goes through different logic if it is a wild card as it requires user input for its color.
     */
    private void playCard() {

        if (selectedCard == null) {
            JOptionPane.showMessageDialog(null, "No card selected.");
        } else {
            Card card = currentPlayerHand.get((int) selectedCard.getClientProperty("index"));

            //wild requires input so controller has to take care of it
            if (card.getType() == CardType.WILD || card.getType() == CardType.WILD_DRAW_TWO) {
                handleWildCard(card);
                currentPlayerHand.remove(card);
            } else {
                gameManager.playCard(card);
                currentPlayerHand.remove(card); //remove card played from list, never implemented in game manager?
            }
        }

    }

    /**
     * Update view 3 major components. Most recurring piece of code.
     */
    public void updateView() {
        updatePlayerCards();
        updateCurrentPlayer();
        updateDiscardPile();
    }

    /**
     * When cursor leaves area over card/JButton.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent event) {
        resetHover();
    }

    /**
     * Implemented for whenever a UNO/JButton Card is pressed, sends logic to handleCardPressed method.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        JButton buttonCard = (JButton) event.getSource(); //get button source

        handleCardPressed(buttonCard);

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

        handleHover(buttonCard);
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
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        if (button.getText().equals("Quit")) {
            System.exit(0);

        } else if (button.getText().equals("Play")) {
            System.out.println("play called");
            playCard();

            updateView();

        } else if (button.getText().equals("Draw")) {
            System.out.println("draw called");

            if (!drawCard && (!(gameManager.getCurrentPlayer().hasPlayableCard(gameManager.topDiscard())))) {
                drawCard = true;
                gameManager.drawCard();
                updatePlayerCards();
            }
        }
    }

    void main(String[] args) {
        //UnoView unoView = new UnoView();
        //unoView.setVisible(true);
        //Controller controller = new Controller();
    }
    
}
