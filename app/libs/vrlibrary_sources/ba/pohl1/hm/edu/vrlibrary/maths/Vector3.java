package ba.pohl1.hm.edu.vrlibrary.maths;

/**
 * A simple model representing a 3 dimensional vector.
 * <p>
 * Created by Pohl on 27.02.2016.
 */
public class Vector3 {

    public static final Vector3 zero = new Vector3(0, 0, 0);
    public static final Vector3 up = new Vector3(0, 1, 0);
    public static final Vector3 down = new Vector3(0, -1, 0);
    public static final Vector3 left = new Vector3(-1, 0, 0);
    public static final Vector3 right = new Vector3(1, 0, 0);
    public static final Vector3 forward = new Vector3(0, 0, 1);
    public static final Vector3 backward = new Vector3(0, 0, -1);

    private static final Vector3 crossProductVec = new Vector3();

    public float x, y, z;

    public Vector3() {
        this(0, 0, 0);
    }

    /**
     * Creates a new {@link Vector3} instance.
     *
     * @param other the other vector to copy for initial values
     */
    public Vector3(final Vector3 other) {
        this(other.x, other.y, other.z);
    }

    /**
     * Creates a new {@link Vector3} instance.
     *
     * @param x the initial x value
     * @param y the initial y value
     * @param z the initial z value
     */
    public Vector3(final float x, final float y, final float z) {
        set(x, y, z);
    }

    /**
     * Calculates the dot product of two vectors.
     *
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the dot product
     */
    public static float dot(final Vector3 v1, final Vector3 v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    /**
     * Calculates the cross product of two vectors.
     *
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the cross product
     */
    public static Vector3 crossProduct(final Vector3 v1, final Vector3 v2) {
        final float x = v1.y * v2.z - v1.z * v2.y;
        final float y = v1.z * v2.x - v1.x * v2.z;
        final float z = v1.x * v2.y - v1.y * v2.x;
        crossProductVec.set(x, y, z);
        return crossProductVec;
    }

    /**
     * Gets the perpendicular vector in the x- and z-axis of the given vector.
     *
     * @param vec the vector
     * @return the perpendicular vector
     */
    public static Vector3 getPerpendicularXZ(final Vector3 vec) {
        return new Vector3(vec.z, 0, -vec.x);
    }

    /**
     * Calculates the angle between to vectors.
     *
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the angle
     */
    public static double getAngleBetween(final Vector3 v1, final Vector3 v2) {
        final Vector3 crossProduct = Vector3.crossProduct(v1, v2);
        final float dotProduct = Vector3.dot(v1, v2);
        double angle = Math.atan2(crossProduct.length(), dotProduct);
        return Math.toDegrees(angle);
    }

    /**
     * Sets the values of this vector.
     *
     * @param x the new x to set
     * @param y the new y to set
     * @param z the new z to set
     * @return this instance
     */
    public Vector3 set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Sets the values of this vector to the values of the other one.
     *
     * @param other the other vector to copy the values from
     * @return this instance
     */
    public Vector3 set(final Vector3 other) {
        return set(other.x, other.y, other.z);
    }

    /**
     * Gets the length of the vector.
     *
     * @return the length of the vector
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Copies this vector.
     *
     * @return the copied vector
     */
    public Vector3 copy() {
        return new Vector3(this);
    }

    /**
     * Normalizes this vector to a length of 1.
     *
     * @return this instance
     */
    public Vector3 normalize() {
        final float maxLength = length();
        if(maxLength != 0) {
            x /= maxLength;
            y /= maxLength;
            z /= maxLength;
        }
        return this;
    }

    /**
     * Calculates the subtraction of this vector and the other.
     *
     * @param other the other vector
     * @return this instance
     */
    public Vector3 sub(Vector3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    /**
     * Adds the other vector to this one.
     *
     * @param other the other vector to add
     * @return this instance
     */
    public Vector3 add(final Vector3 other) {
        return addX(other).addY(other).addZ(other);
    }

    /**
     * Adds the other vector to this one.
     *
     * @param other the other vector to add
     * @return this instance
     */
    public Vector3 addX(final Vector3 other) {
        return addX(other.x);
    }

    /**
     * Adds the other vector to this one.
     *
     * @param other the other vector to add
     * @return this instance
     */
    public Vector3 addY(final Vector3 other) {
        return addY(other.y);
    }

    /**
     * Adds the other vector to this one.
     *
     * @param other the other vector to add
     * @return this instance
     */
    public Vector3 addZ(final Vector3 other) {
        return addZ(other.z);
    }

    /**
     * Adds the given value to this vector.
     *
     * @param x the value to add
     * @return this instance
     */
    public Vector3 addX(final float x) {
        this.x += x;
        return this;
    }

    /**
     * Adds the given value to this vector.
     *
     * @param y the value to add
     * @return this instance
     */
    public Vector3 addY(final float y) {
        this.y += y;
        return this;
    }

    /**
     * Adds the given value to this vector.
     *
     * @param z the value to add
     * @return this instance
     */
    public Vector3 addZ(final float z) {
        this.z += z;
        return this;
    }

    /**
     * Multiplies the given value to this vector.
     *
     * @param value the value to multiply
     * @return this instance
     */
    public Vector3 mult(final float value) {
        return multX(value).multY(value).multZ(value);
    }

    /**
     * Multiplies the other vector to this vector.
     *
     * @param other the other vector to multiply
     * @return this instance
     */
    public Vector3 mult(final Vector3 other) {
        return multX(other).multY(other).multZ(other);
    }

    /**
     * Multiplies the other vector to this vector.
     *
     * @param other the other vector to multiply
     * @return this instance
     */
    public Vector3 multX(final Vector3 other) {
        return multX(other.x);
    }

    /**
     * Multiplies the other vector to this vector.
     *
     * @param other the other vector to multiply
     * @return this instance
     */
    public Vector3 multY(final Vector3 other) {
        return multY(other.y);
    }

    /**
     * Multiplies the other vector to this vector.
     *
     * @param other the other vector to multiply
     * @return this instance
     */
    public Vector3 multZ(final Vector3 other) {
        return multZ(other.z);
    }

    /**
     * Multiplies the given value to this vector.
     *
     * @param x the value to multiply
     * @return this instance
     */
    public Vector3 multX(final float x) {
        this.x *= x;
        return this;
    }

    /**
     * Multiplies the given value to this vector.
     *
     * @param y the value to multiply
     * @return this instance
     */
    public Vector3 multY(final float y) {
        this.y *= y;
        return this;
    }

    /**
     * Multiplies the given value to this vector.
     *
     * @param z the value to multiply
     * @return this instance
     */
    public Vector3 multZ(final float z) {
        this.z *= z;
        return this;
    }

    /**
     * Gets a float[4] array.
     *
     * @return the float array
     */
    public float[] getFloat4() {
        return new float[]{x, y, z, 1};
    }
}
