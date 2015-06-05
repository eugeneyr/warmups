package org.lynxnet.experiments;

public class SyncTest {
    public void doSomething() {
        long s = 0;
        for (int i = 0; i < 10000; i++) {
            s += Math.pow(i, 5);
        }
    }

    public synchronized void doSomethingSync() {
        long s = 0;
        for (int i = 0; i < 10000; i++) {
            s += Math.pow(i, 5);
        }
    }

    public static void main(String[] args) {
        SyncTest test = new SyncTest();
        System.out.println("Warming up the HotSpot compiler and CPU caches...");
        for (int i = 0; i < 100; i++) {
            test.doSomething();
            test.doSomethingSync();
        }
        System.out.println("Running the test...");
        long tss0 = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            test.doSomethingSync();
        }

        long tss1 = System.currentTimeMillis();

        long ts0 = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            test.doSomething();
        }

        long ts1 = System.currentTimeMillis();

        long diff = ts1 - ts0;
        long diffSync = tss1 - tss0;

        System.out.println("Without synchronized, milliseconds: " + diff);
        System.out.println("With synchronized, milliseconds: " + diffSync);
        System.out.println("Difference on 10000 calls, milliseconds: " + (diffSync - diff));
    }
}
