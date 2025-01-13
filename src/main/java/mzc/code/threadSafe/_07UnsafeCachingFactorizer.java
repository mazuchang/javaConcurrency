package mzc.code.threadSafe;

import net.jcip.annotations.NotThreadSafe;

import javax.servlet.*;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * page:19
 * 在 _06SafeCountingFactorizer 中我们通过原子类解决了添加状态导致的线程不安全现象，那么是否多状态场景下，也可以简单通过使用原子类方式确保线程安全呢？
 * 很显然是不行的，本例演示了该场景下线程不安全的现象
 */
@NotThreadSafe
public class _07UnsafeCachingFactorizer extends GenericServlet implements Servlet {
    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<BigInteger>();
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<BigInteger[]>();

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber.get()))
            encodeIntoResponse(resp, lastFactors.get());
        else {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            encodeIntoResponse(resp, factors);
        }
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[]{i};
    }
}