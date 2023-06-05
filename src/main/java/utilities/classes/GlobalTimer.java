package utilities.classes;

public class GlobalTimer {
    private long startTime;
    private long endTime;
    private static float deltaTimeMillis;

    public void init(){
        startTime = System.currentTimeMillis();
    }

    public void update() {
        endTime = System.currentTimeMillis();
        deltaTimeMillis = (float) (endTime - startTime);
        startTime = endTime;
    }

    public static float getDeltaTimeMillis() {
        return deltaTimeMillis;
    }

    public static float getDeltaTimeSeconds() {
        return deltaTimeMillis / 1000f;
    }
}
