public class Card {
    private int suit;
    private int value;
    /*
    0 - Spade
    1 - Club
    2 - Heart
    3 - Diamond
     */
    public Card(int suit, int value){
        this.suit = suit;
        this.value = value;
    }

    public int getSuit(){
        return suit;
    }
    public int getValue(){
        if (value > 10){
            return 10;
        }
        return value;
    }

    public String toString(){
        String cardValue;
        if (value == 11){
            cardValue = "Jack";
        }
        else if (value == 12){
            cardValue = "Queen";
        }
        else if (value == 13){
            cardValue = "King";
        }
        else if (value == 1){
            cardValue = "Ace";
        }
        else{
            cardValue = String.valueOf(value);
        }
        if (suit == 0){
            return (cardValue + " of Spades");
        }
        else if (suit == 1){
            return (cardValue + " of Clubs");
        }
        else if (suit == 2){
            return (cardValue + " of Hearts");
        }
        else{
            return (cardValue + " of Diamonds");
        }
    }
}