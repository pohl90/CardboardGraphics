package ba.pohl1.hm.edu.vrlibrary.physics.spatialhashing;

/**
 * Created by Pohl on 16.04.2016.
 */
public class SpatialHash {

    private static final int TABLE_SIZE = 5000;
    private static final int PRIME_1 = 73856093;
    private static final int PRIME_2 = 19349663;
    private static final int PRIME_3 = 83492791;

    private final Integer x;
    private final Integer y;
    private final Integer z;

    private SpatialHash(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static SpatialHash createHash(final int x, final int y, final int z) {
        return new SpatialHash(x, y, z);
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        final int hash = (x * PRIME_1 ^ y * PRIME_2 ^ z * PRIME_3) % TABLE_SIZE;
        System.out.println(hash);
        return hash;
    }
}
