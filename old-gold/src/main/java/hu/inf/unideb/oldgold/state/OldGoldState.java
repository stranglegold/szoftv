package hu.inf.unideb.oldgold.state;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Az osztály, két játékos által játszott old-gold aktuális állapotát reprezentálja.
 */
@Slf4j
public class OldGoldState {

    /**
     * A játék aktuális állapota.
     */
    @Getter
    private int[] state = {0, 1, 0, 1, 1, 0, 1, 0, 0, 2, 0, 0, 0, 1};
    
    public OldGoldState() {
    }

    public OldGoldState(int[] state) {
        this.state = state;
    }

    /**
     * @param from Az az index amelyről a játékos szeretné mozgatni a coinját.
     * @param to   Az az index amelyre szeretné mozgatni a játékos a coinját.
     * @throws UnsupportedOperationException Ha a játékos olyan helyre próbál
     *                                       lépni amely nem lehetséges a játékszabályok szerint.
     */
    public void step(int from, int to) throws UnsupportedOperationException {
        if (isFirstCoin(from)) {
            remove(from);
            return;
        }

        isValidStep(from, to);
        doStep(from, to);

    }

    /**
     * Eldönti, hogy valid-e az aktuális lépés. Ha nem akkor {@code UnsupportedOperationException} dob.
     *
     * @param from Az az index amelyről a játékos szeretné mozgatni a coinját.
     * @param to   Az az index amelyre szeretné mozgatni a játékos a coinját.
     */
    private void isValidStep(int from, int to) {
        if (to >= from || state[from] == 0 || state[to] != 0) {
            throw new UnsupportedOperationException();
        }

        for (int i = to + 1; i < from; i++) {
            if (state[i] != 0) {
                throw new UnsupportedOperationException();
            }
        }
    }

    /**
     * A lépés megtételéért felel.
     *
     * @param from Az az index amelyről a játékos szeretné mozgatni a coinját.
     * @param to   Az az index amelyre szeretné mozgatni a játékos a coinját.
     */
    private void doStep(int from, int to) {
        state[to] = state[from];
        state[from] = 0;
        log.info("Successfully step from(index) {} to {} with {} figure.", from, to, state[from]);
    }

    /**
     * Eltávolítja a megadott helyről a coint.
     *
     * @param from Az az index amelyről el kell távolítani a coint.
     */
    private void remove(int from) {
        state[from] = 0;
        log.info("Successfully removed from(index) {}, the {} figure.", from, state[from]);
    }

    /**
     * Eldönti, hogy a sorban a megadott indexen lévő coin az első-e.
     *
     * @param coinIndex A coin indexe.
     * @return Igaz, ha az első. Hamis ha nem az első a tömbben.
     */
    public boolean isFirstCoin(int coinIndex) {
        if (state[coinIndex] == 0) {
            return false;
        }

        for (int i = 0; i < coinIndex; i++) {
            if (state[i] != 0) return false;
        }
        return true;
    }

    /**
     * Eldönti, hogy az aktuális állapot célállapot-e.
     *
     * @return Igaz, ha célállapotban vagyunk. Hamis ha nem cél állapotban vagyunk.
     */
    public boolean isGoal() {
        for (int num : state) {
            if (num == 2) return false;
        }
        return true;
    }

}
