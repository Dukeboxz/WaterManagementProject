package Optimizer;

import java.time.LocalDate;

/**
 * Created by stephen on 16/07/17.
 */
public class WeatherObject {

    private LocalDate theDate;
    private int highTemp, lowTemp;
    double rainInInches;

    public WeatherObject(LocalDate date,
                         int highTemperature,
                         int lowTemperature,
                         double inchesOfRain){

        this.theDate=date;
        this.highTemp= highTemperature;
        this.lowTemp=lowTemperature;
        this.rainInInches=inchesOfRain;
    }

    public LocalDate getTheDate(){
        return this.theDate;
    }

    public int getHighTemp() {
        return this.highTemp;
    }

    public int getLowTemp() {
        return this.lowTemp;
    }

    public double getRainInInches(){
        return this.rainInInches;
    }
}
