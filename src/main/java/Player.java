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
            List<Component> oppositeCasts = components.stream().filter(Component::isOpponentCast).collect(Collectors.toList());
            PlayerInventory me = new PlayerInventory(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
            PlayerInventory theOther = new PlayerInventory(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
            String choose = new Chooser().getBest(me, theOther, brews, casts, oppositeCasts);
            // in the first league: BREW <id> | WAIT; later: BREW <id> | CAST <id> [<times>] | LEARN <id> | REST | WAIT
            System.out.println(choose);
        }
    }
}

class PlayerInventory implements Cloneable {

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

    @Override
    public PlayerInventory clone() {
        return new PlayerInventory(blue, green, orange, yellow, score);
    }

    public int getNumberOf(int index) {
        if (index == RupeesIndexer.BLUE.getIndex()) return this.getBlue();
        if (index == RupeesIndexer.GREEN.getIndex()) return this.getGreen();
        if (index == RupeesIndexer.ORANGE.getIndex()) return this.getOrange();
        if (index == RupeesIndexer.YELLOW.getIndex()) return this.getYellow();
        return 0;
    }

    public void update(int index, int deltaValue) {
        if (index == RupeesIndexer.BLUE.getIndex()) blue += deltaValue;
        if (index == RupeesIndexer.GREEN.getIndex()) green += deltaValue;
        if (index == RupeesIndexer.ORANGE.getIndex()) orange += deltaValue;
        if (index == RupeesIndexer.YELLOW.getIndex()) yellow += deltaValue;
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
        if (index == RupeesIndexer.BLUE.getIndex()) return blueCost;
        if (index == RupeesIndexer.GREEN.getIndex()) return greenCost;
        if (index == RupeesIndexer.ORANGE.getIndex()) return orangeCost;
        if (index == RupeesIndexer.YELLOW.getIndex()) return yellowCost;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return actionId == component.actionId &&
                Objects.equals(actionType, component.actionType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionId, actionType);
    }

