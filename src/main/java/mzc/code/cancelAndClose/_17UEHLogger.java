package mzc.code.cancelAndClose;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * page:134
 * 本例中演示了利用 UncaughtExceptionHandler 将线程运行时的异常信息写入到引用程序日志中。
 *
 * UncaughtExceptionHandler 是 Thread API 中提供的一种用于解决未检查异常的主动方法，通过它能检测出某个线程由于
 * 未捕获的异常而终结的情况。
 */
public class _17UEHLogger implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger logger = Logger.getAnonymousLogger();
        logger.log(Level.SEVERE,
                "Thread terminated with exception：" + t.getName(),
                e);
    }
}
