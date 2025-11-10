import javax.swing.*;
import java.util.Map;

public class Controller {

    private Map<JButton, Integer> prevCardZ;
    private UnoView view;
    private int playerCount;

    public Controller() {
        view = new UnoView();
        getNumberPlayers();
        view.setVisible(true);

    }

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
