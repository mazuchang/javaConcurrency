package mzc.code.threadSafe;

import net.jcip.annotations.NotThreadSafe;

import java.util.concurrent.CountDownLatch;

/**
 * page:5
 * 本例演示了一个非线程安全的场景：
 * 在多个线程同时调用UnsafeSequence类中的getNext()方法对其私有的value属性进行自增操作时，会有概率出现相同值的情况
 */
public class _01ThreadUnsafe {

    public static void main(String[] args) {
        UnsafeSequence unsafeSequence = new UnsafeSequence();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i : new int[5]) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(unsafeSequence.getNext());
            }).start();
        }
        countDownLatch.countDown();
    }

}

@NotThreadSafe
class UnsafeSequence {
    private int value;

    public int getNext() {
        return value++;
    }
}

