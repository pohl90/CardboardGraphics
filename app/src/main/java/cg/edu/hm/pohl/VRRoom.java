package cg.edu.hm.pohl;

import android.opengl.Matrix;

import java.nio.FloatBuffer;

import ba.pohl1.hm.edu.vrlibrary.base.Shader;
import ba.pohl1.hm.edu.vrlibrary.base.manager.CardboardGraphics;
import ba.pohl1.hm.edu.vrlibrary.base.manager.RendererManager;
import ba.pohl1.hm.edu.vrlibrary.data.GeometryData;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

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

    public VRRoom() {
        setCollisionEnabled(false);
        RendererManager.getInstance().add(this);

        cubeData = GeometryGenerator.createCube();

        floor = new VRCube(CardboardGraphics.gridShader);
        floor.setCollisionEnabled(false);
        floor.translateY(-0.5f).scale(WIDTH, 1, DEPTH);

        // Define walls
        wallNorth = new VRCube(CardboardGraphics.colorShader);
        wallNorth.translateZ(-0.5f - 0.5f * DEPTH).translateY(0.5f * HEIGHT).scale(WIDTH, HEIGHT, 1);
        wallNorth.setColor(CGConstants.COLOR_BLACK);

        wallSouth = new VRCube(CardboardGraphics.colorShader);
        wallSouth.translateZ(0.5f + 0.5f * DEPTH).translateY(0.5f * HEIGHT).scale(WIDTH, HEIGHT, 1);
        wallSouth.setColor(CGConstants.COLOR_BLACK);

        wallWest = new VRCube(CardboardGraphics.colorShader);
        wallWest.translateX(-0.5f - 0.5f * WIDTH).translateY(0.5f * HEIGHT).scale(1, HEIGHT, DEPTH);
        wallWest.setColor(CGConstants.COLOR_BLACK);

        wallEast = new VRCube(CardboardGraphics.colorShader);
        wallEast.translateX(0.5f + 0.5f * WIDTH).translateY(0.5f * HEIGHT).scale(1, HEIGHT, DEPTH);
        wallEast.setColor(CGConstants.COLOR_BLACK);
    }

    @Override
    public boolean hasMaterial() {
        // Pretend that we have a material set.
        // Otherwise the RendererManager won't draw us...
        return true;
    }

    @Override
    public void draw(float[] view, float[] perspective) {
        Matrix.setIdentityM(getModelMatrix().getFloat16(), 0);
        transform(getModelMatrix());
        // Draw floor
        floor.draw(view, perspective);
        // Draw walls
        wallNorth.draw(view, perspective);
        wallSouth.draw(view, perspective);
        wallWest.draw(view, perspective);
        wallEast.draw(view, perspective);
    }

    private class VRCube extends VRComponent {

        private Shader colorShader;
        private FloatBuffer verticesBuffer;
        private FloatBuffer colorsBuffer;

        private boolean transformed;
        public VRCube(final Shader cubeShader) {
            colorShader = cubeShader;
            verticesBuffer = CGUtils.createFloatBuffer(cubeData.getVerticesArray());
            colorsBuffer = CGUtils.createFloatBuffer(cubeData.getColorsArray());
        }

        @Override
        public void setColor(float[] color) {
            colorsBuffer.position(0);
            for(int i = 0; i < cubeData.getColorsArray().length; i++) {
                colorsBuffer.put(i, color[i % 4]);
            }
        }

        @Override
        public boolean isDirty() {
            return !transformed;
        }

        @Override
        public void draw(float[] view, float[] perspective) {
            if(!transformed) {
                getModelMatrix().set(VRRoom.this.getModelMatrix());
                // Apply transformations
                doTransform();
                transformed = true;
            }
            colorShader.use();

            glUniformMatrix4fv(colorShader.getUniform("u_model"), 1, false, getModelMatrix().getFloat16(), 0);
            glUniformMatrix4fv(colorShader.getUniform("u_view"), 1, false, view, 0);
            glUniformMatrix4fv(colorShader.getUniform("u_projection"), 1, false, perspective, 0);

            verticesBuffer.position(0);
            glVertexAttribPointer(colorShader.getAttribute("a_position"), 3, GL_FLOAT, false, 0, verticesBuffer);
            colorsBuffer.position(0);
            glVertexAttribPointer(colorShader.getAttribute("a_color"), 4, GL_FLOAT, false, 0, colorsBuffer);

            glDrawArrays(GL_TRIANGLES, 0, verticesBuffer.capacity() / 3);
            CGUtils.checkGLError(TAG, "Error while drawing!");
        }

    }
}
