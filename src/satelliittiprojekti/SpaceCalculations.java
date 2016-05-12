package satelliittiprojekti;

import java.util.*;

public class SpaceCalculations {

    public static String getConnectionsString(SpaceObject spaceObject, List<SpaceObject> spaceObjects) {
        List<SpaceObject> connectedspaceObjects = getConnectionsFor(spaceObject, spaceObjects);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < connectedspaceObjects.size()) {
            sb.append(connectedspaceObjects.get(i).getID() + " ");
            i++;
        }
        return sb.toString();
    }

    public static List<SpaceObject> getConnectionsFor(SpaceObject spaceObject, List<SpaceObject> spaceObjects) {
        List<SpaceObject> connectedspaceObjects = new ArrayList<>();
        for (SpaceObject so : spaceObjects) {
            if (canCommunicateWith(so, spaceObject)) {
                connectedspaceObjects.add(so);
            }
        }
        return connectedspaceObjects;
    }

    public static boolean canCommunicateWith(SpaceObject so1, SpaceObject so2) {
        boolean isOtherSpaceObject = !so1.equals(so2);
        return !hasEarthBetween(so1, so2) && isOtherSpaceObject;
    }

    public static boolean hasEarthBetween(SpaceObject so1, SpaceObject so2) {
        // Lasketaan suuntavektorit:
        double s_i = so1.getX() - so2.getX();
        double s_j = so1.getY() - so2.getY();
        double s_k = so1.getZ() - so2.getZ();

        // Leikkauspisteiden yhtälö on muotoa at^2+bt+c=0. Ratkaistaan vakiot a, b, c:
        double a = s_i * s_i + s_j * s_j + s_k * s_k;
        double b = -2 * (so2.getX() * s_i + so2.getY() * s_j + so2.getZ() * s_k);
        double c = so2.getX() * so2.getX()
                + so2.getY() * so2.getY()
                + so2.getZ() * so2.getZ() - (Math.pow(Constants.EARTH_RADIUS_IN_KM, 2));

        double discriminant = b * b - 4 * a * c;

        // Jos diskriminantti <0, tiedetään varmasti ettei leikkauspisteitä ole
        if (discriminant < 0) {
            return false;
        }

        // Ratkaistaan parametrimuotoisen yhtälön parametrit:
        double t_0 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double t_1 = (-b - Math.sqrt(discriminant)) / (2 * a);

        // Leikkauspisteet: sijoitetaan t0, t1 parametrimuotoon:
        Point interceptPoint0 = getInterception(so1.getPoint(), t_0, s_i, s_j, s_k);
        Point interceptPoint1 = getInterception(so1.getPoint(), t_1, s_i, s_j, s_k);

        // Leikkauspisteiden etäisyys satelliitteihin
        double distFromThisToPoint0 = distanceBetweenPoints(so1.getPoint(), interceptPoint0);
        double distFromThisToPoint1 = distanceBetweenPoints(so1.getPoint(), interceptPoint1);

        double distFromOtherToPoint0 = distanceBetweenPoints(so2.getPoint(), interceptPoint0);
        double distFromOtherToPoint1 = distanceBetweenPoints(so2.getPoint(), interceptPoint1);

        //Jos leikkauspisteet yli janan mitan päässä jommasta kummasta
        //janan päätepisteestä, yhteys pelaa.
        double distBetweenSatellites = distanceBetweenSatellites(so1, so2);
        if (Math.max(distFromThisToPoint0, distFromThisToPoint1) > distBetweenSatellites
                || Math.max(distFromOtherToPoint0, distFromOtherToPoint1) > distBetweenSatellites) {
            return false;
        } else {
            return true;
        }
    }

    private static Point getInterception(Point p, double t, double s_i, double s_j, double s_k) {
        double x_0 = p.getX() + t * s_i;
        double y_0 = p.getY() + t * s_j;
        double z_0 = p.getZ() + t * s_k;
        return new Point(x_0, y_0, z_0);
    }

    private static double distanceBetweenSatellites(SpaceObject so1, SpaceObject so2) {
        return distanceBetweenPoints(so1.getPoint(), so2.getPoint());
    }

    private static double distanceBetweenPoints(Point p_1, Point p_2) {
        double distance = Math.sqrt(Math.pow(p_1.getX() - p_2.getX(), 2)
                + Math.pow(p_1.getY() - p_2.getY(), 2)
                + Math.pow(p_1.getZ() - p_2.getZ(), 2));
        return distance;
    }
    
   public static String printSpaceObject(SpaceObject so) {
        return so.getID() + ", [" + so.getX() + "," + so.getY() + ", " + so.getZ() + "]";
    }

}
