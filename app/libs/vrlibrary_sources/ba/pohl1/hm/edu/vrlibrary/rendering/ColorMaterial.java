package ba.pohl1.hm.edu.vrlibrary.rendering;

import android.opengl.GLES30;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.model.data.GeometryData;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;

import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.GL_UNSIGNED_SHORT;
import static android.opengl.GLES30.glDrawElements;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glVertexAttribPointer;

/**
 * Created by Pohl on 26.02.2016.
 */
public class ColorMaterial extends Material {

    private static final String TAG = "ColorMaterial";

    public ColorMaterial(final Shader shader, final GeometryData geometryData) {
        super(shader, geometryData);
    }

    public void updateColorBuffer() {
        getColorsBuffer().position(0);
        for(final float color : geometryData.getColorsArray()) {
            getColorsBuffer().put(Float.floatToRawIntBits(color));
        }
    }

    @Override
    public boolean draw(final Matrix4x4 modelMatrix, final float[] view, float[] perspective) {
        shader.use();

        glUniformMatrix4fv(shader.getUniform("u_model"), 1, false, modelMatrix.getFloat16(), 0);
        glUniformMatrix4fv(shader.getUniform("u_view"), 1, false, view, 0);
        glUniformMatrix4fv(shader.getUniform("u_projection"), 1, false, perspective, 0);

        getVerticesBuffer().position(0);
        glVertexAttribPointer(shader.getAttribute("a_position"), 3, GLES30.GL_FLOAT, false, 0, getVerticesBuffer());
        getColorsBuffer().position(0);
        glVertexAttribPointer(shader.getAttribute("a_color"), 4, GL_FLOAT, false, 0, getColorsBuffer());

        if(geometryData.getIndicesArray() != null) {
            glDrawElements(GL_TRIANGLES, getIndexBuffer().capacity(), GL_UNSIGNED_SHORT, getIndexBuffer());
        } else {
            glDrawArrays(GL_TRIANGLES, 0, getVerticesBuffer().capacity() / 3);
        }
        CGUtils.checkGLError(TAG, "Error while drawing!");
        return true;
    }
}
