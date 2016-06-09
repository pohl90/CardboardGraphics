package ba.pohl1.hm.edu.vrlibrary.navigation.platform;

import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.model.shapes.Plane;
import ba.pohl1.hm.edu.vrlibrary.navigation.TapNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRDrawableNavigator;
import ba.pohl1.hm.edu.vrlibrary.physics.transitions.TranslateTransition;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;

/**
 * Created by Pohl on 11.05.2016.
 */
public class PlatformNavigator extends TapNavigator implements VRDrawableNavigator {

    private Plane platform = new Plane();

    private Vector3 platformPosition = new Vector3();

    public PlatformNavigator() {
        platform.setColor(CGConstants.COLOR_WHITE);
    }

    @Override
    public void dispose() {
        platform.remove(true);
        super.dispose();
    }

    @Override
    public void navigate(VRCamera camera, float deltaMove) {
        final float floorPos = calculateFloorPositionDistance();
        if (floorPos > 0) {
            platform.set(camera).negate();
            platform.rotateY((float) Math.toDegrees(camera.getRotation()[1]));
            platform.translateZ(-floorPos);
            platform.translateY(-0.8f).rotateX(180).scale(0.25f);
            platform.transform(deltaMove);
            platformPosition.set(platform.getPosition());
        }
    }

    @Override
    public void onCardboardTrigger() {
        final Vector3 cameraPos = CardboardGraphics.camera.getPosition();
        new TranslateTransition(CardboardGraphics.camera, cameraPos.copy().mult(-1), platformPosition, 2);
    }

    @Override
    public void onDraw(float[] view, float[] perspective) {
        platform.draw(view, perspective);
    }

    private float calculateFloorPositionDistance() {

        // Retrieve the current forward vector
        final Vector3 forwardVector = CardboardGraphics.camera.getForwardVector();
        // Calculate the angle between forward and the down vector
        final float alpha = (float) Vector3.getAngleBetween(forwardVector, Vector3.down);

        // If the angle is above 90 degree then the camera is not looking towards the floor.
        // Just return 0 here.
        if (alpha >= 90) {
            return 0;
        }

        // Retrieve the current camera's position
        final Vector3 cameraPos = CardboardGraphics.camera.getPosition();
        // Assume the floor is at 0,0,0
        final float cameraHeight = -cameraPos.y;
        // Calculate with trigonometry: b = tan(alpha) * a
        final float length = (float) (Math.tan(Math.toRadians(alpha)) * cameraHeight);
        return length;
    }
}
