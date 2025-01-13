package mzc.code.guiApplication;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * page:159
 * 本例中演示了一个基于 构建的 Executor
 */
public class _02GuiExecutor extends AbstractExecutorService {

    private static final _02GuiExecutor instance = new _02GuiExecutor();

    private _02GuiExecutor() {
    }

    public static _02GuiExecutor instance() {
        return instance;
    }

    public void execute(Runnable r) {
        if (_01SwingUtilities.isEventDispatchThread())
            r.run();
        else
            _01SwingUtilities.invokeLater(r);
    }

    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public boolean isShutdown() {
        return false;
    }

    public boolean isTerminated() {
        return false;
    }
}
