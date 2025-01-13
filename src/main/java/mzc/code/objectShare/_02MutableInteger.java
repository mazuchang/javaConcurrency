package mzc.code.objectShare;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;

/**
 * page:29
 * 本例演示了线程不安全及线程安全的几种情况
 */
public class _02MutableInteger {

    public static void main(String[] args) {
        High32SiteVal high32SiteVal = new High32SiteVal();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i : new int[5]) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                high32SiteVal.setaDouble(3.14);
                high32SiteVal.setaLong(75236431641364653L);
            }).start();
        }
        for (int i : new int[5]) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(high32SiteVal.getaDouble());
                System.out.println(high32SiteVal.getaLong());
            }).start();
        }
        countDownLatch.countDown();
    }
}

/**
 * 仅仅为 set 方法进行同步不足以确保线程安全
 */
@NotThreadSafe
class MutableInteger {
    private int value;

    public int getValue() {
        return value;
    }

    public synchronized void setValue(int value) {
        this.value = value;
    }
}

/**
 * 对于非 volatile 类型的 long 和 double 变量，JVM允许将64位的读操作和写操作分解为两个32位操作，因此多线程操作时无法确保原子性
 * （在 JSR133 规范后也就是 1.5 以后 JVM 只允许将64位的写操作分解为两个32位操作，读操作操作依旧是64位读）
 */
@NotThreadSafe
class High32SiteVal {
    private Long aLong;
    private double aDouble;

    public Long getaLong() {
        return aLong;
    }

    public void setaLong(Long aLong) {
        this.aLong = aLong;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }
}

@ThreadSafe
class SynchronizedInteger {
    @GuardedBy("this")
    private int value;

    public synchronized int getValue() {
        return value;
    }

    public synchronized void setValue(int value) {
        this.value = value;
    }
}
