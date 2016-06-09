package ba.pohl1.hm.edu.vrlibrary.util;

import android.opengl.GLES20;

import ba.pohl1.hm.edu.vrlibrary.navigation.VRNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.arrow.ArrowNavigator;

/**
 * Conainer class for options.
 *
 * Created by Pohl on 04.03.2016.
 */
public class CGOptions {

    public static Class<? extends VRNavigator> NAVIGATOR = ArrowNavigator.class;
    public static boolean COLLISION_ENABLED = true;
    public static boolean CULLING_ENABLED = true;
    public static boolean FOCUSING_ENABLED = true;
    public static int CULLING_MODE = GLES20.GL_BACK;
    public static int FRONT_FACE = GLES20.GL_CCW;
    public static float ANIMATION_SPEED_MODIFIER = 1f;
    public static float CAMERA_SPEED_MODIFIER = 1f;
}
