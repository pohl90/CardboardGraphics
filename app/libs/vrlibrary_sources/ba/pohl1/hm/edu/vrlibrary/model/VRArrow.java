package ba.pohl1.hm.edu.vrlibrary.model;

import ba.pohl1.hm.edu.vrlibrary.model.data.GeometryGenerator;
import ba.pohl1.hm.edu.vrlibrary.rendering.Material;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;

/**
 * Created by Pohl on 07.04.2016.
 */
public class VRArrow extends VRComponent {

    private static final float[] inactiveArrowColor = CGConstants.COLOR_RED;
    private static final float[] focusedArrowColor = CGConstants.COLOR_YELLOW;
    private static final float[] activeArrowColor = CGConstants.COLOR_GREEN;

    private ArrowDirection direction = ArrowDirection.FORWARD;

    private boolean active;
    private VRCamera camera;
    private float zOffset;

    public VRArrow(final float zOffset) {
        this.camera = CardboardGraphics.camera;
        this.zOffset = zOffset;
        setGeometryData(GeometryGenerator.createArrow());
        applyAsColorMaterial(CardboardGraphics.colorShader);
    }

    @Override
    public void setMaterial(Material material) {
        // Do not register as drawable since the ArrowNavigator cares about drawing
        this.material = material;
    }

    @Override
    public void transform(float delta) {
        globalModelMatrix.set(camera).negate();
        globalModelMatrix.rotateY((float) Math.toDegrees(camera.getRotation()[1]));
        globalModelMatrix.translateZ(zOffset);
        doTransform();
    }

    public ArrowDirection getDirection() {
        return direction;
    }

    public void setDirection(ArrowDirection direction) {
        this.direction = direction;
    }

    @Override
    public boolean isDirty() {
        return true; // return always true since the camera is alway "moving"
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        setColor(active ? activeArrowColor : (isFocused() ? focusedArrowColor : inactiveArrowColor));
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if(!focused) {
            setActive(isActive());
        } else {
            setColor(focusedArrowColor);
        }
    }

    @Override
    public String getName() {
        return super.getName() + (active ? " active" : " inactive");
    }

    public enum ArrowDirection {
        FORWARD, BACKWARD, LEFT, RIGHT
    }
}
