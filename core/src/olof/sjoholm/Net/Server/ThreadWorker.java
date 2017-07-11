package olof.sjoholm.Net.Server;

import java.util.LinkedList;

class ThreadWorker {
    private Buffer<Runnable> buffer = new Buffer<Runnable>();
    private Worker worker;

    void start() {
        if (worker == null) {
            worker = new Worker();
            worker.start();
        }
    }

    void stop() {
        if (worker != null) {
            worker.interrupt();
            worker = null;
        }
    }

    void execute(Runnable runnable) {
        buffer.put(runnable);
    }

    private class Worker extends Thread {
        public void run() {
            Runnable runnable;
            while (worker != null) {
                try {
                    runnable = buffer.get();
                    runnable.run();
                } catch (InterruptedException e) {
                    worker = null;
                }
            }
        }
    }

    private class Buffer<T> {
        private LinkedList<T> buffer = new LinkedList<T>();

        synchronized void put(T element) {
            buffer.addLast(element);
            notifyAll();
        }

        synchronized T get() throws InterruptedException {
            while (buffer.isEmpty()) {
                wait();
            }
            return buffer.removeFirst();
        }
    }
}
