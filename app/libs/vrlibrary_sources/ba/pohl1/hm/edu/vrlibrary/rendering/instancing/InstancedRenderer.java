package ba.pohl1.hm.edu.vrlibrary.rendering.instancing;

import android.opengl.GLES30;

import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.Set;

import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FLOAT;

/**
 * Created by Pohl on 09.04.2016.
 */
public class InstancedRenderer {

    private static final int INSTANCE_DATA_LENGTH = 16;
    private static final int FLOATS_PER_COLOR = 4;

    private int vao;
    private int vbo;
    private int ibo;
    private int tbo;
    private int modelVBO;
    private int colorVBO;

    private float[] modelsArray;
    private float[] colorsArray;

    private int colorsStride;
    private int modelOffset;
    private int colorOffset;

    private FloatBuffer matrixBuffer;
    private FloatBuffer colorsBuffer;
    private boolean invalidated;
    private boolean matrixBufferDirty = true;

    private Set<InstancedMaterial> instances = new HashSet<>();
    private boolean initialized;
    private boolean colorBufferDirty;

    public void add(final InstancedMaterial instanceable) {
        instances.add(instanceable);
    }

    public void remove(final InstancedMaterial instanceable) {
        instances.remove(instanceable);
        int size = countInstances() * INSTANCE_DATA_LENGTH;
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, modelVBO);
        GLES30.glBufferData(GL_ARRAY_BUFFER, size * 4, matrixBuffer, GLES30.GL_STREAM_DRAW);
    }

    public int countInstances() {
        return instances.size();
    }

    public void dispose() {
        instances.clear();
    }

    /**
     * Performs a single instanced draw call for all instances of the same {@link InstanceType}.
     *
     * @param view       the view matrix
     * @param projection the projection matrix
     */
    public void draw(final float[] view, final float[] projection) {
        // Grab one of the instances and call draw
        if(countInstances() > 0) {
            instances.iterator().next().drawInstanced(view, projection, vao, ibo, countInstances());
        }
    }

    /**
     * Updates, if necessary the model VBO.
     */
    public void updateModelVBO() {
        modelOffset = 0;
        colorOffset = 0;
        for(final InstancedMaterial material : instances) {
            if(!initialized) {
                init(material);
                if(material.isColor()) {
                    material.setupModel(vao, vbo, ibo);
                    setupModelVBOWithColor(3, 7);
                } else {
                    ((InstancedTextureMaterial) material).setupModel(vao, vbo, ibo, tbo);
                    setupModelVBOWithTexture(3);
                }
            }
            update(material);
            modelOffset += INSTANCE_DATA_LENGTH;
            colorOffset += colorsStride;
        }

        GLES30.glBindVertexArray(vao);
        if(matrixBufferDirty) {
            // Model VBO update
            matrixBuffer.position(0);
            GLES30.glBindBuffer(GL_ARRAY_BUFFER, modelVBO);
            GLES30.glBufferSubData(GL_ARRAY_BUFFER, 0, matrixBuffer.capacity() * 4, matrixBuffer);
            matrixBufferDirty = false;
        }
        if(colorBufferDirty) {
            // Color VBO update
            colorsBuffer.position(0);
            GLES30.glBindBuffer(GL_ARRAY_BUFFER, colorVBO);
            GLES30.glBufferSubData(GL_ARRAY_BUFFER, 0, colorsBuffer.capacity() * 4, colorsBuffer);
            colorBufferDirty = false;
        }
        GLES30.glBindVertexArray(0);
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    /**
     * Initializes this {@link InstancedRenderer} with the first {@link InstancedMaterial} to get information about:
     * <p>
     *     <ul>
     *         <li>Length of the models array (#instances * dataLengthPerInstance)</li>
     *         <li>Length of the colors array (#instances * floatPerColor)</li>
     *         <li>Stride of the colors array (length of the color array of an instance)</li>
     *     </ul>
     * </p>
     * Also several important handles are created:
     * <p>
     *     <ul>
     *         <li>VAO: Handle for the GPU to point at VBOs</li>
     *         <li>Vertices VBO: Handle for the GPU to point at the vertices buffer</li>
     *         <li>Model VBO: Handle for the GPU to point at the model buffer</li>
     *         <li>Index VBO: Handle for the GPU to point at the index buffer</li>
     *         <li>Color VBO: Handle for the GPU to point at the color buffer</li>
     *     </ul>
     * </p>
     * @param firstMaterial
     */
    private void init(InstancedMaterial firstMaterial) {
        modelsArray = new float[countInstances() * INSTANCE_DATA_LENGTH];
        colorsArray = new float[countInstances() * FLOATS_PER_COLOR];
        colorsStride = firstMaterial.getGeometryData().getColorsArray().length;
        vao = CGUtils.createVAO();
        vbo = CGUtils.createVBO();
        ibo = CGUtils.createVBO();
        tbo = CGUtils.createVBO();
        modelVBO = CGUtils.createVBO();
        colorVBO = CGUtils.createVBO();
        initialized = true;
    }

    /**
     * Sets up the model vbo.
     *
     * @param shaderIndex the index within the shader for the matrix mat4
     * @param colorIndex the index within the shader for the color vec4
     */
    private void setupModelVBOWithColor(final int shaderIndex, final int colorIndex) {
        int stride = INSTANCE_DATA_LENGTH * 4;
        int colorSize = colorsArray.length;

        matrixBuffer = CGUtils.createFloatBuffer(modelsArray);
        colorsBuffer = CGUtils.createFloatBuffer(colorsArray);

        GLES30.glBindVertexArray(vao);

        // Matrix VBO
        int size = countInstances() * INSTANCE_DATA_LENGTH;
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, modelVBO);
        GLES30.glBufferData(GL_ARRAY_BUFFER, size * 4, matrixBuffer, GLES30.GL_STREAM_DRAW);

        GLES30.glEnableVertexAttribArray(shaderIndex);
        GLES30.glEnableVertexAttribArray(shaderIndex + 1);
        GLES30.glEnableVertexAttribArray(shaderIndex + 2);
        GLES30.glEnableVertexAttribArray(shaderIndex + 3);

        GLES30.glVertexAttribPointer(shaderIndex, 4, GL_FLOAT, false, stride, 0);
        GLES30.glVertexAttribPointer(shaderIndex + 1, 4, GL_FLOAT, false, stride, INSTANCE_DATA_LENGTH);
        GLES30.glVertexAttribPointer(shaderIndex + 2, 4, GL_FLOAT, false, stride, INSTANCE_DATA_LENGTH * 2);
        GLES30.glVertexAttribPointer(shaderIndex + 3, 4, GL_FLOAT, false, stride, INSTANCE_DATA_LENGTH * 3);

        GLES30.glVertexAttribDivisor(shaderIndex, 1);
        GLES30.glVertexAttribDivisor(shaderIndex + 1, 1);
        GLES30.glVertexAttribDivisor(shaderIndex + 2, 1);
        GLES30.glVertexAttribDivisor(shaderIndex + 3, 1);

        // Color VBO
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, colorVBO);
        GLES30.glBufferData(GL_ARRAY_BUFFER, colorSize * 4, colorsBuffer, GLES30.GL_STREAM_DRAW);

        GLES30.glEnableVertexAttribArray(colorIndex);
        GLES30.glVertexAttribPointer(colorIndex, 4, GL_FLOAT, false, 0, 0);
        GLES30.glVertexAttribDivisor(colorIndex, 1);

        // Unbind
        GLES30.glBindVertexArray(0);
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, 0);
        CGUtils.checkGLError("InstancedRenderer", "");
    }

    /**
     * Sets up the model vbo.
     *
     * @param shaderIndex the index within the shader for the matrix mat4
     */
    private void setupModelVBOWithTexture(final int shaderIndex) {
        int size = countInstances() * INSTANCE_DATA_LENGTH;
        int stride = INSTANCE_DATA_LENGTH * 4;

        matrixBuffer = CGUtils.createFloatBuffer(modelsArray);
        colorsBuffer = CGUtils.createFloatBuffer(colorsArray);

        GLES30.glBindVertexArray(vao);

        // Matrix VBO
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, modelVBO);
        GLES30.glBufferData(GL_ARRAY_BUFFER, size * 4, matrixBuffer, GLES30.GL_STREAM_DRAW);

        GLES30.glEnableVertexAttribArray(shaderIndex);
        GLES30.glEnableVertexAttribArray(shaderIndex + 1);
        GLES30.glEnableVertexAttribArray(shaderIndex + 2);
        GLES30.glEnableVertexAttribArray(shaderIndex + 3);

        GLES30.glVertexAttribPointer(shaderIndex, 4, GL_FLOAT, false, stride, 0);
        GLES30.glVertexAttribPointer(shaderIndex + 1, 4, GL_FLOAT, false, stride, INSTANCE_DATA_LENGTH);
        GLES30.glVertexAttribPointer(shaderIndex + 2, 4, GL_FLOAT, false, stride, INSTANCE_DATA_LENGTH * 2);
        GLES30.glVertexAttribPointer(shaderIndex + 3, 4, GL_FLOAT, false, stride, INSTANCE_DATA_LENGTH * 3);

        GLES30.glVertexAttribDivisor(shaderIndex, 1);
        GLES30.glVertexAttribDivisor(shaderIndex + 1, 1);
        GLES30.glVertexAttribDivisor(shaderIndex + 2, 1);
        GLES30.glVertexAttribDivisor(shaderIndex + 3, 1);

        // Unbind
        GLES30.glBindVertexArray(0);
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, 0);
        CGUtils.checkGLError("InstancedRenderer", "");
    }

    /**
     * Updates the given renderer based on the {@link InstancedMaterial}.
     *
     * @param material the material used to update the renderer
     */
    private void update(final InstancedMaterial material) {
        final VRComponent object = material.getInstancedObject();

        if(invalidated || (material.isColor() && material.isColorBufferDirty())) {
            material.setColorBufferDirty(false);
            colorBufferDirty = true;
            colorsBuffer.position(colorOffset);
            colorsBuffer.put(material.getGeometryData().getColorsArray(), 0, FLOATS_PER_COLOR);
        }
        // Do not update model vbo if object is not dirty
        if(!invalidated && !object.isBufferUpdateNeeded()) {
            return;
        }

        matrixBufferDirty = true;
        final float[] model = object.getBoundingBox().getModelMatrix().getFloat16();
        matrixBuffer.position(modelOffset);
        matrixBuffer.put(model);
        object.setBufferUpdateNeeded(false);
    }

}
