package mzc.code.baseConstructModel;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * page:68/69
 * 对于同步容器来说，如 Vector、Collections.synchronizedList() 包装的集合等，如果在进行复合操作时，
 * 若没有使用同步等方式保证操作的原子性，则依然无法确保操作是线程安全的
 * 本例演示了对同步容器进行迭代操作所引发的线程安全问题
 */
public class _02UnsafeIterator {

    public static void main(String[] args) {
        _02UnsafeIterator unsafeIterator = new _02UnsafeIterator();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Vector vector = new Vector();
        for (int i : new int[10]) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                vector.add(i);
                // ArrayIndexOutOfBoundsException
                // unsafeIterator.unsafeForIterator(vector);
            }).start();
        }

        // ConcurrentModificationException
        unsafeIterator.unsafeForEachIterator();
        countDownLatch.countDown();
    }

    /**
     * 本方法中通过 for 对 list 进行迭代，由于没有保证调用 size() 方法和 get() 方法间的原子性
     * 因此该方法会抛出 ArrayIndexOutOfBoundsException
     */
    public void unsafeForIterator(Vector list) {
        for (int i = 0; i < list.size(); i++) {
            doSomething(list.get(i));
            list.remove(1);
        }
    }

    /**
     * 解决上述问题的方法就是利用同步确保安全的迭代
     */
    public void safeIterator(Vector list) {
        synchronized (list) {
            for (int i = 0; i < list.size(); i++) {
                doSomething(list.get(i));
            }
        }
    }

    /**
     * 本方法中通过 for-each 对 list 进行迭代，其本质上也是通过反复调用 Iterator 的 hasNext() 和 next() 方法实现，
     * 因此在 for-each 中调用数组的 add() remove() 也会抛出 ConcurrentModificationException
     * （考虑到使用同步会影响性能，若容器中元素过多，处理时间很长还可能导致死锁问题发生，因此我们还可以通过将容器克隆一份的方式，从而解决性能开销）
     */
    public void unsafeForEachIterator() {
        List<Widget> widgetList = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 1000; i++) {
            widgetList.add(new Widget());
        }
        for (Widget widget : widgetList) {
            doSomething(widget);
            widgetList.remove(widget);
        }
    }

    /**
     * 对于 ForEach 遍历存在的问题，我们可以通过以下两种方式解决
     */
    public void safeForEachIterator() {
        List<Widget> widgetList = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 1000; i++) {
            widgetList.add(new Widget());
        }
        // 使用 fori
        for (int i = 0; i < widgetList.size(); i++) {
            doSomething(widgetList.get(i));
            widgetList.remove(widgetList.get(i));
        }
        // 使用迭代器进行删除
        Iterator<Widget> iterator = widgetList.iterator();
        while (iterator.hasNext()) {
            Widget next = iterator.next();
            doSomething(next);
            iterator.remove();
        }

    }


    void doSomething(Object o) {
    }
}

class Widget {
}