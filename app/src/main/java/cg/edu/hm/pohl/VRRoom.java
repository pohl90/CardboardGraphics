package cg.edu.hm.pohl;

import ba.pohl1.hm.edu.vrlibrary.base.manager.CardboardGraphics;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;

/**
 * Created by Pohl on 14.04.2016.
 */
public class VRRoom extends VRComponent {

    private static final float WIDTH = 25;
    private static final float DEPTH = 25;
    private static final float HEIGHT = 3;

    private VRCube floor;
    private VRCube wallNorth;
    private VRCube wallSouth;
    private VRCube wallWest;
    private VRCube wallEast;

    public VRRoom() {
        setCollisionEnabled(false);

        floor = new VRCube();
        floor.setCollisionEnabled(false);
        floor.applyAsColorMaterial(CardboardGraphics.gridShader);
        floor.setColor(CGConstants.COLOR_BLUE);
        floor.translateY(-0.5f).scale(WIDTH, 1, DEPTH);

        // Define walls
        wallNorth = new VRCube();
        wallNorth.translateZ(-0.5f - 0.5f * DEPTH).translateY(0.5f * HEIGHT).scale(WIDTH, HEIGHT, 1);
        wallNorth.setColor(CGConstants.COLOR_BLACK);

        wallSouth = new VRCube();
        wallSouth.translateZ(0.5f + 0.5f * DEPTH).translateY(0.5f * HEIGHT).scale(WIDTH, HEIGHT, 1);
        wallSouth.setColor(CGConstants.COLOR_BLACK);

        wallWest = new VRCube();
        wallWest.translateX(-0.5f - 0.5f * WIDTH).translateY(0.5f * HEIGHT).scale(1, HEIGHT, DEPTH);
        wallWest.setColor(CGConstants.COLOR_BLACK);

        wallEast = new VRCube();
        wallEast.translateX(0.5f + 0.5f * WIDTH).translateY(0.5f * HEIGHT).scale(1, HEIGHT, DEPTH);
        wallEast.setColor(CGConstants.COLOR_BLACK);

        add(floor, wallEast, wallNorth, wallSouth, wallWest);
    }

    private class VRCube extends VRComponent {
        public VRCube() {
            setGeometryData(GeometryGenerator.createCube());
            applyAsColorMaterial(CardboardGraphics.colorShader);}

    }
}
