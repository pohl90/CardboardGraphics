package cg.edu.hm.pohl.navigator;

import ba.pohl1.hm.edu.vrlibrary.base.Options;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRNavigator;
import cg.edu.hm.pohl.VRRoom;
import cg.edu.hm.pohl.student.StudentScene;

/**
 * Created by Pohl on 14.04.2016.
 */
public class Navigator implements VRNavigator {

    private final VRCamera camera;
    private final VRRoom vrRoom;
    private final StudentScene studentScene;

    protected boolean enabled;

    public Navigator(VRCamera camera, VRRoom vrRoom, StudentScene studentScene) {
        this.camera = camera;
        this.vrRoom = vrRoom;
        this.studentScene = studentScene;
    }

    @Override
    public void navigate(final float deltaMove) {
        // Move the camera here
        if(enabled) {
            camera.moveForward(deltaMove * Options.CAMERA_SPEED_MODIFIER);
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
