import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class WeighCalculatorTest {

    @Test
    public void shouldCalculateNumberOfSteps() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", +2, -1, 0, 0, 0, 0, 0, true, false),
                new Cast(15, "CAST", -1, 0, +1, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(casts, new LinkedList<>(), new PlayerInventory(0, 0, 0, 0, 0));

        Component brew = new Brew(1, "BREW", 0, 0, -1, 0, 10, 0, 0, false, false);

        new WeighCalculator().calculateStepsFor(2, route, brew, brew.getCostFor(2));
        assertEquals(3, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfStep2s() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", +2, -1, 0, 0, 0, 0, 0, true, false),
                new Cast(15, "CAST", -1, 0, +1, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(casts, new LinkedList<>(), new PlayerInventory(0, 0, 0, 0, 0));
        Component brew = new Brew(1, "BREW", 0, 0, -1, 0, 10, 0, 0, false, false);
        new WeighCalculator().calculateStepsFor(2, route, brew, brew.getCostFor(2));
        assertEquals(3, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfSteps3() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", -2, +1, 0, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(casts, new LinkedList<>(), new PlayerInventory(0, 0, 0, 0, 0));
        Component brew = new Brew(1, "BREW", 0, -1, 0, 0, 10, 0, 0, false, false);
        new WeighCalculator().calculateStepsFor(1, route, brew, brew.getCostFor(1));
        assertEquals(4, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfStepsButMinimum() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, -2, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", 0, +1, -2, 0, 0, 0, 0, true, false),
                new Cast(11, "CAST", 0, 0, +1, 0, 0, 0, 0, true, false),
                new Cast(12, "CAST", 0, +1, 0, -1, 0, 0, 0, true, false),
                new Cast(13, "CAST", 0, 0, 0, +1, 0, 0, 0, true, false)
        );

        Route route = new Route(casts, new LinkedList<>(), new PlayerInventory(0, 0, 0, 0, 0));
        Component brew = new Brew(1, "BREW", -1, 0, 0, 0, 10, 0, 0, false, false);
        new WeighCalculator().calculateStepsFor(0, route, brew, brew.getCostFor(0));
        assertEquals(6, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfStepsButMinimumComplex() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, -1, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(11, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(casts, new LinkedList<>(), new PlayerInventory(0, 0, 0, 0, 0));
        Component brew = new Brew(1, "BREW", -1, 0, 0, 0, 10, 0, 0, false, false);
        new WeighCalculator().calculateStepsFor(0, route, brew, brew.getCostFor(0));
        assertEquals(1, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfSteps4() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", -1, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(12, "CAST", 0, -2, +1, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(casts, new LinkedList<>(), new PlayerInventory(0, 0, 0, 0, 0));
        Component brew = new Brew(1, "BREW", 0, 0, -1, 0, 10, 0, 0, false, false);
        new WeighCalculator().calculateStepsFor(2, route, brew, brew.getCostFor(2));
        assertEquals(6, route.getCurrentSteps());
    }
}