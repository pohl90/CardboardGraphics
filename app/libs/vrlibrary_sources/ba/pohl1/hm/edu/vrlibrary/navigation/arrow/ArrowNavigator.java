package ba.pohl1.hm.edu.vrlibrary.navigation.arrow;

import android.os.Vibrator;

import ba.pohl1.hm.edu.vrlibrary.model.VRArrow;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.navigation.TapNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRDrawableNavigator;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusListener;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusManager;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;
import ba.pohl1.hm.edu.vrlibrary.util.Timer;
import ba.pohl1.hm.edu.vrlibrary.util.UIUtils;

/**
 * Created by Pohl on 23.04.2016.
 */
public class ArrowNavigator extends TapNavigator implements VRDrawableNavigator, FocusListener {

    protected final Vibrator vibrator;
    protected VRArrow currentArrow;
    protected int arrowsFocused = 0;
    protected VRArrow arrowForward;
    protected VRArrow arrowBackward;
    protected boolean running;

    public ArrowNavigator() {
        this.vibrator = CardboardGraphics.vibrator;
        arrowForward = new VRArrow(-1.f);
        arrowForward.setName("Arrow Forward");
        arrowForward.setCollisionEnabled(false);
        arrowForward.setDirection(VRArrow.ArrowDirection.FORWARD);
        arrowForward.translate(0, -1, 0);
        arrowForward.scale(0.5f, 1.0f, 0.35f);

        arrowBackward = new VRArrow(-0.5f);
        arrowBackward.setName("Arrow Backward");
        arrowBackward.setCollisionEnabled(false);
        arrowBackward.setDirection(VRArrow.ArrowDirection.BACKWARD);
        arrowBackward.translate(0, -1, 0).rotateY(180);
        arrowBackward.scale(0.5f, 1.0f, 0.35f);

        currentArrow = arrowForward;
        FocusManager.getInstance().addListener(this, arrowForward, 6);
        FocusManager.getInstance().addListener(this, arrowBackward, 6);
    }

    @Override
    public String getToast() {
        return "Focus an arrow for " + (double) CardboardGraphics.focusTriggerDelay / 1000D + "s";
    }

    @Override
    public void dispose() {
        FocusManager.getInstance().unregister(arrowForward);
        FocusManager.getInstance().unregister(arrowBackward);
    }

    @Override
    public void onDraw(final float[] view, float[] perspective) {
        arrowForward.draw(view, perspective);
        arrowBackward.draw(view, perspective);
    }

    @Override
    public void navigate(final VRCamera camera, float deltaMove) {
        arrowForward.transform(deltaMove);
        arrowBackward.transform(deltaMove);
        super.navigate(camera, currentArrow.getDirection() == VRArrow.ArrowDirection.FORWARD ? deltaMove : -deltaMove);
    }

    @Override
    public void onCardboardTrigger() {
        // Don't need this
    }

    @Override
    public void focusGained(VRComponent component) {
        disable();
        arrowsFocused++;
    }

    @Override
    public void focusLost(VRComponent component) {
        arrowsFocused--;
        if(arrowsFocused == 0 && running) {
            enable();
        }
    }

    @Override
    public void focusHolding(VRComponent component, Timer timer) {
        if(timer.getElapsedTime() > CardboardGraphics.focusTriggerDelay) {
            currentArrow = (VRArrow) component;
            trigger();
            // Reset the timer of this focused component
            timer.reset();
        }
    }

    protected void trigger() {
        // Deactivate both arrows
        arrowForward.setActive(false);
        arrowBackward.setActive(false);

        if(currentArrow != null) {
            running = !running;
            // Activate the currently focused arrow
            currentArrow.setActive(running);
            vibrator.vibrate(200);
            if(CardboardGraphics.hasHUD()) {
                CardboardGraphics.hud.show3DToast(running ? "Enabled" : "Disabled");
            }
        }
    }
}
