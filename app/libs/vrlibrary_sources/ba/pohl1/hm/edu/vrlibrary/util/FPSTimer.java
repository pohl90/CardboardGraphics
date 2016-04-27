package ba.pohl1.hm.edu.vrlibrary.util;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pohl on 17.03.2016.
 */
public class FPSTimer {

    private static final float FLUENT_FPS_VALUE = 30f;
    private static final long MILLIS_TO_SECONDS = 1000L;

    private long startTime, deltaTime, endTime;
    private int frames, fps = 30;
    private List<FPSChangedListener> fpsChangedListeners = new ArrayList<>();

    public void reset() {
        startTime = endTime = SystemClock.uptimeMillis();
        frames = 0;
    }

    public void refresh() {
        endTime = SystemClock.uptimeMillis();
        deltaTime = endTime - startTime;
        if(toSeconds(deltaTime) >= 1.0f) {
            fps = frames;
            for(final FPSChangedListener listener : fpsChangedListeners) {
                listener.fpsUpdate(fps);
            }
            reset();
        } else {
            frames++;
        }
    }

    public float getDelta() {
        return (FLUENT_FPS_VALUE / (getFPS() > 0 ? getFPS() : 1));
    }

    public int getFPS() {
        return fps;
    }

    public void addFPSChangedListener(final FPSChangedListener listener) {
        fpsChangedListeners.add(listener);
    }

    public void removeFPSChangedListener(final FPSChangedListener listener) {
        fpsChangedListeners.remove(listener);
    }

    private long toSeconds(final long millis) {
        return millis / MILLIS_TO_SECONDS;
    }

    public interface FPSChangedListener {
        void fpsUpdate(final int fps);
    }
}
