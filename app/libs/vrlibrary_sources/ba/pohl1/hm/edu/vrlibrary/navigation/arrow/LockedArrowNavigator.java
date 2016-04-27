package ba.pohl1.hm.edu.vrlibrary.navigation.arrow;

import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;
import ba.pohl1.hm.edu.vrlibrary.model.VRArrow;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.util.CGOptions;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;

/**
 * Created by Pohl on 07.04.2016.
 */
public class LockedArrowNavigator extends ArrowNavigator {

    private Vector3 savedForwardVector = new Vector3();

    @Override
    public void navigate(final VRCamera camera, float deltaMove) {
        arrowForward.transform(deltaMove);
        arrowBackward.transform(deltaMove);
        // Move the camera here
        if(enabled) {
            final float deltaDir = currentArrow.getDirection() == VRArrow.ArrowDirection.FORWARD ? deltaMove : -deltaMove;
            camera.move(savedForwardVector, deltaDir * CGOptions.CAMERA_SPEED_MODIFIER);
        }
    }

    @Override
    public void enable() {
        savedForwardVector.set(CardboardGraphics.camera.getForwardVector());
        super.enable();
    }
}
