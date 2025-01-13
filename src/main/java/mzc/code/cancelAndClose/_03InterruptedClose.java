package mzc.code.cancelAndClose;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * page:115
 * 本例中演示了如何使用中断来取消线程的执行
 */
public class _03InterruptedClose {


}

class PrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;

    public PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        BigInteger one = BigInteger.ONE;
        try {
            while (!Thread.currentThread().isInterrupted())
                queue.put(one = one.nextProbablePrime());
        } catch (InterruptedException e) {
            // 允许线程退出
        }
    }

    public void cancel() {
        interrupt();
    }
}
