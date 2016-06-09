package ba.pohl1.hm.edu.vrlibrary.bluetooth;

import android.view.KeyEvent;

/**
 * Created by Pohl on 17.04.2016.
 */
public enum GameControllerAction {

    UP(KeyEvent.KEYCODE_DPAD_UP), DOWN(KeyEvent.KEYCODE_DPAD_DOWN),
    LEFT(KeyEvent.KEYCODE_DPAD_LEFT), RIGHT(KeyEvent.KEYCODE_DPAD_RIGHT),
    A(KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_A), B(KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_B);

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
