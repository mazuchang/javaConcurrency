package mzc.code.threadSafe;

import net.jcip.annotations.ThreadSafe;

import javax.servlet.*;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * page:18
 * 在 _04UnsafeCountingFactorizer 中我们演示了一种线程不安全的场景，
 * 是因为该类中存在额外的状态，导致多线程操作可能会出现 _01ThreadUnsafe 中的问题，在本例中我们将利用 Atomic 包下的原子类解决这个问题
 */
@ThreadSafe
public class _06SafeCountingFactorizer extends GenericServlet implements Servlet {
    private final AtomicLong count = new AtomicLong(0);

    public long getCount() {
        return count.get();
    }

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        count.incrementAndGet();
        encodeIntoResponse(resp, factors);
    }

    void encodeIntoResponse(ServletResponse res, BigInteger[] factors) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return null;
    }

    BigInteger[] factor(BigInteger i) {
        return null;
    }
}
