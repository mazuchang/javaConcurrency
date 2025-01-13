package mzc.code.useCases;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * page:81
 * 本例中利用 FutureTask 来预加载一些稍候需要用到的数据
 */
public class _04FutureTask {

    private final FutureTask<ProductInfo> futureTask = new FutureTask<>(() -> loadProductInfo());
    private final Thread thread = new Thread(futureTask);

    // 预加载数据方法
    ProductInfo loadProductInfo() throws DataLoadException {
        return null;
    }

    /**
     * 可以在初始化时调用该 start() 开启预加载，在稍后的场景中调用 get() 获取到预加载的数据
     */
    public void start() {
        thread.start();
    }

    public ProductInfo get() throws DataLoadException, InterruptedException {
        try {
            return futureTask.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException)
                throw (DataLoadException) cause;
            else
                throw launderThrowable(cause);
        }
    }

    /**
     * 异常处理机制
     * 如果 Throwable 是 Error，那么抛出它，如果是 RuntimeException 那么就返回它，否则就抛出 IllegalStateException
     */
    public static RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;
        else if (t instanceof Error)
            throw (Error) t;
        else
            throw new IllegalStateException("未经检查的异常", t);
    }
}

interface ProductInfo {
}

class DataLoadException extends Exception {
}