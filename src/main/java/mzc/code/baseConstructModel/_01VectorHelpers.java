package mzc.code.baseConstructModel;

import java.util.Vector;

/**
 * page:67/68
 * 同步容器类都是线程安全的，但在某些情况下，依旧需要额外的客户端加锁来保护复合操作，例如迭代/跳转以及条件运算
 * 本例中演示了正确和不正确使用 Vector 的场景
 */
public class _01VectorHelpers {

    /**
     * 不正确的使用，多线程场景下线程交替调用下面两个方法将会抛出 ArrayIndexOutOfBoundsException
     */
    public static Object unsafeGetLast(Vector list) {
        int lastIndex = list.size() - 1;
        return list.get(lastIndex);
    }

    public static void unsafeDeleteLast(Vector list) {
        int lastIndex = list.size() - 1;
        list.remove(lastIndex);
    }

    /**
     * 要想保证原子性需要额外的同步
     */
    public static Object getLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }

    public static void deleteLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }

}
