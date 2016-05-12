package satelliittiprojekti;

import java.util.*;

public class EarthBase implements SpaceObject {

    private final String id;
    private final Point point;

    public EarthBase(String id, double x, double y, double z) {
        this.id = id;
        this.point = new Point(x, y, z);
    }

    public EarthBase(String id, Point point) {
        this.id = id;
        this.point = point;
    }
    
    public EarthBase() {
        this.id = "";
        this.point = new Point();
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public Point getPoint() {
        return this.point;
    }

    @Override
    public double getX() {
        return this.point.getX();
    }

    @Override
    public double getY() {
        return this.point.getY();
    }

    @Override
    public double getZ() {
        return this.point.getZ();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Satellite other = (Satellite) obj;
        if (!Objects.equals(this.id, other.getID())) {
            return false;
        }
        return true;
    }
}
