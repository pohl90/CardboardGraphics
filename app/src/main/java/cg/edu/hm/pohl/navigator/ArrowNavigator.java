package cg.edu.hm.pohl.navigator;

import android.os.Vibrator;

import ba.pohl1.hm.edu.vrlibrary.data.GeometryGenerator;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRDrawableNavigator;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusListener;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusManager;
import ba.pohl1.hm.edu.vrlibrary.util.Timer;
import cg.edu.hm.pohl.AbstractCardboadActivity;
import cg.edu.hm.pohl.VRRoom;
import cg.edu.hm.pohl.student.StudentScene;

/**
 * Created by Pohl on 07.04.2016.
 */
public class ArrowNavigator extends Navigator implements VRDrawableNavigator, FocusListener {

    private int arrowsFocused = 0;
    private Arrow arrowForward;
    private Arrow arrowBackward;
    private Arrow currentArrow;

    private boolean running;
    private Vibrator vibrator;

    public ArrowNavigator(Vibrator vibrator, VRCamera camera, VRRoom vrRoom, StudentScene studentScene) {
        super(camera, vrRoom, studentScene);
        this.vibrator = vibrator;
        arrowForward = new Arrow(camera, -1f);
        arrowForward.setName("Arrow Forward");
        arrowForward.setGeometryData(GeometryGenerator.createArrow());
        arrowForward.applyAsColorMaterial(AbstractCardboadActivity.colorShader);
        arrowForward.setCollisionEnabled(false);
        arrowForward.setDirection(Arrow.ArrowDirection.FORWARD);
        arrowForward.translate(0, -0.9f, 0);
        arrowForward.scale(0.5f, 1.0f, 0.35f);

        arrowBackward = new Arrow(camera, -0.5f);
        arrowBackward.setName("Arrow Backward");
        arrowBackward.setGeometryData(GeometryGenerator.createArrow());
        arrowBackward.applyAsColorMaterial(AbstractCardboadActivity.colorShader);
        arrowBackward.setCollisionEnabled(false);
        arrowBackward.setDirection(Arrow.ArrowDirection.BACKWARD);
        arrowBackward.translate(0, -0.9f, 0).rotateY(180);
        arrowBackward.scale(0.5f, 1.0f, 0.35f);

        FocusManager.getInstance().addListener(this, arrowForward, 6);
        FocusManager.getInstance().addListener(this, arrowBackward, 6);

        currentArrow = arrowForward;
    }

    @Override
    public void onDraw(final float[] view, float[] perspective) {
        arrowForward.draw(view, perspective);
        arrowBackward.draw(view, perspective);
    }

    @Override
    public void navigate(float deltaMove) {
        arrowForward.transform(deltaMove);
        arrowBackward.transform(deltaMove);
        super.navigate(currentArrow.getDirection() == Arrow.ArrowDirection.FORWARD ? deltaMove : -deltaMove);
    }

    @Override
    public void onCardboardTrigger() {
        // Don't need this
    }

    @Override
    public void focusGained(VRComponent component) {
        disable();
        arrowsFocused++;
        if(currentArrow != null && currentArrow.isActive()) {
            return;
        }
        currentArrow = (Arrow) component;
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
        if(timer.getElapsedTime() > 1500L) {
            running = !running;
            // Deactivate both arrows
            arrowForward.setActive(false);
            arrowBackward.setActive(false);

            // Set the current arrow
            currentArrow = (Arrow) component;
            currentArrow.setActive(running);

            // Reset the timer of this focused component
            timer.reset();
            vibrator.vibrate(100);
        }
    }

    public Arrow getArrowForward() {
        return arrowForward;
    }

    public Arrow getArrowBackward() {
        return arrowBackward;
    }
}
