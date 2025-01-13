package mzc.code.cancelAndClose;

import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * page:113
 * 本例中演示了一个不正确使用取消标识的场景，当前程序中的取消机制最终会使得线程停止运行，然而若是在执行过程中调用了一个阻塞方法
 * 那么线程可能永远无法检查到这个取消标识，导致线程永远结束
 */
public class _02UnreliableCloseIdentification {

    private static final int BOUND = 10;

    public void consumePrimes() throws InterruptedException {
        ArrayBlockingQueue<BigInteger> primes = new ArrayBlockingQueue<>(BOUND);
        BrokenPrimeProducer producer = new BrokenPrimeProducer(primes);
        producer.start();
        try {
            while (needMorePrimes())
                consume(primes.take());
        } finally {
            producer.cancel();
        }
    }

    private boolean needMorePrimes() {
        return true;
    }

    private void consume(BigInteger integer) {

    }
}

class BrokenPrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;
    private volatile boolean cancelled = false;

    public BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            BigInteger one = BigInteger.ONE;
            while (!cancelled)
                queue.put(one = one.nextProbablePrime());
        } catch (InterruptedException e) {
        }
    }

    public void cancel() {
        cancelled = true;
    }
}
