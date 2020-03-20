/**
 *
 *  @author Wałachowski Marcin S19541
 *
 */

package zad1;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Service {
    String country;
    String currencyCode;
    final String API_KEY = "f0e8fb94dc137ead3773d33d7c13c111";
    public Service(String country){
        this.country=country;
        currencyCode = Currency.getInstance(new Locale("", getCountryCodeFromName(country))).getCurrencyCode();
    }
    static String getCountryCodeFromName(String countryName){
        Map<String, String> countries = new HashMap<>();
        try{
            BufferedReader bf=new BufferedReader(new FileReader("data\\countries.txt"));
            for (String line = bf.readLine(); line != null; line = bf.readLine()) {
                String[] splitResult = line.split(",");
                countries.put(splitResult[0].toLowerCase(), splitResult[1].toLowerCase());
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return countries.get(countryName.toLowerCase());
    }
    String getStringFromUrl(String urlString){
        try {
            URL url = new URL(urlString);
            String cont = "";
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream(), "UTF-8"))) {
                String line;
                while ((line = in.readLine()) != null) cont += line + "\n";
            }
            return cont;
        }catch(IOException e){
            e.getMessage();
        }
        return "Problem z wczytaniem napisu z URL";
    }
    String getWeather(String city){
        String result = getStringFromUrl(

                "http://api.openweathermap.org/data/2.5/weather?q=" + city.toLowerCase() + ","
                        + city
                        + "&appid=" + API_KEY
        );
        return result;
    }
    Double getRateFor(String currency){
        String result = getStringFromUrl("https://api.exchangeratesapi.io/latest?base=" + currency);
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(result);
            JSONObject jsonObj = (JSONObject) obj;
            JSONObject rates = (JSONObject) jsonObj.get("rates");
            return (double) rates.get(currencyCode);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    Double getNBPRate(){
        if(currencyCode.equals("PLN"))
            return 1.0;
            try {
                Document docA = Jsoup.connect("http://www.nbp.pl/kursy/kursya.html").get();
                Document docB = Jsoup.connect("http://www.nbp.pl/kursy/kursyb.html").get();
                Elements element = docA.getElementsContainingOwnText(currencyCode).next();
                if(element.size()>=1)
                    return Double.parseDouble(element.get(element.size()-1).ownText().replace(',','.'));

                element = docB.getElementsContainingOwnText(currencyCode).next();
                if(element.size()>=1)
                    Double.parseDouble(element.get(element.size() - 1).ownText().replace(',', '.'));


                return 0.0;
            }catch(Exception e){
                e.printStackTrace();
            }

        return 0.0;
    }
}  
