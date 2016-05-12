package satelliittiprojekti;

import java.util.List;

class PuzzleData {

    private final String seed;
    private final List<SpaceObject> satellites;
    private final EarthBase startPoint;
    private final EarthBase endPoint;

    public PuzzleData(String seed, List<SpaceObject> satellites, EarthBase startPoint, EarthBase endPoint) {
        this.seed = seed;
        this.satellites = satellites;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public String getSeed() {
        return seed;
    }

    public List<SpaceObject> getSatellites() {
        return satellites;
    }

    public EarthBase getStartPoint() {
        return startPoint;
    }

    public EarthBase getEndPoint() {
        return endPoint;
    }

}
