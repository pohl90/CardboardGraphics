package ba.pohl1.hm.edu.vrlibrary.maths;

import android.opengl.Matrix;

import java.util.Arrays;

/**
 * A matrix model representing a 4x4 matrix used by OpenGL.
 * <p>
 * Created by Pohl on 27.02.2016.
 */
public class Matrix4x4 {

    /**
     * The Matrix4x4 has the form:
     * _                      _
     * |                        |
     * | M_00  M_10  M_20  M_30 |
     * | M_10  M_11  M_21  M_31 |
     * | M_20  M_12  M_22  M_32 |
     * | M_30  M_13  M_23  M_33 |
     * |_                      _|
     */
    private static final int M_00 = 0;
    private static final int M_10 = 1;
    private static final int M_20 = 2;
    private static final int M_30 = 3;
    private static final int M_01 = 4;
    private static final int M_11 = 5;
    private static final int M_21 = 6;
    private static final int M_31 = 7;
    private static final int M_02 = 8;
    private static final int M_12 = 9;
    private static final int M_22 = 10;
    private static final int M_32 = 11;

    private final Vector3 position = new Vector3();
    private final Vector3 scaling = new Vector3();
    private float[] matrix = new float[16];

    private boolean dirty = true;
    private boolean bufferUpdateNeeded = true;

    /**
     * Creates a new {@link Matrix4x4} instance.
     */
    public Matrix4x4() {
        identity();
    }

    /**
     * Gets the float array representation of the matrix.
     *
     * @return the matrix as a float array
     */
    public float[] getFloat16() {
        return matrix;
    }

    public Vector3 getPosition() {
        return position.set(matrix[12], matrix[13], matrix[14]);
    }

    public Matrix4x4 set(final float[] otherMatrix) {
        matrix[0] = otherMatrix[0];
        matrix[1] = otherMatrix[1];
        matrix[2] = otherMatrix[2];
        matrix[3] = otherMatrix[3];
        matrix[4] = otherMatrix[4];
        matrix[5] = otherMatrix[5];
        matrix[6] = otherMatrix[6];
        matrix[7] = otherMatrix[7];
        matrix[8] = otherMatrix[8];
        matrix[9] = otherMatrix[9];
        matrix[10] = otherMatrix[10];
        matrix[11] = otherMatrix[11];
        matrix[12] = otherMatrix[12];
        matrix[13] = otherMatrix[13];
        matrix[14] = otherMatrix[14];
        matrix[15] = otherMatrix[15];
        invalidate();
        return this;
    }
    /**
     * Sets this matrix based on another.
     *
     * @param other the other matrix to get the values from
     */
    public Matrix4x4 set(final Matrix4x4 other) {
        final float[] otherMatrix = other.getFloat16();
        return set(otherMatrix);
    }

    /**
     * Returns a copy of this matrix.
     *
     * @return the copy of this matrix
     */
    public Matrix4x4 copy() {
        final Matrix4x4 copy = new Matrix4x4();
        copy.set(this);
        return copy;
    }

    /**
     * Sets the identity matrix and returns this instance.
     *
     * @return this instance
     */
    public Matrix4x4 identity() {
        Matrix.setIdentityM(matrix, 0);
        invalidate();
        return this;
    }

    public Matrix4x4 setX(final float x) {
        getFloat16()[12] = x;
        invalidate();
        return this;
    }

    public Matrix4x4 setY(final float y) {
        getFloat16()[13] = y;
        invalidate();
        return this;
    }

    public Matrix4x4 setZ(final float z) {
        getFloat16()[14] = z;
        invalidate();
        return this;
    }

    /**
     * Translates the matrix and returns this instance.
     *
     * @param x the x value to translate
     * @param y the y value to translate
     * @param z the z value to translate
     * @return this instance
     */
    public Matrix4x4 translate(final float x, final float y, final float z) {
        return translate(getFloat16(), x, y, z);
    }

    public Matrix4x4 unscaledTranslate(final float x, final float y, final float z) {
        final Vector3 scaling = extractScaling();
        final float unscaledX = x / scaling.x;
        final float unscaledY = y / scaling.y;
        final float unscaledZ = z / scaling.z;
        return translate(unscaledX, unscaledY, unscaledZ);
    }

    private Matrix4x4 translate(final float[] matrix, final float x, final float y, final float z) {
        Matrix.translateM(matrix, 0, x, y, z);
        invalidate();
        return this;
    }

    /**
     * Translates the matrix by x.
     *
     * @param x the x value to translate
     * @return this instance
     */
    public Matrix4x4 translateX(final float x) {
        return translate(x, 0, 0);
    }

    /**
     * Translates the matrix by y.
     *
     * @param y the y value to translate
     * @return this instance
     */
    public Matrix4x4 translateY(final float y) {
        return translate(0, y, 0);
    }

    /**
     * Translates the matrix by z.
     *
     * @param z the z value to translate
     * @return this instance
     */
    public Matrix4x4 translateZ(final float z) {
        return translate(0, 0, z);
    }

