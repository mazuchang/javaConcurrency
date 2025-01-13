package mzc.code.useCases.memorizer;

import mzc.code.useCases.memorizer.rely.Computable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * page:87
 * 在 _01MemoizationHashMap 中我们采用了 HashMap 作为缓存容器，为了确保线程安全在 compute 方法上加入了同步机制，这种方式无疑会影响性能
 * 本例中我们利用 ConcurrentHashMap 来代替 HashMap，改进 _01MemoizationHashMap 中糟糕的并发行为
 *
 * 利用 ConcurrentHashMap 线程安全的特性可以有更好的并发行为，但它在作为缓存时仍然存在不足 —— 当两个线程同时嗲用 compute 时存在一个漏洞
 * 可能会导致计算得到相同的值，在更多线程环境下这种情况只会更糟糕。导致这个结果的原因在于如果某个线程启动了一个开销很大的计算，
 * 而其他线程无法感知到这个计算正在进行，那么就很可能会重复计算。因此我们希望通过某种方法来使线程间的计算可以被互相感知到，
 * FutureTask 的特性显然就可以让我们很轻松的实现这一点，因此在下一个例子中我们将用 FutureTask 来对当前程序进行优化
 */
public class _02MemoizationConcurrentHashMap<A, V> implements Computable<A, V> {

    private final Map<A, V> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public _02MemoizationConcurrentHashMap(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result != null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
