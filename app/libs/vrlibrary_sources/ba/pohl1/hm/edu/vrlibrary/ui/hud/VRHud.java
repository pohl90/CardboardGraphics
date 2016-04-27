package ba.pohl1.hm.edu.vrlibrary.ui.hud;

import android.content.Context;
import android.util.AttributeSet;

import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;

/**
 * Created by Pohl on 23.04.2016.
 */
public class VRHud extends CardboardOverlayView {

    public VRHud(Context context, AttributeSet attrs) {
        super(context, attrs);
        CardboardGraphics.hud = this;
    }

}
