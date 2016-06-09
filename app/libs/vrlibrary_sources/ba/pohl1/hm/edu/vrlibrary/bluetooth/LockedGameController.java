package ba.pohl1.hm.edu.vrlibrary.bluetooth;

import java.util.HashMap;
import java.util.Map;

import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;

/**
 * Created by Pohl on 09.06.2016.
 */
public class LockedGameController extends GameController {

    private Map<GameControllerAction, Vector3> actionVectors = new HashMap<>();

    @Override
    protected void setActionActive(GameControllerAction action, boolean active) {
        if (!action.isActive()) {
            actionVectors.put(action, super.getDirectionVector(action));
        }
        super.setActionActive(action, active);
    }

    @Override
    protected Vector3 getDirectionVector(GameControllerAction action) {
        if (actionVectors.containsKey(action)) {
            return actionVectors.get(action);
        }
        return Vector3.zero;
    }
}
