package ba.pohl1.hm.edu.vrlibrary.rendering;

import java.util.HashSet;
import java.util.Set;

import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;

/**
 * A manager to take of all matrix transformations. When a {@link VRComponent} gets dirty it will automatically register
 * itself here. Once transformed the component's reference will be thrown away, except the component has animations.
 *
 * Created by Pohl on 16.04.2016.
 */
public class TransformationManager {

    private static final TransformationManager instance = new TransformationManager();

    private Set<VRComponent> addedComponents = new HashSet<>();
    private Set<VRComponent> components = new HashSet<>();
    private Set<VRComponent> animatedComponents = new HashSet<>();

    private TransformationManager() {
        // Prevents instantiation
    }

    /**
     * Gets the singleton instance of the {@link TransformationManager}.
     *
     * @return the instance
     */
    public static TransformationManager getInstance() {
        return instance;
    }

    /**
     * Disposes the {@link TransformationManager}.
     */
    public void dispose() {
        addedComponents.clear();
        components.clear();
        animatedComponents.clear();
    }

    /**
     * Adds the given {@link VRComponent} to the manager.
     *
     * @param component the component to add
     */
    public synchronized void add(final VRComponent component) {
        if(!(component instanceof VRCamera)) {
            addedComponents.add(component);
        }
    }

    /**
     * Performs a transformation call on all registered {@link VRComponent VRComponents}.
     * The given delta can be used by animations to ensure consistent transformations based on the FPS.
     *
     * @param delta the delta based on the fps
     */
    public void applyTransformations(final float delta) {
        if(!addedComponents.isEmpty()) {
            synchronized(this) {
                components.addAll(addedComponents);
                addedComponents.clear();
            }
        }
        animatedComponents.clear();
        while(!components.isEmpty()) {
            final VRComponent component = components.iterator().next();
            transform(component, delta);
            components.remove(component);
        }
        components.addAll(animatedComponents);
    }

    private void transform(final VRComponent component, final float delta) {
        if(component.hasParent() && component.getParent().isDirty()) {
            final VRComponent parent = component.getParent();
            transform(parent, delta);
            components.remove(parent);
        }
        // Remember the components that are animate
        // since we need them to be transformed every frame.
        if(component.hasAnimation()) {
            animatedComponents.add(component);
        }
        component.transform(delta);
        component.validate();
    }
}
