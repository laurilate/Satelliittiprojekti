package satelliittiprojekti;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        PuzzleData puzzleData = FileParser.parsePuzzleData("src/satelliittiprojekti/data1.txt");

        System.out.println("Seed#: " + puzzleData.getSeed());

        System.out.println("Startpoint connections: "+SpaceCalculations.getConnectionsString(puzzleData.getStartPoint(), puzzleData.getSatellites()));
        System.out.println("Endpoint connections: "+SpaceCalculations.getConnectionsString(puzzleData.getEndPoint(), puzzleData.getSatellites()));
        System.out.println("------------------");

        for (SpaceObject satellite : puzzleData.getSatellites()) {
            System.out.println(satellite.getID() + " connections: " + SpaceCalculations.getConnectionsString(satellite, puzzleData.getSatellites()));
        }

    }


}
