package ba.pohl1.hm.edu.vrlibrary.navigation.gamecontroller;

import ba.pohl1.hm.edu.vrlibrary.bluetooth.LockedGameController;

/**
 * Created by Pohl on 09.06.2016.
 */
public class LockedGameControllerNavigator extends GameControllerNavigator {

    public LockedGameControllerNavigator() {
        gameController = new LockedGameController();
    }
}
