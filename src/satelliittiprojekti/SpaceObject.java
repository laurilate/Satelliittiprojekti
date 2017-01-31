package satelliittiprojekti;

public interface SpaceObject {

    public double getX();

    public double getY();

    public double getZ();

    public String getID();

    public Point getPoint();

    @Override
    public int hashCode();

    @Override
    public boolean equals(Object obj);

}
