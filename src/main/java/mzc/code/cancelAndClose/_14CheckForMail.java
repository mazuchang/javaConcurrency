package mzc.code.cancelAndClose;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.Executors.newCachedThreadPool;

/**
 * page:130
 * 本例中演示了使用私有的 Executor 来简化服务的生命周期管理，实现批量处理任务，并且当所有任务都处理完成后才返回
 */
public class _14CheckForMail {

    boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit) throws InterruptedException {
        ExecutorService exec = newCachedThreadPool();
        AtomicBoolean hasNewMail = new AtomicBoolean(false);
        try {
            for (final String host : hosts) {
                exec.execute(() -> {
                    if (checkMail(host))
                        hasNewMail.set(true);
                });
            }
        } finally {
            exec.shutdown();
            exec.awaitTermination(timeout, unit);
        }
        return hasNewMail.get();

    }

    private boolean checkMail(String host) {
        // Check for mail
        return false;
    }
}
