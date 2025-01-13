package mzc.code.objectComposition;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

/**
 * page:50
 * 本例演示了一个实例封闭的场景，在本例中一个包含 Person 类型的 HashSet 对象被封闭在 _02PersonSet 中
 * 并且对该对象的访问都需要通过 _02PersonSet 中的同步方法，如此一来即使 HashSet 本身不是线程安全的，也可以确保线程安全
 * （需要注意的是 HashSet 中的 Person 类并不是线程安全的，如果想要安全的访问则还需要加同步来保证）
 */
@ThreadSafe
public class _02PersonSet {

    @GuardedBy("this")
    private final Set<Person> mySet = new HashSet<>();

    public synchronized void addPerson(Person p) {
        mySet.add(p);
    }

    public synchronized boolean containsPerson(Person p) {
        return mySet.contains(p);
    }

    class Person {
    }
}