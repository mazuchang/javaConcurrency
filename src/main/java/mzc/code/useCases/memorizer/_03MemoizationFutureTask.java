package mzc.code.useCases.memorizer;

import mzc.code.useCases._04FutureTask;
import mzc.code.useCases.memorizer.rely.Computable;

import java.util.Map;
import java.util.concurrent.*;

/**
 * page:88/89
 * 本例中利用 FutureTask 对前两个程序进行改造，使得计算过程可以被其他线程感知到
 */
public class _03MemoizationFutureTask<A, V> implements Computable<A, V>  {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public _03MemoizationFutureTask(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(final A arg) throws InterruptedException {
        Future<V> future = cache.get(arg);
        /**
         * 在当前情况下“没有就添加”这个复合操作不是原子的
         */
        if (future == null) {
            Callable<V> eval = () -> c.compute(arg);
            FutureTask<V> futureTask = new FutureTask<>(eval);
            future = futureTask;
            // cache.put(arg, futureTask);
            cache.putIfAbsent(arg, future); // 原子的“没有就添加”
            // 这里将调用 c.compute(arg)
            futureTask.run();
        }
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw _04FutureTask.launderThrowable(e);
        }
    }
}
