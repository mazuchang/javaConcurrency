package mzc.code.cancelAndClose;

import java.util.*;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * page:130/131
 * 本例演示了如何通过封装 ExecutorService 并重写 execute() 使得其能够记录哪些任务是在关闭后取消的，并在 Executor 结束后返回
 * 被取消的任务清单。此外，要使这项技术发挥作用，任务在返回时必须维持线程的中断状态。
 */
public class _15TrackingExecutor extends AbstractExecutorService {

    private final ExecutorService exec;

    private final Set<Runnable> tasksCancelledAtShutdown = Collections.synchronizedSet(new HashSet<>());

    public _15TrackingExecutor(ExecutorService exec) {
        this.exec = exec;
    }

    public void shutdown() {
        exec.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return exec.shutdownNow();
    }

    public boolean isShutdown() {
        return exec.isShutdown();
    }

    public boolean isTerminated() {
        return exec.isTerminated();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {
        return exec.awaitTermination(timeout, unit);
    }

    public List<Runnable> getCancelledTasks(){
        if(!exec.isTerminated())
            throw new IllegalStateException("");
        return new ArrayList<>(tasksCancelledAtShutdown);
    }

    public void execute(final Runnable runnable) {
        exec.execute(()->{
            try {
                runnable.run();
            }finally {
                if(isShutdown() && Thread.currentThread().isInterrupted())
                    tasksCancelledAtShutdown.add(runnable);
            }
        });
    }

}
