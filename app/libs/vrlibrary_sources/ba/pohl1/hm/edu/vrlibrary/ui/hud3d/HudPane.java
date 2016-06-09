package ba.pohl1.hm.edu.vrlibrary.ui.hud3d;

import ba.pohl1.hm.edu.vrlibrary.R;
import ba.pohl1.hm.edu.vrlibrary.cardboard.CardboardTriggerEvent;
import ba.pohl1.hm.edu.vrlibrary.cardboard.CardboardTriggerListener;
import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.model.observer.VRObserver;
import ba.pohl1.hm.edu.vrlibrary.ui.hud3d.controls.VRControl;
import ba.pohl1.hm.edu.vrlibrary.ui.hud3d.controls.VRHBox;
import ba.pohl1.hm.edu.vrlibrary.ui.hud3d.controls.VRLabel;
import ba.pohl1.hm.edu.vrlibrary.ui.hud3d.controls.VRToggleButton;
import ba.pohl1.hm.edu.vrlibrary.ui.hud3d.controls.VRVBox;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;

/**
 * TODO continue implementation since this is a prototype
 * Created by Pohl on 01.05.2016.
 */
public class HudPane extends VRVBox implements CardboardTriggerListener {

    private static final float PANE_OFFSET_Y = 5f;
    private static final float PANE_OFFSET_X = -2f;

    private Vector3 panePosition = new Vector3();
    private Vector3 helpForwardVector = new Vector3();

    public HudPane() {
        final VRToggleButton button = createToggleControl("Test");
        button.getActiveObservable().addListener(new VRObserver<Boolean>() {
            @Override
            public void onChange(Boolean oldValue, Boolean newValue) {
                System.out.println("SWITCH TO " + newValue);
            }
        });
        final VRLabel label = createLabel(R.drawable.helloworld);
        addComponentsToControl(createHBox(), label, button);
        label.scale(3, 1, 1);
        CardboardGraphics.cardboardTrigger.addListener(this);
    }

    public static VRLabel createLabel(final int resourceId) {
        final VRLabel label = new VRLabel(resourceId);
        return label;
    }

    public static VRToggleButton createToggleControl(final String text) {
        final VRToggleButton control = new VRToggleButton();
        control.setName(text);
        return control;
    }

    public void spawnInFrontOfCamera() {
        final VRCamera camera = CardboardGraphics.camera;
        final Vector3 position = camera.getPosition();
        final Vector3 forwardVector = camera.getForwardVector();
        final float[] rotation = camera.getRotation();

        panePosition.set(position).mult(-1);
        helpForwardVector.set(forwardVector);
        helpForwardVector.y = 0;
        helpForwardVector.normalize();

        panePosition.add(helpForwardVector.mult(5f));

        final float angle = (float) Math.toDegrees(rotation[1]);
        identity().translate(PANE_OFFSET_X + panePosition.x, PANE_OFFSET_Y + panePosition.y, panePosition.z).rotateY(angle);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public VRHBox createHBox() {
        final VRHBox box = new VRHBox();
        add(box);
        return box;
    }

    public VRControl addComponentsToControl(final VRControl control, final VRComponent... components) {
        control.add(components);
        add(control);
        return control;
    }

    @Override
    public void onCardboardTrigger(CardboardTriggerEvent event) {
        spawnInFrontOfCamera();
        event.consume();
    }
}
