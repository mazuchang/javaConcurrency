package mzc.code.cancelAndClose;

import net.jcip.annotations.GuardedBy;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * page:126
 * 本例中演示了为记录日志的服务添加可靠的取消操作，从而解决 _10LogWriter 中带来的问题
 */
public class _11LogService {

    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private final PrintWriter writer;
    @GuardedBy("this")
    private boolean isShutdown;
    @GuardedBy("this")
    private int reservations;

    public _11LogService(Writer writer) {
        this.queue = new LinkedBlockingQueue<>();
        this.loggerThread = new LoggerThread();
        this.writer = new PrintWriter(writer);
    }

    public void start() {
        loggerThread.start();
    }

    public void stop() {
        synchronized (this) {
            isShutdown = true;
        }
        loggerThread.interrupt();
    }

    public void log(String msg) throws InterruptedException {
        synchronized (this) {
            if (isShutdown)
                throw new IllegalStateException("日志服务已关闭！");
            ++reservations;
        }
        queue.put(msg);
    }

    private class LoggerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (_11LogService.this) {
                        if (isShutdown && reservations == 0)
                            break;
                        String msg = queue.take();
                        synchronized (_11LogService.this) {
                            --reservations;
                        }
                        writer.println(msg);
                    }
                } catch (InterruptedException e) {

                } finally {
                    writer.close();
                }
            }
        }
    }
}
