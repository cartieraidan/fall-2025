import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UnoView extends JFrame {

    JButton card1,card2,card3,card4,card5,card6,card7;

    public UnoView() {
        setTitle("Uno");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 800);

        //only for testing purposes, will separate
        JPanel playerCards = new JPanel();
        playerCards.setBackground(Color.RED);
        playerCards.setPreferredSize(new Dimension(500, 300));
        playerCards.setLayout(null);

        JPanel player2 =  new JPanel();
        player2.setBackground(Color.GREEN);
        player2.setPreferredSize(new Dimension(500, 100));

        JPanel player3 =  new JPanel();
        player3.setBackground(Color.BLUE);
        player3.setPreferredSize(new Dimension(100, 400));

        JPanel player4 =  new JPanel();
        player4.setBackground(Color.YELLOW);
        player4.setPreferredSize(new Dimension(100, 400));

        card1 = new JButton("Card 1");
        card2 = new JButton("Card 2");
        card3 = new JButton("Card 3");

        card1.setFocusPainted(false); //when selected no indication that is selected?
        card1.addMouseListener(new MouseAdapter() {
            int prevZ;

            @Override
            public void mouseEntered(MouseEvent event) {
                prevZ = playerCards.getComponentZOrder(card1); //gets previous Z, based on order in array
                playerCards.setComponentZOrder(card1,0);
                card1.setBounds(card1.getX(), card1.getY() - 10, card1.getWidth(), card1.getHeight());
                playerCards.repaint();
                card1.setBackground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent event) {
                playerCards.setComponentZOrder(card1,prevZ);
                card1.setBackground(null);
                card1.setBounds(card1.getX(), card1.getY() + 10, card1.getWidth(), card1.getHeight());
                playerCards.repaint();
            }
        });

        card2.setFocusPainted(false);
        card2.addMouseListener(new MouseAdapter() {
            int prevZ;

            @Override
            public void mouseEntered(MouseEvent event) {
                prevZ = playerCards.getComponentZOrder(card2); //gets previous Z, based on order in array
                playerCards.setComponentZOrder(card2,0);
                card2.setBounds(card2.getX(), card2.getY() - 10, card2.getWidth(), card2.getHeight());
                playerCards.repaint();
                card2.setBackground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent event) {
                playerCards.setComponentZOrder(card2,prevZ);
                card2.setBackground(null);
                card2.setBounds(card2.getX(), card2.getY() + 10, card2.getWidth(), card2.getHeight());
                playerCards.repaint();
            }
        });

        card3.setFocusPainted(false);
        card3.addMouseListener(new MouseAdapter() {
            int prevZ;

            @Override
            public void mouseEntered(MouseEvent event) {
                prevZ = playerCards.getComponentZOrder(card3); //gets previous Z, based on order in array
                playerCards.setComponentZOrder(card3,0);
                card3.setBounds(card3.getX(), card3.getY() - 10, card3.getWidth(), card3.getHeight());
                playerCards.repaint();
                card3.setBackground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent event) {
                playerCards.setComponentZOrder(card3,prevZ);
                card3.setBackground(null);
                card3.setBounds(card3.getX(), card3.getY() + 10, card3.getWidth(), card3.getHeight());
                playerCards.repaint();
            }
        });

        card1.setBounds(0, 10, 130, 200);
        card2.setBounds(40, 10, 130, 200);
        card3.setBounds(80, 10, 130, 200);

        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);

        add(playerCards, BorderLayout.SOUTH);
        add(player2, BorderLayout.NORTH);
        add(player3, BorderLayout.WEST);
        add(player4, BorderLayout.EAST);

        pack();
        setVisible(true);
    }

    void main(String[] args) {
        UnoView unoView = new UnoView();
    }
}
