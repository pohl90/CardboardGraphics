package ba.pohl1.hm.edu.vrlibrary.ui.hud3d.controls;

import ba.pohl1.hm.edu.vrlibrary.cardboard.CardboardTriggerEvent;
import ba.pohl1.hm.edu.vrlibrary.cardboard.CardboardTriggerListener;
import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.model.observer.VRObservable;
import ba.pohl1.hm.edu.vrlibrary.model.observer.VRObserver;
import ba.pohl1.hm.edu.vrlibrary.model.shapes.Cube;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusManager;
import ba.pohl1.hm.edu.vrlibrary.physics.transitions.RotateTransition;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;

/**
 * Created by Pohl on 01.05.2016.
 */
public class VRToggleButton extends VRControl implements CardboardTriggerListener {

    private static final float[] COLOR_ACTIVE = CGConstants.COLOR_GREEN;
    private static final float[] COLOR_INACTIVE = CGConstants.COLOR_RED;

    private VRObservable<Boolean> activeObservable = new VRObservable<>(true);
    private VRObserver<Boolean> activeObserver;

    private VRComponent control = new Cube();

    public VRToggleButton() {
        add(control);
        addListener();
        toggle();
        FocusManager.getInstance().register(control);
        CardboardGraphics.cardboardTrigger.addListener(this);
    }

    @Override
    public void onCardboardTrigger(final CardboardTriggerEvent event) {
        if (control.isFocused()) {
            toggle();
            // Consume the event to prevent the navigation trigger
            event.consume();
        }
    }

    @Override
    public void dispose() {
        activeObservable.dispose();
        CardboardGraphics.cardboardTrigger.removeListener(this);
        super.dispose();
    }

    public VRObservable<Boolean> getActiveObservable() {
        return activeObservable;
    }

    public void toggle() {
        activeObservable.set(!activeObservable.get());
    }

    private void addListener() {
        activeObserver = new VRObserver<Boolean>() {
            @Override
            public void onChange(Boolean oldValue, Boolean newValue) {
                // DO TRANSFORMATION AND COLORS STUFF
                control.setColor(newValue ? COLOR_ACTIVE : COLOR_INACTIVE);
                final Vector3 startRotation = Vector3.zero;
                final Vector3 endRotation = new Vector3(0f, 180f, 0f);
                new RotateTransition(control, startRotation, endRotation, 2f);
            }
        };
        activeObservable.addListener(activeObserver);
    }
}
