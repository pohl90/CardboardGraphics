package ba.pohl1.hm.edu.vrlibrary.physics.animation;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;

/**
 * A simple handler which can be set as a pre or post animation handler in
 * {@link VRComponent#setPreAnimationHandler(AnimationHandler)} and
 * {@link VRComponent#setPostAnimationHandler(AnimationHandler)}.
 * <p>
 * Created by Pohl on 25.02.2016.
 */
public abstract class AnimationHandler {

    /**
     * Applies an animation transformation on the given {@link Matrix4x4}.
     *
     * @param delta
     * @param model the model to animate
     */
    public abstract void animate(float delta, final Matrix4x4 model);

    public void dispose() {

    }

}
