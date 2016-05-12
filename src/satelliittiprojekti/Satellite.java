package satelliittiprojekti;

import java.util.*;

public class Satellite {

    private String id;
    private final Point point;

    private List<Satellite> yhteydet;

    public Satellite(String id, double x, double y, double z) {
        this.id = id;
        this.point = new Point(x, y, z);

        this.yhteydet = new ArrayList<>();

    }

    public String getID() {
        return this.id;
    }

    public Point getPoint() {
        return this.point;
    }

    public Double getX() {
        return this.point.getX();
    }

    public Double getY() {
        return this.point.getY();
    }

    public Double getZ() {
        return this.point.getZ();
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
        return this.id + ", [" + this.getX() + "," + this.getY() + ", " + this.getZ() + "]";
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
        boolean isOtherSatellite = !this.equals(otherSatellite);
        return !hasEarthBetween(otherSatellite) && isOtherSatellite;
    }

    public boolean hasEarthBetween(Satellite otherSatellite) {
        // Lasketaan suuntavektorit:
        double s_i = this.getX() - otherSatellite.getX();
        double s_j = this.getY() - otherSatellite.getY();
        double s_k = this.getZ() - otherSatellite.getZ();

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
        Point interceptPoint0 = getInterception(this.point, t_0, s_i, s_j, s_k);
        Point interceptPoint1 = getInterception(this.point, t_1, s_i, s_j, s_k);

        // Leikkauspisteiden etäisyys satelliitteihin
        double distFromThisToPoint0 = distanceBetweenPoints(this.point, interceptPoint0);
        double distFromThisToPoint1 = distanceBetweenPoints(this.point, interceptPoint1);

        double distFromOtherToPoint0 = distanceBetweenPoints(otherSatellite.point, interceptPoint0);
        double distFromOtherToPoint1 = distanceBetweenPoints(otherSatellite.point, interceptPoint1);

        //Jos leikkauspisteet yli janan mitan päässä jommasta kummasta
        //janan päätepisteestä, yhteys pelaa.
        double distBetweenSatellites = distanceBetweenSatellites(this, otherSatellite);

        if (Math.max(distFromThisToPoint0, distFromThisToPoint1) > distBetweenSatellites
                || Math.max(distFromOtherToPoint0, distFromOtherToPoint1) > distBetweenSatellites) {
            return false;
        }
        return true;
    }

    private Point getInterception(Point p, double t, double s_i, double s_j, double s_k) {
        double x_0 = p.getX() + t * s_i;
        double y_0 = p.getY() + t * s_j;
        double z_0 = p.getZ() + t * s_k;
        return new Point(x_0, y_0, z_0);
    }

    private static double distanceBetweenSatellites(Satellite sat1, Satellite sat2) {
        return distanceBetweenPoints(sat1.point, sat2.point);
    }

    private static double distanceBetweenPoints(Point p_1, Point p_2) {
        double distance = Math.sqrt(Math.pow(p_1.getX() - p_2.getX(), 2)
                + Math.pow(p_1.getY() - p_2.getY(), 2)
                + Math.pow(p_1.getZ() - p_2.getZ(), 2));
        return distance;
    }

}
