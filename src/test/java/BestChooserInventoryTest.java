import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BestChooserInventoryTest {
    @Test
    public void shouldReturnTheBestRecipes() {
        PlayerInventory me = new PlayerInventory(4, 4, 4, 4, 0);
        List<Recipe> recipes = Arrays.asList(
                new Recipe(1, "BREW", -1, -1, -1, -1, 10, 0, 0, false, false),
                new Recipe(10, "BREW", -1, -1, -1, -1, 5, 0, 0, false, false),
                new Recipe(4, "BREW", -1, -1, -1, -1, 15, 0, 0, false, false)
        );
        String choose = new BestChooserInventory().getBest(me, recipes);
        assertEquals("4", choose);
    }

    @Test
    public void shouldReturnTheBestRecipesThatCanITake() {
        PlayerInventory me = new PlayerInventory(4, 4, 4, 4, 0);
        List<Recipe> recipes = Arrays.asList(
                new Recipe(1, "BREW", -1, -1, -1, -1, 10, 0, 0, false, false),
                new Recipe(10, "BREW", -1, -1, -1, -1, 5, 0, 0, false, false),
                new Recipe(4, "BREW", -1, -10, -1, -1, 15, 0, 0, false, false)
        );
        String choose = new BestChooserInventory().getBest(me, recipes);
        assertEquals("1", choose);
    }

    @Test
    public void shouldReturnWaitInCaseNoAvailable() {
        PlayerInventory me = new PlayerInventory(4, 4, 4, 4, 0);
        List<Recipe> recipes = Arrays.asList(
                new Recipe(1, "BREW", -1, -10, -1, -1, 10, 0, 0, false, false),
                new Recipe(10, "BREW", -1, -10, -1, -1, 5, 0, 0, false, false),
                new Recipe(4, "BREW", -1, -10, -1, -1, 15, 0, 0, false, false)
        );
        String choose = new BestChooserInventory().getBest(me, recipes);
        assertEquals("WAIT", choose);
    }
}