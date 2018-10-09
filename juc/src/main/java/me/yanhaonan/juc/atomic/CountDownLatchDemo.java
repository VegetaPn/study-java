package me.yanhaonan.juc.atomic;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Vegeta on 10/9/18.
 */
public class CountDownLatchDemo {
    private static long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        }

        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        return end - start;
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable r = () -> System.out.println("task running...");

        long timeSpend = timeTasks(5, r);
        System.out.println("time spent: " + timeSpend);
    }
}
