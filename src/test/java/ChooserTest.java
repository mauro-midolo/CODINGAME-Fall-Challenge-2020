import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ChooserTest {
    @Test
    public void shouldReturnBestRecipe() {
        PlayerInventory me = new PlayerInventory(4, 4, 4, 4, 0);
        PlayerInventory oppositeInventory = new PlayerInventory(0, 0, 0, 0, 0);
        List<Component> brews = Arrays.asList(
                new Brew(1, "BREW", -1, -1, -1, -1, 10, 0, 0, false, false),
                new Brew(10, "BREW", -1, -1, -1, -1, 5, 0, 0, false, false),
                new Brew(4, "BREW", -1, -1, -1, -1, 15, 0, 0, false, false)
        );
        String choose = new Chooser().getBest(me, oppositeInventory, brews, Collections.emptyList(), Collections.emptyList());
        assertEquals("BREW 4", choose);
    }

    @Test
    public void shouldReturnWaitInCaseNoBrewAreAvailable() {
        PlayerInventory me = new PlayerInventory(4, 4, 4, 4, 0);
        PlayerInventory oppositeInventory = new PlayerInventory(0, 0, 0, 0, 0);
        List<Component> brews = Arrays.asList(
                new Brew(1, "BREW", -1, -10, -1, -1, 10, 0, 0, false, false),
                new Brew(10, "BREW", -1, -10, -1, -1, 5, 0, 0, false, false),
                new Brew(4, "BREW", -1, -10, -1, -1, 15, 0, 0, false, false)
        );
        String choose = new Chooser().getBest(me, oppositeInventory, brews, Collections.emptyList(), Collections.emptyList());
        assertEquals("WAIT", choose);
    }

    @Test
    public void shouldRestToContinue() {
        PlayerInventory me = new PlayerInventory(2, 0, 0, 1, 0);
        PlayerInventory oppositeInventory = new PlayerInventory(0, 0, 0, 0, 0);
        List<Component> brews = Arrays.asList(
                new Brew(1, "BREW", -2, 0, 0, -2, 10, 0, 0, false, false)
        );

        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", 2, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(2, "CAST", -1, +1, 0, 0, 0, 0, 0, false, false),
                new Cast(3, "CAST", 0, -1, +1, 0, 0, 0, 0, false, false),
                new Cast(4, "CAST", 0, 0, -1, 1, 0, 0, 0, false, false)
        );

        String choose = new Chooser().getBest(me, oppositeInventory, brews, casts, Collections.emptyList());
        assertEquals("REST", choose);
    }


    @Test
    public void shouldWaitInCaseNoAction() {
        PlayerInventory me = new PlayerInventory(0, 0, 0, 1, 0);
        PlayerInventory oppositeInventory = new PlayerInventory(0, 0, 0, 0, 0);
        List<Component> brews = Collections.singletonList(
                new Brew(1, "BREW", -2, 0, 0, 0, 10, 0, 0, false, false)
        );

        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", 0, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(2, "CAST", 0, +1, 0, 0, 0, 0, 0, false, false),
                new Cast(3, "CAST", 0, -1, +1, 0, 0, 0, 0, false, false),
                new Cast(4, "CAST", 0, 0, -1, 1, 0, 0, 0, false, false)
        );

        String choose = new Chooser().getBest(me, oppositeInventory, brews, casts, Collections.emptyList());
        assertEquals("WAIT", choose);
    }

    @Test
    public void shouldExcludeACastIfNotAvailableSpaceInInventory() {
        PlayerInventory me = new PlayerInventory(0, 0, 10, 0, 0);
        PlayerInventory oppositeInventory = new PlayerInventory(0, 0, 0, 0, 0);
        List<Component> brews = Arrays.asList(
                new Brew(1, "BREW", -1, 0, 0, 0, 10, 0, 0, false, false),
                new Brew(2, "BREW", 0, -2, 0, 0, 5, 0, 0, false, false)
        );

        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(2, "CAST", 0, +1, -1, 0, 0, 0, 0, true, false)
        );

        String choose = new Chooser().getBest(me, oppositeInventory, brews, casts, Collections.emptyList());
        assertEquals("CAST 2", choose);
    }

    @Test
    public void shouldExcludeACastIfNotAvailableSpaceInInventory2() {
        PlayerInventory me = new PlayerInventory(0, 0, 0, 8, 0);
        PlayerInventory oppositeInventory = new PlayerInventory(0, 0, 0, 0, 0);
        List<Component> brews = Arrays.asList(
                new Brew(1, "BREW", -2, 0, 0, 0, 10, 0, 0, false, false)
        );

        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +3, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(2, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false)
        );
        String choose = new Chooser().getBest(me, oppositeInventory, brews, casts, Collections.emptyList());
        assertEquals("CAST 2", choose);
    }

    @Test
    public void shouldConsideringOppositeScore() {
        PlayerInventory me = new PlayerInventory(1, 0, 0, 0, 0);
        PlayerInventory oppositeInventory = new PlayerInventory(2, 0, 0, 0, 0);
        List<Component> brews = Arrays.asList(
                //3 steps for me;  3 steps for opposite
                new Brew(1, "BREW", 0, -2, 0, 0, 10, 0, 0, false, false),
                //3 steps for me; 1 step for opposite
                new Brew(2, "BREW", -3, 0, 0, 0, 15, 0, 0, false, false)
        );

        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(2, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false)
        );
        List<Component> oppositeCasts = Arrays.asList(
                new Cast(1, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false),
                new Cast(2, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false)
        );
        String choose = new Chooser().getBest(me, oppositeInventory, brews, casts, oppositeCasts);
        assertEquals("CAST 1", choose);
    }

    @Test
    public void shouldConsideringInventoryEvolution() {
        PlayerInventory me = new PlayerInventory(0, 0, 0, 0, 0);
        List<Component> brews = Arrays.asList(
                new Brew(1, "BREW", -1, -1, -1, 0, 2, 0, 0, false, false),
                new Brew(2, "BREW", 0, 0, 0, -2, 2, 0, 0, false, false)
        );

        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", +1, +1, +1, 0, 0, 0, 0, true, false),
                new Cast(2, "CAST", 0, 0, 0, +1, 0, 0, 0, true, false),
                new Cast(3, "CAST", 0, 0, +1, +1, 0, 0, 0, true, false)
        );
        String choose = new Chooser().getBest(me, me, brews, casts, casts);
        assertEquals("CAST 1", choose);
    }

    @Test
    public void shouldConsideringStepWithOpposite() {
        PlayerInventory me = new PlayerInventory(0, 0, 0, 0, 0);
        PlayerInventory other = new PlayerInventory(0, 0, 0, 0, 3);
        List<Component> brews = Arrays.asList(
                new Brew(1, "BREW", -2, -2, 0, 0, 5, 0, 0, false, false),
                new Brew(2, "BREW", 0, 0, 0, -1, 2, 0, 0, false, false)
        );

        List<Component> casts = Arrays.asList(
                new Cast(1, "CAST", 0, 0, 0, +1, 0, 0, 0, true, false),
                new Cast(2, "CAST", +1, 0, 0, 0, 0, 0, 0, true, false),
                new Cast(3, "CAST", 0, +1, 0, 0, 0, 0, 0, true, false)
        );
        String choose = new Chooser().getBest(me, other, brews, casts, casts);
        assertTrue(choose + " is not contains", Arrays.asList("CAST 2", "CAST 3").contains(choose));
    }

}