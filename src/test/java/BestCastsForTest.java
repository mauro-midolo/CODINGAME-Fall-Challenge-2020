import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class BestCastsForTest {

    @Test
    public void shouldCalculateNumberOfStepsConsideringInventory2() {
        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(10, "CAST", -1, +1, 0, 0, 0, 0, 0, true, false)
        );

        PlayerInventory playerInventory = new PlayerInventory(1, 0, 0, 0, 0);

        Brew brew = new Brew(42, Brew.COMPONENT, 0, -1, 0, 0, 10, 00, 0, false, false);
        Component command = new BestCastChooser().getBest(playerInventory, Collections.singletonList(brew), casts);
        assertEquals("CAST 10", command.toString());
    }

}