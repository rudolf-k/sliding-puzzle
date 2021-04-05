public class Stopwatch {
    private long start;

    public void start() {
        start = System.currentTimeMillis();
    }

    public long getElapsedTimeMinutes() {
        return (System.currentTimeMillis() - start) / 60000;
    }

    public long getElapsedTimeSeconds() {
        return ((System.currentTimeMillis() - start) / 1000) % 60;
    }

    public long getElapsedTimeMillis() {
        return (System.currentTimeMillis() - start) % 1000;
    }
}
