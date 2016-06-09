package ba.pohl1.hm.edu.vrlibrary.model.data;

import ba.pohl1.hm.edu.vrlibrary.maths.Vector2;
import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;

/**
 * Created by Pohl on 10.04.2016.
 */
public final class GeometryGenerator {

    private static float[] cubeVertices;
    private static float[] cubeNormals;
    private static float[] cubeTextures;

    private static float[] sphereVertices;
    private static float[] sphereNormals;
    private static float[] sphereTextures;
    private static short[] sphereIndices;

    private GeometryGenerator() {
        // Prevents instantiation
    }

    public static GeometryData createCube(final boolean instanced) {
        loadCubeGeometry();
        float[] colorsArray;
        if(instanced) {
            colorsArray = new float[]{
                0.5f, 0.5f, 0.5f, 1.0f,
            };
        } else {
            colorsArray = new float[]{
                    // Top
                    0.5f, 0.5f, 0.5f, 1.0f,
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
        }
        final GeometryData data = new GeometryData(cubeVertices, cubeNormals);
        data.setTexArray(cubeTextures);
        data.setColorsArray(colorsArray);
        return data;
    }
    /**
     * Creates the data for the sphere.
     * <p/>
     * Source: http://stash.defectivestudios.com:7990/projects/CRDBK/repos/renderbox/browse
     */
    public static GeometryData createSphere(final boolean instancedSphere) {
        loadSphereGeometry();
        final float[] colorsArray = new float[instancedSphere ? 4 : sphereVertices.length / 3 * 4];
        CGUtils.fillColors(colorsArray, CGConstants.COLOR_RED);
        final GeometryData data = new GeometryData(sphereIndices, sphereVertices, sphereNormals, sphereTextures);
        data.setColorsArray(colorsArray);
        return data;
    }

    private static void loadCubeGeometry() {
        if(cubeVertices == null) {
            cubeVertices = new float[]{
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
                    // Back
                    -0.5f, -0.5f, -0.5f,
                    -0.5f, 0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    -0.5f, 0.5f, -0.5f,
                    0.5f, 0.5f, -0.5f,
                    // Front
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
        }
        if(cubeNormals == null) {
            cubeNormals = new float[]{
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
                    // Back
                    0, 0, -1,
                    0, 0, -1,
                    0, 0, -1,
                    0, 0, -1,
                    0, 0, -1,
                    0, 0, -1,
                    // Front
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
        }
        if(cubeTextures == null) {
            cubeTextures = new float[]{
                    // Top
                    0.f, 1.f,
                    1.f, 1.f,
                    0.f, 0.f,
                    1.f, 1.f,
                    1.f, 0.f,
                    0.f, 0.f,
                    // Bottom
                    0.f, 1.f,
                    1.f, 1.f,
                    0.f, 0.f,
                    1.f, 1.f,
                    1.f, 0.f,
                    0.f, 0.f,
                    // Back
                    0.f, 1.f,
                    1.f, 1.f,
                    0.f, 0.f,
                    1.f, 1.f,
                    1.f, 0.f,
                    0.f, 0.f,
                    // Front
                    0.f, 1.f,
                    1.f, 1.f,
                    0.f, 0.f,
                    1.f, 1.f,
                    1.f, 0.f,
                    0.f, 0.f,
                    // Left
                    0.f, 1.f,
                    1.f, 1.f,
                    0.f, 0.f,
                    1.f, 1.f,
                    1.f, 0.f,
                    0.f, 0.f,
                    // Right
                    0.f, 1.f,
                    1.f, 1.f,
                    0.f, 0.f,
                    1.f, 1.f,
                    1.f, 0.f,
                    0.f, 0.f,
            };
        }
    }

    private static void loadSphereGeometry() {
        if(sphereVertices == null) {
            float radius = 0.5f;
            // Longitude |||
            int nbLong = CGConstants.SPHERE_LONGITUDE;
            // Latitude ---
            int nbLat = CGConstants.SPHERE_LATITUDE;

            Vector3[] vertices = new Vector3[(nbLong + 1) * nbLat + nbLong * 2];
            double pi = Math.PI;
            double pi2 = Math.PI * 2;

            //Top and bottom vertices are duplicated
            for(int i = 0; i < nbLong; i++) {
                vertices[i] = new Vector3(Vector3.up).mult(radius);
                vertices[vertices.length - i - 1] = new Vector3(Vector3.up).mult(-radius);
            }
            for(int lat = 0; lat < nbLat; lat++) {
                double a1 = pi * (float) (lat + 1) / (nbLat + 1);
                float sin1 = (float) Math.sin(a1);
                float cos1 = (float) Math.cos(a1);

                for(int lon = 0; lon <= nbLong; lon++) {
                    double a2 = pi2 * (float) (lon == nbLong ? 0 : lon) / nbLong;
                    float sin2 = (float) Math.sin(a2);
                    float cos2 = (float) Math.cos(a2);

                    vertices[lon + lat * (nbLong + 1) + nbLong] = new Vector3(sin1 * cos2, cos1, sin1 * sin2).mult(radius);
                }
            }

            Vector3[] normals = new Vector3[vertices.length];
            for(int n = 0; n < vertices.length; n++) {
                normals[n] = new Vector3(vertices[n]).normalize();
            }

            Vector2[] uvs = new Vector2[vertices.length];
            float uvStart = 1.0f / (nbLong * 2);
            float uvStride = 1.0f / nbLong;
            for(int i = 0; i < nbLong; i++) {
                uvs[i] = new Vector2(uvStart + i * uvStride, 1f);
                uvs[uvs.length - i - 1] = new Vector2(1 - (uvStart + i * uvStride), 0f);
            }
            for(int lat = 0; lat < nbLat; lat++) {
                for(int lon = 0; lon <= nbLong; lon++) {
                    uvs[lon + lat * (nbLong + 1) + nbLong] = new Vector2((float) lon / nbLong, 1f - (float) (lat + 1) / (nbLat + 1));
                }
            }

            int nbFaces = (nbLong + 1) * nbLat + 2;
            int nbTriangles = nbFaces * 2;
            int indices = nbTriangles * 3;
            sphereIndices = new short[indices];

            // Top
            int i = 0;
            for(short lon = 0; lon < nbLong; lon++) {
                sphereIndices[i++] = lon;
                sphereIndices[i++] = (short) (nbLong + lon + 1);
                sphereIndices[i++] = (short) (nbLong + lon);
            }

            // Mid
            for(short lat = 0; lat < nbLat - 1; lat++) {
                for(short lon = 0; lon < nbLong; lon++) {
                    short current = (short) (lon + lat * (nbLong + 1) + nbLong);
                    short next = (short) (current + nbLong + 1);

                    sphereIndices[i++] = current;
                    sphereIndices[i++] = (short) (current + 1);
                    sphereIndices[i++] = (short) (next + 1);

                    sphereIndices[i++] = current;
                    sphereIndices[i++] = (short) (next + 1);
                    sphereIndices[i++] = next;
                }
            }

            // Bottom
            for(short lon = 0; lon < nbLong; lon++) {
                sphereIndices[i++] = (short) (vertices.length - lon - 1);
                sphereIndices[i++] = (short) (vertices.length - nbLong - (lon + 1) - 1);
                sphereIndices[i++] = (short) (vertices.length - nbLong - (lon) - 1);
            }

            // Convert all float[][] into one float[]
            sphereVertices = new float[vertices.length * 3];
            for(i = 0; i < vertices.length; i++) {
                int step = i * 3;
                sphereVertices[step] = vertices[i].x;
                sphereVertices[step + 1] = vertices[i].y;
                sphereVertices[step + 2] = vertices[i].z;
            }
            sphereNormals = new float[normals.length * 3];
            for(i = 0; i < normals.length; i++) {
                int step = i * 3;
                sphereNormals[step] = normals[i].x;
                sphereNormals[step + 1] = normals[i].y;
                sphereNormals[step + 2] = normals[i].z;
            }
            sphereTextures = new float[uvs.length * 2];
            for(i = 0; i < uvs.length; i++) {
                int step = i * 2;
                sphereTextures[step] = uvs[i].x;
                sphereTextures[step + 1] = uvs[i].y;
            }
        }
    }

    public static GeometryData createArrow() {
        final float[] vertices = new float[]{
                -0.5f, 0.0f, 0.5f,
                0.0f, 0.0f, 0.2f,
                0.0f, 0.0f, -0.5f,
                0.0f, 0.0f, -0.5f,
                0.0f, 0.0f, 0.2f,
                0.5f, 0.0f, 0.5f,
        };
        final float[] normals = new float[]{
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        };
        final float[] colors = new float[]{
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
        };
        final GeometryData data = new GeometryData(vertices, normals);
        data.setColorsArray(colors);
        return data;
    }
}
