package satelliittiprojekti;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    
    public static void main(String[] args) throws Exception {
        
        
        File data = new File("src/satelliittiprojekti/data1.txt");
        Scanner lukija = new Scanner(data);

        ArrayList<Satellite> satelliitit = listaaSatelliitit(lukija, 0);
        lukija.close();
       

        data = new File("src/satelliittiprojekti/signaalit1.txt");
        lukija = new Scanner(data);

        ArrayList<Satellite> signaalit = listaaSatelliitit(lukija, 1);
        listaaYhteydet(satelliitit);
        listaaMaaYhteydet(satelliitit, signaalit);
        
        
        for (Satellite s : signaalit) {
            System.out.println(s.getID() + " yhteydet: " + s.getYhteydet());
        }
        
        System.out.println("--------------------------");
        
        
        for (Satellite s : satelliitit) {
            System.out.println(s.getID() + " yhteydet: " + s.getYhteydet());
        }
    }

    public static ArrayList<Satellite> listaaSatelliitit(Scanner lukija, int r) {
        ArrayList<Satellite> satelliitit = new ArrayList<>();
        if (r == 0) {
            lukija.nextLine();
        }
        int laskuri = 0;
        while (lukija.hasNextLine() && laskuri < 20) {
            String rivi = lukija.nextLine();
            String[] osat = rivi.split(",");
            int i = 0;
            laskuri++;
            //System.out.println(osat[0]);
            satelliitit.add(luoSatelliitti(osat[0], osat[1], osat[2], osat[3]));

        }
        return satelliitit;
    }

    public static Satellite luoSatelliitti(String id, String lati, String longi, String alti) {

        double latitudeDegrees = 90 - Double.parseDouble(lati);
        double latitude = Math.toRadians(latitudeDegrees);

        double longitudeDegrees = Double.parseDouble(longi);
        double longitudeDeg = longitudeDegrees;
        if (longitudeDegrees < 0) {
            longitudeDeg = 360 + longitudeDegrees;
        }
        double longitude = Math.toRadians(longitudeDeg);
        //bugi
        double altitude = 1;
        
        if (!id.contains("Signal")) {
            altitude = Constants.EARTH_RADIUS_IN_KM + Double.parseDouble(alti);
        } else {
            altitude = Double.parseDouble(alti);
        }

        double x = Math.cos(longitude) * Math.sin(latitude) * altitude;
        double y = Math.sin(longitude) * Math.sin(latitude) * altitude;
        double z = Math.cos(latitude) * altitude;

        Satellite sat = new Satellite(id, x, y, z);
        return sat;

    }

    public static void listaaYhteydet(ArrayList<Satellite> satelliitit) {
        
        for (Satellite s : satelliitit) {
            s.yhteysMahdollinen(satelliitit);
        }
    }

    public static void listaaMaaYhteydet(ArrayList<Satellite> satelliitit, ArrayList<Satellite> signaalit) {
        for (Satellite sig : signaalit) {
            sig.yhteysSignaaliinMahdollinen(satelliitit);

        }
    }
}
