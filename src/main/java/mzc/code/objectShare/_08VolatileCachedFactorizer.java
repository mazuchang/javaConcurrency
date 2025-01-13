package mzc.code.objectShare;

import jdk.nashorn.internal.ir.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * page:40
 * 在前面的 threadSafe._04UnsafeCountingFactorizer 中我们尝试使用两个 AtomicReferences 变量来保证线程安全，实际上这样是行不通的，
 * 因为这两个变量的读写并不能保证原子性，同样的，用 volatile 类型的变量来保存这些值也不是线程安全的，但是在某些情况下，使用不可变对象能提供一种弱形式的原子性
 *
 * 在本例中我们通过操作一个不可变对象，并用一个 volatile 类型的引用来确保可见性，使得程序在不使用锁的情况下也可以实现线程安全
 */
@ThreadSafe
public class _08VolatileCachedFactorizer extends GenericServlet implements Servlet {
    private volatile OneValueCache cache = new OneValueCache(null, null);

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = cache.getFactors(i);
        if (factors == null) {
            factors = factor(i);
            cache = new OneValueCache(i, factors);
        }
        encodeIntoResponse(resp, factors);
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

@Immutable
class OneValueCache {

    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;

    public OneValueCache(BigInteger i, BigInteger[] factors) {
        lastNumber = i;
        lastFactors = factors;
    }

    public BigInteger[] getFactors(BigInteger i) {
        if (lastNumber == null || !lastNumber.equals(i))
            return null;
        else
            return Arrays.copyOf(lastFactors, lastFactors.length);
    }
}
