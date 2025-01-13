package mzc.code.threadSafe;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;

/**
 * page:6
 * 利用 synchronized 同步可以解决 _01ThreadUnsafe 中线程不安全的问题
 */
public class _02ThreadSafe {

    public static void main(String[] args) {
        SafeSequence safeSequence = new SafeSequence();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i : new int[5]) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(safeSequence.getNext());
            }).start();
        }
        countDownLatch.countDown();
    }

}

@ThreadSafe
class SafeSequence {
    @GuardedBy("this")
    private int value;

    public synchronized int getNext() {
        return value++;
    }
}

