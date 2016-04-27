package ba.pohl1.hm.edu.vrlibrary.navigation.arrow;

import ba.pohl1.hm.edu.vrlibrary.model.VRArrow;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.util.Timer;

/**
 * Created by Pohl on 22.04.2016.
 */
public class LockedArrowTapNavigator extends LockedArrowNavigator {

    private VRArrow focusedArrow;

    @Override
    public String getToast() {
        return "Focus&Tap an arrow";
    }

    @Override
    public void focusHolding(VRComponent component, Timer timer) {
        focusedArrow = (VRArrow) component;
    }

    @Override
    public void onCardboardTrigger() {
        if(arrowsFocused == 1) {
            currentArrow = focusedArrow;
            trigger();
        }
    }
}
