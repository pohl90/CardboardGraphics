package ba.pohl1.hm.edu.vrlibrary.physics.animation;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.physics.CollisionManager;
import ba.pohl1.hm.edu.vrlibrary.physics.Force;

/**
 * Created by Pohl on 19.03.2016.
 */
public class RagdollAnimator extends AnimationHandler {

    private VRComponent ragdoll;
    private Force force;
    private CollisionManager collisionManager = CollisionManager.getInstance();

    public RagdollAnimator(final VRComponent ragdoll) {
        this.ragdoll = ragdoll;
        final float fX = (float) (Math.random() - 0.5f);
        final float fZ = (float) (Math.random() - 0.5f);

        force = new Force(new Vector3(fX, 0, fZ).mult((float) Math.random() * 1f + 1f).addY(-1f));
    }

    @Override
    public void animate(float delta, Matrix4x4 model) {
        applyForce(delta);
    }

    @Override
    public void dispose() {
        ragdoll = null;
    }

    private void applyForce(float delta) {
        if(collisionManager.moveAndCheckForCollision(ragdoll, force.getForceX() * delta, 0, 0)) {
            force.negateX();
        }
        if(collisionManager.moveAndCheckForCollision(ragdoll, 0, 0, force.getForceZ() * delta)) {
            force.negateZ();
        }
    }

    public void setForce(final Force force) {
        this.force = force;
    }
}
