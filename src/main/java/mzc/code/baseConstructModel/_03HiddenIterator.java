package mzc.code.baseConstructModel;

import net.jcip.annotations.GuardedBy;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * page:70
 * 在 _02UnsafeIterator 中我们演示了几种迭代导致的线程安全问题，本例中演示了一种隐藏的迭代
 * 这种情况下，开发者很容易忘记在访问状态时使用正确的同步，从而导致错误情况的发生
 */
public class _03HiddenIterator {
    @GuardedBy("this")
    private final Set<Integer> set = new HashSet<>();

    public synchronized void add(Integer i) {
        set.add(i);
    }

    public synchronized void remove(Integer i) {
        set.remove(i);
    }

    public void addTenThings() {
        Random r = new Random();
        for (int i = 0; i < 10; i++)
            add(r.nextInt());
        /*
        编译器将字符串的连接操作转换为调用 StringBuilder.append(Object)，而这个方法又会调用 toString() 方法，
        标准容器的 toString() 方法将迭代容器，并在每个元素上调用 toString() 来生成容器内容的格式化表示。
        除此之外，容器的 hashCode() 和 equals() 以及 containsAll()、removeAll()、retainAll() 等方法都会导致隐式迭代
         */
        System.out.println("DEBUG: added ten elements to " + set);
    }
}