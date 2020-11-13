import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ChooserTest {
    @Test
    public void shouldReturnBestRecipe() {
        PlayerInventory me = new PlayerInventory(4, 4, 4, 4, 0);
        List<Component> brews = Arrays.asList(
                new Brew(1, "BREW", -1, -1, -1, -1, 10, 0, 0, false, false),
                new Brew(10, "BREW", -1, -1, -1, -1, 5, 0, 0, false, false),
                new Brew(4, "BREW", -1, -1, -1, -1, 15, 0, 0, false, false)
        );
        String choose = new Chooser().getBest(me, brews, Collections.emptyList());
        assertEquals("BREW 4", choose);
    }

    @Test
    public void shouldReturnWaitInCaseNoBrewAreAvailable() {
        PlayerInventory me = new PlayerInventory(4, 4, 4, 4, 0);
        List<Component> brews = Arrays.asList(
                new Brew(1, "BREW", -1, -10, -1, -1, 10, 0, 0, false, false),
                new Brew(10, "BREW", -1, -10, -1, -1, 5, 0, 0, false, false),
                new Brew(4, "BREW", -1, -10, -1, -1, 15, 0, 0, false, false)
        );
        String choose = new Chooser().getBest(me, brews, Collections.emptyList());
        assertEquals("WAIT", choose);
    }

}