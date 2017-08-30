package Optimizer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class WeatherDataTest {

    @Test
    public void test1() {

        String expected = "http://api.wunderground.com/api/88d7a239ed54d0d5/geolookup/conditions/forecast10day/q/zmw:Chesterfield.json";

        assertEquals(expected, WeatherData.createURL("Chesterfield"));

    }

    @Test
    public void test2() {

        //TreeMap<String, String> location = WeatherData.getPotentialLocations("Che");




    }

    @Test
    public void test3() {
        ArrayList<WeatherObject> theWeather = WeatherData.getWeatherData("00000.54.03534");

        assert(theWeather.size() >0);

        for(WeatherObject o : theWeather){
            assert(o.rainInInches >=0);
            assert(o.getHighTemp() > o.getLowTemp());
        }
    }

}