import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

class Player {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int actionCount = in.nextInt();
            List<Component> components = new ArrayList<>();
            for (int i = 0; i < actionCount; i++) {
                components.add(ComponentBuilder.build(in.nextInt(), in.next(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt() != 0, in.nextInt() != 0));
            }
            List<Component> brews = components.stream().filter(Component::isBrew).collect(Collectors.toList());
            List<Component> casts = components.stream().filter(Component::isCast).collect(Collectors.toList());
            List<Component> oppositeCasts = components.stream().filter(Component::isOpponentCast).collect(Collectors.toList());
            List<Component> learn = components.stream().filter(Component::isLearn).collect(Collectors.toList());
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
//        if(blue<0 || green<0 || orange <0 || yellow<0){
//            throw new IllegalArgumentException("Illegal Inventory state");
//        }
    }

    public int getScore() {
        return score;
    }
}

class ComponentResult {
    private final Component component;
    private final long count;

    public ComponentResult(Component component, long count) {
        this.component = component;
        this.count = count;
    }

    public ComponentResult(Component component) {
        this.component = component;
        this.count = 1;
    }

    @Override
    public String toString() {
        if (count == 1) {
            return component.toString();
        }

        return component.toString() + " " + count;
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

    public boolean isRepeatable() {
        return repeatable;
    }

    final boolean repeatable;

    String getActionType() {
        return actionType;
    }

    public void setCastable(boolean castable) {
        this.castable = castable;
    }

    public boolean isLearn() {
        return false;
    }

    ;

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

    public boolean isRest() {
        return false;
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

    @Override
    public String toString() {
        return actionType + " " + actionId;
    }
}

class Learn extends Component implements Cloneable {
    public static String COMPONENT = "LEARN";

    public Learn(int actionId, String actionType, int blueCost, int greenCost, int orangeCost, int yellowCost, int price, int tomeIndex, int taxCount, boolean castable, boolean repeatable) {
        super(actionId, actionType, blueCost, greenCost, orangeCost, yellowCost, price, tomeIndex, taxCount, castable, repeatable);
    }

    @Override
    String getActionType() {
        return COMPONENT;
    }

    @Override
    public boolean isLearn() {
        return true;
    }

    @Override
    public Learn clone() {
        return new Learn(super.actionId, super.actionType, super.blueCost, super.greenCost, super.orangeCost, super.yellowCost, super.price, super.tomeIndex, super.taxCount, super.castable, super.repeatable);
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
        ComponentResult bestResult = new BestCastChooser().getBest(me, oppositeInventory, brews, casts, oppositeCasts);
        return bestResult.toString();
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

    public static final double OPPOSITE_SCORE_WEIGHT = 0.5D;

    public ComponentResult getBest(PlayerInventory me, PlayerInventory oppositeInventory, List<Component> brews, List<Component> casts, List<Component> oppositeCasts) {
        List<Component> brewsToUse = filterBrewBaseScore(me, oppositeInventory, brews);
        Component cheapestBrew = null;
        Double maxRateScore = null;
        Route bestRupeeSteps = null;
        for (Component brew : brewsToUse) {
            Route brewSteps = new WeighCalculator().calculateSteps(me, casts, brew);
            if (notStepsAvailable(brewSteps)) {
                continue;
            }
            Route oppositeBrewSteps = new WeighCalculator().calculateSteps(oppositeInventory, oppositeCasts, brew);
            double rateScore = calculateRateScore(brew, brewSteps, oppositeBrewSteps);
            if (cheapestBrew == null || rateScore > maxRateScore) {
                cheapestBrew = brew;
                maxRateScore = rateScore;
                bestRupeeSteps = brewSteps;
            }
        }
        if (cheapestBrew == null || bestRupeeSteps.getCurrentSteps() == 0) {
            return new ComponentResult(new Wait());
        }

        Component component = bestRupeeSteps.getSteps().get(0);
        if (component.isRepeatable()) {
            long count = bestRupeeSteps.getSteps().stream().filter(component1 -> component1.equals(component)).count();
            return new ComponentResult(component, count);
        }
        return new ComponentResult(component);
    }

    private List<Component> filterBrewBaseScore(PlayerInventory me, PlayerInventory oppositeInventory, List<Component> brews) {
        int diffWithOther = oppositeInventory.getScore() - me.getScore();
        if (diffWithOther > 0) {
            List<Component> brewFiltered = brews.stream().filter(component -> component.getPrice() >= diffWithOther).collect(Collectors.toList());
            if (brewFiltered.size() > 0) {
                return brewFiltered;
            }
        }
        return brews;
    }

    private boolean notStepsAvailable(Route route) {
        return route.getCurrentSteps() == 0;
    }

    public double calculateRateScore(Component brew, Route brewStepRoute, Route oppositeBrewStepRoute) {
        return calculateRateScoreForOnePlayer(brew, brewStepRoute) - (calculateRateScoreForOnePlayer(brew, oppositeBrewStepRoute) * OPPOSITE_SCORE_WEIGHT);
    }

    private double calculateRateScoreForOnePlayer(Component brew, Route brewStepRoute) {
        int price = brew.getPrice();
        return (1D * price) /
                (1D * brewStepRoute.getCurrentSteps());
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
        if (Learn.COMPONENT.equals(actionType)) {
            return new Learn(actionId, actionType, blueCost, greenCost, orangeCost, yellowCost, price, tomeIndex, taxCount, castable, repeatable);
        }
        return new Wait();
    }
}

class WeighCalculator {
    public Route calculateSteps(PlayerInventory me, List<Component> casts, Component brew) {
        Route route = new Route(casts, new LinkedList<>(), me);
        boolean findRoute = true;
        while (notHaveAll(brew, route.getMe()) && findRoute) {
            try {
                calculateStepsFor(route, brew, 0, Optional.empty());
            } catch (CodingGameException ignored) {
                findRoute = false;
            }
        }
        return route;
    }

    private boolean notCostAvailable(Component cast, PlayerInventory me) {
        for (RupeesIndexer value : RupeesIndexer.values()) {
            if (cast.getCostFor(value.getIndex()) < 0 && Math.abs(cast.getCostFor(value.getIndex())) > me.getNumberOf(value.getIndex())) {
                return true;
            }
        }
        return false;
    }

    private boolean notSpaceInInventory(Component cast, PlayerInventory me) {
        int totalInventory = 0;
        for (RupeesIndexer value : RupeesIndexer.values()) {
            totalInventory += me.getNumberOf(value.getIndex()) + cast.getCostFor(value.getIndex());
        }
        return totalInventory > 10;
    }

    private void findBestCast(Route route, Component brew) {
        PlayerInventory inventory = route.getMe();
        Component bestCast = null;
        for (Component cast : route.getCasts()) {

        }
    }

    private boolean notHaveAll(Component brew, PlayerInventory me) {
        for (RupeesIndexer value : RupeesIndexer.values()) {
            if (brew.getCostFor(value.getIndex()) + me.getNumberOf(value.getIndex()) < 0) {
                return true;
            }
            ;
        }
        return false;
    }

    void calculateStepsFor(Route currentRoute, Component brew, int recursiveCount, Optional<Debit> debit) throws CodingGameException {
        if (recursiveCount > 4) {
            throw new NoRouteFoundException("Too much recursive");
        }
        if ((debit.isPresent() && !debit.get().hasDebits(currentRoute.getMe())) || !hasDebit(currentRoute, brew)) {
            return;
        }
        Debit debitToSolve = debit.orElseGet(() -> new Debit(brew.getCostFor(0) + currentRoute.getMe().getBlue(), brew.getCostFor(1) + currentRoute.getMe().getGreen(), brew.getCostFor(2) + currentRoute.getMe().getOrange(), brew.getCostFor(3) + currentRoute.getMe().getYellow()));
        List<Component> castsWithColor = getCastWith(debitToSolve, currentRoute.getCasts());
        if (castsWithColor.isEmpty()) {
            throw new NoCastAvailableException("No Casts useful");
        }
        Route minCastRoute = null;
        for (Component castWithColor : castsWithColor) {
            try {
                Route bestLeafRoute = calculateBestLeaf(currentRoute, castWithColor, brew, minCastRoute, recursiveCount, debit);
                minCastRoute = compareBestRoute(minCastRoute, bestLeafRoute);
            } catch (CodingGameException ignore) {
            }
        }
        if (minCastRoute == null) {
            throw new NoRouteFoundException("");
        }

        currentRoute.updateCasts(minCastRoute.getCasts());
        currentRoute.updateSteps(minCastRoute.getSteps());
        currentRoute.updateInventory(minCastRoute.getMe());
    }

    private boolean hasDebit(Route currentRoute, Component brew) {
        for (RupeesIndexer value : RupeesIndexer.values()) {
            if (brew.getCostFor(value.getIndex()) + currentRoute.getMe().getNumberOf(value.getIndex()) < 0) {
                return true;
            }
        }
        return false;
    }

    private Route compareBestRoute(Route minCastRoute, Route bestLeafRoute) {
        if (minCastRoute == null) {
            return bestLeafRoute;
        }
        if (bestLeafRoute != null && minCastRoute.getCurrentSteps() > bestLeafRoute.getCurrentSteps()) {
            return bestLeafRoute;
        }
        return minCastRoute;
    }


    private Route calculateBestLeaf(Route currentRoute, Component castWithColor, Component brew, Route minCastRoute, int recursiveCount, Optional<Debit> debit) throws CodingGameException {
        Route leafRoute = currentRoute.clone();
        //Debit leafDebit = debit.clone();
        Component leafCastWithColor = leafRoute.getCast(String.valueOf(castWithColor.actionId));

        executeCastsFor(leafRoute, leafCastWithColor, brew, recursiveCount);

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

        calculateStepsFor(leafRoute, brew, recursiveCount + 1, debit);
        return leafRoute;
    }

    private void executeCastsFor(Route leafRoute, Component leafCast, Component brew, int recursiveCount) throws CodingGameException {
        Debit castDebits = new Debit(
                leafCast.getBlueCost() < 0 ? leafCast.getCostFor(0) : 0,
                leafCast.getGreenCost() < 0 ? leafCast.getCostFor(1) : 0,
                leafCast.getOrangeCost() < 0 ? leafCast.getCostFor(2) : 0,
                leafCast.getYellowCost() < 0 ? leafCast.getCostFor(3) : 0);
        calculateStepsFor(leafRoute, brew, recursiveCount + 1, Optional.of(castDebits));
    }

    private List<Component> getCastWith(Debit debit, List<Component> casts) {
        Set<Component> usefulCasts = new LinkedHashSet<>();
        for (Component cast : casts) {
            for (RupeesIndexer value : RupeesIndexer.values()) {
                if (debit.get(value.getIndex()) < 0 && cast.getCostFor(value.getIndex()) > 0) {
                    usefulCasts.add(cast);
                }
            }
        }
        return new ArrayList<>(usefulCasts);
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
        if (count > MAX_INVENTORY_SLOTS) {
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

class Debit implements Cloneable {
    private final int blue;
    private final int green;
    private final int orange;
    private final int yellow;

    public Debit(int blue, int green, int orange, int yellow) {

        this.blue = blue;
        this.green = green;
        this.orange = orange;
        this.yellow = yellow;
    }

    public boolean hasDebits(PlayerInventory me) {
        return (
                (blue < 0 && Math.abs(blue) > me.getBlue()) ||
                        (green < 0 && Math.abs(green) > me.getGreen()) ||
                        (orange < 0 && Math.abs(orange) > me.getOrange()) ||
                        (yellow < 0 && Math.abs(yellow) > me.getYellow()));
    }

    public int get(int index) {
        if (index == RupeesIndexer.BLUE.getIndex()) return blue;
        if (index == RupeesIndexer.GREEN.getIndex()) return green;
        if (index == RupeesIndexer.ORANGE.getIndex()) return orange;
        if (index == RupeesIndexer.YELLOW.getIndex()) return yellow;
        return 0;
    }

    @Override
    public Debit clone() {
        return new Debit(blue, green, orange, yellow);
    }

    @Override
    public String toString() {
        return "Debit{" +
                "blue=" + blue +
                ", green=" + green +
                ", orange=" + orange +
                ", yellow=" + yellow +
                '}';
    }
}

class Logging {
    private static boolean log = true;

    public static void log(String message) {
        if (log)
            System.out.println(message);
    }
}