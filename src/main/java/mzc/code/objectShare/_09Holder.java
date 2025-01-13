package mzc.code.objectShare;

/**
 * page:42
 * 本例演示了一个不正确的发布，由于没有使用同步确保 Holder 对象对其他线程可见，因此在多线程场景下访问就可能出现一些非常奇怪的问题
 *
 * 可变对象必须通过安全的方式发布，要安全地发布一个对象，对象的引用以及对象的状态必须同时对其他线程可见，
 * 一个正确构造的对象可以通过以下方式来安全的发布：
 *  1. 在静态初始化函数中初始化一个对象引用
 *  2. 将对象的引用保存到 volatile 类型的域或者 AtomicReference 对象中
 *  3. 将对象的引用保存到某个正确构造对象的 final 类型域中
 *  4. 将对象引用保存到一个由锁保护的域中
 */
public class _09Holder {

    private int n;

    public _09Holder(int n) {
        this.n = n;
    }

    public void assertSanity() {
        if (n != n)
            throw new AssertionError("This statement is false.");
    }

}
