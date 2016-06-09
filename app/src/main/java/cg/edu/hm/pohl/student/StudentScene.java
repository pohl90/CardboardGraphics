package cg.edu.hm.pohl.student;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.rendering.RendererManager;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;
import cg.edu.hm.pohl.CardboardGraphicsActivity;
import cg.edu.hm.pohl.DataStructures;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Pohl on 14.04.2016.
 */
public class StudentScene extends VRComponent {

    private static final String TAG = "StudentScene";
    private static final int TESSELLATION = 8;
    private static final int NUMBER_OF_VERTICES = TESSELLATION * 3 * 3 * 2;
    private static final float RADIUS = .5f;
    private static final double PI_2 = 2.0f * Math.PI;
    private static final double DELTA_ANGLE = (Math.PI / (TESSELLATION / 2));

    private static final int FLOATS_PER_VERTEX = 3;
    private static final int FLOATS_PER_COLOR = 4;
    private static final int FLOATS_PER_NORMAL = 3;

    private float[] coneVertices;
    private float[] coneColors;
    private float[] coneNormals;
    private float[] coneBottomVertices;
    private float[] coneBottomColors;
    private float[] coneBottomNormals;

    private FloatBuffer verticesBuffer;
    private FloatBuffer colorsBuffer;
    private FloatBuffer normalsBuffer;
    private FloatBuffer verticesBottomBuffer;
    private FloatBuffer colorsBottomBuffer;
    private FloatBuffer normalsBottomBuffer;
    private Shader shader;

    private DataStructures.Matrices matrices = new DataStructures.Matrices();
    private DataStructures.Locations locations = new DataStructures.Locations();
    private DataStructures.AnimationParameters animation = new DataStructures.AnimationParameters();

    private float[] lightpos = { 0.f, 5.f, -4.f, 1.f };
    private float[] lightpos_eye = new float[4];

    private DataStructures.LightParameters light = new DataStructures.LightParameters();

    public StudentScene() {
        shader = CardboardGraphicsActivity.studentSceneShader;
        createCone();
        // Get the shader's attribute and uniform handles used to delegate data from
        // the CPU to the GPU
        locations.vertex_in = shader.getAttribute("vertex_in");
        locations.color_in = shader.getAttribute("color_in");
        locations.normal_in = shader.getAttribute("normal_in");
        locations.pvm = shader.getUniform("pvm");
        locations.vm = shader.getUniform("vm");
        locations.lightpos = shader.getUniform("lightpos");
        locations.light_ambient = shader.getUniform("light.ambient");
        locations.light_diffuse = shader.getUniform("light.diffuse");
        locations.light_specular = shader.getUniform("light.specular");
        // Add this VRComponent in order to be rendered
        RendererManager.getInstance().add(this);
    }

    public void draw(final float[] view, float[] projection) {
        // Use the shader
        shader.use();

        // Set the identity of the matrix
        identity();

        // Transform the shape
        translateZ(-5f);
        translateY(2.0f);
        rotateZ(90);

        // Update collision box bounds
        updateBounds(this);

        // Calculate light position in the eye space
        Matrix.multiplyMV(lightpos_eye, 0, view, 0, lightpos, 0);

        // Calculate Model-View and Model-View-Projection matrices
        Matrix.multiplyMM(matrices.vm, 0, view, 0, getFloat16(), 0);
        Matrix.multiplyMM(matrices.pvm, 0, projection, 0, matrices.vm, 0);

        // Put the parameters into the shaders
        glUniformMatrix4fv(locations.pvm, 1, false, matrices.pvm, 0);
        glUniformMatrix4fv(locations.vm, 1, false, matrices.vm, 0);
        glUniform4fv(locations.lightpos, 1, lightpos_eye, 0);
        glUniform4fv(locations.light_ambient, 1, light.ambient, 0);
        glUniform4fv(locations.light_diffuse, 1, light.diffuse, 0);
        glUniform4fv(locations.light_specular, 1, light.specular, 0);

        // Finally draw the cone
        drawTopPart();
        drawBottomPart();

        // Check for possible errors.
        // If there is one, it is most probably related to an issue with the
        // shader params. Copy this line under every 'glUniform' method call
        // to identify the source of the error.
        CGUtils.checkGLError(TAG, "Error while drawing!");
    }

    private void drawTopPart() {
        glVertexAttribPointer(locations.vertex_in, FLOATS_PER_VERTEX, GL_FLOAT, false, 0, verticesBuffer);
        glVertexAttribPointer(locations.color_in, FLOATS_PER_COLOR, GL_FLOAT, false, 0, colorsBuffer);
        glVertexAttribPointer(locations.normal_in, FLOATS_PER_NORMAL, GL_FLOAT, false, 0, normalsBuffer);

        glDrawArrays(GLES20.GL_TRIANGLES, 0, coneVertices.length / FLOATS_PER_VERTEX);
    }

    private void drawBottomPart() {
        glVertexAttribPointer(locations.vertex_in, FLOATS_PER_VERTEX, GL_FLOAT, false, 0, verticesBottomBuffer);
        glVertexAttribPointer(locations.color_in, FLOATS_PER_COLOR, GL_FLOAT, false, 0, colorsBottomBuffer);
        glVertexAttribPointer(locations.normal_in, FLOATS_PER_NORMAL, GL_FLOAT, false, 0, normalsBottomBuffer);

        glDrawArrays(GLES20.GL_TRIANGLES, 0, coneBottomVertices.length / FLOATS_PER_VERTEX);
    }

