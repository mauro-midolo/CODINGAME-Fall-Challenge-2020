import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BestBrewChooserTest {
    @Test
    public void shouldReturnTheBestRecipes() {
        PlayerInventory me = new PlayerInventory(4, 4, 4, 4, 0);
        List<Component> brews = Arrays.asList(
                new Brew(1, "BREW", -1, -1, -1, -1, 10, 0, 0, false, false),
                new Brew(10, "BREW", -1, -1, -1, -1, 5, 0, 0, false, false),
                new Brew(4, "BREW", -1, -1, -1, -1, 15, 0, 0, false, false)
        );
        Component choose = new BestBrewChooser().getBest(me, brews);
        assertEquals("4", choose.getActionId());
    }

    @Test
    public void shouldReturnTheBestRecipesThatCanITake() {
        PlayerInventory me = new PlayerInventory(4, 4, 4, 4, 0);
        List<Component> brews = Arrays.asList(
                new Brew(1, "BREW", -1, -1, -1, -1, 10, 0, 0, false, false),
                new Brew(10, "BREW", -1, -1, -1, -1, 5, 0, 0, false, false),
                new Brew(4, "BREW", -1, -10, -1, -1, 15, 0, 0, false, false)
        );
        Component choose = new BestBrewChooser().getBest(me, brews);
        assertEquals("1", choose.getActionId());
    }

    @Test
    public void shouldReturnWaitInCaseNoAvailable() {
        PlayerInventory me = new PlayerInventory(4, 4, 4, 4, 0);
        List<Component> brews = Arrays.asList(
                new Brew(1, "BREW", -1, -10, -1, -1, 10, 0, 0, false, false),
                new Brew(10, "BREW", -1, -10, -1, -1, 5, 0, 0, false, false),
                new Brew(4, "BREW", -1, -10, -1, -1, 15, 0, 0, false, false)
        );
        Component choose = new BestBrewChooser().getBest(me, brews);
        assertEquals("WAIT", choose.getActionType());
    }
}