package ba.pohl1.hm.edu.vrlibrary.physics.focus;

import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.util.Timer;

/**
 * A listener to observe notifications of:
 * <p>
 *     <ul>
 *         <li>focus gained</li>
 *         <li>focus lost</li>
 *         <li>focus holding</li>
 *     </ul>
 * </p>
 *
 * Created by Pohl on 03.04.2016.
 */
public interface FocusListener {

    void focusGained(final VRComponent component);

    void focusLost(final VRComponent component);

    void focusHolding(final VRComponent component, final Timer timer);
}
