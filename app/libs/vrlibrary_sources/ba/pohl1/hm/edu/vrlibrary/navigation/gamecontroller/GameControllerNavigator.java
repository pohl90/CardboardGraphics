package ba.pohl1.hm.edu.vrlibrary.navigation.gamecontroller;

import android.util.Log;
import android.view.InputEvent;

import ba.pohl1.hm.edu.vrlibrary.bluetooth.GameController;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRNavigator;

/**
 * Created by Pohl on 17.04.2016.
 */
public class GameControllerNavigator implements VRNavigator {

    protected GameController gameController;

    public GameControllerNavigator() {
        gameController = new GameController();
    }

    @Override
    public String getToast() {
        return "Use DPAD to move. A and B to move up and down";
    }

    public void controllerEvent(final InputEvent event) {
        if(GameController.isGameControllerDevice(event)) {
            final float eventTime = event.getEventTime();
            Log.i("EventTime", "" + eventTime);
            // Process all historical movement samples in the batch
            gameController.updateInputEvent(event);
        }
    }

    @Override
    public void navigate(final VRCamera camera, float deltaMove) {
        gameController.processMovement(deltaMove);
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void onCardboardTrigger() {

    }

    @Override
    public void dispose() {

    }
}
