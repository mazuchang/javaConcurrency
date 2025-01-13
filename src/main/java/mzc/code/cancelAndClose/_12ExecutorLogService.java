package mzc.code.cancelAndClose;

import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * page:127
 * 本例演示了利用 ExecutorService 进行关闭的日志服务
 */
public class _12ExecutorLogService {

    private final ExecutorService exec = newSingleThreadExecutor();
    private final PrintWriter writer;
    public static final long TIMEOUT = 1000;

    public _12ExecutorLogService(PrintWriter writer) {
        this.writer = new PrintWriter(writer);
    }

    public void log(String msg) {
        exec.execute(() ->
                // 记录日志
                writer.println(msg));
    }

    public void stop() {
        try {
            exec.shutdown();
            exec.awaitTermination(TIMEOUT, SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}
