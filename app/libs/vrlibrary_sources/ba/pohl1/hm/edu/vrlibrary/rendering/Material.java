package ba.pohl1.hm.edu.vrlibrary.rendering;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.model.data.GeometryData;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;

/**
 * A material is responsible for setting up the shaders and is used to draw with OpenGL.
 * <p>
 * Created by Pohl on 26.02.2016.
 */
public abstract class Material {

    private static final String TAG = "Material";
    protected static float[] modelView = new float[16];
    protected static float[] modelViewProjection = new float[16];

    protected Shader shader;
    protected GeometryData geometryData;

    private IntBuffer verticesBuffer;
    private IntBuffer colorsBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer normalsBuffer;
    private IntBuffer textureBuffer;

    private boolean isInstanced;
    private boolean colorBufferDirty = true;

    public Material(final Shader shader, final GeometryData geometryData) {
        this.shader = shader;
        this.geometryData = geometryData;
    }

    /**
     * Sets the flag whether this material should be treated as instanced.
     *
     * @param isInstanced {@code true} if the material should be treated as instanced
     */
    public void setIsInstanced(boolean isInstanced) {
        this.isInstanced = isInstanced;
    }

    /**
     * Gets whether this material should be treated as instanced.
     *
     * @return {@code true} if this material should be treated as instanced
     */
    public boolean isInstanced() {
        return isInstanced;
    }

    /**
     * Gets whether the color buffer is dirty.
     *
     * @return {@code true} if the buffer is dirty
     */
    public boolean isColorBufferDirty() {
        return colorBufferDirty;
    }

    /**
     * Sets whether the color buffer is dirty.
     *
     * @param colorBufferDirty {@code true} if the buffer is dirty
     */
    public void setColorBufferDirty(boolean colorBufferDirty) {
        this.colorBufferDirty = colorBufferDirty;
    }

    /**
     * Gets the {@link IntBuffer} for the vertices.
     *
     * @return the int buffer for the vertices
     */
    protected IntBuffer getVerticesBuffer() {
        if(verticesBuffer == null) {
            verticesBuffer = CGUtils.createIntBuffer(geometryData.getVerticesArray());
        }
        return verticesBuffer;
    }

    /**
     * Gets the {@link FloatBuffer} for the normals.
     *
     * @return the float buffer for the normals
     */
    protected FloatBuffer getNormalsBuffer() {
        if(normalsBuffer == null) {
            normalsBuffer = CGUtils.createFloatBuffer(geometryData.getNormalsArray());
        }
        return normalsBuffer;
    }

    /**
     * Gets the {@link FloatBuffer} for the textures.
     *
     * @return the float buffer for the vertices
     */
    protected IntBuffer getTextureBuffer() {
        if(textureBuffer == null) {
            textureBuffer = CGUtils.createIntBuffer(geometryData.getTexArray());
        }
        return textureBuffer;
    }

    /**
     * Gets the {@link IntBuffer} for the colors.
     *
     * @return the int buffer for the colors
     */
    protected IntBuffer getColorsBuffer() {
        if(colorsBuffer == null) {
            colorsBuffer = CGUtils.createIntBuffer(geometryData.getColorsArray());
        }
        return colorsBuffer;
    }

    /**
     * Gets the {@link ShortBuffer} for the indices.
     *
     * @return the short buffer for the indices
     */
    protected ShortBuffer getIndexBuffer() {
        if(indexBuffer == null) {
            indexBuffer = CGUtils.createShortBuffer(geometryData.getIndicesArray());
        }
        return indexBuffer;
    }

    /**
     * Gets the {@link GeometryData}.
     *
     * @return the geometry data
     */
    public GeometryData getGeometryData() {
        return geometryData;
    }

    /**
     * Sets the {@link GeometryData}.
     *
     * @parma geometryData the geometry data
     */
    public void setGeometryData(GeometryData geometryData) {
        this.geometryData = geometryData;
    }

    /**
     * Draws with the given {@link Matrix4x4} and the associated projection matrix.
     *
     * @param modelMatrix the model matrix
     * @param perspective the projection matrix
     */
    public abstract boolean draw(final Matrix4x4 modelMatrix, final float[] view, final float[] perspective);
}
