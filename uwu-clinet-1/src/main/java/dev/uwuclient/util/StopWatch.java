package dev.uwuclient.util;

public class StopWatch {
    private long millis;
//I HAVE MADE 50 OF THESE CLASSES HOLYYYY FUCKKKK IM BUSTING SO HARDDDD
    public StopWatch() {
        reset();
    }

    public boolean finished(final long delay) {
        return System.currentTimeMillis() - delay >= millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public void reset() {
        this.millis = System.currentTimeMillis();
    }

    public long getMillis() {
        return millis;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - this.millis;
    }
}
