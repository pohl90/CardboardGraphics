package ba.pohl1.hm.edu.vrlibrary.util;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES30;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glUseProgram;

/**
 * Created by Pohl on 14.04.2016.
 */
public class Shader {

    private static final String TAG = "Shader";
    private final int vertexShaderId;
    private final int fragmentShaderId;
    private Context context;
    private Map<String, Integer> attributes = new HashMap<>();
    private Map<String, Integer> uniforms = new HashMap<>();

    private int program;
    private int vertexShader;
    private int fragmentShader;

    /**
     * Creates a new shader.
     *
     * @param vertexShaderId
     * @param fragmentShaderId
     */
    public Shader(final int vertexShaderId, final int fragmentShaderId) {
        this.context = CardboardGraphics.context;
        this.vertexShaderId = vertexShaderId;
        this.fragmentShaderId = fragmentShaderId;
        setup();
    }

    /**
     * Converts a raw text file, saved as a resource, into an OpenGL ES shader.
     *
     * @param tag   The tag used for logging
     * @param type  The type of shader we will be creating.
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The shader object handler.
     */
    public static int loadGLShader(final String tag, int type, final Resources resources, int resId) {
        String code = readRawTextFile(resources, resId);
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, code);
        GLES30.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if(compileStatus[0] == 0) {
            Log.e(tag, "Error compiling shader: " + GLES30.glGetShaderInfoLog(shader));
            GLES30.glDeleteShader(shader);
            shader = 0;
        }

        if(shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }

    /**
     * Converts a raw text file into a string.
     *
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The context of the text file, or null in case of error.
     */
    public static String readRawTextFile(final Resources resources, int resId) {
        InputStream inputStream = resources.openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isParamSetupDone() {
        return !attributes.isEmpty() && !uniforms.isEmpty();
    }

    public int getAttribute(final String name) {
        if(!attributes.containsKey(name)) {
            attributes.put(name, glGetAttribLocation(program, name));
            enableAttribute(name);
        }
        return attributes.get(name);
    }

    public int getUniform(final String name) {
        return getUniform(name, true);
    }

    public int getUniform(final String name, final boolean enable) {
        if(!uniforms.containsKey(name)) {
            uniforms.put(name, glGetUniformLocation(program, name));
            if(enable) {
                enableAttribute(name);
            }
        }
        return uniforms.get(name);
    }

    public void enableAttribute(final String name) {
        if(attributes.containsKey(name)) {
            glEnableVertexAttribArray(attributes.get(name));
        } else if(uniforms.containsKey(name)) {
            glEnableVertexAttribArray(uniforms.get(name));
        }
    }

    public void use() {
        GLES30.glUseProgram(program);
    }

    public void dispose() {
        GLES30.glDeleteProgram(program);
    }

    private void setup() {
        program = GLES30.glCreateProgram();
        vertexShader = loadGLShader(TAG, GLES30.GL_VERTEX_SHADER, context.getResources(), vertexShaderId);
        fragmentShader = loadGLShader(TAG, GLES30.GL_FRAGMENT_SHADER, context.getResources(), fragmentShaderId);

        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);

        glLinkProgram(program);
        glUseProgram(program);
    }
}
