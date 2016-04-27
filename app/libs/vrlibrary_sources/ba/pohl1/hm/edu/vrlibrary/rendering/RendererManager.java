package ba.pohl1.hm.edu.vrlibrary.rendering;

import java.util.HashSet;
import java.util.Set;

import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;

/**
 * A manager to take care of all drawing calls. Drawable {@link VRComponent VRComponents} can register themselves
 * in order to be drawn.
 *
 * Created by Pohl on 16.04.2016.
 */
public class RendererManager {

    private static final RendererManager instance = new RendererManager();
    private final Set<VRComponent> components = new HashSet<>();

    private RendererManager() {
        // Prevents instantiation
    }

    /**
     * Gets the singleton instance of the {@link RendererManager}.
     *
     * @return the instance
     */
    public static RendererManager getInstance() {
        return instance;
    }

    /**
     * Adds the given {@link VRComponent} to the renderer.
     *
     * @param component the component to add
     */
    public void add(final VRComponent component) {
        components.add(component);
    }

    /**
     * Removes the given {@link VRComponent} from the renderer.
     *
     * @param component the component to remove
     */
    public void remove(final VRComponent component) {
        components.remove(component);
    }

    /**
     * Performs draw calls on all registered {@link VRComponent VRComponents}.
     *
     * @param view       the view matrix
     * @param projection the projection matrix
     */
    public void render(final float[] view, final float[] projection) {
        for(final VRComponent component : components) {
            if(component.isVisible()) {
                component.draw(view, projection);
            }
        }
    }

    /**
     * Disposes the {@link RendererManager}.
     */
    public void dispose() {
        components.clear();
    }
}
