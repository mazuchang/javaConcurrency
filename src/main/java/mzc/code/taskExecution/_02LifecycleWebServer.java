package mzc.code.taskExecution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * page:101
 * 本例中利用 Executor 的生命周期特性（主要利用可关闭特性）实现了一个支持关闭操作的 Web 服务器
 */
public class _02LifecycleWebServer {

    private final ExecutorService exec = Executors.
            newCachedThreadPool();

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(80);
        // 检查线程池是否处于关闭状态
        while (!exec.isShutdown()) {
            try {
                final Socket accept = serverSocket.accept();
                exec.execute(() -> handleRequest(accept));
            } catch (RejectedExecutionException e) {
                // 这里可能会抛出 RejectedExecutionException 是因为在检查完后线程池进入了关闭状态，此时再添加任务就会抛出拒绝执行异常
                if (!exec.isShutdown())
                    log("task submission rejected", e);
            }

        }
    }

    void handleRequest(Socket connection) {
        Request request = readRequest(connection);
        if (!isShutdownRequest(request))
            stop();
        else
            dispatchRequest(request);
    }


    public void stop() {
        exec.shutdown();
    }

    private void log(String msg, Exception e) {
        Logger.getAnonymousLogger().log(Level.WARNING, msg, e);
    }

    private Request readRequest(Socket s) {
        return null;
    }

    private void dispatchRequest(Request r) {
    }

    private boolean isShutdownRequest(Request r) {
        return false;
    }
}

interface Request {
}