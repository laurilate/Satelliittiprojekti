package satelliittiprojekti;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileParser {

    public static PuzzleData parsePuzzleData(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        ArrayList<SpaceObject> satellites = new ArrayList<>();
        String seed = "";
        EarthBase startPoint = new EarthBase();
        EarthBase endPoint = new EarthBase();
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            if (row.contains("#")) {
                String[] cols = row.split(":");
                seed = cols[1];
            } else {
                String[] cols = row.split(",");
                String rowHeader = cols[0];
                if (rowHeader.equals("ROUTE")) {
                    startPoint = new EarthBase("Start point", new Point(cols[1], cols[2], "0.002"));
                    endPoint = new EarthBase("End point", new Point(cols[3], cols[4], "0.002"));
                } else {
                    Point point = new Point(cols[1], cols[2], cols[3]);
                    Satellite satellite = new Satellite(cols[0], point);
                    satellites.add(satellite);
                }
            }
        }
        return new PuzzleData(seed, satellites, startPoint, endPoint);
    }
}
