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

        int resultSteps = new WeighCalculator().calculateStepsFor(2, casts);
        assertEquals(3, resultSteps);
    }

    @Test
    public void shouldCalculateNumberOfStep2s() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", +2, -1, 0, 0, 0, 0, 0, true, false),
                new Cast(15, "CAST", -1, 0, +1, 0, 0, 0, 0, true, false)
        );

        int resultSteps = new WeighCalculator().calculateStepsFor(2, casts);
        assertEquals(3, resultSteps);
    }

    @Test
    public void shouldCalculateNumberOfSteps3() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", -2, +1, 0, 0, 0, 0, 0, true, false)
        );

        int resultSteps = new WeighCalculator().calculateStepsFor(1, casts);
        assertEquals(4, resultSteps);
    }
    @Test
    public void shouldCalculateNumberOfSteps4() throws IOException {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", -1, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(12, "CAST", 0, -2, +1, 0, 0, 0, 0, true, false)
        );

        int resultSteps = new WeighCalculator().calculateStepsFor(2, casts);
        assertEquals(6, resultSteps);
    }

    @Test
    public void shouldCalculateNumberOfSteps5() {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", -1, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(12, "CAST", 0, -2, +1, 0, 0, 0, 0, true, false)
        );

        Map<Integer, Integer> resultSteps = new WeighCalculator().calculateSteps(casts);
        assertEquals(1, resultSteps.get(0).intValue());
        assertEquals(2, resultSteps.get(1).intValue());
        assertEquals(6, resultSteps.get(2).intValue());
    }

}