import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class UnoView extends JFrame {


    private ArrayList<JButton> cards;
    private JButton hoveredCard = null;
    private JPanel playerCards;

    private JPanel player2;
    private JPanel player3;
    private JPanel player4;

    public UnoView() {
        cards = new ArrayList<JButton>();

        setTitle("Uno");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);


        //only for testing purposes, will separate
        playerCards = new JPanel();
        playerCards.setBackground(Color.RED);
        playerCards.setPreferredSize(new Dimension(800, 300));
        playerCards.setLayout(null);



        player2 =  new JPanel();
        player2.setBackground(Color.GREEN);
        player2.setPreferredSize(new Dimension(800, 100));

        player3 =  new JPanel();
        player3.setBackground(Color.BLUE);
        player3.setPreferredSize(new Dimension(100, 400));

        player4 =  new JPanel();
        player4.setBackground(Color.YELLOW);
        player4.setPreferredSize(new Dimension(100, 400));

        /*
       for (int i = 0; i < 10; i++) {
           JButton card = new JButton("card" + i);
           card.setBounds(60 * i, 10, 130, 200);
           card.setFocusPainted(false);

           card.addMouseMotionListener(new MouseAdapter() {

               @Override
               public void mouseMoved(MouseEvent event) {
                   handleHover(card);

               }
           });

           card.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseExited(MouseEvent event) {
                   resetHover();
               }
           });

           cards.add(card);
           playerCards.add(card);
       }


         */

        //add(playerCards, BorderLayout.SOUTH);



        //setVisible(true);
    }

    public void addPanel(JPanel panel, String layout) {
        add(panel, layout);
        add(player2, BorderLayout.NORTH);
        add(player3, BorderLayout.WEST);
        add(player4, BorderLayout.EAST);
        pack();
    }

    public JPanel getPlayerCards() {
        return playerCards;
    }

    private void handleHover(JButton card) {


        if (hoveredCard != card) {
            resetHover();
            hoveredCard = card;

            playerCards.setComponentZOrder(card, 0);
            card.setLocation(card.getX(), card.getY() - 10);
            playerCards.repaint();
        }

    }

    private void resetHover() {
        if (hoveredCard != null) {
            //setComponentZOrder() here is where need to reset properly
            hoveredCard.setLocation(hoveredCard.getX(), hoveredCard.getY() + 10);
            hoveredCard = null;
            playerCards.repaint();
        }
    }


}
