package ba.pohl1.hm.edu.vrlibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;

/**
 * Sources: http://www.learnopengles.com/android-lesson-four-introducing-basic-texturing/
 * <p>
 * Created by Pohl on 11.02.2016.
 */
public class CGUtils {

    private static final Map<Integer, Integer> cachedTextureHandles = new HashMap<>();

    private static final Vector3 objectToCamera = new Vector3();
    // Help vector so we don't have to instantiate every time a new vector
    private static final Vector3 helpVector = new Vector3();

    public static boolean isLookingAt(final Vector3 headView, final VRComponent camera, final VRComponent object, final float angleLimit) {
        final Vector3 cameraPos = camera.getBoundingBox().getPosition();
        final Vector3 position = object.getBoundingBox().getPosition();
        objectToCamera.set(position.x, position.y, position.z).sub(cameraPos);

        helpVector.set(headView.x, headView.y, headView.z);
        final double angle = Vector3.getAngleBetween(headView, objectToCamera.normalize());
        return angle <= angleLimit;
    }

    public static void fillColors(final float[] target, final float[] color) {
        for(int i = 0; i < target.length; i += 4) {
            target[i] = color[0];
            target[i + 1] = color[1];
            target[i + 2] = color[2];
            target[i + 3] = color[3];
        }
    }

    public static void fillColors(final float[] target, final float[]... colors) {
        final int differentColors = colors.length;
        final int colorStride = target.length / differentColors;

        for(int i = 0; i < differentColors; i++) {
            final int offset = i * colorStride;
            for(int j = offset; j < offset + colorStride; j += 4) {
                target[j] = colors[i][0];
                target[j + 1] = colors[i][1];
                target[j + 2] = colors[i][2];
                target[j + 3] = colors[i][3];
            }
        }
    }

    /**
     * Loads the texture with the given id.
     *
     * @param context    the application's context
     * @param resourceId the id of the texture
     * @return the texture handle
     */
    public static int loadTexture(final Context context, final int resourceId) {
        final int[] textureHandle = new int[1];

        if(!cachedTextureHandles.containsKey(resourceId)) {
            GLES20.glGenTextures(1, textureHandle, 0);

            if(textureHandle[0] != 0) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false; // Pre scaling off

                final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);

                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

                bitmap.recycle();

                if(textureHandle[0] == 0) {
                    throw new RuntimeException("Error loading texture.");
                }

                cachedTextureHandles.put(resourceId, textureHandle[0]);
            }
        }
        return cachedTextureHandles.get(resourceId);
    }

    public static void clearTextures() {
        for(final int handle : cachedTextureHandles.values()) {
            final int[] handleArray = new int[]{handle};
            GLES30.glDeleteTextures(1, handleArray, 0);
        }
        cachedTextureHandles.clear();
    }

    /**
     * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
     *
     * @param label Label to report in case of error.
     */
    public static void checkGLError(final String tag, final String label) {
        int error;
        while((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(tag, label + ": glError " + error);
            throw new RuntimeException(label + ": glError " + error);
        }
    }

    public static float[] randomColor() {
        final int colors = 10;
        final double delta = 1 / (double) colors;
        final double random = Math.random();
        if(random < delta) {
            return CGConstants.COLOR_BLUE;
        }
        if(random < delta * 2) {
            return CGConstants.COLOR_CYAN;
        }
        if(random < delta * 3) {
            return CGConstants.COLOR_GREEN;
        }
        if(random < delta * 4) {
            return CGConstants.COLOR_LIGHT_GREEN;
        }
        if(random < delta * 5) {
            return CGConstants.COLOR_YELLOW;
        }
        if(random < delta * 6) {
            return CGConstants.COLOR_WHITE;
        }
        if(random < delta * 7) {
            return CGConstants.COLOR_DARK_GOLDEN_ROD;
        }
        if(random < delta * 8) {
            return CGConstants.COLOR_DARK_MAGENTA;
        }
        if(random < delta * 9) {
            return CGConstants.COLOR_DEEP_PINK;
        }
        return CGConstants.COLOR_RED;
    }

    /**
     * Creates a new VBO handle.
     *
     * @return the generated handle for the vbo
     */
    public static int createVBO() {
        int[] vboArray = new int[1];
        GLES30.glGenBuffers(1, vboArray, 0);
        return vboArray[0];
    }

    /**
     * Creates a new VAO handle.
     *
     * @return the generated handle for the vao
     */
    public static int createVAO() {
        int[] vaoArray = new int[1];
        GLES30.glGenVertexArrays(1, vaoArray, 0);
        return vaoArray[0];
    }

    /**
     * Creates a {@link IntBuffer} based on the given data.
     *
     * @param data the data for the buffer
     * @return the int buffer
     */
    public static IntBuffer createIntBuffer(final float[] data) {
        final int[] tmpData = new int[data.length];
        for(int i = 0; i < tmpData.length; i++) {
            tmpData[i] = Float.floatToRawIntBits(data[i]);
        }
        final ByteBuffer bbVertices = ByteBuffer.allocateDirect(tmpData.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        final IntBuffer intBuffer = bbVertices.asIntBuffer();
        intBuffer.put(tmpData);
        intBuffer.flip();
        return intBuffer;
    }

    /**
     * Creates a {@link FloatBuffer} based on the given data.
     *
     * @param data the data for the buffer
     * @return the float buffer
     */
    public static FloatBuffer createFloatBuffer(final float[] data) {
        ByteBuffer bbVertices = ByteBuffer.allocateDirect(data.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        final FloatBuffer fBuffer = bbVertices.asFloatBuffer();
        fBuffer.put(data);
        fBuffer.position(0);
        return fBuffer;
    }

    /**
     * Creates a {@link ShortBuffer} based on the given data.
     *
     * @param data the data for the buffer
     * @return the short buffer
     */
    public static ShortBuffer createShortBuffer(final short[] data) {
        if(data == null) {
            return null;
        }
        ByteBuffer bbVertices = ByteBuffer.allocateDirect(data.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        final ShortBuffer fBuffer = bbVertices.asShortBuffer();
        fBuffer.put(data);
        fBuffer.position(0);
        return fBuffer;
    }
}
