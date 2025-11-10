import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller implements MouseListener, MouseMotionListener {

    private Map<JButton, Integer> prevCardZ; //used for tracking button prev z
    private UnoView view;
    private int playerCount;
    private ArrayList<Player> players;
    private GameManager gameManager;
    private List<Card> currentPlayerHand;

    private JButton hoveredButton = null;
    private JButton selectedCard = null;

    private boolean gameOver;
    private boolean roundOver;

    public Controller() {
        view = new UnoView();
        players = new ArrayList<>();
        prevCardZ = new HashMap<>();

        getNumberPlayers();
        getPlayerNames();

        //setup game
        gameManager = new GameManager(players);
        gameManager.startgame();
        updatePlayerCards(); //updates current player hand UI
        updateDiscardPile(); //updates discard pile
        updateCurrentPlayer();

        view.setVisible(true);
        
        gameOver = false;
        roundOver = false;
        
        gameLoop();

    }

    private void gameLoop() {
        while (!gameOver) {
            
        }
    }

    //displays current player name
    private void updateCurrentPlayer() {
        String name = players.get(gameManager.getCurrentPlayerIndex()).getName();
        view.currentPlayerDisplay(name);
    }

    //updates the top discard pile after every state change
    private void updateDiscardPile() {
        Card topCard = gameManager.topDiscard();
        JButton discard = new JButton();
        discard.setEnabled(false);
        discard.setFocusPainted(false);

        discard = setCardStyle(discard, topCard);

        discard.setBounds(
                300,
                100,
                130,
                200
        );


        view.addCenterCard(discard);
    }

    private void updatePlayerCards() {
        Player currentPlayer = players.get(gameManager.getCurrentPlayerIndex());
        currentPlayerHand = currentPlayer.gethand();

        JPanel playerCards = getPlayerCards();
        prevCardZ.clear(); //reset map for updated hand

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

        //add panel to view
        view.addPanel(playerCards, BorderLayout.SOUTH);
        view.repaint(); //just in case missed something
    }

    //sets color and type of card
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

    //whenever card/button played need to remove it from the list or just clear to map itself with fresh
    public void addButtonZ(JButton button, int z) {
        prevCardZ.put(button, z);
    }

    //add player to list and initializes their name
    private void getPlayerNames() {
        for(int i = 0; i < playerCount; i++){
            String name = JOptionPane.showInputDialog("Enter the name of the player " + (i + 1) + ": ");

            players.add(new Player(name));
        }
    }

    //get player count
    private void getNumberPlayers() {
        playerCount = Integer.parseInt(JOptionPane.showInputDialog("Input number of players(2-4): "));
        while (playerCount < 2 || playerCount > 4){
            JOptionPane.showMessageDialog(null,"Invalid number. We have 2-4 players.");
            playerCount = Integer.parseInt(JOptionPane.showInputDialog("Input number of players(2-4): "));
        }
    }

    //implement this method for when a card is pressed
    @Override
    public void mouseClicked(MouseEvent event) {
        JButton buttonCard = (JButton) event.getSource(); //get button source
        //might not need this
        int index = (int) buttonCard.getClientProperty("index"); //get the index of button for card in hand
        Card card = currentPlayerHand.get(index); //get card selected

        handleCardPressed(buttonCard);

    }

    //handles for when a card is pressed
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

        playerCards.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        resetHover();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent event) {
        //get current card hovering over
        //!!!!!!might only need this for selecting the card lol
        JButton buttonCard = (JButton) event.getSource();
        int index = (int) buttonCard.getClientProperty("index");
        Card card = currentPlayerHand.get(index);

        handleHover(buttonCard);
    }

    //makes card hover when mouse over it
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

    //makes card not hover
    private void resetHover() {
        if (hoveredButton != null) {
            JPanel playerCards = getPlayerCards();
            hoveredButton.setLocation(hoveredButton.getX(), hoveredButton.getY() + 10);
            playerCards.setComponentZOrder(hoveredButton, prevCardZ.get(hoveredButton));

            hoveredButton = null;
            playerCards.repaint();
        }
    }

    //returns panel
    private JPanel getPlayerCards() {
        return view.getPlayerCards();
    }

    void main(String[] args) {
        //UnoView unoView = new UnoView();
        //unoView.setVisible(true);
        //Controller controller = new Controller();
    }
}
