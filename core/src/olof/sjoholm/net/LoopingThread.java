package olof.sjoholm.net;

public class LoopingThread {
    private final Thread thread;
    private volatile boolean isRunning;

    public LoopingThread(final Runnable infiniteRunnable) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    infiniteRunnable.run();
                }
            }
        });
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void start() {
        isRunning = true;
        thread.start();
    }

    public void stop() {
        isRunning = false;
    }
}
