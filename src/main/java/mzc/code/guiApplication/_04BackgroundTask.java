package mzc.code.guiApplication;

import java.util.concurrent.*;

/**
 * pge:163
 * 本例中通过 Future 来标识一个长时间的任务，该任务类支持取消、完成通知以及进度通知
 */
public abstract class _04BackgroundTask<V> {

    private final FutureTask<V> computation = new Computation();

    private class Computation extends FutureTask<V> {

        public Computation() {
            super(() -> _04BackgroundTask.this.compute());
        }

        protected final void done() {
            _02GuiExecutor.instance().execute(() -> {
                V value = null;
                Throwable thrown = null;
                boolean cancelled = false;
                try {
                    V v = get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    thrown = e.getCause();
                } catch (CancellationException e) {
                    cancelled = true;
                } finally {
                    onCompletion(value, thrown, cancelled);
                }
            });
        }

    }

    protected void setProgress(final int current, final int max) {
        _02GuiExecutor.instance().execute(() -> onProgress(current, max));
    }

    // 在后台线程中被取消
    protected abstract V compute() throws Exception;

    // 在事件线程中被取消
    protected void onCompletion(V result, Throwable exception,
                                boolean cancelled) {
    }

    protected void onProgress(int current, int max) {
    }

    // Other Future methods just forwarded to computation
    public boolean cancel(boolean mayInterruptIfRunning) {
        return computation.cancel(mayInterruptIfRunning);
    }

    public V get() throws InterruptedException, ExecutionException {
        return computation.get();
    }

    public V get(long timeout, TimeUnit unit)
            throws InterruptedException,
            ExecutionException,
            TimeoutException {
        return computation.get(timeout, unit);
    }

    public boolean isCancelled() {
        return computation.isCancelled();
    }

    public boolean isDone() {
        return computation.isDone();
    }

    public void run() {
        computation.run();
    }

}
