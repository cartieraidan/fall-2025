
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class UnoView extends JFrame {

    private JPanel playerCards;
    private JPanel topPanel;

    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel centerPanel;

    private JPanel player2;


    private JButton play;
    private JButton draw;

    GameManager gameManager;
    Controller controller;

    /**
     * Constructor that initializes most of all game panel.
     */
    public UnoView() {

        setTitle("Uno");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false);
        setSize(1000, 800);


        //Gets the card png by getting file name from card
        String cardFileName = "/images/background/player-cards.png";
        String centerFileName = "/images/background/center-card.png";
        String topFileName = "/images/background/top.png";

        //Get image icon for card
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(cardFileName)));
        Image cardBack = icon.getImage();

        ImageIcon icon1 = new ImageIcon(Objects.requireNonNull(getClass().getResource(centerFileName)));
        Image centerBack = icon1.getImage();

        ImageIcon icon2 = new ImageIcon(Objects.requireNonNull(getClass().getResource(topFileName)));
        Image topBack = icon2.getImage();




        //only for testing purposes, will separate
        playerCards = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(cardBack, 0, 0, getWidth(), getHeight(), this);
            }
        };
        //playerCards.setBackground(Color.LIGHT_GRAY);
        playerCards.setPreferredSize(new Dimension(800, 300));
        playerCards.setLayout(null);

        topPanel = new JPanel(new GridLayout(1, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(topBack, 0, 0, getWidth(), getHeight(), this);
            }
        };;
        //topPanel.setBackground(Color.lightGray);
        topPanel.setPreferredSize(new Dimension(800, 200));

        centerPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(centerBack, 0, 0, getWidth(), getHeight(), this);
            }
        };
        centerPanel.setPreferredSize(new Dimension(400, 400));
        add(centerPanel, BorderLayout.CENTER);

        leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        rightPanel = new JPanel();
        rightPanel.setOpaque(false);

        topPanel.add(leftPanel);
        topPanel.add(rightPanel);

        add(topPanel,  BorderLayout.NORTH);


        player2 =  new JPanel();
        player2.setBackground(Color.GREEN);
        player2.setPreferredSize(new Dimension(800, 100));



    }

    /**
     * Game operation buttons.
     */
    public void addControlButtons() {
        JPanel panel = getRightPanel(); //JPanel for game controls

        //quit JButton
        String quitFileName = "/images/background/quit-button.png";
        ImageIcon quitIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(quitFileName)));

        JButton quit = new JButton(quitIcon);
        quit.setBorderPainted(false);
        quit.setContentAreaFilled(false);
        quit.setFocusPainted(false);

        quit.addActionListener(controller);

        quit.setText("Quit");
        quit.setPreferredSize(new Dimension(
                quitIcon.getIconWidth(),
                quitIcon.getIconHeight()
        ));



        panel.add(quit);

        //play JButton
        String playFileName = "/images/background/play-button.png";
        ImageIcon playIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(playFileName)));

        play = new JButton(playIcon);
        play.setBorderPainted(false);
        play.setContentAreaFilled(false);
        play.setFocusPainted(false);

        play.addActionListener(controller);

        play.setText("Play");
        play.setPreferredSize(new Dimension(
                playIcon.getIconWidth(),
                playIcon.getIconHeight()
        ));

        panel.add(play);

        //draw JButton
        String drawFileName = "/images/background/draw-button.png";
        ImageIcon drawIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(drawFileName)));

        draw = new JButton(drawIcon);
        draw.setBorderPainted(false);
        draw.setContentAreaFilled(false);
        draw.setFocusPainted(false);

        draw.addActionListener(controller);
        draw.setEnabled(false);

        draw.setText("Draw");
        draw.setPreferredSize(new Dimension(
                drawIcon.getIconWidth(),
                drawIcon.getIconHeight()
        ));

        panel.add(draw);
    }

    /**
     * Method for adding discard card on pile.
     *
     * @param button Card player played.
     */
    public void addCenterCard(JButton button) {
        centerPanel.removeAll();
        centerPanel.add(button);
        revalidate();
        repaint();
    }

    /**
     * Method for adding panel to JFrame.
     *
     * @param panel JPanel adding to frame.
     * @param layout Location of frame.
     */
    public void addPanel(JPanel panel, String layout) {
        add(panel, layout);
        pack();
    }

    /**
     * Panel that is the main player cards container.
     *
     * @return JPanel of current player container.
     */
    public JPanel getPlayerCards() {
        return playerCards;
    }

    /**
     * Get Panel for game controls.
     *
     * @return JPanel.
     */
    public JPanel getRightPanel() {
        return rightPanel;
    }

    /**
     * Get Play button.
     *
     * @return JButton of play.
     */
    public JButton getPlayButton() {
        return play;
    }

    /**
     * Get Draw button.
     *
     * @return JButton of draw.
     */
    public JButton getDrawButton() {
        return draw;
    }

    /**
     * Updates the banner for player name.
     *
     * @param name String name of character.
     */
    public void currentPlayerDisplay(String name) {
        JLabel playerName = new JLabel("Player: " + name);
        playerName.setFont(new Font("Arial", Font.BOLD, 14));
        playerName.setForeground(Color.black);

        leftPanel.removeAll(); //clear JLabel
        leftPanel.add(playerName);

        repaint();
    }

    /**
     * Updates the banner for player score.
     *
     * @param score Int of score player.
     */
    public void addUpdateScore(int score) {
        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setForeground(Color.black);

        leftPanel.add(scoreLabel);

        repaint();
    }

    public void subscribe (GameManager gameManager) {

        this.gameManager = gameManager;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void addButtonCard(JButton buttonCard) {
        buttonCard.addMouseMotionListener(controller);
        buttonCard.addMouseListener(controller);
    }
}
