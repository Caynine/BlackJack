import java.util.Random;
import java.util.LinkedList;
import java.util.Queue;
public class Shoe {
    int numDecks;
    final int numCards = 52*numDecks;
    Queue<Card> cards = new LinkedList<>();
    /*
    0 - Spade
    1 - Club
    2 - Heart
    3 - Diamond
     */
    public Shoe(int numDecks){
        int emptyIndices = 52*numDecks;
        Random rand = new Random();
        while (emptyIndices > 0) {
            int suit = rand.nextInt(4);
            int value = rand.nextInt(14);
            int count = 0;
            for (Card card : cards){
                if (card.getSuit() == suit && card.getValue() == value){
                    count++;
                }
            }
            if (count <= numDecks && value != 0){
                Card card = new Card(suit, value);
                cards.add(card);
                emptyIndices--;
            }
        }
    }

    public Card dealCard(){
        return cards.poll();
    }
}
