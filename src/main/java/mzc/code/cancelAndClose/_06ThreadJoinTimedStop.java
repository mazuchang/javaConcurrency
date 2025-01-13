package mzc.code.cancelAndClose;

import mzc.code.useCases._04FutureTask;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * page:119
 * 本例中演示了利用 Thread.join() 实现任务的定时停止，通过这种方式可以解决 _05UnreliableTimedStop 中出现的问题，但仍有不足，
 * 由于其依赖于一个限时的 join() 因此无法知道执行控制是因为线程正常退出而返回还是因为 join 超时而返回
 */
public class _06ThreadJoinTimedStop {

    private static final ScheduledExecutorService cancelExec = newScheduledThreadPool(1);

    public static void timedRun(final Runnable r,
                                long timeout,
                                TimeUnit unit) throws InterruptedException {
        class ReThrowableTask implements Runnable {
            private volatile Throwable t;

            @Override
            public void run() {
                try {
                    r.run();
                } catch (Throwable t) {
                    this.t = t;
                }
            }

            void rethrow() {
                if (t != null)
                    throw _04FutureTask.launderThrowable(t);
            }
        }

        ReThrowableTask task = new ReThrowableTask();
        final Thread taskThread = new Thread(task);
        taskThread.start();
        cancelExec.schedule(() -> taskThread.interrupt(), timeout, unit);
        taskThread.join(unit.toMillis(timeout));
        task.rethrow();
    }

}
