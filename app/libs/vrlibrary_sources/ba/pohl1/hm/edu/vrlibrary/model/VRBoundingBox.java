package ba.pohl1.hm.edu.vrlibrary.model;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;

/**
 * A model class containing information about:
 * <p>
 *     <ul>
 *         <li>position</li>
 *         <li>size</li>
 *         <li>minimum bounds</li>
 *         <li>maximum bounds</li>
 *         <li>center bounds</li>
 *     </ul>
 * </p>
 *
 * Created by Pohl on 02.03.2016.
 */
public class VRBoundingBox {

    private Vector3 position = new Vector3();
    private Vector3 size = new Vector3();
    private Matrix4x4 matrix = new Matrix4x4();
    private Matrix4x4 unmodifiedModelMatrix = new Matrix4x4();

    /**
     * Updates the bounds of this box based on the given {@link Matrix4x4}.
     *
     * @param matrix the matrix to get the values from
     */
    public void update(final Matrix4x4 matrix) {
        this.matrix.set(matrix);
        position.x = matrix.getFloat16()[12];
        position.y = matrix.getFloat16()[13];
        position.z = matrix.getFloat16()[14];

        final Vector3 scaling = matrix.extractScaling();
        size.x = scaling.x;
        size.y = scaling.y;
        size.z = scaling.z;
    }

    public Matrix4x4 getModelMatrix() {
        return matrix;
    }

    public Vector3 getPosition() {
        return position;
    }

    public float getCenterX() {
        return position.x;
    }

    public float getCenterY() {
        return position.y;
    }

    public float getCenterZ() {
        return position.z;
    }

    public float getMinX() {
        return getCenterX() - 0.5f * getWidth();
    }

    public float getMinY() {
        return getCenterY() - 0.5f * getHeight();
    }

    public float getMinZ() {
        return getCenterZ() - 0.5f * getDepth();
    }

    public float getMaxX() {
        return getCenterX() + 0.5f * getWidth();
    }

    public float getMaxY() {
        return getCenterY() + 0.5f * getHeight();
    }

    public float getMaxZ() {
        return getCenterZ() + 0.5f * getDepth();
    }

    public float getWidth() {
        return size.x;
    }

    public float getHeight() {
        return size.y;
    }

    public float getDepth() {
        return size.z;
    }

    public Matrix4x4 getUnmodifiedModelMatrix() {
        return unmodifiedModelMatrix;
    }

    public void setUnmodifiedModelMatrix(Matrix4x4 unmodifiedModelMatrix) {
        this.unmodifiedModelMatrix.set(unmodifiedModelMatrix);
    }

    @Override
    public String toString() {
        return getModelMatrix() != null ? getModelMatrix().toString() : "";
    }
}
