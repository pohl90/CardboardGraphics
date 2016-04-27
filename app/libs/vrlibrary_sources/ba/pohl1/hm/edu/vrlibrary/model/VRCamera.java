package ba.pohl1.hm.edu.vrlibrary.model;

import android.opengl.Matrix;

import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;

import java.util.Set;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;
import ba.pohl1.hm.edu.vrlibrary.physics.CollisionManager;

/**
 * A specific {@link VRComponent} representing the main camera.
 * <p>
 * Created by Pohl on 20.02.2016.
 */
public class VRCamera extends VRComponent {

    private final float[] forwardVectorInput = new float[3];
    private final float[] rotation = new float[4];
    private Matrix4x4 collisionModel;
    private float[] view = new float[16];
    private float[] headView = new float[16];
    private Vector3 forwardVector = new Vector3();
    private boolean canMoveInX = true;
    private boolean canMoveInY = true;
    private boolean canMoveInZ = true;

    public VRCamera() {
        setName("Camera");
    }

    /**
     * Moves the camera into the forward direction multiplied with the given modifier.
     * Per default no direction is locked.
     *
     * @param modifier the modifer
     * @see #setCanMoveInX(boolean)
     * @see #setCanMoveInY(boolean)
     * @see #setCanMoveInZ(boolean)
     */
    public void moveForward(final float modifier) {
        move(getForwardVector(), modifier);
    }

    /**
     * Moves the camera into the given {@link Vector3 direction} multiplied with the given modifier.
     *
     * @param direction the direction
     * @param modifier  the modifier
     */
    public void move(final Vector3 direction, final float modifier) {
        final float dx = direction.x * modifier;
        final float dy = direction.y * modifier;
        final float dz = direction.z * modifier;

        // Check for x axis
        translate(dx, 0, 0);
        // Check if the camera can move forward
        if(isCollisionEnabled()) {
            CollisionManager.getInstance().raycastUpdate(this);
            final Set<VRCollider> collisions = CollisionManager.getInstance().getCollisions(this);
            if(collisions != null) {
                // Collision detected, do not translate the camera
                // Reset the translation
                translate(-dx, 0, 0);
                notifyCollisionListeners(collisions);
            }
            CollisionManager.getInstance().update(this);
        }
        // Check for x axis
        translate(0, dy, 0);
        // Check if the camera can move forward
        if(isCollisionEnabled()) {
            CollisionManager.getInstance().raycastUpdate(this);
            final Set<VRCollider> collisions = CollisionManager.getInstance().getCollisions(this);
            if(collisions != null) {
                // Collision detected, do not translate the camera
                // Reset the translation
                translate(0, -dy, 0);
                notifyCollisionListeners(collisions);
            }
            CollisionManager.getInstance().update(this);
        }
        // Check for z axis
        translate(0, 0, dz);
        // Check if the camera can move forward
        if(isCollisionEnabled()) {
            CollisionManager.getInstance().raycastUpdate(this);
            final Set<VRCollider> collisions = CollisionManager.getInstance().getCollisions(this);
            if(collisions != null) {
                // Collision detected, do not translate the camera
                // Reset the translation
                translate(0, 0, -dz);
                notifyCollisionListeners(collisions);
            }
            CollisionManager.getInstance().update(this);
        }
    }

    /**
     * Updates the camera based on the associated {@link HeadTransform}.
     * It provides the forward vector and the head view.
     *
     * @param headTransform
     * @see HeadTransform#getForwardVector(float[], int)
     */
    public void updateCamera(final HeadTransform headTransform) {
        headTransform.getEulerAngles(rotation, 0);
        headTransform.getHeadView(headView, 0);
        headTransform.getForwardVector(forwardVectorInput, 0);
        forwardVector.set(forwardVectorInput[0], forwardVectorInput[1], forwardVectorInput[2]);
    }

    /**
     * Updates the camera based on the associated {@link Eye}. The eye's view and the current camera's local model are
     * used to get the ViewModel.
     *
     * @param eye the eye to get the view from
     */
    public void updateCamera(final Eye eye) {
        Matrix.multiplyMM(view, 0, eye.getEyeView(), 0, getCameraModel(), 0);
    }

    public float[] getHeadView() {
        return headView;
    }

    public float[] getRotation() {
        return rotation;
    }

    /**
     * Gets the current forward vector.
     *
     * @return the current forward vector
     */
    public Vector3 getForwardVector() {
        return forwardVector;
    }

    /**
     * Gets the current ViewModel.
     *
     * @return the current view model
     */
    public float[] getView() {
        return view;
    }

    /**
     * Gets the current camera's local model.
     *
     * @return the current local model
     */
    public float[] getCameraModel() {
        return getLocalModel();
    }

    /**
     * Sets the position of the camera.
     *
     * @param newPosition the new position to set
     */
    public void setPosition(final Vector3 newPosition) {
        identity();
        translate(newPosition.x, newPosition.y, newPosition.z);
    }

    @Override
    public Matrix4x4 translate(float x, float y, float z) {
        super.translate(canMoveInX ? -x : 0, canMoveInY ? -y : 0, canMoveInZ ? -z : 0);
        updateCameraBounds();
        return this;
    }

    private void updateCameraBounds() {
        getBoundingBox().update(getCollisionModel().set(this).negate());
    }

    private Matrix4x4 getCollisionModel() {
        if(collisionModel == null) {
            collisionModel = new Matrix4x4();
        }
        return collisionModel;
    }

    /**
     * Sets whether the camera can move in the x axis.
     *
     * @param canMoveInX {@code true} if the camera can move
     */
    public void setCanMoveInX(boolean canMoveInX) {
        this.canMoveInX = canMoveInX;
    }

    /**
     * Sets whether the camera can move in the y axis.
     *
     * @param canMoveInY {@code true} if the camera can move
     */
    public void setCanMoveInY(boolean canMoveInY) {
        this.canMoveInY = canMoveInY;
    }

    /**
     * Sets whether the camera can move in the z axis.
     *
     * @param canMoveInZ {@code true} if the camera can move
     */
    public void setCanMoveInZ(boolean canMoveInZ) {
        this.canMoveInZ = canMoveInZ;
    }
}
