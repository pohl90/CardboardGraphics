package ba.pohl1.hm.edu.vrlibrary.rendering.instancing;

import java.util.HashMap;
import java.util.Map;

/**
 * A manager to take care of instanced rendering.
 *
 * Created by Pohl on 09.04.2016.
 */
public class InstancedRendererManager {

    private static final InstancedRendererManager instance = new InstancedRendererManager();

    private final Map<InstanceType, InstancedRenderer> typeToMaterials = new HashMap<>();

    private InstancedRendererManager() {
        // Prevents instantiation
    }

    /**
     * Gets the singleton instance of the {@link InstancedRendererManager}.
     *
     * @return the instance
     */
    public static InstancedRendererManager getInstance() {
        return instance;
    }

    /**
     * Disposes this manager.
     */
    public void dispose() {
        for(final InstancedRenderer renderer : typeToMaterials.values()) {
            renderer.dispose();
        }
        typeToMaterials.clear();
    }

    /**
     * Registers the given {@link InstancedMaterial}.
     *
     * @param instancedMaterial the material to register
     */
    public void registerInstancedMaterial(final InstancedMaterial instancedMaterial) {
        if(!typeToMaterials.containsKey(instancedMaterial.getType())) {
            typeToMaterials.put(instancedMaterial.getType(), new InstancedRenderer());
        }
        typeToMaterials.get(instancedMaterial.getType()).add(instancedMaterial);
    }

    /**
     * Unregisters the given {@link InstancedMaterial}.
     *
     * @param instancedMaterial the material to unregister
     */
    public void unregisterInstancedMaterial(InstancedMaterial instancedMaterial) {
        if(typeToMaterials.containsKey(instancedMaterial.getType())) {
            typeToMaterials.get(instancedMaterial.getType()).remove(instancedMaterial);
        }
    }

    /**
     * Updates all {@link InstancedRenderer InstancedRenderers} by calling {@link InstancedRenderer#updateModelVBO()}.
     */
    public void updateInstancedModels() {
        for(final InstancedRenderer instancedRenderer : typeToMaterials.values()) {
            instancedRenderer.updateModelVBO();
        }
    }

    /**
     * Performs a instanced draw call on all {@link InstancedRenderer InstancedRenderers}.
     *
     * @param view       the view matrix
     * @param projection the projection matrix
     */
    public void drawInstanced(final float[] view, final float[] projection) {
        for(final InstancedRenderer holder : typeToMaterials.values()) {
            holder.draw(view, projection);
        }
    }
}
