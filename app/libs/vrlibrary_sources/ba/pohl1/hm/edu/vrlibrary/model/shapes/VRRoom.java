package ba.pohl1.hm.edu.vrlibrary.model.shapes;

import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;

/**
 * Created by Pohl on 12.03.2016.
 */
public class VRRoom extends VRComponent {

    private final Cube floor;
    private final Cube wallNorth;
    private final Cube wallSouth;
    private final Cube wallWest;
    private final Cube wallEast;

    public VRRoom() {
        this(50, 10, 50);
    }

    public VRRoom(final float width, final float height, final float depth) {
        setName("Room");
        setCollisionEnabled(false);

        // Define the grid like floor
        floor = new Cube(CGConstants.COLOR_BLUE);
        floor.setName("Floor");
        floor.applyAsColorMaterial(CardboardGraphics.gridShader);
        floor.translateY(-0.5f).scale(width, 1, depth);

        // Define walls
        wallNorth = new Cube(CGConstants.COLOR_BLACK);
        wallNorth.setName("Wall North");
        wallNorth.translateZ(-0.5f - 0.5f * depth).translateY(0.5f * height).scale(width, height, 1);

        wallSouth = new Cube(CGConstants.COLOR_BLACK);
        wallSouth.setName("Wall South");
        wallSouth.translateZ(0.5f + 0.5f * depth).translateY(0.5f * height).scale(width, height, 1);

        wallWest = new Cube(CGConstants.COLOR_BLACK);
        wallWest.setName("Wall West");
        wallWest.translateX(-0.5f - 0.5f * width).translateY(0.5f * height).scale(1, height, depth);

        wallEast = new Cube(CGConstants.COLOR_BLACK);
        wallEast.setName("Wall East");
        wallEast.translateX(0.5f + 0.5f * width).translateY(0.5f * height).scale(1, height, depth);

        add(floor, wallNorth, wallSouth, wallWest, wallEast);
    }

    public Cube getWallEast() {
        return wallEast;
    }

    public Cube getWallNorth() {
        return wallNorth;
    }

    public Cube getWallSouth() {
        return wallSouth;
    }

    public Cube getWallWest() {
        return wallWest;
    }
}

