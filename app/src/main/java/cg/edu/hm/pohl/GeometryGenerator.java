package cg.edu.hm.pohl;

import ba.pohl1.hm.edu.vrlibrary.data.GeometryData;

/**
 * Created by Pohl on 14.04.2016.
 */
public class GeometryGenerator {

    public static GeometryData createCube() {
        float[] verticesArray = new float[]{
                // Top
                -0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                // Bottom
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                // Front
                -0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                // Back
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                // Left
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                // Right
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
        };
        float[] normalsArray = new float[]{
                // Top
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                // Bottom
                0, -1, 0,
                0, -1, 0,
                0, -1, 0,
                0, -1, 0,
                0, -1, 0,
                0, -1, 0,
                // Front
                0, 0, -1,
                0, 0, -1,
                0, 0, -1,
                0, 0, -1,
                0, 0, -1,
                0, 0, -1,
                // Back
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                // Left
                -1, 0, 0,
                -1, 0, 0,
                -1, 0, 0,
                -1, 0, 0,
                -1, 0, 0,
                -1, 0, 0,
                // Right
                1, 0, 0,
                1, 0, 0,
                1, 0, 0,
                1, 0, 0,
                1, 0, 0,
                1, 0, 0,
        };
        float[] colorsArray = new float[]{
                // Top
                -0.5f, 0.5f, -0.5f, 1.0f,
                -0.5f, 0.5f, 0.5f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f,
                -0.5f, 0.5f, -0.5f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f,
                // Bottom
                -0.5f, -0.5f, -0.5f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f,
                -0.5f, -0.5f, 0.5f, 1.0f,
                -0.5f, -0.5f, -0.5f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f,
                // Front
                -0.5f, -0.5f, -0.5f, 1.0f,
                -0.5f, 0.5f, -0.5f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f,
                -0.5f, 0.5f, -0.5f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f,
                // Back
                -0.5f, -0.5f, 0.5f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f,
                -0.5f, 0.5f, 0.5f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f,
                -0.5f, 0.5f, 0.5f, 1.0f,
                // Left
                -0.5f, -0.5f, -0.5f, 1.0f,
                -0.5f, -0.5f, 0.5f, 1.0f,
                -0.5f, 0.5f, 0.5f, 1.0f,
                -0.5f, -0.5f, -0.5f, 1.0f,
                -0.5f, 0.5f, 0.5f, 1.0f,
                -0.5f, 0.5f, -0.5f, 1.0f,
                // Right
                0.5f, -0.5f, -0.5f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f,
        };
        final GeometryData data = new GeometryData(verticesArray, normalsArray);
        data.setColorsArray(colorsArray);
        return data;
    }
}
