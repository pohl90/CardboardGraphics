package ba.pohl1.hm.edu.vrlibrary.ui.hud3d.controls;

import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;

/**
 * Created by Pohl on 06.05.2016.
 */
public class VRVBox extends VRControl {

    private float nextY = 0f;

    @Override
    public void add(VRComponent... children) {
        for (final VRComponent child : children) {
            final float y = nextY();
            child.setY(y);
        }
        super.add(children);
    }

    private float nextY() {
        final float toReturn = nextY;
        nextY -= CGConstants.VBOX_SPACING;
        return toReturn;
    }
}
