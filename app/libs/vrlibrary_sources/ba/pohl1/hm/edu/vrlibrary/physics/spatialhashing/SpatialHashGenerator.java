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
        final int indexMinX = getIndex(getCellWidth(), minX);
        final int indexMaxX = getIndex(getCellWidth(), maxX);

        final float minY = box.getMinY();
        final float maxY = box.getMaxY();
        final int indexMinY = getIndex(getCellHeight(), minY);
        final int indexMaxY = getIndex(getCellHeight(), maxY);

        final float minZ = box.getMinZ();
        final float maxZ = box.getMaxZ();
        final int indexMinZ = getIndex(getCellDepth(), minZ);
        final int indexMaxZ = getIndex(getCellDepth(), maxZ);
        return new SpatialHashInfo(indexMinX, indexMaxX, indexMinY, indexMaxY, indexMinZ, indexMaxZ);
    }

    private static int getIndex(final float cellSize, final float index) {
        return (int) (index / cellSize);
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
}