    /**
     * Rotates the matrix by the given parameters.
     *
     * @param angle the rotation's angle
     * @param x     the x axis weight
     * @param y     the y axis weight
     * @param z     the z axis weight
     * @return this instance
     */
    public Matrix4x4 rotate(float angle, float x, float y, float z) {
        return rotate(getFloat16(), angle, x, y, z);
    }

    public Matrix4x4 rotate(final float[] matrix, final float angle, final float x, final float y, final float z) {
        Matrix.rotateM(matrix, 0, angle, x, y, z);
        invalidate();
        return this;
    }

    /**
     * Rotates the matrix by the given angle around x.
     *
     * @param angle the rotation's angle
     * @return this instance
     */
    public Matrix4x4 rotateX(final float angle) {
        return rotate(angle, 1, 0, 0);
    }

    /**
     * Rotates the matrix by the given angle around y.
     *
     * @param angle the rotation's angle
     * @return this instance
     */
    public Matrix4x4 rotateY(final float angle) {
        return rotate(angle, 0, 1, 0);
    }

    /**
     * Rotates the matrix by the given angle around z.
     *
     * @param angle the rotation's angle
     * @return this instance
     */
    public Matrix4x4 rotateZ(final float angle) {
        return rotate(angle, 0, 0, 1);
    }

    /**
     * Scales the matrix by the given value.
     *
     * @param value the scaling value
     * @return this instance
     */
    public Matrix4x4 scale(final float value) {
        return scale(value, value, value);
    }

    /**
     * Scales the matrix by the given values.
     *
     * @param x the x axis scaling
     * @param y the y axis scaling
     * @param z the z axis scaling
     * @return this instance
     */
    public Matrix4x4 scale(final float x, final float y, final float z) {
        return scale(getFloat16(), x, y, z);
    }

    private Matrix4x4 scale(final float[] matrix, final float x, final float y, final float z) {
        if(!(x == 1 && y == 1 && z == 1)) {
            Matrix.scaleM(matrix, 0, x, y, z);
            invalidate();
        }
        return this;
    }

    /**
     * Applies a {@link Matrix#multiplyMM(float[], int, float[], int, float[], int)} on this matrix.
     *
     * @param leftHandMatrix  the left hand matrix
     * @param rightHandMatrix the right hand matrix
     * @return this instane
     */
    public Matrix4x4 multiplyMM(Matrix4x4 leftHandMatrix, Matrix4x4 rightHandMatrix) {
        Matrix.multiplyMM(getFloat16(), 0, leftHandMatrix.getFloat16(), 0, rightHandMatrix.getFloat16(), 0);
        invalidate();
        return this;
    }

    /**
     * Negates/Inverts thie matrix.
     *
     * @return this instance
     */
    public Matrix4x4 negate() {
        Matrix.invertM(matrix, 0, matrix.clone(), 0);
        invalidate();
        return this;
    }

    /**
     * Gets the value of the given index.
     *
     * @param index the index to get the value from
     * @return the value
     */
    public float get(final int index) {
        return matrix[index];
    }

    /**
     * Extracts the current scaling value of the model matrix.
     * Returns a float array with the form:
     * <p>scale x</p>
     * <p>scale y</p>
     * <p>scale z</p>
     *
     * @return
     */
    public Vector3 extractScaling() {
        final float m00 = get(M_00);
        final float m01 = get(M_01);
        final float m02 = get(M_02);
        final float m10 = get(M_10);
        final float m11 = get(M_11);
        final float m12 = get(M_12);
        final float m20 = get(M_20);
        final float m21 = get(M_21);
        final float m22 = get(M_22);
        final float scaleX = (float) Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        final float scaleY = (float) Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        final float scaleZ = (float) Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        scaling.set(scaleX, scaleY, scaleZ);
        return scaling;
    }

    /**
     * Marks the matrix as dirty and sets the flag for the model buffer by calling {@link #setBufferUpdateNeeded(boolean)}.
     */
    public void invalidate() {
        dirty = true;
        setBufferUpdateNeeded(true);
    }

    /**
     * Clears the dirty flag.
     */
    public void validate() {
        dirty = false;
    }

    /**
     * Gets whether this {@link Matrix4x4} is dirty.
     *
     * @return {@code true} if the matrix is dirty
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Gets whether the model buffer needs an update.
     *
     * @return {@code true} if the model buffer needs an update
     */
    public boolean isBufferUpdateNeeded() {
        return bufferUpdateNeeded;
    }

    /**
     * Sets whether the model buffer needs an update.
     *
     * @param bufferUpdateNeeded {@code true} if the model buffer needs an update
     */
    public void setBufferUpdateNeeded(boolean bufferUpdateNeeded) {
        this.bufferUpdateNeeded = bufferUpdateNeeded;
    }

    @Override
    public String toString() {
        return getClass().getName() + Arrays.toString(getFloat16());
    }
}
