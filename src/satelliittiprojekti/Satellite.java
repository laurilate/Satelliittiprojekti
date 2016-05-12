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

    public double suoranEtaisyysOrigosta(Satellite s) {
        double i = this.x - s.getX();
        double j = this.y - s.getY();
        double k = this.z - s.getZ();

        double[] matriisi = new double[3];

        matriisi[0] = (this.y * k) - (this.z * j);
        matriisi[1] = -((this.x * k) - (this.z * i));
        matriisi[2] = (this.x * j) - (this.y * i);

        double eOsoittaja = Math.sqrt(
                Math.pow(matriisi[0], 2)
                + Math.pow(matriisi[1], 2)
                + Math.pow(matriisi[2], 2));

        double eNimittaja = Math.sqrt(
                Math.pow(i, 2)
                + Math.pow(j, 2)
                + Math.pow(k, 2));

        return eOsoittaja / eNimittaja;

    }

    public Double satelliitinEtaisyys() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
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

    public double keskipisteenEtaisyys(Satellite s) {
        double x = (this.x + s.getX()) / 2;
        double y = (this.y + s.getY()) / 2;
        double z = (this.z + s.getZ()) / 2;

        double kpEtaisyys = Math.sqrt(Math.pow(x, 2)
                + Math.pow(y, 2)
                + Math.pow(z, 2));

        return kpEtaisyys;
    }

    public double lyhimmanJananPistEtaKoord(Satellite s) {
        //AB
        double ai = s.getX() - this.x;
        double aj = s.getY() - this.y;
        double ak = s.getZ() - this.z;
        //AC
        double bi = this.x;
        double bj = this.y;
        double bk = this.z;
        //AB piste AC
        double adI = ai * bi;
        double adJ = aj * bj;
        double adK = ak * bk;
        //AB piste AB
        double bdI = bi * bi;
        double bdJ = bj * bj;
        double bdK = bk * bk;

//        double I = s.getX()*adI;
//        double J = s.getY()*adJ;
//        double K = s.getZ()*adK;
        double CF = (Math.sqrt(Math.pow(adI, 2) + Math.pow(adJ, 2) + Math.pow(adK, 2))) / Math.pow(ai, 2) + Math.pow(aj, 2) + Math.pow(ak, 2);

        double DI = this.x + (ai * CF);
        double DJ = this.y + (aj * CF);
        double DK = this.z + (ak * CF);

        double etaisyys = Math.abs((bj * ak - aj * bk) - (bi * ak - ai * bk) + (bi * aj - ai * bj))
                / Math.sqrt(Math.pow(ai, 2) + Math.pow(aj, 2) + Math.pow(ak, 2));
        double etaisyys1 = Math.sqrt(Math.pow(DI, 2) + Math.pow(DJ, 2) + Math.pow(DK, 2));
        return etaisyys;
    }

    public boolean koe(Satellite s) {
        //1
        double di = s.getX() - this.x;
        double dj = s.getY() - this.y;
        double dk = s.getZ() - this.z;
        //2
        double fi = this.x;
        double fj = this.y;
        double fk = this.z;

        //plugging..
        int r = 6371;
        double a = Math.pow(di, 2) + Math.pow(dj, 2) + Math.pow(dk, 2);
        double b = 2 * (di * fi + dj * fj + dk * fk);
        double c = (Math.pow(fi, 2) + Math.pow(fj, 2) + Math.pow(fk, 2)) - (r * r);

        double discriminant = (b * b) - (4 * a * c);
        if (discriminant < 0) {
            // no intersection
        } else {
            // ray didn't totally miss sphere,
            // so there is a solution to
            // the equation.

            discriminant = Math.sqrt(discriminant);

            // either solution may be on or off the ray so need to test both
            // t1 is always the smaller value, because BOTH discriminant and
            // a are nonnegative.
            double t1 = (-b - discriminant) / (2 * a);
            double t2 = (-b + discriminant) / (2 * a);
            System.out.println(t1 + " ," + t2);

            // 3x HIT cases:
            //          -o->             --|-->  |            |  --|->
            // Impale(t1 hit,t2 hit), Poke(t1 hit,t2>1), ExitWound(t1<0, t2 hit), 
            // 3x MISS cases:
            //       ->  o                     o ->              | -> |
            // FallShort (t1>1,t2>1), Past (t1<0,t2<0), CompletelyInside(t1<0, t2>1)
            if (t1 >= 0 && t1 <= 1) {
                // t1 is the intersection, and it's closer than t2
                // (since t1 uses -b - discriminant)
                // Impale, Poke
                return true;
            }

            // here t1 didn't intersect so we are either started
            // inside the sphere or completely past it
            if (t2 >= 0 && t2 <= 1) {
                // ExitWound
                return true;
            }

            // no intn: FallShort, Past, CompletelyInside
            return false;

        }
        return false;
    }

}
