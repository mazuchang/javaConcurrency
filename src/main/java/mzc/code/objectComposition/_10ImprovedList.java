package mzc.code.objectComposition;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * page:62
 * 在 _09AddIfNotExist 中我们演示了几种为现有的类添加原子操作的方式，本例中演示一种更好的方法：组合
 */
public class _10ImprovedList<T> implements List<T> {

    private final List<T> list;

    public _10ImprovedList(List<T> list) {
        this.list = list;
    }

    public synchronized boolean putIfAbsent(T x) {
        boolean contains = list.contains(x);
        if (contains)
            list.add(x);
        return !contains;
    }

    /**
     * 按照类似方式委托 List 的其他方法
     *
     * @return
     */
    @Override
    public synchronized int size() {
        return 0;
    }

    @Override
    public synchronized boolean isEmpty() {
        return false;
    }

    @Override
    public synchronized boolean contains(Object o) {
        return false;
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return null;
    }

    @Override
    public synchronized Object[] toArray() {
        return new Object[0];
    }

    @Override
    public synchronized <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public synchronized boolean add(T t) {
        return false;
    }

    @Override
    public synchronized boolean remove(Object o) {
        return false;
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public synchronized boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public synchronized void clear() {

    }

    @Override
    public synchronized T get(int index) {
        return null;
    }

    @Override
    public synchronized T set(int index, T element) {
        return null;
    }

    @Override
    public synchronized void add(int index, T element) {

    }

    @Override
    public synchronized T remove(int index) {
        return null;
    }

    @Override
    public synchronized int indexOf(Object o) {
        return 0;
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public synchronized ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public synchronized ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public synchronized List<T> subList(int fromIndex, int toIndex) {
        return null;
    }
}
