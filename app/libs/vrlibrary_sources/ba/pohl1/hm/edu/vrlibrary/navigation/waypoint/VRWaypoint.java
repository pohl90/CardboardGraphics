package ba.pohl1.hm.edu.vrlibrary.navigation.waypoint;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.model.shapes.Cube;
import ba.pohl1.hm.edu.vrlibrary.physics.animation.AnimationHandler;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusListener;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusManager;
import ba.pohl1.hm.edu.vrlibrary.rendering.ColorMaterial;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;
import ba.pohl1.hm.edu.vrlibrary.util.Timer;

/**
 * Created by Pohl on 22.04.2016.
 */
public class VRWaypoint extends Cube implements FocusListener {

    private static float[] focusedColor = CGConstants.COLOR_GREEN;
    private float[] unfocusedColorsArray;

    public VRWaypoint() {
        setCollisionEnabled(false);
        applyAsColorMaterial(CardboardGraphics.colorShader);
        setRandomColors();
        setPostAnimationHandler(new AnimationHandler() {
            @Override
            public void animate(float delta, Matrix4x4 model) {
                randomRotate();
            }
        });
        FocusManager.getInstance().addListener(this, this);
    }

    private void randomRotate() {
        final double randomX = Math.random() * 3;
        final double randomY = Math.random() * 3;
        final double randomZ = Math.random() * 3;
        rotateX((float) randomX);
        rotateY((float) randomY);
        rotateZ((float) randomZ);
    }

    private void setRandomColors() {
        final float[] c1 = CGUtils.randomColor();
        final float[] c2 = CGUtils.randomColor();
        final float[] c3 = CGUtils.randomColor();
        final float[] c4 = CGUtils.randomColor();
        final float[] c5 = CGUtils.randomColor();
        final float[] c6 = CGUtils.randomColor();
        unfocusedColorsArray = new float[getGeometryData().getColorsArray().length];
        CGUtils.fillColors(unfocusedColorsArray, c1, c2, c3, c4, c5, c6);
        setColorsArray();
    }

    private void setColorsArray() {
        getGeometryData().setColorsArray(unfocusedColorsArray.clone());
        ((ColorMaterial) getMaterial()).updateColorBuffer();
    }

    @Override
    public void focusGained(VRComponent component) {
        setColor(focusedColor);
    }

    @Override
    public void focusLost(VRComponent component) {
        setColorsArray();
    }

    @Override
    public void focusHolding(VRComponent component, Timer timer) {

    }
}
