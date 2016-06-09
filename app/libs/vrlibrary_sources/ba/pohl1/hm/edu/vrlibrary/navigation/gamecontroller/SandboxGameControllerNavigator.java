package ba.pohl1.hm.edu.vrlibrary.navigation.gamecontroller;

import ba.pohl1.hm.edu.vrlibrary.bluetooth.SandboxGameController;

/**
 * Created by Pohl on 17.04.2016.
 */
public class SandboxGameControllerNavigator extends GameControllerNavigator {

    public SandboxGameControllerNavigator() {
        gameController = new SandboxGameController();
    }

    @Override
    public String getToast() {
        return "Use DPAD to move. A and B to move up and down. X and Y to spawn a cube.";
    }
}
