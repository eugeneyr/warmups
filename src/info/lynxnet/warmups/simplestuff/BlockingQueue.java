package info.lynxnet.warmups.simplestuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class BlockingQueue<T> {
    private List<T> elements = new ArrayList<T>();

    static volatile boolean stopIt = false;

    public synchronized T dequeue() throws InterruptedException {
        while (!stopIt && elements.isEmpty()) {
            wait();
        }
        return stopIt ? null : elements.remove(0);
    }

    public synchronized void enqueue(T element) {
        elements.add(element);
        notify();
    }
    public synchronized void shutdown() {
        stopIt = true;
        notifyAll();
    }

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3);
        ExecutorService exec = Executors.newFixedThreadPool(3);
        BlockingQueue<Integer> queue = new BlockingQueue<Integer>();
        Producer producer = new Producer(queue, barrier);
        Consumer thingOne = new Consumer(queue, barrier, "thing One");
        Consumer thingTwo = new Consumer(queue, barrier, "thing Two");
        Future prodFuture = exec.submit(producer);
        Future oneFuture = exec.submit(thingOne);
        Future twoFuture = exec.submit(thingTwo);
        try {
            prodFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        exec.shutdown();
    }

    static class Producer implements Runnable {
        private BlockingQueue<Integer> queue;
        private CyclicBarrier barrier;

        Producer(BlockingQueue<Integer> queue, CyclicBarrier barrier) {
            this.queue = queue;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            Random random = new Random();
            random.setSeed(System.currentTimeMillis());
            for (int i = 0; i < 1000; i++) {
                queue.enqueue(i);
                try {
                    Thread.sleep(random.nextInt(100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            queue.shutdown();
        }
    }

    static class Consumer implements Runnable {
        private BlockingQueue<Integer> queue;
        private CyclicBarrier barrier;
        private String name;

        Consumer(BlockingQueue<Integer> queue, CyclicBarrier barrier, String name) {
            this.queue = queue;
            this.barrier = barrier;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            while (!stopIt) {
                try {
                    Integer i = queue.dequeue();
                    if (i == null) {
                        System.out.println(name + ": Empty");
                    } else {
                        System.out.println(name + ": " + i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
