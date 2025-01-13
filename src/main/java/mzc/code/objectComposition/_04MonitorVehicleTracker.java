package mzc.code.objectComposition;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * page:52/53
 * 本例演示了一个基于Java监视器模式实现的 “车辆追踪器”
 */
@ThreadSafe
public class _04MonitorVehicleTracker {

    @GuardedBy("this")
    private final Map<String, MutablePoint> locations;

    public static void main(String[] args) {

    }

    public _04MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations(){
        return deepCopy(locations);
    }

    public synchronized MutablePoint getLocation(String id) {
        MutablePoint mutablePoint = locations.get(id);
        return mutablePoint == null ? null : new MutablePoint(mutablePoint);
    }

    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint mutablePoint = locations.get(id);
        if(mutablePoint == null)
            throw new IllegalArgumentException("No such ID：" + id);
        mutablePoint.x = x;
        mutablePoint.y = y;
    }

    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> mutablePointMap) {
        Map<String, MutablePoint> result = new HashMap<>();
        for (String id : mutablePointMap.keySet()) {
            result.put(id, new MutablePoint(mutablePointMap.get(id)));
        }
        return Collections.unmodifiableMap(result);
    }

}

/**
 * 用于表示车辆位置的类，虽然 MutablePoint 不是线程安全的，但追踪器类是线程安全的，它所包含的 Map 对象和可变的 Point 对象都未曾发布
 * 因此也不存在线程安全的问题
 */
@NotThreadSafe
class MutablePoint {
    public int x, y;

    public MutablePoint() {
        x = 0;
        y = 0;
    }

    public MutablePoint(MutablePoint p) {
        this.x = p.x;
        this.y = p.y;
    }
}
