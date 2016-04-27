package ba.pohl1.hm.edu.vrlibrary.bluetooth;

import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;

/**
 * Created by Pohl on 17.04.2016.
 */
public class GameController {
    public static GameControllerAction UP = GameControllerAction.UP;
    public static GameControllerAction LEFT = GameControllerAction.LEFT;
    public static GameControllerAction RIGHT = GameControllerAction.RIGHT;
    public static GameControllerAction DOWN = GameControllerAction.DOWN;
    public static GameControllerAction A = GameControllerAction.A;
    public static GameControllerAction B = GameControllerAction.B;

    private static List<GameControllerAction> actions = new ArrayList<>();

    static {
        actions.add(UP);
        actions.add(DOWN);
        actions.add(LEFT);
        actions.add(RIGHT);
        actions.add(DOWN);
        actions.add(A);
        actions.add(B);
    }

    private VRCamera camera;
    private Vector3 directionVector = new Vector3();

    public GameController() {
        this.camera = CardboardGraphics.camera;

    }

    public static boolean isGameControllerDevice(InputEvent event) {
        // Check that input comes from a device with directional pads.
        return (event.getSource() & InputDevice.SOURCE_DPAD)
                != InputDevice.SOURCE_DPAD;
    }

    public void processMovement(final float delta) {

        for(final GameControllerAction action : actions) {
            if(!action.isActive()) {
                // If the action is not enabled, then just continue with the next one
                continue;
            }
            switch(action) {
                case UP:
                    directionVector.set(camera.getForwardVector().x, 0, camera.getForwardVector().z).normalize();
                    camera.move(directionVector, delta);
                    break;
                case DOWN:
                    directionVector.set(camera.getForwardVector().x, 0, camera.getForwardVector().z).normalize();
                    camera.move(directionVector, -delta);
                    break;
                case LEFT:
                    camera.move(Vector3.getPerpendicularXZ(camera.getForwardVector()), delta);
                    break;
                case RIGHT:
                    camera.move(Vector3.getPerpendicularXZ(camera.getForwardVector()), -delta);
                    break;
                case A:
                    camera.move(Vector3.up, delta);
                    break;
                case B:
                    camera.move(Vector3.down, delta);
                    break;
            }
        }
    }

    public void updateInputEvent(InputEvent event) {
        if(!isGameControllerDevice(event)) {
            return;
        }

        // If the input event is a KeyEvent, check its key code.
        if(event instanceof KeyEvent) {
            // Use the key code to find the D-pad direction.
            KeyEvent keyEvent = (KeyEvent) event;

            final int keyCode = keyEvent.getKeyCode();
            final GameControllerAction action = GameControllerAction.fromId(keyCode);
            if(action != null) {
                action.setActive(keyEvent.getAction() == KeyEvent.ACTION_DOWN);
            }
        }
    }
}
