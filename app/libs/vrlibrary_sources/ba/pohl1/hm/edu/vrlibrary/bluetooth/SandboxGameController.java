package ba.pohl1.hm.edu.vrlibrary.bluetooth;

import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.model.shapes.Cube;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusManager;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;

/**
 * Created by Pohl on 06.05.2016.
 */
public class SandboxGameController extends GameController {

    private Vector3 helpPositionVector = new Vector3();
    private Vector3 helpForwardVector = new Vector3();
    private Cube cube;
    private VRComponent focusedComponent;
    private boolean spawned;
    private boolean actionYActive;

    @Override
    protected void processAction(GameControllerAction action, float delta) {
        switch (action) {
            case X:
                if (!spawned) {
                    spawnCube();
                    spawned = true;
                }
                break;
            case Y:
                if (!actionYActive) {
                    actionYActive = true;
                    focusedComponent = FocusManager.getInstance().getMostRecentFocusTarget();
                }
                if (focusedComponent != null) {
                    placeInFrontOfCamera(focusedComponent);
                }
                break;
            case UP2:
                if (hasFocusedComponent()) {
                    if (L2.isActive()) {
                        focusedComponent.translateY(delta * 2);
                    } else {
                        focusedComponent.rotateY(delta * 2);
                    }
                }
                break;
            case DOWN2:
                if (hasFocusedComponent()) {
                    if (L2.isActive()) {
                        focusedComponent.translateY(-delta * 2);
                    } else {
                        focusedComponent.rotateY(-delta * 2);
                    }
                }
                break;
            case LEFT2:
                if (hasFocusedComponent()) {
                    if (L2.isActive()) {
                        focusedComponent.translateX(delta * 2);
                    } else if (L1.isActive()) {
                        focusedComponent.translateZ(delta * 2);
                    } else {
                        focusedComponent.rotateX(delta * 2);
                    }
                }
                break;
            case RIGHT2:
                if (hasFocusedComponent()) {
                    if (L2.isActive()) {
                        focusedComponent.translateX(-delta * 2);
                    } else if (L1.isActive()) {
                        focusedComponent.translateZ(-delta * 2);
                    } else {
                        focusedComponent.rotateX(-delta * 2);
                    }
                }
                break;
            default:
                super.processAction(action, delta);
                break;
        }
    }

    @Override
    protected void setActionActive(GameControllerAction action, boolean active) {
        if (action == GameControllerAction.X && !active) {
            spawned = false;
        }
        if (action == GameControllerAction.Y && !active) {
            actionYActive = false;
        }
        super.setActionActive(action, active);
    }

    private boolean hasFocusedComponent() {
        return focusedComponent != null;
    }

    private void spawnCube() {
        cube = new Cube(CGUtils.randomColor());
        FocusManager.getInstance().register(cube, CGConstants.DEFAULT_FOCUS_DEGREE_LIMIT);
        placeInFrontOfCamera(cube);
    }

    private void placeInFrontOfCamera(final VRComponent component) {
        final VRCamera camera = CardboardGraphics.camera;
        final Vector3 objectPosition = component.getPosition();
        final Vector3 position = camera.getPosition();
        final Vector3 forwardVector = camera.getForwardVector();

        helpPositionVector.set(position).mult(-1);
        helpForwardVector.set(forwardVector);
        helpForwardVector.normalize();
        helpForwardVector.mult(new Vector3(objectPosition).sub(helpPositionVector).length());

        helpPositionVector.add(helpForwardVector);

        component.setX(helpPositionVector.x);
        component.setY(helpPositionVector.y);
        component.setZ(helpPositionVector.z);
    }
}
