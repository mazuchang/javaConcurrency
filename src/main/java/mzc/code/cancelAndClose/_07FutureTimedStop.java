package mzc.code.cancelAndClose;

import mzc.code.useCases._04FutureTask;

import java.util.concurrent.*;

/**
 * page:121
 * 本例中通过将任务提交给一个 ExecutorService 并通过一个定时的 Future.get() 来获得结果。如果 get() 在返回时抛出了一个
 * TimeoutException，那么任务将通过 Future 来取消。如果任务在被取消前就抛出一个异常，那么该异常将被重新抛出以便有调用者来处理异常
 */
public class _07FutureTimedStop {

    private static final ExecutorService taskExec = Executors.newCachedThreadPool();

    public static void timedRun(Runnable r,
                                long timeout,
                                TimeUnit unit) {
        Future<?> task = taskExec.submit(r);
        try {
            task.get(timeout, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            // 如果在任务中抛出了异常，那么重新抛出该异常
            throw _04FutureTask.launderThrowable(e);
        } catch (TimeoutException e) {
            // 接下来任务将取消
        } finally {
            // 如果任务已结束，那么执行取消操作也不会带来任何影响，如果任务正在运行，那么将被中断
            task.cancel(true);
        }
    }

}
