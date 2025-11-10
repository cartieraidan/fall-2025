import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Scanner;

/**
 * The GameManager class manages the state and flow of the text-based UNO game. 
 * Right now, it handles, players, deck, discard pile, turns and the logic.
 *
 * @author Lawrence Chen
 * @version 1.0
 */
public class GameManager {
    private final List<Player> players;
    private final Deck deck;
    private final Stack<Card> discardPile;
    private int currentPlayerIndex;
    private int direction;
    private Controller controller;

    /**
     * Constructs a GameManager with a list of players
     *
     * @param players the players that are participating in the game
     */
    public GameManager(List<Player> players) {
        this.players = new ArrayList<>(players);
        this.deck = new Deck();
        this.discardPile = new Stack<>();
        this.currentPlayerIndex = 0;
        this.direction = 1;
        this.controller = controller;
    }

    /**
     * Initialize and start game (shuffle, deal initial hands, set up discard)
     */
    public void startgame() {
        deck.shuffle();
        dealInitialhands();
        Card firstCard = deck.drawCard();
        //no wild cards as first card
        while (firstCard.getType() != CardType.NUMBER) {
            deck.addCard(firstCard);
            deck.shuffle();
            firstCard = deck.drawCard();
        }
        discardPile.push(firstCard);
    }

    /**
     * Allows controller to push card to discard pile.
     * 
     * @param card being played by player
     */
    public void pushToDiscardPile(Card card) {
        discardPile.push(card);
    }

    /**
     * Deals 7 cards from the deck to each player
     */
    private void dealInitialhands() {
        //deal all players their cards
        for (Player p : players) {
            for (int i = 0; i < 7; i++) {
                p.drawCard(deck);
            }
        }

    }

    /**
     * Advances the turn to the next player
     */
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
    }

    /**
     * Attempt to play a card on behalf of the current player
     *
     * @param card the card to play
     * @return true if played successfully, false otherwise
     */
    public boolean playCard(Card card) {
        //check if the move is valid
        if (checkvalidMove(card)) {
            discardPile.push(card);
            handleActionCard(card);
            return true;
        }
        return false;
    }

    /**
     * Current player can draw one card from the deck
     */
    public void drawCard() {
        Player p = getCurrentPlayer();
        p.drawCard(deck);
    }

    /**
     * Checks if card can be played on top of discard top card
     *
     * @param card to attempt match
     * @return true if the card can be played, false otherwise
     */
    public boolean checkvalidMove(Card card) {
        return card.matches(topDiscard());
    }

    /**
     * Displays current state in the console. Top card on discard pile and the hand of the player that called.
     */
    public void displayState() {
        Player current = getCurrentPlayer();
        System.out.println("\nTop Discard: " + topDiscard());
        System.out.println(current.getName() + "'s hand: " + current.gethand());
        System.out.println("Current Player: " + getCurrentPlayer().getName());
    }


    /**
     * Handles special action cards(Reverse, Skip, Draw One, Wild, Wild Draw Two) after card has been played.
     *
     * @param card the action card played
     */
    private void handleActionCard(Card card) {
        switch (card.getType()) {
            //case for regular card
            case NUMBER -> nextTurn();
            //case for reverse card
            case REVERSE -> {
                direction *= -1;
                nextTurn();
            }
            //case for skip card
            case SKIP -> {
                nextTurn();
                nextTurn();
            }
            //case for draw_one card
            case DRAW_ONE -> {
                nextTurn();
                getCurrentPlayer().drawCard(deck);
                nextTurn();
            }
            /*
            //case for wild cards
            case WILD, WILD_DRAW_TWO -> {
                Scanner sc = new Scanner(System.in);
                String input;
                CardColour chosen = null;
                //ask user for color until valid input
                while (true) {

                    String temp = controller.handleWildCard(card);
                    input = temp.trim().toUpperCase();
                    try {
                        chosen = CardColour.valueOf(input);
                        break;
                    } catch (IllegalArgumentException e) {
                        controller.handleWildCardError();
                    }
                }



                card.setColour(chosen);
                nextTurn();
                //case for wild draw two cards
                if (card.getType() == CardType.WILD_DRAW_TWO) {
                    getCurrentPlayer().drawCard(deck);
                    getCurrentPlayer().drawCard(deck);
                    nextTurn();
                }
            }

             */
            default -> {
            }
        }
    }

    public void handleWildCard(Card card) {

    }

    /**
     * Gets the current player whose turn it is
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Gets the top card of the discard pile
     *
     * @return the top discard card
     */
    public Card topDiscard() {
        return discardPile.peek();
    }

    /**
     * Gets the list of all players in the game.
     *
     * @return list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the deck
     *
     * @return deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Gets the discard pile we are working with
     *
     * @return the discard pile
     */
    public Stack<Card> getDiscardPile() {
        return discardPile;
    }

    /**
     * Gets the current player index
     *
     * @return the index of the current player
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Gets the direction the game is played in
     *
     * @return the direction of the game state
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Sets the direction of the game state
     *
     * @param direction of the game
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Checks if the player's hands are empty
     * @return true or false depending on if hand is empty
     */
    public boolean checkEmptyHand() {
        //check every player's hand
        for (Player p : players) {
            if (p.gethand().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the round winner at the end of the round.
     * @return player with zero cards
     */
    public Player getRoundWinner() {
        for (Player p : players) {
            if (p.gethand().isEmpty()) {
                return p;
            }
        }
        return null;
    }

    /**
     * Updates the winner's score by adding the total points
     * from all other players' hands
     */
    public void updatePlayerScore() {
        Player winner = getRoundWinner();
        if (winner == null) return;

        int totalPoints = 0;
        //add up total points value
        for (Player p : players) {
            if (p != winner) {
                totalPoints += p.gethandValue();
            }
        }

        winner.addScore(totalPoints);
    }
}


