package cg.edu.hm.pohl;

import android.opengl.Matrix;

import java.nio.FloatBuffer;

import ba.pohl1.hm.edu.vrlibrary.base.Shader;
import ba.pohl1.hm.edu.vrlibrary.base.rendering.Material;
import ba.pohl1.hm.edu.vrlibrary.data.GeometryData;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.util.BAConstants;
import ba.pohl1.hm.edu.vrlibrary.util.BAUtils;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static cg.edu.hm.pohl.AbstractCardboadActivity.colorShader;

/**
 * Created by Pohl on 14.04.2016.
 */
public class VRRoom extends VRComponent {

    private static final String TAG = "VRRoom";
    private static final float WIDTH = 25;
    private static final float DEPTH = 25;
    private static final float HEIGHT = 3;

    private GeometryData cubeData;

    private VRCube floor;

    private VRCube wallNorth;
    private VRCube wallSouth;
    private VRCube wallWest;
    private VRCube wallEast;

    private static final float[] modelView = new float[16];
    private static final float[] modelViewProjection = new float[16];

    public VRRoom() {
        setCollisionEnabled(false);
        cubeData = GeometryGenerator.createCube();

        floor = new VRCube(AbstractCardboadActivity.gridShader);
        floor.setCollisionEnabled(false);
        floor.translateY(-0.5f).scale(WIDTH, 1, DEPTH);

        // Define walls
        wallNorth = new VRCube(colorShader);
        wallNorth.translateZ(-0.5f - 0.5f * DEPTH).translateY(0.5f * HEIGHT).scale(WIDTH, HEIGHT, 1);
        wallNorth.setColor(BAConstants.COLOR_BLACK);

        wallSouth = new VRCube(colorShader);
        wallSouth.translateZ(0.5f + 0.5f * DEPTH).translateY(0.5f * HEIGHT).scale(WIDTH, HEIGHT, 1);
        wallSouth.setColor(BAConstants.COLOR_BLACK);

        wallWest = new VRCube(colorShader);
        wallWest.translateX(-0.5f - 0.5f * WIDTH).translateY(0.5f * HEIGHT).scale(1, HEIGHT, DEPTH);
        wallWest.setColor(BAConstants.COLOR_BLACK);

        wallEast = new VRCube(colorShader);
        wallEast.translateX(0.5f + 0.5f * WIDTH).translateY(0.5f * HEIGHT).scale(1, HEIGHT, DEPTH);
        wallEast.setColor(BAConstants.COLOR_BLACK);


        translateY(-1f);
    }

    @Override
    public void draw(float[] view, float[] perspective) {
        matrixStack.pushMatrix();

        // Apply transformations
        doTransform();

        // Draw floor
        floor.draw(view, perspective);
        // Draw walls
        wallNorth.draw(view, perspective);
        wallSouth.draw(view, perspective);
        wallWest.draw(view, perspective);
        wallEast.draw(view, perspective);

        matrixStack.popMatrix();
    }

    private class VRCube extends VRComponent {

        private Shader colorShader;
        private FloatBuffer verticesBuffer;
        private FloatBuffer colorsBuffer;

        public VRCube(final Shader cubeShader) {
            colorShader = cubeShader;
            verticesBuffer = Material.createFloatBuffer(cubeData.getVerticesArray());
            colorsBuffer = Material.createFloatBuffer(cubeData.getColorsArray());
        }

        @Override
        public void draw(float[] view, float[] perspective) {
            matrixStack.pushMatrix();

            // Apply transformations
            doTransform();

            // Calculate MV and MVP matrices
            Matrix.multiplyMM(modelView, 0, view, 0, getModelMatrix().getFloat16(), 0);
            Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);

            colorShader.use();

            glUniformMatrix4fv(colorShader.getUniform("u_model"), 1, false, getModelMatrix().getFloat16(), 0);
            glUniformMatrix4fv(colorShader.getUniform("u_view"), 1, false, view, 0);
            glUniformMatrix4fv(colorShader.getUniform("u_projection"), 1, false, perspective, 0);

            verticesBuffer.position(0);
            glVertexAttribPointer(colorShader.getAttribute("a_position"), 3, GL_FLOAT, false, 0, verticesBuffer);
            colorsBuffer.position(0);
            glVertexAttribPointer(colorShader.getAttribute("a_color"), 4, GL_FLOAT, false, 0, colorsBuffer);

            glDrawArrays(GL_TRIANGLES, 0, verticesBuffer.capacity() / 3);
            BAUtils.checkGLError(TAG, "Error while drawing!");

            matrixStack.popMatrix();
        }

        private void setColor(final float[] color) {
            final float[] colorsArray = new float[colorsBuffer.capacity()];
            BAUtils.fillColors(colorsArray, color);
            colorsBuffer.put(colorsArray);
        }
    }
}
