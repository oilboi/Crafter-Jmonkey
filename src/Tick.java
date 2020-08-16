public class Tick {
    private static long lastTime = System.nanoTime();
    private static long deltaTime = 0;
    private static long time = 0;

    public static long tick( long accumulatedTime) {
        time = System.nanoTime();
        deltaTime = time - lastTime;
        lastTime = time;
        accumulatedTime += deltaTime;
        return accumulatedTime;
    }
}
