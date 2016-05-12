package satelliittiprojekti;

public class Point {

    private double x;
    private double y;
    private double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point() {
        
    }

    public Point(String lati, String longi, String alti) {
        double latitudeDegrees = 90 - Double.parseDouble(lati);
        double latitude = Math.toRadians(latitudeDegrees);

        double longitudeDegrees = Double.parseDouble(longi);
        double longitudeDeg = longitudeDegrees;
        if (longitudeDegrees < 0) {
            longitudeDeg = 360 + longitudeDegrees;
        }
        double longitude = Math.toRadians(longitudeDeg);

        double altitude = Constants.EARTH_RADIUS_IN_KM + Double.parseDouble(alti);

        this.x = Math.cos(longitude) * Math.sin(latitude) * altitude;
        this.y = Math.sin(longitude) * Math.sin(latitude) * altitude;
        this.z = Math.cos(latitude) * altitude;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