    private void createCone() {
        coneVertices = new float[NUMBER_OF_VERTICES];
        coneColors = new float[NUMBER_OF_VERTICES / 3 * 4];
        coneNormals = new float[NUMBER_OF_VERTICES];
        coneBottomVertices = new float[NUMBER_OF_VERTICES];
        coneBottomColors = new float[NUMBER_OF_VERTICES / 3 * 4];
        coneBottomNormals = new float[NUMBER_OF_VERTICES];

        int index = 0;
        for(float angle = 0.0f; angle < PI_2; angle += DELTA_ANGLE) {
            // Calculate x and z of the cone
            float x1 = (float) (RADIUS * Math.sin(angle));
            float z1 = (float) (RADIUS * Math.cos(angle));
            float x2 = (float) (RADIUS * Math.sin(angle + DELTA_ANGLE));
            float z2 = (float) (RADIUS * Math.cos(angle + DELTA_ANGLE));

            // Calculate offset for vertices and colors
            final int vertexIndex = index * 9;
            final int colorIndex = index * 12;
            // Increment the index
            index++;

            // First vertex
            coneVertices[vertexIndex] = 0;
            coneVertices[vertexIndex + 1] = .5f;
            coneVertices[vertexIndex + 2] = 0;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = 1f;
            coneNormals[vertexIndex + 2] = 0;
            // Second vertex
            coneVertices[vertexIndex + 3] = x1;
            coneVertices[vertexIndex + 4] = -.5f;
            coneVertices[vertexIndex + 5] = z1;
            coneNormals[vertexIndex + 3] = x1;
            coneNormals[vertexIndex + 4] = .5f;
            coneNormals[vertexIndex + 5] = z1;
            // Third vertex
            coneVertices[vertexIndex + 6] = x2;
            coneVertices[vertexIndex + 7] = -.5f;
            coneVertices[vertexIndex + 8] = z2;
            coneNormals[vertexIndex + 6] = x2;
            coneNormals[vertexIndex + 7] = .5f;
            coneNormals[vertexIndex + 8] = z2;

            // First bottom vertex
            coneBottomVertices[vertexIndex] = 0;
            coneBottomVertices[vertexIndex + 1] = -.5f;
            coneBottomVertices[vertexIndex + 2] = 0;
            coneBottomNormals[vertexIndex] = 0;
            coneBottomNormals[vertexIndex + 1] = 1f;
            coneBottomNormals[vertexIndex + 2] = 0;
            // Second bottom vertex
            coneBottomVertices[vertexIndex + 3] = x1;
            coneBottomVertices[vertexIndex + 4] = -.5f;
            coneBottomVertices[vertexIndex + 5] = z1;
            coneBottomNormals[vertexIndex + 3] = 0;
            coneBottomNormals[vertexIndex + 4] = 1f;
            coneBottomNormals[vertexIndex + 5] = 0;
            // Third bottom vertex
            coneBottomVertices[vertexIndex + 6] = x2;
            coneBottomVertices[vertexIndex + 7] = -.5f;
            coneBottomVertices[vertexIndex + 8] = z2;
            coneBottomNormals[vertexIndex + 6] = 0;
            coneBottomNormals[vertexIndex + 7] = 1f;
            coneBottomNormals[vertexIndex + 8] = 0;

            // Alternate the color
            float colorR, colorG, colorB, colorA;
            if((index % 2) == 0) {
                colorR = 1f;
                colorG = 0f;
                colorB = 0f;
                colorA = 1f;
            } else {
                colorR = 0f;
                colorG = 0f;
                colorB = 1f;
                colorA = 1f;
            }
            coneColors[colorIndex] = colorR;
            coneColors[colorIndex + 1] = colorG;
            coneColors[colorIndex + 2] = colorB;
            coneColors[colorIndex + 3] = colorA;
            coneColors[colorIndex + 4] = colorR;
            coneColors[colorIndex + 5] = colorG;
            coneColors[colorIndex + 6] = colorB;
            coneColors[colorIndex + 7] = colorA;
            coneColors[colorIndex + 8] = colorR;
            coneColors[colorIndex + 9] = colorG;
            coneColors[colorIndex + 10] = colorB;
            coneColors[colorIndex + 11] = colorA;

            coneBottomColors[colorIndex] = colorR;
            coneBottomColors[colorIndex + 1] = colorG;
            coneBottomColors[colorIndex + 2] = colorB;
            coneBottomColors[colorIndex + 3] = colorA;
            coneBottomColors[colorIndex + 4] = colorR;
            coneBottomColors[colorIndex + 5] = colorG;
            coneBottomColors[colorIndex + 6] = colorB;
            coneBottomColors[colorIndex + 7] = colorA;
            coneBottomColors[colorIndex + 8] = colorR;
            coneBottomColors[colorIndex + 9] = colorG;
            coneBottomColors[colorIndex + 10] = colorB;
            coneBottomColors[colorIndex + 11] = colorA;
        }

        // Create buffers needed by the GPU
        verticesBuffer = CGUtils.createFloatBuffer(coneVertices);
        colorsBuffer = CGUtils.createFloatBuffer(coneColors);
        normalsBuffer = CGUtils.createFloatBuffer(coneNormals);

        verticesBottomBuffer = CGUtils.createFloatBuffer(coneBottomVertices);
        colorsBottomBuffer = CGUtils.createFloatBuffer(coneBottomColors);
        normalsBottomBuffer = CGUtils.createFloatBuffer(coneBottomNormals);
    }
}
