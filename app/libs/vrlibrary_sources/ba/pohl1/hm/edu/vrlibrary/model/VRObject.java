package ba.pohl1.hm.edu.vrlibrary.model;

import android.opengl.Matrix;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.model.observer.VRObservable;
import ba.pohl1.hm.edu.vrlibrary.physics.animation.AnimationHandler;
import ba.pohl1.hm.edu.vrlibrary.rendering.RendererManager;

/**
 * Very base class for virtual reality objects.
 * <p>
 * Created by Pohl on 19.02.2016.
 */
public abstract class VRObject extends Matrix4x4 {

    protected Matrix4x4 globalModelMatrix = new Matrix4x4();
    private Matrix4x4 preAnimationMatrix = new Matrix4x4();
    private AnimationHandler preAnimationHandler;
    private AnimationHandler postAnimationHandler;

    private VRObservable<Boolean> focusedObservable = new VRObservable<>(false);
    private boolean visible = true;

    private String name = "VRObject";

    /**
     * Creates a new {@link VRObject} instance.
     */
    public VRObject() {
        this(0, 0, 0);
    }

    /**
     * Creates a new {@link VRObject} instance.
     *
     * @param x the initial x coordinate
     * @param y the initial y coordinate
     * @param z the initial z coordinate
     */
    public VRObject(float x, float y, float z) {
        Matrix.setIdentityM(getFloat16(), 0);
        translate(x, y, z);
    }

    /**
     * Gets whether the {@link VRObject} is visible. The {@link RendererManager}
     * will not draw this object is if it isn't visible.
     *
     * @return {@code true} if the object is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets whether the {@link VRObject} is visible. The {@link RendererManager}
     * will not draw this object is if it isn't visible.
     *
     * @param visible {@code true} if the object is visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Gets whether this {@link VRObject} is focused.
     *
     * @return {@code true} if the object is focused
     */
    public boolean isFocused() {
        return focusedObservable.get();
    }

    /**
     * Sets whether this {@link VRObject} is focused.
     *
     * @param focused {@code true} if the object is focused
     */
    public void setFocused(boolean focused) {
        focusedObservable.set(focused);
    }

    /**
     * Gets the {@link VRObservable} for the focus state.
     *
     * @return the observable for the focus state
     */
    public VRObservable<Boolean> getFocusedObservable() {
        return focusedObservable;
    }

    /**
     * Gets the pre {@link AnimationHandler}.
     *
     * @return the pre animation handler
     */
    public AnimationHandler getPreAnimationHandler() {
        return preAnimationHandler;
    }

    /**
     * Sets the {@link AnimationHandler} which gets called in the pre animation phase.
     *
     * @param preAnimationHandler the animation handler to set
     */
    public void setPreAnimationHandler(AnimationHandler preAnimationHandler) {
        this.preAnimationHandler = preAnimationHandler;
    }

    /**
     * Gets the post {@link AnimationHandler}.
     *
     * @return the post animation handler
     */
    public AnimationHandler getPostAnimationHandler() {
        return postAnimationHandler;
    }

    /**
     * Sets the {@link AnimationHandler} which gets called in the post animation phase.
     *
     * @param postAnimationHandler the animation handler to set
     */
    public void setPostAnimationHandler(AnimationHandler postAnimationHandler) {
        this.postAnimationHandler = postAnimationHandler;
    }

    /**
     * Gets the global {@link Matrix4x4} model.
     *
     * @return the global matrix model
     */
    public Matrix4x4 getModelMatrix() {
        return globalModelMatrix;
    }

    /**
     * Calls the pre and post {@link AnimationHandler AnimationHandlers}.
     *
     * @param delta
     */
    public void animate(float delta) {
        if(preAnimationHandler != null) {
            preAnimationHandler.animate(delta, preAnimationMatrix.identity());
            multiplyMM(preAnimationMatrix, this);
        }
        if(postAnimationHandler != null) {
            postAnimationHandler.animate(delta, this);
        }
    }

    /**
     * Gets whether this {@link VRObject} is animated.
     *
     * @return {@code true} if it's animated
     */
    public boolean hasAnimation() {
        return preAnimationHandler != null || postAnimationHandler != null;
    }

    /**
     * Gets the local {@link Matrix4x4} as a float[].
     *
     * @return the local model as float[]
     */
    public float[] getLocalModel() {
        return getFloat16();
    }

    /**
     * Gets the name of the {@link VRObject}.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the {@link VRObject}.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Disposes the {@link VRObject}.
     */
    public void dispose() {
        // Do nothing here
    }

    @Override
    public String toString() {
        return getName() + ": " + getModelMatrix();
    }

}
