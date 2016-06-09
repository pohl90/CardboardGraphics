package ba.pohl1.hm.edu.vrlibrary.ui.hud3d.controls;

import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;

/**
 * Created by Pohl on 06.05.2016.
 */
public class VRHBox extends VRControl {

    private float nextX = 0f;

    @Override
    public void add(VRComponent... children) {
        for (final VRComponent child : children) {
            final float x = nextX();
            child.setX(x);
        }
        super.add(children);
    }

    private float nextX() {
        final float toReturn = nextX;
        nextX += CGConstants.HBOX_SPACING;
        return toReturn;
    }
}
