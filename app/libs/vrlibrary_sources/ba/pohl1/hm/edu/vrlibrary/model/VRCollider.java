package ba.pohl1.hm.edu.vrlibrary.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.physics.CollisionManager;
import ba.pohl1.hm.edu.vrlibrary.physics.VRCollisionListener;
import ba.pohl1.hm.edu.vrlibrary.util.CGOptions;


/**
 * A layer to encapsulate collision related stuff.
 *
 * Created by Pohl on 02.03.2016.
 */
public abstract class VRCollider extends VRObject {

    private final List<VRCollisionListener> collisionListeners = new ArrayList<>();
    private boolean collisionEnabled;
    private VRBoundingBox boundingBox;

    public VRCollider() {
        this(true);
    }

    public VRCollider(final boolean collisionEnabled) {
        setCollisionEnabled(collisionEnabled);
    }

    /**
     * Updates the collision bounds based on the given model {@link Matrix4x4}.
     *
     * @param matrix the matrix to update the bounds
     */
    public void updateBounds(final Matrix4x4 matrix) {
        getBoundingBox().update(matrix);
        if(isCollisionEnabled()) {
            CollisionManager.getInstance().update(this);
        }
    }

    /**
     * Gets whether collision is enabled.
     *
     * @return {@code true} if collision is enabled
     */
    public boolean isCollisionEnabled() {
        return CGOptions.COLLISION_ENABLED && collisionEnabled;
    }

    /**
     * Sets whether collision is enabled.
     *
     * @param collisionEnabled {@code true} if collision is enabled
     */
    public void setCollisionEnabled(boolean collisionEnabled) {
        this.collisionEnabled = collisionEnabled;
    }

    /**
     * Gets the {@link VRBoundingBox}.
     *
     * @return the bounding box
     */
    public VRBoundingBox getBoundingBox() {
        if(boundingBox == null) {
            boundingBox = new VRBoundingBox();
        }
        return boundingBox;
    }

    /**
     * Adds a {@link VRCollisionListener} to this {@link VRCollider} to notify when a collision occurs.
     *
     * @param listener the listener to add
     */
    public void addCollisionListener(final VRCollisionListener listener) {
        collisionListeners.add(listener);
    }

    /**
     * Removes a {@link VRCollisionListener} from this {@link VRCollider}.
     *
     * @param listener the listener to remove
     */
    public void removeCollisionListener(final VRCollisionListener listener) {
        collisionListeners.remove(listener);
    }

    /**
     * Notifies all registered {@link VRCollisionListener VRCollisionListeners}.
     *
     * @param collisions the collided {@link VRCollider VRColliders}
     */
    public void notifyCollisionListeners(final Set<VRCollider> collisions) {
        for(final VRCollisionListener listener : collisionListeners) {
            listener.onCollisionWith(this, collisions);
        }
    }
}
