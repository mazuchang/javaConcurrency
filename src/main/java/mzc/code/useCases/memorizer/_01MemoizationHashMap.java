package mzc.code.useCases.memorizer;

import mzc.code.useCases.memorizer.rely.Computable;
import net.jcip.annotations.GuardedBy;

import java.util.HashMap;
import java.util.Map;

/**
 * page:86
 * 本例中利用 HashMap 实现了一个缓存，首先通过 Map 保存之前的计算结果，compute 方法首先检查需要的结果是否存在于缓存中
 * 如果存在则返回，反之先保存再返回，由于 HashMap 本身不是线程安全的，为了保证线程安全，在 compute 方法上加入了同步
 */
public class _01MemoizationHashMap<A, V> implements Computable<A, V> {

    @GuardedBy("this")
    private final Map<A, V> cache = new HashMap<>();
    private final Computable<A, V> c;

    public _01MemoizationHashMap(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result != null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
