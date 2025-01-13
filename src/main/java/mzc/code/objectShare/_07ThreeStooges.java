package mzc.code.objectShare;

import net.jcip.annotations.Immutable;

import java.util.HashSet;
import java.util.Set;

/**
 * page:39
 * 不可变对象:
 * 不可变对象不等于将对象中所有的域都声明成 final 的，因为在 final 类型的域中依然可以保存对可变对象的引用
 * 只有当满足以下条件时，对象才是不可变的：
 * 1. 对象创建以后其状态就不能修改
 * 2. 对象那个的所有域都是 final 类型
 * 3. 对象是正确创建的（在对象的创建期间，this 引用没有逸出）
 */
@Immutable
public class _07ThreeStooges {

    private final Set<String> stooges = new HashSet<>();

    public _07ThreeStooges() {
        stooges.add("java");
        stooges.add("python");
        stooges.add("go");
    }

    public boolean isStooge(String name) {
        return stooges.contains(name);
    }
}
