package mzc.code.objectComposition;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * page:54
 * 本例中我们将状态通过一个 final 的 ConcurrentHashMap 来存储，并通过一个不可变的 Point 类来代替 _04MonitorVehicleTracker 中的 MutablePoint
 * 如此一来可以解决 _04MonitorVehicleTracker 中每次 getLocation 就要复制一份数据导致数据不一致的情况
 */
@ThreadSafe
public class _05DelegatingVehicleTracker {

    private final ConcurrentMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;

    public _05DelegatingVehicleTracker(ConcurrentMap<String, Point> locations) {
        this.locations = new ConcurrentHashMap<String, Point>(locations);
        this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
    }

    public Map<String, Point> getLocations() {
        return this.unmodifiableMap;
    }

    public Point getLocation(String id) {
        return this.locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (this.locations.replace(id, new Point(x, y)) == null)
            throw new IllegalArgumentException("invalid vehicle name:" + id);
    }


}

@Immutable
class Point {

    public final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
