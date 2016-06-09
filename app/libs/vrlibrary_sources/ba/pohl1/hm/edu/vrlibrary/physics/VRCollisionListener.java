package ba.pohl1.hm.edu.vrlibrary.physics;

import java.util.Set;

import ba.pohl1.hm.edu.vrlibrary.model.VRCollider;

/**
 * A listeners to notify whether the registered {@link VRCollider} has a collision.
 *
 * Created by Pohl on 04.03.2016.
 */
public interface VRCollisionListener {

    void onCollisionWith(VRCollider collider, final Set<VRCollider> collisions);
}
