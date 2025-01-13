package mzc.code.objectComposition;

import net.jcip.annotations.GuardedBy;

/**
 * page:51
 * 本例演示了通过一个私有锁来保护状态，这样可以通过细粒度的枷锁来提高可伸缩性
 */
public class _03PrivateLock {

    private final Object myLock = new Object();

    @GuardedBy("myLock")
    Widget widget;

    void someMethod() {
        synchronized (myLock) {
            // 访问或修改 Widget 的状态
        }
    }

    class Widget {
    }
}

