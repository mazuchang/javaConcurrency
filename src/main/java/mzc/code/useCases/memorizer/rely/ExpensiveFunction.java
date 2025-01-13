package mzc.code.useCases.memorizer.rely;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

/**
 * Computable 包装器，帮助记住之前的计算结果，并将缓存过程封装起来
 */
public class ExpensiveFunction implements Computable<String, BigInteger> {

    @Override
    public BigInteger compute(String arg) throws InterruptedException {
        // 模拟长时间计算
        TimeUnit.SECONDS.sleep(10);
        return new BigInteger(arg);
    }

}
