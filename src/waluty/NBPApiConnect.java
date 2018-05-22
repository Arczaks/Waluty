/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package waluty;

import java.io.IOException;
import java.io.InputStream;
import static java.lang.Math.sqrt;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author Archax
 */
public class NBPApiConnect {
    //dostępne kody walut dla tabel kursów kupna i sprzedaży
    public static String[] a = { "AUD", "CAD", "CHF", "CZK", "DKK", "EUR", "GBP", "HUF", "JPY", "NOK", "SEK", "USD" };
        
    private final ArrayList<String> dostepneKody;
    private String code;
    private String startDate;
    private String endDate;
    private final ArrayList<Sample> probki;
    
    public NBPApiConnect(){
        dostepneKody = new ArrayList(Arrays.asList(a));
        code = "";
        startDate = "";
        endDate = "";
        probki = new ArrayList();
    }

    // załadowanie parametrów zapytania
    public void loadParameters(){
        System.out.println("Start");
        try (Scanner scr = new Scanner (System.in)){
            while (!dostepneKody.contains(code)){
                System.out.println("Podaj kod waluty");
                code = scr.next();
            }
            startDate = loadDate(scr, "początkowa");
            endDate = loadDate(scr, "końcowa");
        }
    }
    
    // wczytanie daty
    private String loadDate(Scanner sc, String typ){
        String date = "";
        
        String dateFormat = "^((?:19|20)\\d\\d)[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])";
        while (!date.matches(dateFormat)){
            System.out.println("Podaj datę " + typ);
            date = sc.next();
        }
        return date;
    }
    
    private String getURL(){
       String url = "";
       if (!(code.equals("") && startDate.equals("") & endDate.equals(""))){
           url += "http://api.nbp.pl/api/exchangerates/rates/c/" 
                   + code + "/" 
                   + startDate + "/" 
                   + endDate + "/";
       } else {
           url = null;
       }
       return url;
    }

    public void sendRequest(){
        String url = getURL();
        //url = "http://api.nbp.pl/api/exchangerates/rates/c/USD/2018-05-10/2018-05-11/";
        if (url != null){
            try (InputStream is = new URL(url).openStream()) {
                JSONObject json = new JSONObject(new Scanner(is).useDelimiter("\\A").next());
                parseJSON(json);
            } catch (MalformedURLException ex) {
                Logger.getLogger(NBPApiConnect.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | JSONException ex) {
                Logger.getLogger(NBPApiConnect.class.getName()).log(Level.SEVERE, null, ex);
            } 
        } else {
            System.out.println("error");
        }
    }
    
    private void parseJSON(JSONObject json) throws JSONException{
        JSONArray jsonArray = json.getJSONArray("rates");
    //    System.out.println(jsonArray.toString() + " " + jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++){
            double kupno = jsonArray.getJSONObject(i).getDouble("bid");
            double sprzedaz = jsonArray.getJSONObject(i).getDouble("ask");
            probki.add(new Sample(kupno, sprzedaz));
        }
    }
    
    public void showResults(){
        showAverage();
        showStandardDeviation();
    }
    
    private void showAverage(){
        double d = 0.0;
        for (int i = 0; i < probki.size(); i++){
            d += probki.get(i).getKupno();
        }
        d /= probki.size();
        System.out.print(d + " ");
    }
    
    private void showStandardDeviation(){
        double d = 0.0;
        for (int i = 0; i < probki.size(); i++){
            d += probki.get(i).getSprzedaz();
        }
        d /= probki.size(); 
        double w = 0.0;
        for (int i = 0; i < probki.size(); i++){
            w += (probki.get(i).getSprzedaz() - d) * (probki.get(i).getSprzedaz() - d);
        }
        w /= probki.size();
        w = sqrt(w);
        System.out.print(w + " ");
    }
}
