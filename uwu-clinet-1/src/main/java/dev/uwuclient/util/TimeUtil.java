package dev.uwuclient.util;

public final class TimeUtil {

    public long lastMS = 0L;

    public int convertToMS(final int d) {
        return 1000 / d;
    }
//I HAVE MADE 50 OF THESE CLASSES HOLYYYY FUCKKKK IM BUSTING SO HARDDDD
    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - this.lastMS;
    }

    public boolean hasReached(final long milliseconds) {
        return getCurrentMS() - lastMS >= milliseconds;
    }

    public long getDelay() {
        return System.currentTimeMillis() - lastMS;
    }

    public void reset() {
        lastMS = getCurrentMS();
    }

    /*
      tbh i skidded from intent and don't remember what it does
     */
    public void setLastMS() {
        lastMS = System.currentTimeMillis();
    }

    /*
      tbh i skidded from intent and i don't remember what it does
     */
    public void setLastMS(final long lastMS) {
        this.lastMS = lastMS;
    }
}