package mzc.code.cancelAndClose;

import javafx.concurrent.Task;

import java.util.concurrent.BlockingQueue;

/**
 * page:118
 * 本例中利用 Interrupt() 在线程抛出中断异常后重新恢复中断，使得线程得以继续执行
 */
public class _04NonCancelTask {
    
    public Task getNextTask(BlockingQueue<Task> queue) {
        boolean interrupted = false;
        try {
            while (true)
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
        } finally {
            if (interrupted)
                // 重新尝试
                Thread.currentThread().interrupt();
        }
    }
}
