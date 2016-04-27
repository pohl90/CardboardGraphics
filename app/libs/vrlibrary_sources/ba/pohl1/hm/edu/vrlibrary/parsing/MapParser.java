package ba.pohl1.hm.edu.vrlibrary.parsing;

import android.graphics.Bitmap;
import android.graphics.Color;

import ba.pohl1.hm.edu.vrlibrary.R;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.model.data.GeometryGenerator;
import ba.pohl1.hm.edu.vrlibrary.model.shapes.Cube;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;

/**
 * Created by Pohl on 24.04.2016.
 */
public class MapParser {

    private static final float SIZE = 3;
    private static final float COIN_SIZE = .15f;
    private static final float WALL_HEIGHT = 1.5f;
    private static final float[] FLOOR_COLOR = CGConstants.COLOR_BLUE;

    private static final float[] WALL_COLOR = CGConstants.COLOR_GREEN;
    private static final int ID_WALL = Color.BLACK;
    private static final int ID_COIN = Color.YELLOW;
    private static final int ID_RED_FLOOR = Color.RED;

    public static VRComponent parse(final Bitmap bitmap) {
        final int width = bitmap.getWidth();
        final int depth = bitmap.getHeight();
        final int[] pixels = new int[width * depth];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, depth);
        // Create the root
        final VRComponent root = new VRComponent();
        root.setCollisionEnabled(false);

        // Create a floor
        final VRComponent floor = new Cube();
        floor.setColor(FLOOR_COLOR);
        floor.scale(width * SIZE, 0.2f, depth * SIZE);
        root.add(floor);

        // Parse the map
        for(int z = 0; z < depth; z++) {
            for(int x = 0; x < width; x++) {
                final int color = bitmap.getPixel(x, z);
                final VRComponent component = generate(color, width, depth, x, z);
                if(component != null) {
                    root.add(component);
                }
            }
        }
        System.out.println("WALLS = " + (root.getChildren().size() - 1));
        return root;
    }

    private static VRComponent generate(final int color, final int width, final int depth, final int x, final int z) {
        if(color == ID_WALL) {
            return generateWall(width, depth, x, z);
        } else if(color == ID_COIN) {
            return generateCoin(width, depth, x, z);
        }

        return null;
    }

    private static VRComponent generateWall(final int width, final int depth, final int x, final int z) {
        final Cube wall = new Cube();
        wall.setColor(CGUtils.randomColor());
        wall.applyAsInstancedTextureMaterial(CardboardGraphics.instancedTextureShader, R.drawable.labyrinth_tex);
        // First scale with SIZE
        wall.scale(SIZE);
        // Translate to the position
        final float dx = -width / 2 + x;
        final float dz = -depth / 2 + z;
        wall.translateX(dx);
        wall.translateZ(dz);
        // Then scale
        wall.scale(1, WALL_HEIGHT, 1).translateY(0.5f);
        return wall;
    }

    private static VRComponent generateCoin(final int width, final int depth, final int x, final int z) {
        final VRComponent coin = new VRComponent();
        coin.setGeometryData(GeometryGenerator.createSphere(false));
        coin.setColor(CGUtils.randomColor());
        coin.setName("Coin");
        coin.applyAsColorMaterial(CardboardGraphics.colorShader);
        coin.scale(SIZE);
        // Translate to the position
        final float dx = -width / 2 + x;
        final float dz = -depth / 2 + z;
        coin.translateX(dx).translateZ(dz).translateY(0.5f).scale(COIN_SIZE);
        return coin;
    }
}
