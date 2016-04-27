package ba.pohl1.hm.edu.vrlibrary.physics.spatialhashing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ba.pohl1.hm.edu.vrlibrary.model.VRCollider;

/**
 * Created by Pohl on 24.03.2016.
 */
public class SpatialGridCell {

    private final Set<VRCollider> children = new HashSet<>();
    private final int x;
    private final int y;
    private final int z;

    public SpatialGridCell(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void addChild(final VRCollider child) {
        if(children.contains(child)) {
            return;
        }
        children.add(child);
    }

    public void removeChild(final VRCollider child) {
        children.remove(child);
    }

    public int size() {
        return children.size();
    }

    public boolean contains(final VRCollider child) {
        return children.contains(child);
    }

    @Override
    public String toString() {
        return Arrays.toString(children.toArray());
    }

    public Set<VRCollider> getChildren() {
        return children;
    }

    public void clear() {
        getChildren().clear();
    }
}
