import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {

    private Map<JButton, Integer> prevCardZ;
    private UnoView view;
    private int playerCount;
    private ArrayList<Player> players;
    private GameManager gameManager;

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
        List<Card> currentPlayerHand = currentPlayer.gethand();

        JPanel playerCards = view.getPlayerCards();
       
        int offset = (playerCards.getWidth() - 180) / currentPlayerHand.size();

        for (int i = 0; i < players.size(); i++) {
            JButton buttonCard = new JButton(String.valueOf(currentPlayerHand.get(i).getValue())); //testing



            buttonCard.setBounds(((i == 1) ? 50 : 50 + offset * i), 10, 130, 200);
            buttonCard.setFocusPainted(false);
        }
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
}
