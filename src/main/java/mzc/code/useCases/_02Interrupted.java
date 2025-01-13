package mzc.code.useCases;

import java.util.concurrent.TimeUnit;

/**
 * Java并发编程的艺术 —— page:95
 * 本例演示了如何利用中断安全地终止一个线程
 */
public class _02Interrupted {

    public static void main(String[] args) throws InterruptedException {

        Thread one = new Thread(new Runner(), "CountThread");
        one.start();
        // 主线程睡眠一秒，使 CountThread 能够感知中断而结束
        TimeUnit.SECONDS.sleep(1);
        // 利用中断终止线程
        one.interrupt();

        Runner runner = new Runner();
        Thread two = new Thread(runner, "CountThread");
        two.start();
        TimeUnit.SECONDS.sleep(1);
        // 利用标志位终止线程
        runner.cancel();
    }

    static class Runner implements Runnable {
        private long i;
        private volatile boolean on = true;

        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted())
                i++;
            System.out.println("Count i = " + i);
        }

        public void cancel() {
            on = false;
        }

    }
}
