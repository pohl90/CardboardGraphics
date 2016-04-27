package ba.pohl1.hm.edu.vrlibrary.physics.spatialhashing;

/**
 * Created by Pohl on 24.03.2016.
 */
public class SpatialHashInfo {

    private int indexMinX;
    private int indexMaxX;
    private int indexMinY;
    private int indexMaxY;
    private int indexMinZ;
    private int indexMaxZ;

    public SpatialHashInfo(final int indexMinX, final int indexMaxX, final int indexMinY, final int indexMaxY, final int indexMinZ, final int indexMaxZ) {
        this.indexMinX = indexMinX;
        this.indexMaxX = indexMaxX;
        this.indexMinY = indexMinY;
        this.indexMaxY = indexMaxY;
        this.indexMinZ = indexMinZ;
        this.indexMaxZ = indexMaxZ;
    }

    public int getIndexMinX() {
        return indexMinX;
    }

    public int getIndexMaxX() {
        return indexMaxX;
    }

    public int getIndexMinY() {
        return indexMinY;
    }

    public int getIndexMaxY() {
        return indexMaxY;
    }

    public int getIndexMinZ() {
        return indexMinZ;
    }

    public int getIndexMaxZ() {
        return indexMaxZ;
    }

    public void merge(final SpatialHashInfo otherInfo) {
        indexMinX = Math.min(getIndexMinX(), otherInfo.getIndexMinX());
        indexMaxX = Math.max(getIndexMaxX(), otherInfo.getIndexMaxX());
        indexMinY = Math.min(getIndexMinY(), otherInfo.getIndexMinY());
        indexMaxY = Math.max(getIndexMaxY(), otherInfo.getIndexMaxY());
        indexMinZ = Math.min(getIndexMinZ(), otherInfo.getIndexMinZ());
        indexMaxZ = Math.max(getIndexMaxZ(), otherInfo.getIndexMaxZ());
    }

    @Override
    public String toString() {
        return "minX=" + getIndexMinX() + " maxX=" + getIndexMaxX() +
                "minY=" + getIndexMinY() + " maxY=" + getIndexMaxY() +
                "minZ=" + getIndexMinZ() + " maxZ=" + getIndexMaxZ();
    }
}
