package satelliittiprojekti;

import java.util.*;

public class Satellite {

    private String id;
    private double x;
    private double y;
    private double z;
    private List<Satellite> yhteydet;

    public Satellite(String id, double x, double y, double z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yhteydet = new ArrayList<>();
    }

    public String getID() {
        return this.id;
    }

    public Double getX() {
        return this.x;
    }

    public Double getY() {
        return this.y;
    }

    public Double getZ() {
        return this.z;
    }

    public String getYhteydet() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < this.yhteydet.size()) {
            sb.append(this.yhteydet.get(i).getID() + " ");
            i++;
        }
        return "" + sb;
        
    }

    @Override
    public String toString() {
        return this.id + ", [" + this.x + "," + this.y + ", " + this.z + "]";
    }

    public void yhteysMahdollinen(List<Satellite> satelliitit) {

        for (Satellite s : satelliitit) {
            if (!hasEarthBetween(s) && !this.yhteydet.contains(s) && !this.getID().equals(s.getID())) {
                this.yhteydet.add(s);
            }
        }
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public void yhteysSignaaliinMahdollinen(List<Satellite> satelliitit) {
        for (Satellite s : satelliitit) {
            // System.out.println(keskipisteenEtaisyys(s));
            if (!hasEarthBetween(s) && !this.yhteydet.contains(s) && !this.equals(s)) {
                this.yhteydet.add(s);
            }
        }
    }

    public boolean canCommunicateWith(Satellite otherSatellite) {
        return !hasEarthBetween(otherSatellite);
        // Satelliitit leikkaava suora: ParametrisizedLine getLine(Satellite otherSatellite)
        // diskrimanantti
        // jos d <0 return true
        // else laske leikkauspisteet maan kanssa
        // jos leikkauspistet välissä --> false
        // else true
    }

    public boolean hasEarthBetween(Satellite otherSatellite) {
        // Lasketaan suuntavektorit:
        double s_i = this.x - otherSatellite.getX();
        double s_j = this.y - otherSatellite.getY();
        double s_k = this.z - otherSatellite.getZ();

        // Leikkauspisteiden yhtälö on muotoa at^2+bt+c=0. Ratkaistaan vakiot a, b, c:
        double a = s_i * s_i + s_j * s_j + s_k * s_k;
        double b = -2 * (otherSatellite.getX() * s_i + otherSatellite.getY() * s_j + otherSatellite.getZ() * s_k);
        double c = otherSatellite.getX() * otherSatellite.getX()
                + otherSatellite.getY() * otherSatellite.getY()
                + otherSatellite.getZ() * otherSatellite.getZ() - (Math.pow(Constants.EARTH_RADIUS_IN_KM, 2));

        double discriminant = b * b - 4 * a * c;

        // Jos diskriminantti <0, tiedetään varmasti ettei leikkauspisteitä ole
        if (discriminant < 0) {
            return false;
        }
        
        // Ratkaistaan parametrimuotoisen yhtälön parametrit:
        double t_0 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double t_1 = (-b - Math.sqrt(discriminant)) / (2 * a);
        
        // Leikkauspisteet: sijoitetaan t0, t1 parametrimuotoon:
        double x_0 = this.x + t_0 * s_i;
        double y_0 = this.y + t_0 * s_j;
        double z_0 = this.z + t_0 * s_k;

        double x_1 = this.x + t_1 * s_i;
        double y_1 = this.y + t_1 * s_j;
        double z_1 = this.z + t_1 * s_k;

        // leikkauspisteiden etäisyys janan päätepisteisiin
        double distanceToPoint1 = distanceToCoordinate(x_0, y_0, z_0);
        double d0this = Math.sqrt(Math.pow(this.x - x_0, 2)
                + Math.pow(this.y - y_0, 2)
                + Math.pow(this.z - z_0, 2));
        double d1this = Math.sqrt(Math.pow(this.x - x_1, 2)
                + Math.pow(this.y - y_1, 2)
                + Math.pow(this.z - z_1, 2));

        double d0s = Math.sqrt(Math.pow(otherSatellite.getX() - x_0, 2)
                + Math.pow(otherSatellite.getY() - y_0, 2)
                + Math.pow(otherSatellite.getZ() - z_0, 2));
        double d1s = Math.sqrt(Math.pow(otherSatellite.getX() - x_1, 2)
                + Math.pow(otherSatellite.getY() - y_1, 2)
                + Math.pow(otherSatellite.getZ() - z_1, 2));

            //Jos leikkauspisteet yli janan mitan päässä jommasta kummasta
        //janan päätepisteestä, yhteys pelaa.
        double dJana = distanceTo(otherSatellite);
        //System.out.println("Djana: "+dJana+" d0t: "+d0this+" d1t: "+d1this+" d0s: "+d0s+" d1s: "+d1s);
        if (Math.max(d0this, d1this) > dJana || Math.max(d0s, d1s) > dJana) {
            return false;
        }

        return true;
    }

    public double distanceTo(Satellite otherSatellite) {
        return distanceToCoordinate(otherSatellite.getX(),otherSatellite.getY(), otherSatellite.getZ());
//        double distance = Math.sqrt(Math.pow(this.x - otherSatellite.getX(), 2)
//                + Math.pow(this.y - otherSatellite.getY(), 2)
//                + Math.pow(this.z - otherSatellite.getZ(), 2));
//        return distance;
    }
    
    public double distanceToCoordinate(double x, double y, double z) {
        double distance = Math.sqrt(Math.pow(this.x - x, 2)
                + Math.pow(this.y - y, 2)
                + Math.pow(this.z - z, 2));
        return distance;
    }

}
