import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {

            int actionCount = in.nextInt(); // the number of spells and recipes in play
            ArrayList<Recepy> recepies = new ArrayList<Recepy>();
            for (int i = 0; i < actionCount; i++) {
                recepies.add(new Recepy(in.nextInt(), in.next(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt() != 0, in.nextInt() != 0));
            }


            PlayerInventory me;
            PlayerInventory theOther;
            me = new PlayerInventory(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
            theOther = new PlayerInventory(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());

            String chooseRepecy = new Chooser().getBest(me, recepies);
            // in the first league: BREW <id> | WAIT; later: BREW <id> | CAST <id> [<times>] | LEARN <id> | REST | WAIT
            System.out.println("BREW " + chooseRepecy);
        }
    }
}

class PlayerInventory {

    public int getBlue() {
        return blue;
    }

    public int getGreen() {
        return green;
    }

    public int getOrange() {
        return orange;
    }

    public int getYellow() {
        return yellow;
    }

    public int blue;
    public int green;
    public int orange;
    public int yellow;
    private final int score;

    public PlayerInventory(int blue, int green, int orange, int yellow, int score) {
        this.blue = blue;
        this.green = green;
        this.orange = orange;
        this.yellow = yellow;
        this.score = score;
    }
}

class Recepy implements Comparable {

    public Recepy(int actionId) {
        this.actionId = actionId;
    }

    public int actionId;
    public String actionType;

    public int getBlueCost() {
        return blueCost;
    }

    public int getGreenCost() {
        return greenCost;
    }

    public int getOrangeCost() {
        return orangeCost;
    }

    public int getYellowCost() {
        return yellowCost;
    }

    public int blueCost;
    public int greenCost;
    public int orangeCost;
    public int yellowCost;
    public int price;
    public int tomeIndex;
    public int taxCount;
    public boolean castable;
    public boolean repeatable;

    public Recepy(int actionId, String actionType, int blueCost, int greenCost, int orangeCost, int yellowCost, int price, int tomeIndex, int taxCount, boolean castable, boolean repeatable) {
        this.actionId = actionId;
        this.actionType = actionType;
        this.blueCost = blueCost;
        this.greenCost = greenCost;
        this.orangeCost = orangeCost;
        this.yellowCost = yellowCost;
        this.price = price;
        this.tomeIndex = tomeIndex;
        this.taxCount = taxCount;
        this.castable = castable;
        this.repeatable = repeatable;
    }


    public int getPrice() {
        return price;
    }

    public int compareTo(Object o) {
        return -1 * Integer.compare(getPrice(), ((Recepy) o).getPrice());
    }

    public int getActionId() {
        return this.actionId;
    }
}

class Chooser {

    public String getBest(PlayerInventory me, List<Recepy> recepies) {
        Collections.sort(recepies);
        for (Recepy recepy : recepies) {
            if (
                    me.getBlue() + recepy.getBlueCost() >= 0 &&
                            me.getGreen() + recepy.getGreenCost() >= 0 &&
                            me.getOrange() + recepy.getOrangeCost() >= 0 &&
                            me.getYellow() + recepy.getYellowCost() >= 0
            ) {
                return String.valueOf(recepy.getActionId());
            }
        }
        return "WAIT";
    }
}