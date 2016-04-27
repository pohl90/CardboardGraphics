package ba.pohl1.hm.edu.vrlibrary.physics.transitions;

import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;

/**
 * Created by Pohl on 22.04.2016.
 */
public class TranslateTransition extends GLTransition {

    private final VRComponent component;

    private float dx, dy, dz;

    public TranslateTransition(final VRComponent component, final Vector3 from, final Vector3 to, final float stepSizeInPercent) {
        super(100 / stepSizeInPercent);
        this.component = component;
        dx = (to.x - from.x) / steps;
        dy = (to.y - from.y) / steps;
        dz = (to.z - from.z) / steps;
    }

    public boolean isDone() {
        return steps <= 0;
    }

    public boolean execute() {
        steps--;
        component.translate(dx, dy, dz);
        return isDone();
    }
}
