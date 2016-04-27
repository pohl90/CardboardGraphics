package ba.pohl1.hm.edu.vrlibrary.rendering.instancing;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.model.data.GeometryData;
import ba.pohl1.hm.edu.vrlibrary.rendering.Material;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;

/**
 * Created by Pohl on 10.04.2016.
 */
public abstract class InstancedMaterial extends Material {

    private final InstanceType type;
    private final VRComponent instancedObject;

    public InstancedMaterial(final Shader shader, final GeometryData geometryData, final InstanceType type, final VRComponent instancedObject) {
        super(shader, geometryData);
        this.type = type;
        this.instancedObject = instancedObject;
    }

    @Override
    public boolean draw(Matrix4x4 modelMatrix, final float[] view, float[] perspective) {
        // Do nothing here since this should be rendered instanced.
        return false;
    }

    public InstanceType getType() {
        return type;
    }

    public VRComponent getInstancedObject() {
        return instancedObject;
    }

    /**
     * Check whether the material is colored. If not, it is textured.
     *
     * @return {@code true} if the material is colored
     */
    public abstract boolean isColor();

    public abstract void drawInstanced(final float[] view, final float[] perspective, final int vao, final int ibo, final int instances);

    public abstract void setupModel(final int vao, final int vbo, final int ibo);
}
