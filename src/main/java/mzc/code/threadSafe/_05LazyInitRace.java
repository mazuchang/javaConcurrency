package mzc.code.threadSafe;

import net.jcip.annotations.NotThreadSafe;

import java.util.concurrent.CountDownLatch;

/**
 * page:17
 * 本例中包含了一个竞态条件，因此是非线程安全的，在多线程场景下调用 getInstance() 会有几率出现返回的实例不一致的情况
 */
@NotThreadSafe
public class _05LazyInitRace {

    private ExpensiveObject instance = null;

    public ExpensiveObject getInstance() {
        if (instance == null)
            instance = new ExpensiveObject();
        return instance;
    }

    public static void main(String[] args) {
        _05LazyInitRace lazyInitRace = new _05LazyInitRace();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i : new int[5]) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(lazyInitRace.getInstance());
            }).start();
        }
        countDownLatch.countDown();
    }

}

class ExpensiveObject {

}