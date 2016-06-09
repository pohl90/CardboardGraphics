package ba.pohl1.hm.edu.vrlibrary.physics.focus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;
import ba.pohl1.hm.edu.vrlibrary.util.CGOptions;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.Timer;

import static ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics.camera;

/**
 * Created by Pohl on 03.04.2016.
 */
public class FocusManager {

    private static final FocusManager instance = new FocusManager();

    private final Map<VRComponent, Float> focusCandidates = new HashMap<>();
    private final Map<VRComponent, List<FocusListener>> focusTargetListeners = new HashMap<>();
    private final Map<VRComponent, Timer> focusTargetTimers = new HashMap<>();
    private Set<VRComponent> focusTargets = new HashSet<>();

    private FocusManager() {
        // Prevents instantiation
    }

    /**
     * Gets the singleton instance of the {@link FocusManager}.
     *
     * @return the instance
     */
    public static FocusManager getInstance() {
        return instance;
    }

    public Set<VRComponent> getFocusTargets() {
        return focusTargets;
    }

    public VRComponent getMostRecentFocusTarget() {
        return focusTargets.isEmpty() ? null : focusTargets.iterator().next();
    }

    /**
     * Disposes this {@link FocusManager}.
     */
    public void dispose() {
        focusCandidates.clear();
        focusTargetListeners.clear();
        focusTargetTimers.clear();
        focusTargets.clear();
    }

    /**
     * Updates all focus targets. Broadcasts notifications defined in the {@link FocusListener}.
     */
    public void updateFocusTargets() {
        if(!CGOptions.FOCUSING_ENABLED) {
            focusTargets.clear();
            return;
        }
        final Set<VRComponent> targets = new HashSet<>();
        for(final VRComponent candidate : focusCandidates.keySet()) {
            if(CGUtils.isLookingAt(camera.getForwardVector(), camera, candidate, focusCandidates.get(candidate))) {
                targets.add(candidate);
                if(!focusTargets.contains(candidate)) {
                    // Notify focus gained
                    candidate.setFocused(true);
                    notifyListeners(candidate, FocusType.GAINED);
                } else {
                    // Notify focus holding
                    notifyListeners(candidate, FocusType.HOLDING);
                }
            }
        }
        // Notify focus lost
        for(final VRComponent currentTarget : focusTargets) {
            if(!targets.contains(currentTarget)) {
                currentTarget.setFocused(false);
                notifyListeners(currentTarget, FocusType.LOST);
            }
        }
        focusTargets = targets;
    }

    /**
     * Registers the given {@link VRComponent} with the associate angle limit.
     *
     * @param component  the component to register
     */
    public void register(final VRComponent component) {
        focusCandidates.put(component, CGConstants.DEFAULT_FOCUS_DEGREE_LIMIT);
    }

    /**
     * Registers the given {@link VRComponent} with the associate angle limit.
     *
     * @param component  the component to register
     * @param angleLimit the angle
     */
    public void register(final VRComponent component, final float angleLimit) {
        focusCandidates.put(component, angleLimit);
    }

    /**
     * Unregisters the given {@link VRComponent}.
     *
     * @param component the component to register
     */
    public void unregister(VRComponent component) {
        getListenersFor(component).clear();
        focusCandidates.remove(component);
    }

    /**
     * Adds the {@link FocusListener} for the given {@link VRComponent}.
     *
     * @param listener the listener to add
     * @param target the target to observer
     */
    public void addListener(final FocusListener listener, final VRComponent target) {
        addListener(listener, target, CGConstants.DEFAULT_FOCUS_DEGREE_LIMIT);
    }

    /**
     * Adds the {@link FocusListener} for the given {@link VRComponent}.
     *
     * @param listener the listener to add
     * @param target the target to observer
     * @param angleLimit the angle
     */
    public void addListener(final FocusListener listener, final VRComponent target, final float angleLimit) {
        register(target, angleLimit);
        getListenersFor(target).add(listener);
    }

    private void notifyListeners(final VRComponent component, final FocusType type) {
        for(final FocusListener listener : getListenersFor(component)) {
            switch(type) {
                case GAINED:
                    listener.focusGained(component);
                    focusTargetTimers.put(component, new Timer().start());
                    break;
                case LOST:
                    component.setFocused(false);
                    listener.focusLost(component);
                    if(focusTargetTimers.get(component) != null) {
                        focusTargetTimers.remove(component).stop();
                    }
                    break;
                case HOLDING:
                    listener.focusHolding(component, focusTargetTimers.get(component));
                    break;
            }
        }
    }

    private List<FocusListener> getListenersFor(final VRComponent component) {
        if(!focusTargetListeners.containsKey(component)) {
            focusTargetListeners.put(component, new ArrayList<FocusListener>());
        }
        return focusTargetListeners.get(component);
    }

    private enum FocusType {
        GAINED, LOST, HOLDING
    }
}
