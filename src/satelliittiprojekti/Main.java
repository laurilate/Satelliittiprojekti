package satelliittiprojekti;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        PuzzleData puzzleData = FileParser.parsePuzzleData("src/satelliittiprojekti/data1.txt");

        System.out.println("Seed#: " + puzzleData.getSeed());

        System.out.println("Startpoint connections: "+SpaceCalculations.getConnectionsString(puzzleData.getStartPoint(), puzzleData.getSatellites()));
        //System.out.println(SpaceCalculations.printSpaceObject(puzzleData.getStartPoint()));
        System.out.println("Endpoint connections: "+SpaceCalculations.getConnectionsString(puzzleData.getEndPoint(), puzzleData.getSatellites()));
        //System.out.println(SpaceCalculations.printSpaceObject(puzzleData.getEndPoint()));
        System.out.println("------------------");

        for (SpaceObject satellite : puzzleData.getSatellites()) {
            System.out.println(satellite.getID() + " yhteydet: " + SpaceCalculations.getConnectionsString(satellite, puzzleData.getSatellites()));
        }

        /*
         ArrayList<Satellite> satelliitit = listaaSatelliitit(lukija, 0);
         lukija.close();
       

         data = new File("src/satelliittiprojekti/signaalit1.txt");
         lukija = new Scanner(data);

         ArrayList<Satellite> signaalit = listaaSatelliitit(lukija, 1);
        
         */
//        for (Satellite s : signaalit) {
//            System.out.println(s.getID() + " yhteydet: " + s.getConnectionsString(satelliitit));
//        }
//
//        System.out.println("--------------------------");
//
//        for (Satellite s : satelliitit) {
//            System.out.println(s.getID() + " yhteydet: " + s.getConnectionsString(satelliitit));
//        }
    }

//    public static ArrayList<Satellite> listaaSatelliitit(Scanner lukija, int r) {
//        ArrayList<Satellite> satelliitit = new ArrayList<>();
//        if (r == 0) {
//            lukija.nextLine();
//        }
//        int laskuri = 0;
//        while (lukija.hasNextLine() && laskuri < 20) {
//            String rivi = lukija.nextLine();
//            String[] osat = rivi.split(",");
//            int i = 0;
//            laskuri++;
//
//            satelliitit.add(luoSatelliitti(osat[0], osat[1], osat[2], osat[3]));
//
//        }
//        return satelliitit;
//    }
}
