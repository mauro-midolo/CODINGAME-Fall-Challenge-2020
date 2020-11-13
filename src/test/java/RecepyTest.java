import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RecepyTest {
    @Test
    public void shouldOrderDescende() {
        List<Recepy> recepies = Arrays.asList(
                new Recepy(1, "", 0, 0, 0, 0, 5, 0, 0, false, false),
        new Recepy(2,"",0,0,0,0,7,0,0,false,false),
                new Recepy(3,"",0,0,0,0,2,0,0,false,false)
        );
        Collections.sort(recepies);
        assertEquals(2,recepies.get(0).getActionId());
    }
}