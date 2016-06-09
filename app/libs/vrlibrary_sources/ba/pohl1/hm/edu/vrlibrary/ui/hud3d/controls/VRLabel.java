package ba.pohl1.hm.edu.vrlibrary.ui.hud3d.controls;

import ba.pohl1.hm.edu.vrlibrary.model.shapes.Plane;
import ba.pohl1.hm.edu.vrlibrary.rendering.TextureMaterial;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;

/**
 * Created by Pohl on 02.05.2016.
 */
public class VRLabel extends Plane {

    public VRLabel(final int resourceId) {
        final TextureMaterial mat = new TextureMaterial(CardboardGraphics.textureShader, getGeometryData(), resourceId);
        setMaterial(mat);
    }

}
