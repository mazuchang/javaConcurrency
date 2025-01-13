package mzc.code.objectShare;

import mzc.code.objectShare.rely.EventListener;
import mzc.code.objectShare.rely.EventSource;

import java.util.HashSet;
import java.util.Set;

/**
 * page:33
 * 本例演示了对象发布中各种不安全的场景
 */
public class _03PublishAndEscape {

    public static void main(String[] args) {

    }

}

/**
 * 不安全的发布，会导致私有的内部类溢出它所在的作用域
 */
class UnsafePublish {
    public static Set<Secret> knownSecrets;

    private class Secret {
    }

    public void initialize() {
        knownSecrets = new HashSet<Secret>();
    }

}

/**
 * 不安全的发布，会导致私有的属性溢出它所在的作用域
 */
class UnsafeStates {

    private String[] language = new String[]{"java", "cpp", "javascript", "python", "go"};

    public String[] getLanguage() {
        return language;
    }

}

/**
 * 不安全的发布，会导致隐式的this溢出
 */
class ThisEscape {

    /**
     * 在通过 ThisEscape 发布 EventListener 时也隐含地发送了 ThisEscape 实例本身
     * 因为在这个内部类的实例中包含了对 ThisEscape 实例的隐含引用
     */
    public ThisEscape(EventSource source) {
        source.registerListener(new EventListener() {
            @Override
            public void onEvent(mzc.code.objectShare.rely.Event e) {

            }
        });
    }

    void doSomething(Event e) {
    }

    interface Event {
    }
}
