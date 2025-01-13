package mzc.code.useCases;

import java.util.concurrent.CountDownLatch;

/**
 * page:79
 * 本例中演示了利用 CountDownLatch 实现线程的同时执行及全部结束
 */
public class _03CountDownLatch {

    public static void main(String[] args) throws InterruptedException {
        long l = timeTasks(10, () -> {
            System.out.print(Thread.currentThread().getName() + "执行\n");
        });
        System.out.println("总耗时：" + l + "秒");
    }

    public static long timeTasks(int nThreads, final Runnable task) throws InterruptedException {

        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            new Thread(() -> {
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
            }).start();
        }

        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        return end - start;
    }
}
