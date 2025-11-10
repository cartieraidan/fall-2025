import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class UnoView extends JFrame {

    private JPanel playerCards;
    private JPanel topPanel;

    private JPanel leftPanel;
    private JPanel rightPanel;

    private JPanel player2;
    private JPanel player3;
    private JPanel player4;

    public UnoView() {

        setTitle("Uno");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);


        //only for testing purposes, will separate
        playerCards = new JPanel();
        playerCards.setBackground(Color.RED);
        playerCards.setPreferredSize(new Dimension(800, 300));
        playerCards.setLayout(null);

        topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setBackground(Color.lightGray);
        topPanel.setPreferredSize(new Dimension(800, 200));

        leftPanel = new JPanel();
        rightPanel = new JPanel();

        topPanel.add(leftPanel);
        topPanel.add(rightPanel);

        add(topPanel,  BorderLayout.NORTH);


        player2 =  new JPanel();
        player2.setBackground(Color.GREEN);
        player2.setPreferredSize(new Dimension(800, 100));

        player3 =  new JPanel();
        player3.setBackground(Color.BLUE);
        player3.setPreferredSize(new Dimension(100, 400));

        player4 =  new JPanel();
        player4.setBackground(Color.YELLOW);
        player4.setPreferredSize(new Dimension(100, 400));


    }

    public void addCenterCard(JButton button) {
        JPanel center = new JPanel(null);
        center.add(button);

        add(center, BorderLayout.CENTER);

        pack();
        repaint();
    }

    public void addPanel(JPanel panel, String layout) {
        add(panel, layout);
        //add(player2, BorderLayout.NORTH);
        add(player3, BorderLayout.WEST);
        add(player4, BorderLayout.EAST);
        pack();
    }

    public JPanel getPlayerCards() {
        return playerCards;
    }

    public JPanel getRightPanel() {
        return rightPanel;
    }

    public void currentPlayerDisplay(String name) {
        JLabel playerName = new JLabel("Player: " + name);
        playerName.setFont(new Font("Arial", Font.BOLD, 14));
        playerName.setForeground(Color.black);

        leftPanel.add(playerName);

        repaint();
    }


}
