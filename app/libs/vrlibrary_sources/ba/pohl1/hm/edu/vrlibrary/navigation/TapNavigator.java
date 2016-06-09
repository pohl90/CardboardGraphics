package ba.pohl1.hm.edu.vrlibrary.navigation;

import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.util.CGOptions;

/**
 * Created by Pohl on 04.03.2016.
 */
public class TapNavigator implements VRNavigator {

    protected boolean enabled;

    @Override
    public String getToast() {
        return "Tap to trigger navigation";
    }

    @Override
    public void navigate(final VRCamera camera, final float deltaMove) {
        // Move the camera here
        if(enabled) {
            camera.moveForward(deltaMove * CGOptions.CAMERA_SPEED_MODIFIER);
        }
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public void onCardboardTrigger() {
        if(enabled) {
            disable();
        } else {
            enable();
        }
    }

    @Override
    public void dispose() {
        // Nothing to dispose
    }
}
