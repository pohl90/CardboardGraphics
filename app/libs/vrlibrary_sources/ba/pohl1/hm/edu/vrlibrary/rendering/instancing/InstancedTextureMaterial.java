package ba.pohl1.hm.edu.vrlibrary.rendering.instancing;

import android.opengl.GLES30;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.model.data.GeometryData;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_SHORT;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Pohl on 24.04.2016.
 */
public class InstancedTextureMaterial extends InstancedMaterial {

    private static final String TAG = "InstancedTextureMaterial";
    private final int textureId;

    public InstancedTextureMaterial(final Shader shader, final int textureResourceId, InstanceType instanceType, VRComponent component, final GeometryData geometryData) {
        super(shader, geometryData, instanceType, component);
        textureId = CGUtils.loadTexture(CardboardGraphics.context, textureResourceId);
    }

    @Override
    public boolean isColor() {
        return false;
    }

    @Override
    public void drawInstanced(final float[] view, final float[] perspective, final int vao, final int ibo, final int instances) {
        shader.use();

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0);

        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0
        glUniform1i(shader.getUniform("u_Texture", false), 0);
        glUniformMatrix4fv(shader.getUniform("u_View"), 1, false, view, 0);
        glUniformMatrix4fv(shader.getUniform("u_Projection"), 1, false, perspective, 0);
        // Bind VAO
        GLES30.glBindVertexArray(vao);
        if(geometryData.getIndicesArray() != null) {
            GLES30.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            // Draw instanced
            GLES30.glDrawElementsInstanced(GL_TRIANGLES, geometryData.getIndicesArray().length, GL_UNSIGNED_SHORT, 0, instances);
            // Unbind
            GLES30.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        } else {
            final int size = geometryData.getVerticesArray().length;
            GLES30.glDrawArraysInstanced(GL_TRIANGLES, 0, size / 3, instances);
        }
        GLES30.glBindVertexArray(0);
        CGUtils.checkGLError(TAG, "draw error!");
    }

    @Override
    public void setupModel(int vao, int vbo, int ibo) {
        final IntBuffer verticesBuffer = CGUtils.createIntBuffer(geometryData.getVerticesArray());
        final ShortBuffer indicesBuffer = CGUtils.createShortBuffer(geometryData.getIndicesArray());

        // Bind VAO
        GLES30.glBindVertexArray(vao);

        GLES30.glEnableVertexAttribArray(0);

        // Vertices VBO
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GL_ARRAY_BUFFER, verticesBuffer.capacity() * 4, verticesBuffer, GL_STATIC_DRAW);
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, 0);

        if(indicesBuffer != null) {
            GLES30.glEnableVertexAttribArray(2);
            GLES30.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            GLES30.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.capacity() * 2, indicesBuffer, GL_STATIC_DRAW);
            GLES30.glVertexAttribPointer(2, 2, GL_SHORT, false, 0, 0);
        }

        // Unbind
        GLES30.glBindVertexArray(0);
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, 0);
        GLES30.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        CGUtils.checkGLError(TAG, "setup failed");
    }

    @Override
    public boolean draw(final Matrix4x4 modelMatrix, final float[] view, float[] perspective) {
        // Don't need this when rendering instanced.
        return false;
    }

    public void setupModel(int vao, int vbo, int ibo, int tbo) {
        setupModel(vao, vbo, ibo);
        // Bind VAO
        GLES30.glBindVertexArray(vao);

        // Texture VBO
        final IntBuffer textureBuffer = getTextureBuffer();
        glEnableVertexAttribArray(7);
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, tbo);
        GLES30.glBufferData(GL_ARRAY_BUFFER, textureBuffer.capacity() * 4, textureBuffer, GL_STATIC_DRAW);
        GLES30.glVertexAttribPointer(7, 2, GLES30.GL_FLOAT, false, 0, 0);

        // Unbind
        GLES30.glBindVertexArray(0);
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
