package mzc.code.cancelAndClose;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * page:125
 * 本例中演示了一个简单的日志服务实例，其中日志操作在单独的日志线程中执行，产生的日志消息通过队列提交给日志线程，并由日志线程写入.
 * 然而当前程序并不完善，为了能使 LogWriter 这样的服务在软件产品中能发挥作用，还需要实现一种终止日志线程的方法，从而避免使 JVM 无法正常关闭
 */
public class _10LogWriter {

    private final BlockingQueue<String> queue;
    private final LoggerThread logger;
    private static final int CAPACITY = 1000;

    public _10LogWriter(Writer writer) {
        this.queue = new LinkedBlockingQueue<>(CAPACITY);
        this.logger = new LoggerThread(writer);
    }

    public void start() {
        logger.start();
    }

    public void log(String msg) throws InterruptedException {
        queue.put(msg);
    }

    private class LoggerThread extends Thread {
        private final PrintWriter writer;

        public LoggerThread(Writer writer) {
            this.writer = new PrintWriter(writer, true); // autoflush
        }

        @Override
        public void run() {
            try {
                while (true)
                    writer.println(queue.take());
            } catch (InterruptedException ignored) {
            } finally {
                writer.close();
            }
        }
    }
}
