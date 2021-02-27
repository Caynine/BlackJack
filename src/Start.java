import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Start {
    public static void main(String[] args){
        double startMoney = 1000;
        boolean playStatus = true;
        ArrayList<String> playersInfo = new ArrayList<String>(); //info for all players
        Shoe shoe = new Shoe(1);
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter your name:");
        String userName = keyboard.nextLine().toUpperCase();
        File originalFile = new File("blackjackInfo.txt");
        File tempFile = new File("blackjackInfotemp.txt");
        try{ //reading file for player balance
            BufferedReader file = new BufferedReader(new FileReader(originalFile));
            String [] data = file.readLine().split(",");
            //String [] info = data.split(",");
            for (int i = 0; i < data.length; i++){
                if (data[i].equals(userName)){
                    startMoney = Integer.parseInt(data[i+1]);
                    break;
                    //i++;
                }
                else{
                    playersInfo.add(data[i]);
                }
            }
            System.out.println(startMoney);
            file.close();
        }
        catch (IOException e){
            System.out.println("Cannot read file contents");
            e.printStackTrace();
        }
        Player player = new Player(userName.toUpperCase(), startMoney);
        Player dealer = new Player(null, 0);
        System.out.println("\nWelcome back to Blackjack " + player.getName() + "!\n");

        //game loop
        while (playStatus == true){
            dealer.hand.clear();
            player.hand.clear();
            player.handValue = 0;
            dealer.handValue = 0;
            player.hasBlackjack = false;
            dealer.hasBlackjack = false;
            player.busted = false;
            dealer.busted = false;

            //betting
            System.out.println("\n\nYour balance is: $" + player.balance + "\nHow much would you like to bet?");
            double bet = Integer.parseInt(keyboard.nextLine());
            if (player.balance >= bet)
                player.balance -= bet;
            else {
                System.out.println("\nEnter an amount you can afford.\nHere is a practise hand.");
                bet = 0;
            }

            //deal cards
            for (int i = 0; i < 4; i++){
                if (i == 0 || i == 2){
                    player.hand.add(shoe.dealCard());
                }
                else{
                    dealer.hand.add(shoe.dealCard());
                }
            }
            System.out.println("\nYour hand is:");
            printHand(player);
            updateHandValue(player);
            if (player.handValue == 21 && dealer.handValue != 21){
                System.out.println("You got a blackjack!");
                player.hasBlackjack = true;
            }
            else
                System.out.println("Your hand value is: " + player.handValue);
            System.out.println("\nDealer's up card:");
            System.out.println(dealer.hand.get(0).toString());
            updateHandValue(dealer);


            //check insurance
            if (player.balance > bet*0.5 && dealer.hand.get(0).getValue() == 1) {
                String getInsurance;
                boolean insuranceBought = false;
                System.out.println("Would you like to purchase insurance?");
                getInsurance = keyboard.nextLine();
                if (getInsurance.equals("yes")) {
                    player.balance -= bet*0.5;
                    insuranceBought = true;
                    System.out.println("Insurance bought.");
                }
                if (dealer.handValue == 21){
                    if (insuranceBought){
                        player.balance += bet*1.5;
                        System.out.println("The dealer has a blackjack, you were payed: $" + bet*1.5);
                    }
                    else{
                        System.out.println("The dealer has a blackjack. You lose this hand.");
                        dealer.hasBlackjack = true;
                    }
                }
                else{
                        System.out.println("The dealer does not have a blackjack.");
                }
            }

            //action
            while (player.hasBlackjack == false && dealer.hasBlackjack == false){
                System.out.println("\nWhat would you like to do?\n1 - Hit\n2 - Stand\n3 - Double");
                int action = Integer.parseInt(keyboard.nextLine());
                if (action == 1){ //hit
                    player.hand.add(shoe.dealCard());
                    System.out.println("You were dealt a " + player.hand.get(player.hand.size() - 1).toString());
                    updateHandValue(player);
                    System.out.println("Your hand value is: " + player.handValue);
                }
                else if (action == 2){ //stand
                    break;
                }
                else if (action == 3){ //double down
                    if (player.balance >= bet) {
                        player.balance -= bet;
                        bet += bet;
                        player.hand.add(shoe.dealCard());
                        System.out.println("You were dealt a " + player.hand.get(player.hand.size() - 1).toString());
                        updateHandValue(player);
                        System.out.println("Your hand value is: " + player.handValue);
                        break;
                    }
                    else
                        System.out.println("You don't have enough money to double.");
                }
                else{ //split (odd numbers even numbers)
                    System.out.println("This does not exist");
                }
                if (player.handValue > 21){
                    player.busted = true;
                    break;
                }
            }

            if (dealer.handValue == 21)
                dealer.hasBlackjack = true;
            //dealer action
            System.out.println("\n\nDealer's hand: \n" + dealer.hand.get(0).toString() + "\n" + dealer.hand.get(1).toString());
            while (dealer.handValue < 17 && player.busted == false){
                dealer.hand.add(shoe.dealCard());
                System.out.println("\nDealer deals a " + dealer.hand.get(dealer.hand.size()-1));
                updateHandValue(dealer);
                if (dealer.handValue > 21)
                    dealer.busted = true;
            }

            //payouts
            if (player.busted){
                System.out.println("You busted. You receive nothing.");
            }
            else if (dealer.hasBlackjack){
                if (player.hasBlackjack) {
                    System.out.println("Push. You collect your original wager: $" + bet);
                    player.balance += bet;
                }
                else
                    System.out.println("Dealer has a blackjack. You receive nothing.");
            }
            else if (player.hasBlackjack){
                System.out.println("You got a blackjack. You receive: $" + bet*2.5);
                player.balance += bet*2.5;
            }
            else if (dealer.busted){
                System.out.println("Dealer busted, you receive: $" + bet*2);
                player.balance += bet*2;
            }
            else if (dealer.handValue > player.handValue){
                System.out.println("Dealer is closer to 21. You receive nothing,");
            }
            else if (player.handValue > dealer.handValue){
                System.out.println("You are closer to 21. You receive: $" + bet*2);
                player.balance += bet*2;
            }
            else{
                System.out.println("Push. You collect your original wage: $" + bet);
                player.balance += bet;
            }

            //end of hand
            System.out.println("Would you like to play another hand?");
            String answer = keyboard.nextLine();
            if (answer.equals("no")){
                playStatus = false;
                System.out.println("Your finishing balance is: $" + player.balance);
                System.out.println("With a difference of: $" + (player.balance-startMoney));
            }
        }

        //writing to file contents
        try{
            FileWriter file = new FileWriter(tempFile);
            for (String info: playersInfo){
                file.write(info + ",");
            }
            file.write(userName.toUpperCase() + "," + (int)player.balance + ",");
            file.close();
            if (!originalFile.delete()){
                System.out.println("Could not delete.");
            }
            if (!tempFile.renameTo(originalFile)){
                System.out.println("Could not rename.");
            }
        }
        catch (IOException e){
            System.out.println("Cannot write to file");
            e.printStackTrace();
        }
    }

    public static void updateHandValue(Player player){
        player.handValue = 0;
        for (Card card : player.hand){
            if (card.getValue() == 1){
                player.handValue += 11;
            }
            else
                player.handValue += card.getValue();
        }
        for (Card card : player.hand){
            if (card.getValue() == 1 && player.handValue > 21){
                player.handValue -= 10;
            }
        }
    }

    public static void printHand(Player player){
        for (Card card : player.hand){
            System.out.println(card.toString());
        }
    }
}
