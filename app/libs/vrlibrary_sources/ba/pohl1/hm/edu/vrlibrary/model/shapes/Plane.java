package ba.pohl1.hm.edu.vrlibrary.model.shapes;

import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.model.data.GeometryGenerator;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;

/**
 * Created by Pohl on 02.05.2016.
 */
public class Plane extends VRComponent {

    public Plane() {
        this(CGUtils.randomColor());
    }

    public Plane(final float[] color) {
        setGeometryData(GeometryGenerator.createPlane());
        setColor(color);
        applyAsColorMaterial(CardboardGraphics.colorShader);
    }
}
