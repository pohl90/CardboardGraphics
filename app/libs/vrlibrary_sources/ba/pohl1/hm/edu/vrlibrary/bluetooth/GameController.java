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
    public static GameControllerAction UP2 = GameControllerAction.UP2;
    public static GameControllerAction LEFT2 = GameControllerAction.LEFT2;
    public static GameControllerAction RIGHT2 = GameControllerAction.RIGHT2;
    public static GameControllerAction DOWN2 = GameControllerAction.DOWN2;
    public static GameControllerAction A = GameControllerAction.A;
    public static GameControllerAction B = GameControllerAction.B;
    public static GameControllerAction X = GameControllerAction.X;
    public static GameControllerAction Y = GameControllerAction.Y;
    public static GameControllerAction R1 = GameControllerAction.R1;
    public static GameControllerAction R2 = GameControllerAction.R2;
    public static GameControllerAction L1 = GameControllerAction.L1;
    public static GameControllerAction L2 = GameControllerAction.L2;

    private static List<GameControllerAction> actions = new ArrayList<>();
    private static Vector3 helpVector = new Vector3();

    static {
        actions.add(UP);
        actions.add(DOWN);
        actions.add(LEFT);
        actions.add(RIGHT);
        actions.add(UP2);
        actions.add(DOWN2);
        actions.add(LEFT2);
        actions.add(RIGHT2);
        actions.add(A);
        actions.add(B);
        actions.add(X);
        actions.add(Y);
        actions.add(R1);
        actions.add(R2);
        actions.add(L1);
        actions.add(L2);
    }

    protected VRCamera camera;

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
            processAction(action, delta * GameControllerAction.getAccelerator());
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
                setActionActive(action, keyEvent.getAction() == KeyEvent.ACTION_DOWN);
            }
        }
    }

    protected void setActionActive(final GameControllerAction action, final boolean active) {
        action.setActive(active);
    }

    protected void processAction(final GameControllerAction action, final float delta) {
        camera.move(getDirectionVector(action), delta);
    }

    protected Vector3 getDirectionVector(final GameControllerAction action) {
        switch (action) {
            case UP:
                helpVector.x = camera.getForwardVector().x;
                helpVector.y = 0;
                helpVector.z = camera.getForwardVector().z;
                helpVector.normalize();
                break;
            case DOWN:
                helpVector.x = -camera.getForwardVector().x;
                helpVector.y = 0;
                helpVector.z = -camera.getForwardVector().z;
                helpVector.normalize();
                break;
            case LEFT:
                helpVector.set(Vector3.getPerpendicularXZ(camera.getForwardVector()));
                break;
            case RIGHT:
                helpVector.set(Vector3.getPerpendicularXZ(camera.getForwardVector())).mult(-1);
                break;
            case A:
                helpVector.set(Vector3.up);
                break;
            case B:
                helpVector.set(Vector3.down);
                break;
        }
        return helpVector;
    }
}
