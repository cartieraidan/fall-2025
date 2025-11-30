import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Controller  implements ActionListener {

    private SequenceView view;
    private Sequence seq;

    private ArrayList<Integer> playerSeq;

    public Controller() {
        seq = new Sequence();
        view = new SequenceView(this, seq);

        playerSeq = new ArrayList<>();
    }

    public JButton[][] getButtons() {
        return view.getButtons();
    }

    private boolean addToSeq(Integer i) {
        if (playerSeq.isEmpty() && i != 1) {
            return false;
        }

        if (playerSeq.isEmpty()) {
            playerSeq.add(i);
            //System.out.println("worked");
            return true;
        } else if (i - playerSeq.getLast() == 1) {
            playerSeq.add(i);
            //System.out.println("worked2");
            return true;
        } else {
            return false;
        }
    }

    public boolean doneGame() {
        return playerSeq.size() == 16;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();

        //System.out.println(button.getText());
        boolean addSeq = this.addToSeq(Integer.valueOf(button.getText()));
        if (addSeq) {
            button.setEnabled(false);

            button.updateUI();
        } else {
            JOptionPane.showMessageDialog(null, "Game Over");
            System.exit(0);
        }

        if (this.doneGame()) {
            JOptionPane.showMessageDialog(null, "Congrats you won!");
            System.exit(0);
        }

        //System.out.println("int value: " + Integer.valueOf(button.getText()));



    }

    public static void main(String[] args) {
        Controller controller = new Controller();

        /** for test unit
        JButton[][] buttons = controller.getButtons();

        ActionEvent test = new ActionEvent(buttons[0][0], 1, "");
        controller.actionPerformed(test);
         */
    }
}
