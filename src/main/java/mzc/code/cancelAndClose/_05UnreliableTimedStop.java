package mzc.code.cancelAndClose;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * page:119
 * 本例中演示了通过定时调度任务实现超时中断的效果，然而这是个错误的案例，由于该任务可以从任意线程中调用，而我们无法得知调用者的
 * 中断策略（是否能够响应中断），因此这种在外部线程中安排中断的方式是不可靠的
 */
public class _05UnreliableTimedStop {

    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);

    public static void timedRun(Runnable r,
                                long timeout,
                                TimeUnit unit) {
        final Thread taskThread = Thread.currentThread();
        cancelExec.schedule(() -> taskThread.interrupt(), timeout, unit);
        r.run();
    }

}
