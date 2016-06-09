package ba.pohl1.hm.edu.vrlibrary.bluetooth;

import android.view.KeyEvent;

/**
 * Created by Pohl on 17.04.2016.
 */
public enum GameControllerAction {

    UP(KeyEvent.KEYCODE_DPAD_UP), DOWN(KeyEvent.KEYCODE_DPAD_DOWN),
    LEFT(KeyEvent.KEYCODE_DPAD_LEFT), RIGHT(KeyEvent.KEYCODE_DPAD_RIGHT),
    UP2(KeyEvent.KEYCODE_I), DOWN2(KeyEvent.KEYCODE_K),
    LEFT2(KeyEvent.KEYCODE_J), RIGHT2(KeyEvent.KEYCODE_L),
    A(KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_DEL), B(KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_B, KeyEvent.KEYCODE_SPACE),
    X(KeyEvent.KEYCODE_1), Y(KeyEvent.KEYCODE_4),
    R1(KeyEvent.KEYCODE_6), R2(KeyEvent.KEYCODE_8),
    L1(KeyEvent.KEYCODE_5), L2(KeyEvent.KEYCODE_7);

    private int[] ids;
    private boolean active = false;

    GameControllerAction(final int... ids) {
        this.ids = ids;
    }

    public static GameControllerAction fromId(final int id) {
        for(final GameControllerAction action : values()) {
            for(final int actionId : action.getIds()) {
                if(actionId == id) {
                    return action;
                }
            }
        }
        return null;
    }

    public static float getAccelerator() {
        return R2.isActive() ? 2.0f : 1.0f;
    }

    public int[] getIds() {
        return ids;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
