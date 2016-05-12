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

    /*public String getConnectionsString(List<Satellite> satellites);

     public List<Satellite> getConnectionsFor(List<Satellite> satellites);
     public boolean canCommunicateWith(Satellite otherSatellite);

     public boolean hasEarthBetween(Satellite otherSatellite);

    

     @Override
     public int hashCode();

     @Override
     public boolean equals(Object obj);

    
     @Override
     public String toString();*/
}
