package mzc.code.objectShare;

import mzc.code.objectShare.rely.Event;
import mzc.code.objectShare.rely.EventListener;
import mzc.code.objectShare.rely.EventSource;

/**
 * page:34
 * 本例演示了该如何正确发布对象
 */
public class _04SafeListener {
    private final EventListener listener;

    private _04SafeListener() {
        listener = new EventListener() {
            public void onEvent(Event e) {
                doSomething(e);
            }
        };
    }

    public static _04SafeListener newInstance(EventSource source) {
        _04SafeListener safe = new _04SafeListener();
        source.registerListener(safe.listener);
        return safe;
    }

    void doSomething(Event e) {
    }


}
