import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SequenceView extends JFrame {

    private JButton[][] buttons = new JButton[4][4];
    private JPanel board;

    public SequenceView(ActionListener listener, Sequence list) {
        setTitle("Sequence View");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setBackground(Color.GREEN);

        board = new JPanel(new GridLayout(4, 4));

        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Integer label = list.getIndex(count);
                count++;
                buttons[i][j] = new JButton(label.toString());
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                buttons[i][j].setActionCommand(label.toString()); //can replace with int???
                buttons[i][j].addActionListener(listener);
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(Color.GREEN);
                board.add(buttons[i][j]);

            }
        }



        board.setBackground(Color.GREEN);
        add(board);
        setVisible(true);
    }

    public JButton[][] getButtons() {
        return buttons;
    }

    /**
    public static void main(String[] args) {
        //SequenceView view = new SequenceView();
        Sequence sequence = new Sequence();

        for (int i = 0; i <= 15; i++) {
            System.out.println(sequence.getIndex(i));
        }
    }
     */
}
