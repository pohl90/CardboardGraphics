package ba.pohl1.hm.edu.vrlibrary.navigation;

import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;

/**
 * Created by Pohl on 04.03.2016.
 */
public interface VRNavigator {

    void navigate(final VRCamera camera, final float deltaMove);

    void enable();

    void disable();

    void onCardboardTrigger();

    void dispose();

    String getToast();
}
