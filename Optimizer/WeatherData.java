package Optimizer;

import javafx.scene.control.Alert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by stephen on 11/07/17.
 * Class to get weather data from weather underground using JsonSimple methods to read from the sent JSON file
 * This is then used to create a weather object to store relevant daata
 */
public class WeatherData {

    /**
     *  Creates a url that can be used to get data from Weather Underground
     * @param town
     * @return String
     */
    public static String createURL(String town){
        String url = "http://api.wunderground.com/api/88d7a239ed54d0d5/geolookup/conditions/forecast10day/q/zmw:" + town + ".json";

        return url;
    }

    /**
     * Send http query to weather underground to get potential list of locations
     * @param part
     * @return
     */
    public static TreeMap<String, String> getPotentialLocations(String part){

        TreeMap<String, String> name = new TreeMap<>();
        Alert failtoGetLocations = new Alert(Alert.AlertType.ERROR);
        failtoGetLocations.setHeaderText("Failed to connect to location data using default ");

        try {
            String url = "http://autocomplete.wunderground.com/aq?query=" + part + "&c=GB" ;
            URL webaddress = new URL(url);
            URLConnection wc = webaddress.openConnection();

            BufferedReader in = new BufferedReader( new InputStreamReader(wc.getInputStream()));

            String inputLine;
            StringBuilder ab = new StringBuilder();
            JSONParser parser = new JSONParser();



            while((inputLine=in.readLine()) != null){
                System.out.println(inputLine);
                ab.append(inputLine);
            }


            Object obj = parser.parse(ab.toString());
            JSONObject lv1 = (JSONObject) obj;
            JSONArray potentialTown = (JSONArray) lv1.get("RESULTS");
            for(Object elem : potentialTown){

                String potentialName = ((JSONObject) elem).get("name").toString();
                String zmw = ((JSONObject) elem).get("zmw").toString();
                name.put(potentialName, zmw);
            }



        } catch (MalformedURLException e){
            failtoGetLocations.show();
            name.put("Birmingham, United Kingdon", "00000.10.03781");

        } catch(IOException f) {
            failtoGetLocations.show();
            name.put("Birmingham, United Kingdon", "00000.10.03781");




        } catch(ParseException g){
            failtoGetLocations.show();
            name.put("Birmingham, United Kingdon", "00000.10.03781");


        }

        return name;
    }

    /**
     * Method send http request to weather underground to get weather details for location of garden
     * Returns data in WeatherObject
     * Uses method contained within the JsonSimple API
     * https://code.google.com/archive/p/json-simple/
     * @param town
     * @return
     */
    public static ArrayList<WeatherObject> getWeatherData(String town){

        String url =  createURL(town);

        ArrayList<WeatherObject> tenDaysOfWeather = new ArrayList<>();


        JSONParser parser = new JSONParser();
        JSONArray a = new JSONArray();

        try {
            URL webaddress = new URL(url);
            URLConnection wc = webaddress.openConnection();


            BufferedReader in = new BufferedReader(new InputStreamReader(wc.getInputStream()));


            String inputLine;
            StringBuilder ab = new StringBuilder();

            while((inputLine = in.readLine())!=null) {
               // System.out.println(inputLine);

                ab.append(inputLine);


            }
            //System.out.println("TEST");

            Object obj = parser.parse(ab.toString());

            JSONObject lv1 = (JSONObject) obj;
            JSONObject parent = (JSONObject) lv1.get("response");
            String s = parent.get("termsofService").toString();
            System.out.println(s);
            JSONObject location = (JSONObject) lv1.get("location");
            String city = location.get("city").toString();
            System.out.println(city);
            JSONObject forecast = (JSONObject) lv1.get("forecast");
            JSONObject txt_forecast = (JSONObject) forecast.get("txt_forecast");
            String date = txt_forecast.get("date").toString();
            System.out.println(date);
            JSONObject simpleForecast = (JSONObject) forecast.get("simpleforecast");
            JSONArray foreCastDay = (JSONArray) simpleForecast.get("forecastday");

            for(Object elem : foreCastDay){

                JSONObject element = (JSONObject)((JSONObject) elem).get("date");
                String day = element.get("day").toString();
                String month = element.get("month").toString();
                String year = element.get("year").toString();
                //System.out.println(" Day " + day + " month " + month + " year " + year);

                JSONObject tempHigh = (JSONObject)((JSONObject) elem).get("high");
                String celciusHigh = tempHigh.get("celsius").toString();
               // System.out.println("The high= " + celciusHigh);

                JSONObject tempLow = (JSONObject)((JSONObject) elem).get("low");
                String celciusLow = tempLow.get("celsius").toString();
                //System.out.println("The low=" + celciusLow);

                JSONObject dayRain = (JSONObject)((JSONObject) elem).get("qpf_allday");
                String inchesOfRain = dayRain.get("in").toString();
                //System.out.println("inches of Rain " + inchesOfRain);

                LocalDate weatherDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                int weatherHighTemp = Integer.parseInt(celciusHigh);
                int weatherLowTemp = Integer.parseInt(celciusLow);
                double weatherRainfall = Double.parseDouble(inchesOfRain);

                WeatherObject temp = new WeatherObject(weatherDate, weatherHighTemp, weatherLowTemp, weatherRainfall);
                tenDaysOfWeather.add(temp);
            }






        } catch (MalformedURLException e){

        } catch(IOException e){

            System.out.println("Could not get weather Data ");

        } catch (ParseException f){

        }

        return tenDaysOfWeather;

    }







    public static void main(String[] args) {

        Garden testGarden = Database.createGarden(2, true);

        System.out.println("*" + testGarden.getLocation() + "*");

        ArrayList<WeatherObject> test = getWeatherData(testGarden.getLocation().trim());

        for(WeatherObject a : test){
            System.out.println(a.getHighTemp());
        }

       // TreeMap test2 = getPotentialLocations("ches");
    }

}
