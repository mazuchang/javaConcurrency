package mzc.code.useCases;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * page:83
 * 本例中演示使用 Semaphore 为容器设置边界，类似于缓冲池，从池中添加元素时需要先持有信号量，删除时释放
 */
public class _05Semaphore<T> {

    private final Set<T> set;
    private final Semaphore sem;

    public _05Semaphore(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<>());
        sem = new Semaphore(bound);
    }

    public boolean add(T o) throws InterruptedException {
        sem.acquire();
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return wasAdded;
        } finally {
            if (!wasAdded)
                sem.release();
        }
    }

    public boolean remove(Object o) {
        boolean wasRemoved = set.remove(o);
        if (wasRemoved)
            sem.release();
        return wasRemoved;
    }
}
