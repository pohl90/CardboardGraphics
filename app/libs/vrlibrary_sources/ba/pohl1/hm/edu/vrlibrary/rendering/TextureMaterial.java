package ba.pohl1.hm.edu.vrlibrary.rendering;

import android.opengl.Matrix;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.model.data.GeometryData;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;

import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_TEXTURE0;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.GL_UNSIGNED_SHORT;
import static android.opengl.GLES30.glActiveTexture;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glDrawArrays;
import static android.opengl.GLES30.glDrawElements;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glVertexAttribPointer;

/**
 * Created by Pohl on 27.02.2016.
 */
public class TextureMaterial extends Material {

    private static final String TAG = "TextureMaterial";
    private int textureId;

    public TextureMaterial(final Shader shader, final GeometryData geometryData, final int textureResourceId) {
        this(shader, geometryData, new int[]{CGUtils.loadTexture(CardboardGraphics.context, textureResourceId)});
    }

    public TextureMaterial(final Shader shader, final GeometryData geometryData, final int[] textureId) {
        super(shader, geometryData);
        this.textureId = textureId[0];
    }

    @Override
    public boolean draw(final Matrix4x4 modelMatrix, final float[] view, float[] perspective) {
        // Set the preloaded shaders to use
        shader.use();

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0);

        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0
        glUniform1i(shader.getUniform("u_Texture"), 0);
        CGUtils.checkGLError(TAG, "draw failure!");

        // We only need this call if the light would move
        //glUniform3fv(shader.getUniform("u_LightPos"), 1, AbstractCardboardActivity.light.getLightPosInEyeSpace(), 0);
        //glUniform4fv(shader.getUniform("u_LightColor"), 1, SolarSystemActivity.light.getColor(), 0);
        CGUtils.checkGLError(TAG, "draw failure!");

        // Set the ModelView in the shader, used to calculate lighting
        Matrix.multiplyMM(modelView, 0, view, 0, modelMatrix.getFloat16(), 0);
        glUniformMatrix4fv(shader.getUniform("u_MVMatrix"), 1, false, modelView, 0);
        CGUtils.checkGLError(TAG, "draw failure!");

        // Get the MV and MVP matrices for this model
        //Matrix.multiplyMM(modelView, 0, AbstractCardboardActivity.camera.getView(), 0, modelMatrix.get(), 0);
        Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);
        CGUtils.checkGLError(TAG, "draw failure!");

        // Set the MVP matrix for eye position
        glUniformMatrix4fv(shader.getUniform("u_MVP"), 1, false, modelViewProjection, 0);
        CGUtils.checkGLError(TAG, "draw failure!");

        // Set vertex attributes
        glVertexAttribPointer(shader.getAttribute("a_Position"), 3, GL_FLOAT, false, 0, getVerticesBuffer());
        CGUtils.checkGLError(TAG, "draw failure!");
        glVertexAttribPointer(shader.getAttribute("a_Normal"), 3, GL_FLOAT, false, 0, getNormalsBuffer());
        CGUtils.checkGLError(TAG, "draw failure!");
        glVertexAttribPointer(shader.getAttribute("a_TexCoordinate"), 2, GL_FLOAT, false, 0, getTextureBuffer());
        CGUtils.checkGLError(TAG, "draw failure!");

        if(getGeometryData().getIndicesArray() != null) {
            // Draw the shape with an index buffer
            glDrawElements(GL_TRIANGLES, getIndexBuffer().capacity(), GL_UNSIGNED_SHORT, getIndexBuffer());
        } else {
            glDrawArrays(GL_TRIANGLES, 0, getVerticesBuffer().capacity() / 3);
        }
        CGUtils.checkGLError(TAG, "draw failure!");
        return true;
    }

}
