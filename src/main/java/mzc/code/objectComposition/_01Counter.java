package mzc.code.objectComposition;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * page:46
 * 本例中演示了如何设计一个线程安全的类
 * <p>
 * 在设计线程安全类的过程中，需要包含以下三个基本要素：
 * 1. 找出构成对象状态的所有变量
 * 2. 找出约束状态变量的不可变性条件
 * 3. 建立对象状态的并发访问管理策略
 */
@ThreadSafe
public class _01Counter {

    @GuardedBy("this")
    private long value = 0;

    public synchronized long getValue() {
        return value;
    }

    public synchronized long increment() {
        if (value == Long.MAX_VALUE)
            throw new IllegalStateException("counter overflow");
        return ++value;
    }
}
