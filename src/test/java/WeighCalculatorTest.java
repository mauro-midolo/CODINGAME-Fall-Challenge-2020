import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class WeighCalculatorTest {

    @Test
    public void shouldCalculateNumberOfSteps() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", +2, -1, 0, 0, 0, 0, 0, true, false),
                new Cast(15, "CAST", -1, 0, +1, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(0,casts);
                new WeighCalculator().calculateStepsFor(2, route);
        assertEquals(3, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfStep2s() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", +2, -1, 0, 0, 0, 0, 0, true, false),
                new Cast(15, "CAST", -1, 0, +1, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(0, casts);
        new WeighCalculator().calculateStepsFor(2, route);
        assertEquals(3, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfSteps3() throws IOException {
        List<Component> casts = Arrays.asList(
               new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
               new Cast(10, "CAST", -2, +1, 0, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(0, casts);
        new WeighCalculator().calculateStepsFor(1, route);
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

        Route route = new Route(0, casts);
        new WeighCalculator().calculateStepsFor(RupeesIndexer.BLUE.getIndex(), route);
        assertEquals(6, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfStepsButMinimumComplex() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, -1, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(11, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(0, casts);
        new WeighCalculator().calculateStepsFor(RupeesIndexer.BLUE.getIndex(), route);
        assertEquals(1, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfSteps4() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", -1, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(12, "CAST", 0, -2, +1, 0, 0, 0, 0, true, false)
        );

        Route route = new Route(0, casts);
        new WeighCalculator().calculateStepsFor(RupeesIndexer.ORANGE.getIndex(), route);
        assertEquals(6, route.getCurrentSteps());
    }

    @Test
    public void shouldCalculateNumberOfSteps5() {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", -1, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(12, "CAST", 0, -2, +1, 0, 0, 0, 0, true, false)
        );

        Map<Integer, Integer> resultSteps = new WeighCalculator().calculateSteps(casts);
        assertEquals(1, resultSteps.get(RupeesIndexer.BLUE.getIndex()).intValue());
        assertEquals(2, resultSteps.get(RupeesIndexer.GREEN.getIndex()).intValue());
        assertEquals(6, resultSteps.get(RupeesIndexer.ORANGE.getIndex()).intValue());
    }
}