    public boolean isRest() {
        return false;
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

    @Override
    public String toString() {
        return actionType;
    }
}

class
Cast extends Component implements Cloneable {
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

    @Override
    public String toString() {
        return actionType + " " + actionId;
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
    public String toString() {
        return actionType + " " + actionId;
    }

    @Override
    public boolean isBrew() {
        return true;
    }
}

class Rest extends Component {

    public static String COMPONENT = "REST";

    protected Rest() {
        super(-1, COMPONENT, 0, 0, 0, 0, 0, 0, 0, false, false);
    }

    @Override
    public String getActionType() {
        return COMPONENT;
    }

    @Override
    public Component clone() {
        return new Rest();
    }

    @Override
    public boolean isRest() {
        return true;
    }

    @Override
    public String toString() {
        return actionType;
    }
}

class Chooser {

    public String getBest(PlayerInventory me, PlayerInventory oppositeInventory, List<Component> brews, List<Component> casts, List<Component> oppositeCasts) {
        Component inventoryChoose = new BestBrewChooser().getBest(me, brews);
        if (inventoryChoose.getActionType().equals(Brew.COMPONENT)) {
            return inventoryChoose.toString();
        }
        if (casts.size() == 0) {
            return inventoryChoose.toString();
        }
        Component best = new BestCastChooser().getBest(me, oppositeInventory,brews, casts, oppositeCasts);
        return best.toString();
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

class BestCastChooser {

    public static final double OPPOSITE_SCORE_WEIGTH = 0.5D;

    public Component getBest(PlayerInventory me, PlayerInventory oppositeInventory, List<Component> brews, List<Component> casts, List<Component> oppositeCasts) {

        Component cheapestBrew = null;
        Double maxRateScore = null;
        Map<Integer, Route> bestRupeeSteps = null;
        for (Component brew : brews) {
            Map<Integer, Route> rupeeSteps = new WeighCalculator().calculateSteps(me, casts, brew);
            if(notStepsAvailable(rupeeSteps, brew, me)){
                continue;
            }
            Map<Integer, Route> oppositeRupeeSteps = new WeighCalculator().calculateSteps(oppositeInventory, oppositeCasts, brew);
            double rateScore = calculateRateScore(me, oppositeInventory,brew, rupeeSteps, oppositeRupeeSteps);
            if (cheapestBrew == null || rateScore > maxRateScore) {
                cheapestBrew = brew;
                maxRateScore = rateScore;
                bestRupeeSteps = rupeeSteps;
            }
        }
        if(cheapestBrew == null){
            return new Wait();
        }
        Optional<Integer> missingIndex = getMissing(cheapestBrew, me).stream().filter(bestRupeeSteps::containsKey).findFirst();
        if (!missingIndex.isPresent()) {
            return new Wait();
        }
        return bestRupeeSteps.get(missingIndex.get()).getSteps().get(0);
    }

    private boolean notStepsAvailable(Map<Integer, Route> rupeeSteps, Component brew, PlayerInventory me) {
        int targetBlue = brew.getBlueCost();
        int targetYellow = brew.getYellowCost();
        int targetOrange = brew.getOrangeCost();
        int targetGreen = brew.getGreenCost();
        int missingBlue = targetBlue + me.getBlue() < 0 ? Math.abs(targetBlue + me.getBlue()) : 0;
        int missingYellow = targetYellow + me.getYellow() < 0 ? Math.abs(targetYellow + me.getYellow()) : 0;
        int missingOrange = targetOrange + me.getOrange() < 0 ? Math.abs(targetOrange + me.getOrange()) : 0;
        int missingGreen = targetGreen + me.getGreen() < 0 ? Math.abs(targetGreen + me.getGreen()) : 0;
        return (missingBlue > 0 && !rupeeSteps.containsKey(0)) ||
                (missingYellow > 0 && !rupeeSteps.containsKey(1)) ||
                (missingOrange > 0 && !rupeeSteps.containsKey(2)) ||
                (missingGreen > 0 && !rupeeSteps.containsKey(3));
    }

    private List<Integer> getMissing(Component brew, PlayerInventory me) {
        int targetBlue = brew.getBlueCost();
        int targetYellow = brew.getYellowCost();
        int targetOrange = brew.getOrangeCost();
        int targetGreen = brew.getGreenCost();
        List<Integer> missingIndex = new ArrayList<>();
        int missingBlue = targetBlue + me.getBlue();
        if (missingBlue < 0) {
            missingIndex.add(RupeesIndexer.BLUE.getIndex());
        }
        int missingYellow = targetYellow + me.getYellow();
        if (missingYellow < 0) {
            missingIndex.add(RupeesIndexer.YELLOW.getIndex());
        }
        int missingOrange = targetOrange + me.getOrange();
        if (missingOrange < 0) {
            missingIndex.add(RupeesIndexer.ORANGE.getIndex());
        }
        int missingGreen = targetGreen + me.getGreen();
        if (missingGreen < 0) {
            missingIndex.add(RupeesIndexer.GREEN.getIndex());
        }
        return missingIndex;
    }

    public double calculateRateScore(PlayerInventory me, PlayerInventory oppositeInventory, Component brew, Map<Integer, Route> rupeeMandatorySteps, Map<Integer, Route> oppositeRupeeSteps) {
        return calculateRateScoreForOnePlayer(me, brew, rupeeMandatorySteps) - (calculateRateScoreForOnePlayer(oppositeInventory, brew, oppositeRupeeSteps) * OPPOSITE_SCORE_WEIGTH) ;
    }

    private double calculateRateScoreForOnePlayer(PlayerInventory me, Component brew, Map<Integer, Route> rupeeMandatorySteps) {
        int price = brew.getPrice();
        return (1D * price) /
                (1D * (rupeeMandatorySteps.containsKey(0) ? rupeeMandatorySteps.get(0).getCurrentSteps() : 0) +
                1D * (rupeeMandatorySteps.containsKey(1) ? rupeeMandatorySteps.get(1).getCurrentSteps() : 0) +
                1D * (rupeeMandatorySteps.containsKey(2) ? rupeeMandatorySteps.get(2).getCurrentSteps() : 0) +
                1D * (rupeeMandatorySteps.containsKey(3) ? rupeeMandatorySteps.get(3).getCurrentSteps()  : 0));
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
    public Map<Integer, Route> calculateSteps(PlayerInventory me, List<Component> casts, Component brew) {
        Map<Integer, Route> rupeeSteps = new HashMap<>();
        for (int i = 0; i < 4; i += 1) {
            try {
                Route route = new Route(casts, new LinkedList<>(), me);
                calculateStepsFor(i, route, brew, brew.getCostFor(i));
                rupeeSteps.put(i, route);
            } catch (CodingGameException ignored) {}
        }
        return rupeeSteps;
    }

    void calculateStepsFor(int index, Route currentRoute, Component brew, int debitsCount) throws CodingGameException {

        if (currentRoute.getMe().getNumberOf(index) >= Math.abs(debitsCount)) {
            return;
        }
        while (currentRoute.getMe().getNumberOf(index) < Math.abs(debitsCount)) {
            List<Component> castsWithColor = getCastWith(index, currentRoute.getCasts());
            if (castsWithColor.isEmpty()) {
                throw new NoCastAvailableException("No color present" + index);
            }
            Route minCastRoute = null;
            for (Component castWithColor : castsWithColor) {
                minCastRoute = calculateBestLeaf(currentRoute, minCastRoute, castWithColor, brew);
            }
            if(minCastRoute == null){
                throw new NoRouteFoundException("");
            }
            currentRoute.updateCasts(minCastRoute.getCasts());
            currentRoute.updateSteps(minCastRoute.getSteps());
            currentRoute.updateInventory(minCastRoute.getMe());
        }
    }

    private Route calculateBestLeaf(Route currentRoute, Route minCastRoute, Component castWithColor, Component brew) throws CodingGameException {
        Route leafRoute = currentRoute.clone();
        Component leafCastWithColor = leafRoute.getCast(String.valueOf(castWithColor.actionId));
        for (RupeesIndexer rupee : RupeesIndexer.values()) {
            executeCastsFor(leafRoute, leafCastWithColor, rupee.getIndex(), brew);
        }
        if (!leafCastWithColor.isCastable()) {
            leafRoute.addStep(new Rest());
            leafRoute.getCasts().forEach(cast -> cast.setCastable(true));
        }
        leafCastWithColor.setCastable(false);
        try {
            leafRoute.updateInventory(leafCastWithColor);
        } catch (InventorySpaceExceededException e) {
            return minCastRoute;
        }
        leafRoute.addStep(leafCastWithColor);
        if (minCastRoute == null || minCastRoute.getCurrentSteps() > leafRoute.getCurrentSteps()) {
            minCastRoute = leafRoute;
        }
        return minCastRoute;
    }

    private void executeCastsFor(Route leafRoute, Component leafCast, int index, Component brew) throws CodingGameException {
        if (leafCast.getCostFor(index) < 0) {
            int debitsCount = getDebitsCount(leafCast, index);
//            for (int i = 0; i < getDebitsCount(leafCast, index); i += 1) {
            calculateStepsFor(index, leafRoute, brew, debitsCount);
//            }
        }
    }

    private int getDebitsCount(Component first, int index) {
        return abs(first.getCostFor(index));
    }

    private List<Component> getCastWith(int i, List<Component> casts) {
        return casts.stream().filter(cast -> cast.getCostFor(i) > 0).collect(Collectors.toList());
    }
}

enum RupeesIndexer {
    BLUE(0),
    GREEN(1),
    ORANGE(2),
    YELLOW(3);

    public int getIndex() {
        return index;
    }

    private final int index;

    RupeesIndexer(int index) {
        this.index = index;
    }
}

class Route implements Cloneable {
    private List<Component> casts;
    private List<Component> steps;
    private static Integer MAX_INVENTORY_SLOTS = 10;

    public PlayerInventory getMe() {
        return me;
    }

    private PlayerInventory me;

    Route(List<Component> casts, List<Component> steps, PlayerInventory me) {
        this.casts = casts.stream().map(Component::clone).collect(Collectors.toList());
        this.steps = steps;
        this.me = me.clone();
    }

    @Override
    public Route clone() {
        return new Route(casts.stream().map(Component::clone).collect(Collectors.toList()), steps.stream().map(Component::clone).collect(Collectors.toList()), me);
    }

    public void updateCasts(List<Component> newCasts) {
        casts = newCasts;
    }

    public void updateSteps(List<Component> newSteps) {
        steps = newSteps;
    }

    public List<Component> getCasts() {
        return casts;
    }

    public Component getCast(String ActionId) {
        return casts.stream().filter(component -> component.getActionId().equals(String.valueOf(ActionId))).findFirst().get();
    }

    public int getCurrentSteps() {
        return steps.size();
    }

    public List<Component> getSteps() {
        return steps;
    }

    public void addStep(Component component) {
        steps.add(component);
    }

    public void updateInventory(Component leafCastWithColor) throws InventorySpaceExceededException {
        for (RupeesIndexer rupee : RupeesIndexer.values()) {
            me.update(rupee.getIndex(), leafCastWithColor.getCostFor(rupee.getIndex()));
        }
        Integer count = Arrays.stream(RupeesIndexer.values()).map(rupeesIndexer -> me.getNumberOf(rupeesIndexer.getIndex())).reduce(0, Integer::sum);
        if(count > MAX_INVENTORY_SLOTS){
            throw new InventorySpaceExceededException("Too much item into inventory");
        }
    }

    public void updateInventory(PlayerInventory me) {
        this.me = me;
    }
}

class CodingGameException extends Exception {
    public CodingGameException(String message) {
        super(message);
    }
}

class InventorySpaceExceededException extends CodingGameException {
    public InventorySpaceExceededException(String message) {
        super(message);
    }
}

class NoCastAvailableException extends CodingGameException {
    public NoCastAvailableException(String message) {
        super(message);
    }
}

class NoRouteFoundException extends CodingGameException {
    public NoRouteFoundException(String message) {
        super(message);
    }
}