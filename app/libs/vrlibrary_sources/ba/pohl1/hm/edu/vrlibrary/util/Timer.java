package ba.pohl1.hm.edu.vrlibrary.util;

import android.os.SystemClock;

/**
 * Created by Pohl on 03.04.2016.
 */
public class Timer {

    private long startTime, endTime, elapsedTime;
    private boolean running;

    public Timer start() {
        startTime = SystemClock.uptimeMillis();
        running = true;
        return this;
    }

    public Timer stop() {
        endTime = SystemClock.uptimeMillis();
        elapsedTime = endTime - startTime;
        running = false;
        return this;
    }

    public Timer reset() {
        stop();
        elapsedTime = 0;
        start();
        return this;
    }

    public long getElapsedTime() {
        return running ? SystemClock.uptimeMillis() - startTime : elapsedTime;
    }
}
