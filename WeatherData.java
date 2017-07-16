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
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by stephen on 11/07/17.
 * Class to get weather data from weather underground
 */
public class WeatherData {

    public static String createURL(String town){
        String url = "http://api.wunderground.com/api/88d7a239ed54d0d5/geolookup/conditions/forecast10day/q/UK/" + town + ".json";

        return url;
    }

    public static ArrayList<WeatherObject> getWeatherData(String town){

        String url = createURL(town);

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
                System.out.println(inputLine);

                ab.append(inputLine);


            }
            System.out.println("TEST");

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
                System.out.println(" Day " + day + " month " + month + " year " + year);

                JSONObject tempHigh = (JSONObject)((JSONObject) elem).get("high");
                String celciusHigh = tempHigh.get("celsius").toString();
                System.out.println("The high= " + celciusHigh);

                JSONObject tempLow = (JSONObject)((JSONObject) elem).get("low");
                String celciusLow = tempLow.get("celsius").toString();
                System.out.println("The low=" + celciusLow);

                JSONObject dayRain = (JSONObject)((JSONObject) elem).get("qpf_allday");
                String inchesOfRain = dayRain.get("in").toString();
                System.out.println("inches of Rain " + inchesOfRain);

                LocalDate weatherDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                int weatherHighTemp = Integer.parseInt(celciusHigh);
                int weatherLowTemp = Integer.parseInt(celciusLow);
                double weatherRainfall = Double.parseDouble(inchesOfRain);

                WeatherObject temp = new WeatherObject(weatherDate, weatherHighTemp, weatherLowTemp, weatherRainfall);
                tenDaysOfWeather.add(temp);
            }






        } catch (MalformedURLException e){

        } catch(IOException e){

        } catch (ParseException f){

        }

        return tenDaysOfWeather;

    }





    public static void main(String[] args) {
    }

}
