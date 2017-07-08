import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

/**
 * Created by stephen on 14/06/17.
 * class to create plot object
 */
public class Plot {


    double size;

   String name;
    LocalDate datePlanted;
    Plant plant;
    int noOfPlants;
    double soil;
    double environment;
    int priority;


    public Plot(String name,
                double size,
                LocalDate datePlanted,
                Plant plant,
                int number,
                double soil,
                double environment,
                int priority){

        this.name=name;
        this.size=size;
        this.datePlanted=datePlanted;
        this.plant=plant;
        this.noOfPlants=number;
        this.soil = soil;
        this.environment= environment;
        this.priority=priority;

//
//


    }

    /**
     * Method returns optimal requirment for plot object based on date
     * @param days
     * @return
     */
    public double getOptimal(int days){

        double optimalRequirement = 0.0;
        int todaysDate = (int)this.datePlanted.until(LocalDate.now(), ChronoUnit.DAYS);

        if(days >= (this.plant.getSt1_days()+this.plant.getSt2_days()+this.plant.getSt3_days())){
            optimalRequirement = this.plant.getSt4_or();

        } else if(days >= (this.plant.getSt1_days()+ this.plant.getSt2_days()) & days < (this.plant.getSt1_days()+ this.plant.getSt2_days()+ this.plant.getSt3_days())){
            optimalRequirement = this.plant.getSt3_or();
        }
        else if(days >= this.plant.getSt1_days() & days < (this.plant.getSt1_days()+ this.plant.getSt2_days())){
            optimalRequirement = this.plant.getSt2_or();
        }
        else {
            optimalRequirement = this.plant.getSt1_or();
        }

        return optimalRequirement;
    }


    /**
     * Method gets basic requirement for a plot object based on where it is within its growth cycle on a particular date
     * @param days
     * @return
     */
    public double getBasic(int days){

        double basicRequirement = 0.0;
        int todaysDate = (int)this.datePlanted.until(LocalDate.now(), ChronoUnit.DAYS);

        if(days >= (this.plant.getSt1_days()+this.plant.getSt2_days()+this.plant.getSt3_days())){
            basicRequirement = this.plant.getSt4_br();

        } else if(days >= (this.plant.getSt1_days()+ this.plant.getSt2_days()) & days < (this.plant.getSt1_days()+ this.plant.getSt2_days()+ this.plant.getSt3_days())){
            basicRequirement = this.plant.getSt3_br();
        }
        else if(days >= this.plant.getSt1_days() & days < (this.plant.getSt1_days()+ this.plant.getSt2_days())){
            basicRequirement = this.plant.getSt2_br();
        }
        else {
            basicRequirement = this.plant.getSt1_br();
        }

        return basicRequirement;
    }


    /**
     * method get the stage prioirty value for plot based on where it is in its growth stage
     * @param days
     * @return
     */
    public int getStagePriority(int days){

        int stagePriority = 1;

        if(days >= (this.plant.getSt1_days()+this.plant.getSt2_days()+this.plant.getSt3_days())){
            stagePriority=1;

        } else if(days >= (this.plant.getSt1_days()+ this.plant.getSt2_days()) & days < (this.plant.getSt1_days()+ this.plant.getSt2_days()+ this.plant.getSt3_days())){
            stagePriority = 3;
        }
        else if(days >= this.plant.getSt1_days() & days < (this.plant.getSt1_days()+ this.plant.getSt2_days())){
            stagePriority = 2;
        }
        else {
            stagePriority = 4;
        }


        return stagePriority;
    }

    /**
     * Method that returns an integer value to show priority of the plot based on stage of plot and plot priority
     * @param int days
     * @return int
     */
    public int getPlotPriorityValue(int days){
        return this.getPriority()+ this.getStagePriority(days);
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public LocalDate getDatePlanted() {
        return datePlanted;
    }

    public void setDatePlanted(LocalDate datePlanted) {
        this.datePlanted = datePlanted;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public int getNoOfPlants() {
        return noOfPlants;
    }

    public void setNoOfPlants(int noOfPlants) {
        this.noOfPlants = noOfPlants;
    }

    public double getSoil() {
        return soil;
    }

    public void setSoil(double soil) {
        this.soil = soil;
    }

    public double getEnvironment() {
        return environment;
    }

    public void setEnvironment(double environment) {
        this.environment = environment;
    }

    public int getPriority() {
        return this.priority;
    }

    public String getName() {
        return this.name;
    }

}
