import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private void initializeControls() {
        System.out.println("Initializing controls called");
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

    public void handleWildCard(Card card) {
        System.out.println("handleWildCard called");

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

    private void roundLoop() {
        System.out.println("roundLoop called");
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

    //displays current player name
    private void updateCurrentPlayer() {
        System.out.println("updateCurrentPlayer called");
        String name = gameManager.getCurrentPlayer().getName();
        view.currentPlayerDisplay(name);
    }

    //updates the top discard pile after every state change
    private void updateDiscardPile() {
        System.out.println("updateDiscardPile called");
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

    private void resetVars() {
        prevCardZ.clear(); //reset map for updated hand
        hoveredButton = null; //reset var
        selectedCard = null; //reset var
    }

    private void updatePlayerCards() {
        System.out.println("updatePlayerCards called");
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
    private JButton setCardStyle(JButton buttonCard, Card card) {
        System.out.println("setCardStyle called");
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
        System.out.println("addButtonZ called");
        prevCardZ.put(button, z);
    }

    //add player to list and initializes their name
    private void getPlayerNames() {
        System.out.println("getPlayerNames called");
        for(int i = 0; i < playerCount; i++){
            String name = JOptionPane.showInputDialog("Enter the name of the player " + (i + 1) + ": ");

            players.add(new Player(name));
        }
    }

    //get player count
    private void getNumberPlayers() {
        System.out.println("getNumberPlayers called");
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

        handleCardPressed(buttonCard);

    }

    //handles for when a card is pressed
    private void handleCardPressed(JButton button) {
        System.out.println("handleCardPressed called");
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

    private void playableCard(JButton selectedCard) {
        System.out.println("playableCard called");
        Card card = currentPlayerHand.get((int) selectedCard.getClientProperty("index")); //make this a function to call for all others
        if (gameManager.checkvalidMove(card)) {
            play.setEnabled(true);
        } else {
            play.setEnabled(false);
        }
        view.repaint();
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
        JButton buttonCard = (JButton) event.getSource();

        handleHover(buttonCard);
    }

    //makes card hover when mouse over it
    private void handleHover(JButton buttonCard) {
        System.out.println("handleHover called");
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
        System.out.println("resetHover called");
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

    //returns panel
    private JPanel getPlayerCards() {
        System.out.println("getPlayerCards called");
        return view.getPlayerCards();
    }

    void main(String[] args) {
        //UnoView unoView = new UnoView();
        //unoView.setVisible(true);
        //Controller controller = new Controller();
    }

    private void playCard() {
        System.out.println("playCard called");

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

    public void updateView() {
        updatePlayerCards();
        updateCurrentPlayer();
        updateDiscardPile();
    }

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



}
