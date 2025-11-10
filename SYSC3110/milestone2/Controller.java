import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller implements MouseListener, MouseMotionListener {

    private Map<JButton, Integer> prevCardZ;
    private UnoView view;
    private int playerCount;
    private ArrayList<Player> players;
    private GameManager gameManager;
    private List<Card> currentPlayerHand;

    private JButton hoveredButton = null;

    private boolean gameOver;
    private boolean roundOver;

    public Controller() {
        view = new UnoView();
        players = new ArrayList<>();

        getNumberPlayers();
        getPlayerNames();

        //setup game
        gameManager = new GameManager(players);
        gameManager.startgame();
        updatePlayerCards();

        //need to use this topDiscard() to see top of discard for middle




        view.setVisible(true);

    }

    private void updatePlayerCards() {
        Player currentPlayer = players.get(gameManager.getCurrentPlayerIndex());
        currentPlayerHand = currentPlayer.gethand();

        JPanel playerCards = getPlayerCards();

        int offset = (playerCards.getPreferredSize().width - 180) / currentPlayerHand.size();
        
        for (int i = 0; i < currentPlayerHand.size(); i++) {
            JButton buttonCard = new JButton(String.valueOf(currentPlayerHand.get(i).getValue())); //testing
            
            buttonCard.setBounds(((i == 0) ? 50 : 50 + offset * i), 10, 130, 200);
            buttonCard.setFocusPainted(false);

            buttonCard.putClientProperty("index", i);
            buttonCard.addMouseMotionListener(this);
            buttonCard.addMouseListener(this);

            playerCards.add(buttonCard);
        }

        //add panel to view
        view.addPanel(playerCards, BorderLayout.SOUTH);
        view.repaint(); //just in case missed something
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


    void main(String[] args) {
        //UnoView unoView = new UnoView();
        //unoView.setVisible(true);
        //Controller controller = new Controller();
    }

    //implement this method for when a card is pressed
    @Override
    public void mouseClicked(MouseEvent e) {

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
            hoveredButton = null;
            playerCards.repaint();
        }
    }

    //returns panel
    private JPanel getPlayerCards() {
        return view.getPlayerCards();
    }
}
