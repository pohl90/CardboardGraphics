package cg.edu.hm.pohl.navigator;

import ba.pohl1.hm.edu.vrlibrary.base.rendering.Material;
import ba.pohl1.hm.edu.vrlibrary.data.GeometryGenerator;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.util.BAConstants;
import cg.edu.hm.pohl.AbstractCardboadActivity;

/**
 * Created by Pohl on 15.04.2016.
 */
public class Arrow extends VRComponent {

    private VRCamera camera;

    private static final float[] inactiveArrowColor = BAConstants.COLOR_RED;
    private static final float[] focusedArrowColor = BAConstants.COLOR_YELLOW;
    private static final float[] activeArrowColor = BAConstants.COLOR_GREEN;

    private ArrowDirection direction = ArrowDirection.FORWARD;

    private boolean active;
    private float zOffset;

    public Arrow(final VRCamera camera, final float zOffset) {
        this.camera = camera;
        this.zOffset = zOffset;
        setGeometryData(GeometryGenerator.createArrow());
        applyAsColorMaterial(AbstractCardboadActivity.colorShader);
    }

    public Arrow(final VRCamera camera) {
        this(camera, 0);
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
