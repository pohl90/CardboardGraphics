package ba.pohl1.hm.edu.vrlibrary.navigation.waypoint;

import java.util.ArrayList;
import java.util.List;

import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRNavigator;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusListener;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusManager;
import ba.pohl1.hm.edu.vrlibrary.physics.transitions.TranslateTransition;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;
import ba.pohl1.hm.edu.vrlibrary.util.Timer;
import ba.pohl1.hm.edu.vrlibrary.util.UIUtils;

import static ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics.camera;

/**
 * Created by Pohl on 22.04.2016.
 */
public class WaypointNavigator implements VRNavigator, FocusListener {

    private List<VRWaypoint> waypoints = new ArrayList<>();

    private TranslateTransition translateTransition;
    private VRWaypoint focusedWaypount;

    public WaypointNavigator() {
        addWaypointAt(5, 1, 5);
        addWaypointAt(-5, 1, 5);
        addWaypointAt(5, 1, -5);
        addWaypointAt(-5, 1, -5);
    }

    @Override
    public String getToast() {
        return "Focus&Tap a way point";
    }

    /**
     * Adds a new {@link VRWaypoint} at the given position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public void addWaypointAt(final float x, final float y, final float z) {
        addWaypointAt(0.4f, x, y, z);
    }

    /**
     * Adds a new {@link VRWaypoint} at the given position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public void addWaypointAt(final float scaling, final float x, final float y, final float z) {
        final VRWaypoint waypoint = new VRWaypoint();
        waypoint.translate(x, y, z);
        waypoint.scale(scaling);
        FocusManager.getInstance().addListener(this, waypoint);
        waypoints.add(waypoint);
    }

    @Override
    public void navigate(VRCamera camera, float deltaMove) {
        if(translateTransition != null) {
            translateTransition.execute();
            if(translateTransition.isDone()) {
                translateTransition = null;
            }
        }
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void onCardboardTrigger() {
        if(focusedWaypount != null) {
            translateTransition = new TranslateTransition(camera, camera.getPosition().copy().mult(-1f), focusedWaypount.getPosition(), 2);
        }
    }

    @Override
    public void dispose() {
        for(final VRWaypoint waypoint : waypoints) {
            waypoint.remove(true);
            FocusManager.getInstance().unregister(waypoint);
        }
        waypoints.clear();
    }

    @Override
    public void focusGained(VRComponent component) {
        focusedWaypount = (VRWaypoint) component;
        if(CardboardGraphics.hasHUD()) {
            UIUtils.runInUIThread(new Runnable() {
                @Override
                public void run() {
                    CardboardGraphics.hud.show3DToast("Tap to go there");
                }
            });
        }
    }

    @Override
    public void focusLost(VRComponent component) {
        focusedWaypount = null;
    }

    @Override
    public void focusHolding(VRComponent component, Timer timer) {
        // Not used
    }
}
