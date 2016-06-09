package ba.pohl1.hm.edu.vrlibrary.util;

import android.content.Context;
import android.os.Vibrator;

import com.google.vrtoolkit.cardboard.CardboardView;

import ba.pohl1.hm.edu.vrlibrary.cardboard.CardboardTrigger;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.ui.hud2d.VRHud;

/**
 * This class holds various global attributes that are frequently used.
 *
 * Created by Pohl on 21.04.2016.
 */
public class CardboardGraphics {

    public static Vibrator vibrator;

    public static Shader colorShader;
    public static Shader instancedColorShader;
    public static Shader instancedTextureShader;
    public static Shader gridShader;
    public static Shader textureShader;
    public static Shader crosshairShader;

    public static VRCamera camera;
    public static Context context;

    public static CardboardView cardboardView;
    public static CardboardTrigger cardboardTrigger;
    public static VRHud hud;

    public static long focusTriggerDelay = 1000L; // in ms

    private CardboardGraphics() {
        // Prevents instantiation
    }

    public static boolean hasHUD() {
        return hud != null;
    }
}
