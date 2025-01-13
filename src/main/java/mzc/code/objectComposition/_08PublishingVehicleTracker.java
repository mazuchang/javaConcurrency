package mzc.code.objectComposition;

import net.jcip.annotations.GuardedBy;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * page:59
 * 在 _04MonitorVehicleTracker 中我们利用线程封闭的思想实现了一个简单的“车辆追踪器”，由于其中保存车辆位置信息的 MutablePoint 类
 * 并不是线程安全的，因此当需要返回车辆位置信息时，需要通过 MutablePoint 拷贝构造函数或调用 deepCopy 方法，由此就会带来一些性能上的问题
 * 在本例中我们通过一个可变且线程安全的 SafePoint，就可以实现“实时返回”
 */
public class _08PublishingVehicleTracker {

    private final Map<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiableMap;

    public _08PublishingVehicleTracker(Map<String, SafePoint> locations) {
        this.locations = new ConcurrentHashMap<>(locations);
        this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
    }

    public Map<String, SafePoint> getLocations() {
        return unmodifiableMap;
    }

    public SafePoint getLocations(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (!locations.containsKey(id))
            throw new IllegalArgumentException("invalid vehicle name：" + id);
        locations.get(id).set(x, y);
    }

}

class SafePoint {

    @GuardedBy("this")
    private int x, y;

    public SafePoint(int[] a) {
        this(a[0], a[1]);
    }

    public SafePoint(SafePoint p) {
        this(p.get());
    }

    public SafePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public synchronized int[] get() {
        return new int[]{x, y};
    }

    public synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}