package mzc.code.threadSafe;

import net.jcip.annotations.NotThreadSafe;

import javax.servlet.*;
import java.math.BigInteger;

/**
 * page:15
 * 在本例中我们依旧是编写了一个因式分解服务，由于添加了额外的状态使得程序出现多个竞态条件，因此是线程不安全的
 */
@NotThreadSafe
public class _04UnsafeCountingFactorizer extends GenericServlet implements Servlet {
    private long count = 0;

    public long getCount() {
        return count;
    }

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        ++count;
        encodeIntoResponse(resp, factors);
    }

    void encodeIntoResponse(ServletResponse res, BigInteger[] factors) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[]{i};
    }
}
