package ba.pohl1.hm.edu.vrlibrary.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.model.VRBoundingBox;
import ba.pohl1.hm.edu.vrlibrary.model.VRCollider;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.physics.spatialhashing.SpatialGridCell;
import ba.pohl1.hm.edu.vrlibrary.physics.spatialhashing.SpatialHashGenerator;
import ba.pohl1.hm.edu.vrlibrary.physics.spatialhashing.SpatialHashInfo;

/**
 * This class manages everything that is related to collision detection.
 * A spatial hashing algorithm is implemented here.
 *
 * Created by Pohl on 24.03.2016.
 */
public class CollisionManager {

    private static final CollisionManager INSTANCE = new CollisionManager();
    private static final Matrix4x4 currentModelMatrix = new Matrix4x4();
    private final Map<VRCollider, List<SpatialGridCell>> cachedColliders = new HashMap<>();
    private final Map<VRCollider, SpatialHashInfo> cachedInfo = new HashMap<>();
    private float cellWidth;
    private float cellDepth;
    private float cellHeight;

    private Map<Integer, Map<Integer, Map<Integer, SpatialGridCell>>> spatialHashTable = new HashMap<>();

    private CollisionManager() {
        // Prevents instantiation from outside
    }

    /**
     * Gets the singleton instance of the {@link CollisionManager}.
     *
     * @return the instance
     */
    public static CollisionManager getInstance() {
        return INSTANCE;
    }

    /**
     * Disposes the {@link CollisionManager}.
     */
    public void dispose() {
        cachedColliders.clear();
        cachedInfo.clear();
        spatialHashTable.clear();
    }

    /**
     * Moves the given {@link VRComponent} in x, y and z direction, checks for collision and, if a
     * collision is detected, the component's position is reset.
     *
     * @param component the component to move
     * @param x         the delta x
     * @param y         the delta y
     * @param z         the delta z
     * @return {@code true} if there was a collision
     */
    public boolean moveAndCheckForCollision(final VRComponent component, final float x, final float y, final float z) {
        final VRBoundingBox box = component.getBoundingBox();
        final Matrix4x4 modelMatrix = box.getUnmodifiedModelMatrix();
        if(modelMatrix == null) {
            // Collision bounds are not yet initialized.
            return false;
        }

        // Translate the component in x, y, z.
        component.translate(x, y, z);
        // Copy the current model matrix.
        currentModelMatrix.set(modelMatrix);
        // Include component's transformation.
        component.transform(currentModelMatrix);
        // Update the component's collision box.
        box.update(currentModelMatrix);
        // Do a raycast update for the spatial hashing.
        raycastUpdate(component);

        boolean hasCollision = false;
        final Set<VRCollider> collisions = getCollisions(component);
        if(collisions != null) {
            // Notify collision listeners.
            component.notifyCollisionListeners(collisions);
            // Revert translation changes.
            component.translate(-x, -y, -z);
            // Copy the current model matrix.
            currentModelMatrix.set(modelMatrix);
            // Include component's transformation.
            component.transform(currentModelMatrix);
            // Update the component's collision box.
            box.update(currentModelMatrix);
            hasCollision = true;
        }
        update(component);
        return hasCollision;
    }

    /**
     * Gets the {@link SpatialGridCell} for the given collider.
     *
     * @param collider the component to get the cell for
     * @return the grid cell
     */
    public Set<VRCollider> getCollisions(final VRComponent collider) {
        if(!cachedColliders.containsKey(collider)) {
            return null;
        }
        final List<SpatialGridCell> gridCells = cachedColliders.get(collider);
        for(final SpatialGridCell cell : gridCells) {
            if(cell.contains(collider) && cell.size() > 1) {
                final Set<VRCollider> colliders = new HashSet<>(cell.getChildren());
                colliders.remove(collider);
                return colliders;
            }
        }
        return null;
    }

    /**
     * Initializes the {@link CollisionManager} with the given values.
     *
     * @param cellWidth  the width of a collision cell
     * @param cellHeight the height of a collision cell
     * @param cellDepth  the depth of a collision cell
     */
    public void init(final float cellWidth, final float cellHeight, final float cellDepth) {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.cellDepth = cellDepth;
    }

    /**
     * Updates the collision bounds of the given {@link VRCollider}.
     *
     * @param collider the collider to update
     */
    public void update(final VRCollider collider) {
        clearGridCells(collider);
        final SpatialHashInfo info = SpatialHashGenerator.generate(collider);
        //Log.i("Collision Update", info + " | " + collider);
        if(info == null) {
            return;
        }
        updateGridCells(collider, info);
    }

