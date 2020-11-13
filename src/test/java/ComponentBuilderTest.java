import org.junit.Test;

import static org.junit.Assert.*;

public class ComponentBuilderTest {
    @Test
    public void shouldReturnBrewWhenItIsBrew() {
        Component brew = ComponentBuilder.build(1, "BREW", -1, -1, -1, -1, 10, 0, 0, false, false);
        assertEquals(Brew.class.getCanonicalName(), brew.getClass().getCanonicalName());
    }

    @Test
    public void shouldReturnCastWhenItIsCast() {
        Component cast = ComponentBuilder.build(1, "CAST", -1, -1, -1, -1, 10, 0, 0, false, false);
        assertEquals(Cast.class.getCanonicalName(), cast.getClass().getCanonicalName());
    }

    @Test
    public void shouldReturnOpponentCastWhenItIsCast() {
        Component opponentCast = ComponentBuilder.build(1, "OPPONENT_CAST", -1, -1, -1, -1, 10, 0, 0, false, false);
        assertEquals(OpponentCast.class.getCanonicalName(), opponentCast.getClass().getCanonicalName());
    }
}