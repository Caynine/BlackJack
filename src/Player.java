import java.util.ArrayList;

public class Player {
    private String name;
    double balance;
    ArrayList<Card> hand;
    int handValue;
    boolean busted;
    boolean hasBlackjack;

    public Player(String name, double money){
        this.name = name;
        this.balance = money; //start money
        hand = new ArrayList<>();
        handValue = 0;
        busted = false;
        hasBlackjack = false;
    }

    public String getName(){
        return name;
    }
}
