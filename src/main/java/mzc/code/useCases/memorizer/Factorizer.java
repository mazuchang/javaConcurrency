package mzc.code.useCases.memorizer;

import mzc.code.useCases.memorizer.rely.Computable;
import net.jcip.annotations.ThreadSafe;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.math.BigInteger;

/**
 * page:90
 * 本例中我们演示了在一个因式分解 servlet 中使用 Memoization 缓存
 */
@ThreadSafe
public class Factorizer {

    private final Computable<BigInteger, BigInteger[]> c = (arg) -> factor(arg);
    private final Computable<BigInteger, BigInteger[]> cache = new _03MemoizationFutureTask<>(c);

    public void service(ServletRequest request, ServletResponse response) {
        try {
            BigInteger bigInteger = extractFromRequest(request);
            encodeIntoResponse(response, cache.compute(bigInteger));
        } catch (InterruptedException e) {
            encodeError(response, "factorization interrupted");
        }
    }

    void encodeError(ServletResponse resp, String errorString) {
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
