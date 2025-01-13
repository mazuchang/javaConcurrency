package mzc.code.guiApplication;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

/**
 * page:158
 * 在 Swing 中所有组件和数据模型队形都被封闭在事件线程中运行，Swing 中的组件以及模型只能在这个事件分发线程中进行创建和修改以及查询，
 * 然而也有少数方法可以安全地从其他线程调用，本例中通过 Executor 来实现 SwingUtilities 就实现了这一点
 */
public class _01SwingUtilities {

    private static final ExecutorService exec = Executors.newSingleThreadExecutor(new SwingThreadFactory());
    private static volatile Thread swingThread;

    private static class SwingThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            return thread;
        }
    }

    public static boolean isEventDispatchThread() {
        return Thread.currentThread() == swingThread;
    }

    public static void invokeLater(Runnable runnable) {
        exec.execute(runnable);
    }

    public static void invokeAndWait(Runnable runnable) throws InterruptedException, InvocationTargetException {
        Future<?> future = exec.submit(runnable);
        try {
            future.get();
        } catch (ExecutionException e) {
            throw new InvocationTargetException(e);
        }
    }

}
