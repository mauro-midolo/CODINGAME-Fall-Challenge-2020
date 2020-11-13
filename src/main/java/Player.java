import java.util.*;

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
            ArrayList<Recipe> recipes = new ArrayList<>();
            for (int i = 0; i < actionCount; i++) {
                recipes.add(new Recipe(in.nextInt(), in.next(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt() != 0, in.nextInt() != 0));
            }


            PlayerInventory me;
            PlayerInventory theOther;
            me = new PlayerInventory(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
            theOther = new PlayerInventory(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());

            String choose = new Chooser().getBest(me, recipes);
            // in the first league: BREW <id> | WAIT; later: BREW <id> | CAST <id> [<times>] | LEARN <id> | REST | WAIT
            System.out.println(choose);
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

class Recipe {

    public Recipe(int actionId) {
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

    public Recipe(int actionId, String actionType, int blueCost, int greenCost, int orangeCost, int yellowCost, int price, int tomeIndex, int taxCount, boolean castable, boolean repeatable) {
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

    public int getActionId() {
        return this.actionId;
    }
}

class Chooser {

    public String getBest(PlayerInventory me, List<Recipe> recipes) {
        String inventoryChoose = new BestChooserInventory().getBest(me, recipes);
        if(!inventoryChoose.equals("WAIT")){
            return "BREW " + inventoryChoose;
        }
        return "WAIT";
    }

}

class BestChooserInventory {
    public String getBest(PlayerInventory me, List<Recipe> recipes) {
        recipes.sort((o1, o2) -> -1 * Integer.compare(o1.getPrice(), o2.getPrice()));
        for (Recipe recipe : recipes) {
            if (
                    me.getBlue() + recipe.getBlueCost() >= 0 &&
                            me.getGreen() + recipe.getGreenCost() >= 0 &&
                            me.getOrange() + recipe.getOrangeCost() >= 0 &&
                            me.getYellow() + recipe.getYellowCost() >= 0
            ) {
                return String.valueOf(recipe.getActionId());
            }
        }
        return "WAIT";
    }
}