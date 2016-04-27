package ba.pohl1.hm.edu.vrlibrary.physics.spatialhashing;

import ba.pohl1.hm.edu.vrlibrary.model.VRBoundingBox;
import ba.pohl1.hm.edu.vrlibrary.model.VRCollider;
import ba.pohl1.hm.edu.vrlibrary.physics.CollisionManager;

/**
 * Created by Pohl on 24.03.2016.
 */
public final class SpatialHashGenerator {

    private SpatialHashGenerator() {
        // Prevents instantiation
    }

    public static SpatialHashInfo generate(final VRCollider collider) {
        final VRBoundingBox box = collider.getBoundingBox();
        if(box == null) {
            return null;
        }
        final float minX = box.getMinX();
        final float maxX = box.getMaxX();
        final int indexMinX = getIndex(getWidth(), getCellWidth(), minX);
        final int indexMaxX = getIndex(getWidth(), getCellWidth(), maxX);
        if(indexMinX == -1 || indexMaxX == -1) {
            return null;
        }

        final float minY = box.getMinY();
        final float maxY = box.getMaxY();
        final int indexMinY = getIndex(getHeight(), getCellHeight(), minY);
        final int indexMaxY = getIndex(getHeight(), getCellHeight(), maxY);
        if(indexMinY == -1 || indexMaxY == -1) {
            return null;
        }

        final float minZ = box.getMinZ();
        final float maxZ = box.getMaxZ();
        final int indexMinZ = getIndex(getDepth(), getCellDepth(), minZ);
        final int indexMaxZ = getIndex(getDepth(), getCellDepth(), maxZ);
        if(indexMinZ == -1 || indexMaxZ == -1) {
            return null;
        }
        return new SpatialHashInfo(indexMinX, indexMaxX, indexMinY, indexMaxY, indexMinZ, indexMaxZ);
    }

    private static int getIndex(final int size, final float cellSize, final float index) {
        final int indexAsInt = (int) (index / cellSize + size / 2);
        if(indexAsInt >= 0 && indexAsInt < size) {
            return indexAsInt;
        }
        return -1;
    }


    private static float getCellWidth() {
        return CollisionManager.getInstance().getCellWidth();
    }

    private static float getCellHeight() {
        return CollisionManager.getInstance().getCellHeight();
    }

    private static float getCellDepth() {
        return CollisionManager.getInstance().getCellDepth();
    }

    private static int getWidth() {
        return CollisionManager.getInstance().getWidth();
    }

    private static int getHeight() {
        return CollisionManager.getInstance().getHeight();
    }

    private static int getDepth() {
        return CollisionManager.getInstance().getDepth();
    }
}
