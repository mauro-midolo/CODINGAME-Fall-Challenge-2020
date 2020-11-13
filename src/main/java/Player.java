import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

class Player {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int actionCount = in.nextInt(); // the number of spells and recipes in play
            List<Component> components = new ArrayList<>();
            for (int i = 0; i < actionCount; i++) {
                components.add(ComponentBuilder.build(in.nextInt(), in.next(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt() != 0, in.nextInt() != 0));
            }
            List<Component> brews = components.stream().filter(Component::isBrew).collect(Collectors.toList());
            List<Component> casts = components.stream().filter(Component::isCast).collect(Collectors.toList());
            PlayerInventory me = new PlayerInventory(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
            PlayerInventory theOther = new PlayerInventory(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
            String choose = new Chooser().getBest(me, brews, casts);
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

abstract class Component implements Cloneable {
    protected Component(int actionId, String actionType, int blueCost, int greenCost, int orangeCost, int yellowCost, int price, int tomeIndex, int taxCount, boolean castable, boolean repeatable) {
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

    final int actionId;
    final String actionType;
    final int blueCost;
    final int greenCost;
    final int orangeCost;
    final int yellowCost;
    final int price;
    final int tomeIndex;
    final int taxCount;
    boolean castable;
    final boolean repeatable;

    String getActionType() {
        return actionType;
    }

    public void setCastable(boolean castable) {
        this.castable = castable;
    }

    public abstract Component clone();

    boolean isBrew() {
        return false;
    }

    public boolean isCast() {
        return false;
    }

    public boolean isOpponentCast() {
        return false;
    }

    public String getActionId() {
        return String.valueOf(actionId);
    }

    public int getPrice() {
        return price;
    }

    public int getCostFor(int index) {
        if (index == 0) return blueCost;
        if (index == 1) return greenCost;
        if (index == 2) return orangeCost;
        if (index == 3) return yellowCost;
        return 0;
    }

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

    public boolean isCastable() {
        return castable;
    }
}

class Wait extends Component {
    public static String COMPONENT = "WAIT";

    protected Wait() {
        super(0, COMPONENT, 0, 0, 0, 0, 0, 0, 0, false, false);
    }

    @Override
    public Component clone() {
        return new Wait();
    }
}

class Cast extends Component implements Cloneable {
    public static String COMPONENT = "CAST";

    public Cast(int actionId, String actionType, int blueCost, int greenCost, int orangeCost, int yellowCost, int price, int tomeIndex, int taxCount, boolean castable, boolean repeatable) {
        super(actionId, actionType, blueCost, greenCost, orangeCost, yellowCost, price, tomeIndex, taxCount, castable, repeatable);
    }

    @Override
    String getActionType() {
        return COMPONENT;
    }

    @Override
    public boolean isCast() {
        return true;
    }

    @Override
    public Cast clone() {
        return new Cast(super.actionId, super.actionType, super.blueCost, super.greenCost, super.orangeCost, super.yellowCost, super.price, super.tomeIndex, super.taxCount, super.castable, super.repeatable);
    }
}

class OpponentCast extends Cast {
    public static String COMPONENT = "OPPONENT_CAST";

    public OpponentCast(int actionId, String actionType, int blueCost, int greenCost, int orangeCost, int yellowCost, int price, int tomeIndex, int taxCount, boolean castable, boolean repeatable) {
        super(actionId, actionType, blueCost, greenCost, orangeCost, yellowCost, price, tomeIndex, taxCount, castable, repeatable);
    }

    @Override
    String getActionType() {
        return COMPONENT;
    }

    @Override
    public boolean isCast() {
        return false;
    }

    @Override
    public boolean isOpponentCast() {
        return true;
    }
}

class Brew extends Component {

    public static String COMPONENT = "BREW";

    protected Brew(int actionId, String actionType, int blueCost, int greenCost, int orangeCost, int yellowCost, int price, int tomeIndex, int taxCount, boolean castable, boolean repeatable) {
        super(actionId, actionType, blueCost, greenCost, orangeCost, yellowCost, price, tomeIndex, taxCount, castable, repeatable);
    }

    @Override
    public String getActionType() {
        return COMPONENT;
    }

    @Override
    public Component clone() {
        return new Brew(actionId, actionType, blueCost, greenCost, orangeCost, yellowCost, price, tomeIndex, taxCount, castable, repeatable);
    }

    @Override
    public boolean isBrew() {
        return true;
    }
}

class Chooser {

    public String getBest(PlayerInventory me, List<Component> brews, List<Component> casts) {
        Component inventoryChoose = new BestBrewChooser().getBest(me, brews);
        if (inventoryChoose.getActionType().equals(Brew.COMPONENT)) {
            return Brew.COMPONENT + " " + inventoryChoose.getActionId();
        }
        Map<Integer, Integer> rupieSteps = new WeighCalculator().calculateSteps(casts);

        Component cheapestBrew = null;
        Double maxRateScore = null;
        for (Component brew : brews) {
            double rateScore = calculateRateScore(me, brew, rupieSteps);
            if (cheapestBrew == null || rateScore > maxRateScore) {
                cheapestBrew = brew;
                maxRateScore = rateScore;
            }
        }

        return Wait.COMPONENT;
    }

    public double calculateRateScore(PlayerInventory me, Component brew, Map<Integer, Integer> rupieMandatorySteps) {
        int targetBlue = brew.getBlueCost();
        int targetYellow = brew.getYellowCost();
        int targetOrange = brew.getOrangeCost();
        int targetGreen = brew.getGreenCost();
        int price = brew.getPrice();


        int missingBlue = Math.abs(targetBlue + me.getBlue());
        int missingYellow = Math.abs(targetYellow + me.getYellow());
        int missingOrange = Math.abs(targetOrange + me.getOrange());
        int missingGreen = Math.abs(targetGreen + me.getGreen());
        if ((missingBlue > 0 && !rupieMandatorySteps.containsKey(0)) ||
                (missingBlue > 1 && !rupieMandatorySteps.containsKey(1)) ||
                (missingBlue > 2 && !rupieMandatorySteps.containsKey(2)) ||
                (missingBlue > 3 && !rupieMandatorySteps.containsKey(3))
        ) {
            return 0;
        }
        return (1D * price) / (rupieMandatorySteps.get(0) * missingBlue +
                rupieMandatorySteps.get(1) * missingGreen +
                rupieMandatorySteps.get(2) * missingOrange +
                rupieMandatorySteps.get(3) * missingYellow);
    }

}

class BestBrewChooser {
    public Component getBest(PlayerInventory me, List<Component> brews) {
        brews.sort((o1, o2) -> -1 * Integer.compare(o1.getPrice(), o2.getPrice()));
        for (Component brew : brews) {
            if (
                    me.getBlue() + brew.getBlueCost() >= 0 &&
                            me.getGreen() + brew.getGreenCost() >= 0 &&
                            me.getOrange() + brew.getOrangeCost() >= 0 &&
                            me.getYellow() + brew.getYellowCost() >= 0
            ) {
                return brew;
            }
        }
        return new Wait();
    }
}

class ComponentBuilder {
    public static Component build(int actionId, String actionType, int blueCost, int greenCost, int orangeCost, int yellowCost, int price, int tomeIndex, int taxCount, boolean castable, boolean repeatable) {
        if (Brew.COMPONENT.equals(actionType)) {
            return new Brew(actionId, actionType, blueCost, greenCost, orangeCost, yellowCost, price, tomeIndex, taxCount, castable, repeatable);
        }
        if (Cast.COMPONENT.equals(actionType)) {
            return new Cast(actionId, actionType, blueCost, greenCost, orangeCost, yellowCost, price, tomeIndex, taxCount, castable, repeatable);
        }
        if (OpponentCast.COMPONENT.equals(actionType)) {
            return new OpponentCast(actionId, actionType, blueCost, greenCost, orangeCost, yellowCost, price, tomeIndex, taxCount, castable, repeatable);
        }
        return new Wait();
    }
}

class WeighCalculator {
    public Map<Integer, Integer> calculateSteps(List<Component> casts) {
        Map<Integer, Integer> rupieSteps = new HashMap<>();
        for (int i = 0; i < 4; i += 1) {
            try {
                rupieSteps.put(i, calculateStepsFor(i, casts.stream().map(Component::clone).collect(Collectors.toList())));
            } catch (IOException ignored) {
            }
        }
        return rupieSteps;
    }

    int calculateStepsFor(int index, List<Component> casts) throws IOException {
        int count = 0;
        Optional<Component> castWithColor = getCastWith(index, casts);
        if (!castWithColor.isPresent()) {
            throw new IOException("No color present in Cast: " + index);
        }
        Component first = castWithColor.get();

        for (int i = 0; i < 4; i += 1) {
            count = executeCartsFor(casts, count, first, i);
        }

        if (!first.isCastable()) {
            count++;
            casts.forEach(cast -> cast.setCastable(true));
        } else {
            first.setCastable(false);
        }

        count++;
        return count;
    }

    private int executeCartsFor(List<Component> casts, int count, Component first, int index) throws IOException {
        if (first.getCostFor(index) < 0) {
            for (int i = 0; i < getDebitsCount(first, index); i += 1) {
                count += calculateStepsFor(index, casts);

            }
        }
        return count;
    }

    private int getDebitsCount(Component first, int index) {
        return abs(first.getCostFor(index));
    }

    private Optional<Component> getCastWith(int i, List<Component> casts) {
        return casts.stream().filter(cast -> cast.getCostFor(i) > 0).findFirst();
    }


}