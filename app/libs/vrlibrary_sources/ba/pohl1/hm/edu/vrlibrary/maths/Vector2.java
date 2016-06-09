package ba.pohl1.hm.edu.vrlibrary.maths;

/**
 * A simple model representing a 2 dimensional vector.
 * <p>
 * Created by Pohl on 27.02.2016.
 */
public class Vector2 {

    public float x, y;

    public Vector2() {
        this(0, 0);
    }

    /**
     * Creates a new {@link Vector2} instance.
     *
     * @param x the initial x value
     * @param y the initial y value
     */
    public Vector2(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds the other {@link Vector2} to this one.
     *
     * @param other the other vector to add
     * @return this instance
     */
    public Vector2 addX(final Vector2 other) {
        return addX(other.x);
    }

    /**
     * Adds the other {@link Vector2} to this one.
     *
     * @param other the other vector to add
     * @return this instance
     */
    public Vector2 addY(final Vector2 other) {
        return addY(other.y);
    }

    /**
     * Adds the x value to this vector.
     *
     * @param x the x value to add
     * @return this instance
     */
    public Vector2 addX(final float x) {
        this.x += x;
        return this;
    }

    /**
     * Adds the y value to this vector.
     *
     * @param y the y value to add
     * @return this instance
     */
    public Vector2 addY(final float y) {
        this.y += y;
        return this;
    }

    /**
     * Multiplies the given value to this vector.
     *
     * @param value the value to multiply
     * @return this instance
     */
    public Vector2 mult(final float value) {
        return multX(value).multY(value);
    }

    /**
     * Multiplies the given {@link Vector2} to this vector.
     *
     * @param other the other vector to multiply
     * @return this instance
     */
    public Vector2 mult(final Vector2 other) {
        return multX(other).multY(other);
    }

    /**
     * Multiplies the given {@link Vector2} to this vector.
     *
     * @param other the other vector to multiply
     * @return this instance
     */
    public Vector2 multX(final Vector2 other) {
        return multX(other.x);
    }

    /**
     * Multiplies the given {@link Vector2} to this vector.
     *
     * @param other the other vector to multiply
     * @return this instance
     */
    public Vector2 multY(final Vector2 other) {
        return multY(other.y);
    }

    /**
     * Multiplies the given value to this vector.
     *
     * @param x the value to multiply
     * @return this instance
     */
    public Vector2 multX(final float x) {
        this.x *= x;
        return this;
    }

    /**
     * Multiplies the given value to this vector.
     *
     * @param y the value to multiply
     * @return this instance
     */
    public Vector2 multY(final float y) {
        this.y *= y;
        return this;
    }

}
