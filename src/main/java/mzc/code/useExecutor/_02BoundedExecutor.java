package mzc.code.useExecutor;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * page:145
 * 本例中利用 Semaphore 来限制任务的到达率，当工作队列被填满后，就可以阻塞 execute 的提交
 */
@ThreadSafe
public class _02BoundedExecutor {

    private final Executor exec;
    private final Semaphore semaphore;

    public _02BoundedExecutor(Executor exec, int bound) {
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }

    public void submitTask(final Runnable command) throws InterruptedException {
        semaphore.acquire();
        try {
            exec.execute(() -> {
                try {
                    command.run();
                } finally {
                    semaphore.release();
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }
    }

}
