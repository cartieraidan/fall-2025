import java.util.*;

/**
 * A poker hand is a list of cards, which can be of some "kind" (pair, straight, etc.)
 * 
 */
public class Hand implements Comparable<Hand> {

    public enum Kind {HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, 
        FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH}

    private final List<Card> cards;

    /**
     * Create a hand from a string containing all cards (e,g, "5C TD AH QS 2D")
     */
    public Hand(String c) {
        cards = new ArrayList<>();

        String[] cardsList = c.split(" ");
        for (String card : cardsList) {
           cards.add(new Card(card));
        }

        /* for testing purposes
        for (Card card : cards) {
            System.out.println(card.getRank());
            System.out.println(card.getSuit());
            System.out.println("next card");
        }

         */
    }
    
    /**
     * @returns true if the hand has n cards of the same rank
	 * e.g., "TD TC TH 7C 7D" returns True for n=2 and n=3, and False for n=1 and n=4
     */
    protected boolean hasNKind(int n) {
        HashMap<Card.Rank, Integer> score = new HashMap<>();
        for (Card card : cards) {
            Card.Rank rank = card.getRank();

            if (score.get(rank) == null) {
                score.put(rank, 1);
            } else {
                Integer value = score.get(rank);
                score.replace(rank, 1 + value);
            }

        }

        for (Integer total : score.values()) {

            if (total == n) {
                return true;
            }
        }

        return false;
    }
    
    /**
	 * Optional: you may skip this one. If so, just make it return False
     * @returns true if the hand has two pairs
     */
    public boolean isTwoPair() {
        HashMap<Card.Rank, Integer> score = new HashMap<>();
        for (Card card : cards) {
            Card.Rank rank = card.getRank();

            if (score.get(rank) == null) {
                score.put(rank, 1);
            } else {
                Integer value = score.get(rank);
                score.replace(rank, 1 + value);
            }

        }

        int twoPairCount = 0;
        for (Integer total : score.values()) {
            if (total == 2) {
                twoPairCount += 1;
            }
        }

        return twoPairCount == 2;

    }   
    
    /**
     * @returns true if the hand is a straight 
     */
    public boolean isStraight() {
        ArrayList<Integer> rankIndex = new ArrayList<>();

        for (Card card : cards) {
            rankIndex.add(card.getRank().ordinal());
        }
        Collections.sort(rankIndex);

        //for ACE and DEUCE situation where it is ACE DEUCE THREE....
        //ACE index in enum is 12 but how I calculate it I need it to be a value that fits the code
        //if we change it and there is no straight it would never happen with a ACE and DEUCE
        // so we can change it for that situation
        if (rankIndex.getFirst() == 0 && rankIndex.getLast() == 12) {
            rankIndex.set(rankIndex.indexOf(rankIndex.getLast()), -1);
        }

        Collections.sort(rankIndex);

        //Ensures no duplicates in list so if some numbers added up to 4 it would not trigger
        for (Integer number : rankIndex) {
            if (Collections.frequency(rankIndex, number) > 1) {
                return false;
            }
        }

        //Requires a sorted list
        Integer prevCard = null;
        int count = 0;
        for (Integer currentCard : rankIndex) {

            if (prevCard != null) {
                count += currentCard - prevCard;
            }
            prevCard = currentCard;

        }

        return count == 4;
    }
    
    /**
     * @returns true if the hand is a flush
     */
    public boolean isFlush() {
        HashMap<Card.Suit, Integer> score = new HashMap<>();
        for (Card card : cards) {
            Card.Suit suit = card.getSuit();

            if (score.get(suit) == null) {
                score.put(suit, 1);
            } else {
                Integer value = score.get(suit);
                score.replace(suit, 1 + value);
            }

        }

        for (Integer total : score.values()) {
            if (total == 5) {
                return true;
            }
        }

        return false;
    }
    
    @Override
    public int compareTo(Hand h) {
        //hint: delegate!
		//and don't worry about breaking ties

        Integer handOne = this.kind().ordinal();
        Integer handTwo = h.kind().ordinal();

        if (handTwo > handOne) return -1;
        else if (handTwo.equals(handOne)) return 0;
        else return 1;

    }
    
    /**
	 * This method is already implemented and could be useful! 
     * @returns the "kind" of the hand: flush, full house, etc.
     */
    public Kind kind() {
        if (isStraight() && isFlush()) return Kind.STRAIGHT_FLUSH;
        else if (hasNKind(4)) return Kind.FOUR_OF_A_KIND; 
        else if (hasNKind(3) && hasNKind(2)) return Kind.FULL_HOUSE;
        else if (isFlush()) return Kind.FLUSH;
        else if (isStraight()) return Kind.STRAIGHT;
        else if (hasNKind(3)) return Kind.THREE_OF_A_KIND;
        else if (isTwoPair()) return Kind.TWO_PAIR;
        else if (hasNKind(2)) return Kind.PAIR; 
        else return Kind.HIGH_CARD;
    }

    public static void main(String[] args) {
        //Hand sf = new Hand("6C 7C 8C 9C TC");
        //Hand sf = new Hand("TD TC TH 7C 7D");
        //Hand sf = new Hand("TD 5C 4H 7C 7D");
        //Hand sf = new Hand("5D 6D 7D 8D 9D");
        //System.out.println(sf.hasNKind(3));
        //System.out.println(sf.isTwoPair());
       // System.out.println(sf.isStraight());
        //System.out.println(sf.isFlush());
        /*
        Hand fk = new Hand("9D 9H 9S 9C 7D");
        Hand fh = new Hand("TD TC TH 7C 7D");

        System.out.println(fh.kind());
        System.out.println(fk.kind());
        System.out.println(fh.compareTo(fk));

        */
        Hand pair = new Hand("QD KD AC 2H 3S");
        System.out.println(pair.kind());
        //System.out.println(pair.hasNKind(2));
        System.out.println(Hand.Kind.PAIR);




    }

}
