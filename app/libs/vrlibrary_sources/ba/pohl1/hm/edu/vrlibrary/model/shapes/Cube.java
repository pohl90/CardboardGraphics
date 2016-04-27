package ba.pohl1.hm.edu.vrlibrary.model.shapes;

import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.model.data.GeometryGenerator;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;

/**
 * Created by Pohl on 04.03.2016.
 */
public class Cube extends VRComponent {

    public Cube() {
        this(CGUtils.randomColor());
    }

    public Cube(final float[] color) {
        setColor(color);
        applyAsColorMaterial(CardboardGraphics.colorShader);
    }


    @Override
    public void applyAsColorMaterial(Shader shader) {
        setGeometryData(GeometryGenerator.createCube(false));
        setColor(getColor());
        super.applyAsColorMaterial(shader);
    }

    @Override
    public void applyAsInstancedColorMaterial(Shader shader) {
        setGeometryData(GeometryGenerator.createCube(true));
        setColor(getColor());
        super.applyAsInstancedColorMaterial(shader);
    }
}