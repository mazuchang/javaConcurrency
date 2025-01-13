package mzc.code.useExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * page:149-151
 * 本例中演示了如何通过线程池实现任务的并行化执行
 */
public abstract class _05TransformingSequential {

    /**
     * 串行迭代执行
     */
    void processSequentially(List<Element> elements) {
        for (Element element : elements) {
            process(element);
        }
    }

    /**
     * 并行迭代执行
     */
    void processInParallel(Executor exec, List<Element> elements) {
        for (final Element element : elements) {
            exec.execute(() -> process(element));
        }
    }

    /**
     * 串行递归执行
     */
    public <T> void sequentialRecursive(List<Node<T>> nodes, Collection<T> results) {
        for (Node<T> node : nodes) {
            results.add(node.compute());
            sequentialRecursive(node.getChildren(), results);
        }
    }

    /**
     * 并行递归执行
     */
    public <T> void parallelRecursive(final Executor exec,
                                      List<Node<T>> nodes,
                                      final Collection<T> results) {
        for (final Node<T> node : nodes) {
            exec.execute(() -> results.add(node.compute()));
            parallelRecursive(exec, node.getChildren(), results);
        }
    }

    /**
     * 等待并行方式计算的结果
     */
    public <T> Collection<T> getParallelResults(List<Node<T>> nodes) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Queue<T> resultQueue = new ConcurrentLinkedQueue<>();
        parallelRecursive(exec, nodes, resultQueue);
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        return resultQueue;
    }


    public abstract void process(Element e);

    interface Element {
    }

    interface Node<T> {
        T compute();

        List<Node<T>> getChildren();
    }
}
