package mzc.code.useExecutor;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;

/**
 * page:154
 * 本例中演示了利用闭锁机制，构造出一个阻塞的并且可携带结果的闭锁，ValueLatch 中使用 CountDownLatch 来实现所需的闭锁行为，
 * 并且使用锁定机制来确保解答只会被设置一次
 */
@ThreadSafe
public class _08ValueLatch<T> {

    @GuardedBy("this")
    private T value = null;
    private final CountDownLatch done = new CountDownLatch(1);

    public boolean isSet() {
        return (done.getCount() == 0);
    }

    public synchronized void setValue(T newValue) {
        if (!isSet()) {
            value = newValue;
            done.countDown();
        }
    }

    public T getValue() throws InterruptedException {
        done.await();
        synchronized (this) {
            return value;
        }
    }

}
