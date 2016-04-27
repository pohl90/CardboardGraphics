package ba.pohl1.hm.edu.vrlibrary.physics;

import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;

/**
 * Created by Pohl on 19.03.2016.
 */
public class Force {

    public static final Force GRAVITY = new Force(Vector3.down.mult(10));
    private Vector3 force;

    public Force(final Vector3 vec) {
        force = new Vector3(vec);
    }

    public float getForceX() {
        return force.x;
    }

    public void negateX() {
        force.x *= -1;
    }

    public float getForceY() {
        return force.y;
    }

    public void negateY() {
        force.y *= -1;
    }

    public float getForceZ() {
        return force.z;
    }

    public void negateZ() {
        force.z *= -1;
    }

    public Force reduceForceY() {
        force.multY(0.9f);
        return this;
    }
}
