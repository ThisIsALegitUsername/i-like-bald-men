package dev.uwuclient.util;

public class TimerUtil {
//I HAVE MADE 50 OF THESE CLASSES HOLYYYY FUCKKKK IM BUSTING SO HARDDDD
    private static long halfSecond = 500000000;
    private long lastTime;

    private long getDeltaTime() {
        return (System.nanoTime() - lastTime);
    }

    private void updateTime() {
        this.lastTime = System.nanoTime();
    }

    public TimerUtil() {
        this.lastTime = System.nanoTime();
    }

    public boolean hasHalfSecondPassed() {
        if (getDeltaTime() >= halfSecond) {
            updateTime();
            return true;
        } else return false;
    }
    
    public boolean hasTimePassed(long ms) {
    	if (getDeltaTime() >= (ms * 1000000)) {
            updateTime();
            return true;
        } else return false;
    }
}

