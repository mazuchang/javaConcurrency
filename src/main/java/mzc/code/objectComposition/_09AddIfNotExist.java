package mzc.code.objectComposition;

import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * page:60/61
 * 本例演示如何在现有的线程安全类中添加功能 —— 在现有的线程安全类中增加一个 “不存在则添加”的方法
 */
public class _09AddIfNotExist {

    /**
     * 扩展类机制：
     * 如果要在已有的线程安全类中增加一个方法，最简单的方式当然是直接修改该类，但大多数情况下这都是无法实现的
     * 另一种方式就是扩展这个类，假设这个类考虑到了可扩展性，我们就可以直接通过继承方式对该类进行扩展，本类就采用了扩展 Vector 的方式实现
     */
    @ThreadSafe
    class BetterVector<E> extends Vector<E> {
        public synchronized boolean PutIfAbsent(E x) {
            boolean absent = !contains(x);
            if (absent)
                add(x);
            return absent;
        }
    }

    /**
     * 客户端加锁机制
     * 对于由 Collections.synchronizedList 封装的 ArrayList，上面介绍的两种方式都行不通，因为客户代码并不知道在同步封装器工厂方法中
     * 返回的 List 对象的类型，对于这种场景，我们可以将扩展代码放入一个“辅助类”中
     */
    class ListHelper<E> {
        public List<E> list = Collections.synchronizedList(new ArrayList<E>());

        /**
         * 这里演示了一个错误的加锁方式，原因就在于 synchronized 修饰方法，锁的是当前对象的 this，而不是对象的 list 属性，这也就意味着
         * 在调用 putIfAbsent() 时，list 依然可能被其他线程修改。
         */
        public synchronized boolean putIfAbsent(E x) {
            boolean absent = !list.contains(x);
            if (absent)
                list.add(x);
            return absent;
        }

        /**
         * 正确的加锁方式
         */
        public boolean putIfAbsentSync(E x) {
            synchronized (list) {
                boolean absent = !list.contains(x);
                if (absent)
                    list.add(x);
                return absent;
            }
        }
    }

}
