package mzc.code.useExecutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * page:146
 * 在本例中演示了自定义了线程的工厂方法，通过在基类中定制一些其他行为，可以实现包括为线程指定名称、设置自定义 UncaughtExceptionHandler
 * 向 Logger 中写入信息，维护一些统计信息等。
 */
public class _03LoggerThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        return null;
    }

}

class LoggerAppThread extends Thread {
    public static final String DEFAULT_NAME = "LOGGER_THREAD";
    private static volatile boolean debugLifecycle = false;
    private static final AtomicInteger created = new AtomicInteger();
    private static final AtomicInteger alive = new AtomicInteger();
    private static final Logger log = Logger.getAnonymousLogger();

    public LoggerAppThread(Runnable r) {
        this(r, DEFAULT_NAME);
    }

    public LoggerAppThread(Runnable runnable, String name) {
        super(runnable, name + "_" + created.incrementAndGet());
        setUncaughtExceptionHandler((thread, throwable) -> log.log(Level.SEVERE, "UNCAUGHT in thread" + thread.getName(), throwable));
    }

    public void run() {
        // 复制 debug 标志以确保一致的值
        boolean debug = debugLifecycle;
        if (debug) log.log(Level.FINE, "Created" + getName());
        try {
            alive.incrementAndGet();
            super.run();
        } finally {
            alive.decrementAndGet();
            if (debug) log.log(Level.FINE, "Exiting" + getName());
        }
    }

}



