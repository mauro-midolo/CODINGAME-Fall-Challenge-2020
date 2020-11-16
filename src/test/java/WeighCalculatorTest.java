import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class WeighCalculatorTest {

    @Test
    public void shouldCalculateNumberOfSteps() throws CodingGameException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", +2, -1, 0, 0, 0, 0, 0, true, false),
                new Cast(15, "CAST", -1, 0, +1, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(casts, new LinkedList<>(), new PlayerInventory(0, 0, 0, 0, 0));

        Component brew = new Brew(1, "BREW", 0, 0, -1, 0, 10, 0, 0, false, false);

        new WeighCalculator().calculateStepsFor(route, brew, 0, Optional.empty());
        assertEquals(3, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfStep2s() throws CodingGameException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", +2, -1, 0, 0, 0, 0, 0, true, false),
                new Cast(15, "CAST", -1, 0, +1, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(casts, new LinkedList<>(), new PlayerInventory(0, 0, 0, 0, 0));
        Component brew = new Brew(1, "BREW", 0, 0, -1, 0, 10, 0, 0, false, false);
        new WeighCalculator().calculateStepsFor(route, brew, 0, Optional.empty());
        assertEquals(3, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfSteps3() throws CodingGameException {
        List<Component> casts = Arrays.asList(
                new Cast(10, "CAST", -2, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false)
        );

        Component brew = new Brew(1, "BREW", 0, -1, 0, 0, 10, 0, 0, false, false);

        Route route = new Route(casts, new LinkedList<>(), new PlayerInventory(0, 0, 0, 0, 0));

        new WeighCalculator().calculateStepsFor(route, brew, 0, Optional.empty());
        assertEquals(4, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfStepsButMinimum() throws CodingGameException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, -2, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", 0, +1, -2, 0, 0, 0, 0, true, false),
                new Cast(11, "CAST", 0, 0, +1, 0, 0, 0, 0, true, false),
                new Cast(12, "CAST", 0, +1, 0, -1, 0, 0, 0, true, false),
                new Cast(13, "CAST", 0, 0, 0, +1, 0, 0, 0, true, false)
        );

        Route route = new Route(casts, new LinkedList<>(), new PlayerInventory(0, 0, 0, 0, 0));
        Component brew = new Brew(1, "BREW", -1, 0, 0, 0, 10, 0, 0, false, false);
        new WeighCalculator().calculateStepsFor(route, brew, 0, Optional.empty());
        assertEquals(6, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfStepsButMinimumComplex() throws CodingGameException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, -1, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(11, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(casts, new LinkedList<>(), new PlayerInventory(0, 0, 0, 0, 0));
        Component brew = new Brew(1, "BREW", -1, 0, 0, 0, 10, 0, 0, false, false);
        new WeighCalculator().calculateStepsFor(route, brew, 0, Optional.empty());
        assertEquals(1, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfSteps4() throws CodingGameException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", -1, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(12, "CAST", 0, -2, +1, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(casts, new LinkedList<>(), new PlayerInventory(0, 0, 0, 0, 0));
        Component brew = new Brew(1, "BREW", 0, 0, -1, 0, 10, 0, 0, false, false);
        new WeighCalculator().calculateStepsFor(route, brew, 0, Optional.empty());
        assertEquals(6, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateSteps() throws CodingGameException {
        Component brew = new Brew(1, "BREW", -1, -1, -1, 0, 10, 0, 0, false, false);
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(2, "CAST", -1, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(3, "CAST", -1, -1, +1, 0, 0, 0, 0, true, false)
        );


        PlayerInventory me = new PlayerInventory(0, 0, 0, 0, 0);

        Route route = new WeighCalculator().calculateSteps(me, casts, brew);
        assertEquals(10, route.getCurrentSteps());
    }

    @Test
    public void shouldConsiderLearns() {
        PlayerInventory me = new PlayerInventory(0, 0, 0, 0, 0);
        Component brews = new Brew(1, "BREW", -1, -1, 0, 0, 0, 0, 0, false, false);

        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, +1, 0, 0, 0, true, false),
                new Cast(2, "CAST", 0, +1, 0, +1, 0, 0, 0, true, false),
                new Cast(90, "CAST", +1, +1, 0, 0, 0, 0, 0, true, false)
        );

        Route route = new WeighCalculator().calculateSteps(me, casts, brews);
        assertEquals("CAST 90", route.getSteps().get(0).toString());
        assertEquals(1, route.getCurrentSteps());
    }

    @Test
    public void shouldConsiderToTakeInsteadToHave() {
        PlayerInventory me = new PlayerInventory(1, 0, 0, 0, 0);
        Component brew =
                new Brew(100, "BREW", -1, -1, 0, 0, 10, 0, 0, false, false);


        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(2, "CAST", -1, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(3, "CAST", 0, -1, 0, +1, 0, 0, 0, true, false)
        );

        Route choose = new WeighCalculator().calculateSteps(me, casts, brew);
        assertEquals(2, choose.getCurrentSteps());
    }

    @Test
    public void shouldConsiderKeepAndContinue() {
        PlayerInventory me = new PlayerInventory(0, 0, 0, 0, 0);
        Component brew =
                new Brew(100, "BREW", -1, -1, 0, 0, 10, 0, 0, false, false);


        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(2, "CAST", +1, -1, 0, 0, 0, 0, 0, true, false),
                new Cast(3, "CAST", -1, +1, 0, 0, 0, 0, 0, true, false)
        );

        Route choose = new WeighCalculator().calculateSteps(me, casts, brew);
        assertEquals(4, choose.getCurrentSteps());
    }
}