    /**
     * Performs a raycast update of the collision cells. This method makes sure
     * that a collider does not "go" through walls when moving very fast.
     *
     * @param collider the collider to update
     */
    public void raycastUpdate(final VRCollider collider) {
        SpatialHashInfo info = SpatialHashGenerator.generate(collider);
        if(info == null) {
            return;
        }
        if(cachedInfo.containsKey(collider)) {
            info.merge(cachedInfo.get(collider));
            //clearGridCells(collider);
        }
        //Log.i("Raycast", info.toString());
        updateGridCells(collider, info);
    }

    /**
     * Removes the given {@link SpatialGridCell}.
     *
     * @param cell the cell to remove
     */
    public void removeCell(final SpatialGridCell cell) {
        getZCellTable(cell.getX(), cell.getY()).remove(cell.getZ());
    }

    /**
     * Gets the width of a cell.
     *
     * @return the width of cell
     */
    public float getCellWidth() {
        return cellWidth;
    }

    /**
     * Gets the height of a cell.
     *
     * @return the height of cell
     */
    public float getCellHeight() {
        return cellHeight;
    }

    /**
     * Gets the depth of a cell.
     *
     * @return the depth of cell
     */
    public float getCellDepth() {
        return cellDepth;
    }

    /**
     * Removes the given {@link VRComponent} from the collision area.
     *
     * @param component the component to remove
     */
    public void remove(VRComponent component) {
        clearGridCells(component);
    }

    /**
     * Clears all grid cell for the given {@link VRCollider}.
     *
     * @param collider the collider to clear the cells for
     */
    private void clearGridCells(final VRCollider collider) {
        if(cachedColliders.containsKey(collider)) {
            for(final SpatialGridCell cell : cachedColliders.get(collider)) {
                cell.removeChild(collider);
                if(cell.size() == 0) {
                    removeCell(cell);
                }
            }
            cachedColliders.remove(collider);
            cachedInfo.remove(collider);
        }
    }

    /**
     * Updates and sets new {@link SpatialGridCell}s for the given {@link VRCollider} based on
     * the associated {@link SpatialHashInfo}.
     *
     * @param collider the collider to update
     * @param info the info for the update
     */
    private void updateGridCells(final VRCollider collider, final SpatialHashInfo info) {
        final int minX = info.getIndexMinX();
        final int maxX = info.getIndexMaxX();
        final int minY = info.getIndexMinY();
        final int maxY = info.getIndexMaxY();
        final int minZ = info.getIndexMinZ();
        final int maxZ = info.getIndexMaxZ();

        final List<SpatialGridCell> gridCells = new ArrayList<>();
        for (int y = minY; y <= maxY; y += getCellHeight()) {
            for (int z = minZ; z <= maxZ; z += getCellDepth()) {
                for (int x = minX; x <= maxX; x += getCellWidth()) {
                    final SpatialGridCell cell = getCell(x, y, z);
                    cell.addChild(collider);
                    gridCells.add(cell);
                }
            }
        }
        cachedColliders.put(collider, gridCells);
        cachedInfo.put(collider, info);
    }

    private Map<Integer, Map<Integer, SpatialGridCell>> getYZCellTable(final int x) {
        if(!spatialHashTable.containsKey(x)) {
            final Map<Integer, Map<Integer, SpatialGridCell>> yzCellTable = new HashMap<>();
            spatialHashTable.put(x, yzCellTable);
        }
        return spatialHashTable.get(x);
    }

    private Map<Integer, SpatialGridCell> getZCellTable(final int x, final int y) {
        final Map<Integer, Map<Integer, SpatialGridCell>> yzCellTable = getYZCellTable(x);
        if(!yzCellTable.containsKey(y)) {
            final Map<Integer, SpatialGridCell> gridCellMap = new HashMap<>();
            yzCellTable.put(y, gridCellMap);
        }
        return yzCellTable.get(y);
    }

    private SpatialGridCell getCell(final int x, final int y, final int z) {
        final Map<Integer, SpatialGridCell> gridCellMap = getZCellTable(x, y);
        if(!gridCellMap.containsKey(z)) {
            gridCellMap.put(z, new SpatialGridCell(x, y, z));
        }
        return gridCellMap.get(z);
    }

}
