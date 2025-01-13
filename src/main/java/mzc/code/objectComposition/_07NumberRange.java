package mzc.code.objectComposition;

import net.jcip.annotations.NotThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * page:56
 * 在本例中利用 AtomicInteger 来对多状态进行管理，由于多状态间包含一个约束条件，因此在不进行额外同步的情况下，当前程序是非线程安全的
 */
@NotThreadSafe
public class _07NumberRange {

    // 不变性条件 lower <= upper
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i) {
        // 不安全的“先检查后执行”
        if (i > upper.get())
            throw new IllegalArgumentException("can't set lower to " + i + " > upper");
        lower.set(i);
    }

    public void setUpper(int i) {
        // 不安全的“先检查后执行”
        if (i < lower.get())
            throw new IllegalArgumentException("can't set upper to " + i + " < lower");
        upper.set(i);
    }

    public boolean isInRange(int i) {
        return (i >= lower.get() && i <= upper.get());
    }
}
