package hu.inf.unideb.oldgold.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OldGoldStateTest {


    @Test
    void testStep() {
        assertThrows(UnsupportedOperationException.class, () -> new OldGoldState().step(7, 8));
        assertThrows(UnsupportedOperationException.class, () -> new OldGoldState().step(3, 0));
        assertThrows(UnsupportedOperationException.class, () -> new OldGoldState().step(0, 3));

        OldGoldState oldGoldState = new OldGoldState();
        oldGoldState.step(1, 0);
        assertNotEquals(oldGoldState.getState(), new int[]{1, 0, 0, 1, 1, 0, 1, 0, 0, 2, 0, 0, 0, 1});
        oldGoldState.step(3, 1);
        assertNotEquals(oldGoldState.getState(), new int[]{1, 1, 0, 0, 1, 0, 1, 0, 0, 2, 0, 0, 0, 1});
    }

    @Test
    void testIsGoal() {
        OldGoldState oldGoldState = new OldGoldState();
        assertFalse(oldGoldState.isGoal());
        oldGoldState = new OldGoldState(new int[]{0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1});
        assertTrue(oldGoldState.isGoal());
    }
}
