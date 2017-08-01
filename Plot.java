import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

/**
 * Created by stephen on 14/06/17.
 * class to create plot object
 */
public class Plot implements  Comparable<Plot>{


    int id;
    double size;

   String name;
    LocalDate datePlanted;
    Plant plant;
    int noOfPlants;
    double soil;
    double environment;
    double priority;


    public Plot(int id,
                String name,
                double size,
                LocalDate datePlanted,
                Plant plant,
                int number,
                double soil,
                double environment,
                double priority){

        this.id=id;
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

    public int compareTo(Plot comparePlot){

      return this.getName().compareTo(comparePlot.getName());
    }

    /**
     * Method returns optimal requirment for plot object based on date
     * @param days
     * @return
     */
    public double getOptimal(int days, LocalDate date){

        LocalDate planted = this.datePlanted;

        int noDays = (int)planted.until(date.plusDays(days), ChronoUnit.DAYS);




        double optimalRequirement = 0.0;


        if(noDays >= (this.plant.getSt1_days()+this.plant.getSt2_days()+this.plant.getSt3_days()) & noDays <= this.plant.getSt1_days()+this.plant.getSt2_days() + this.plant.getSt3_days() + this.plant.getSt4_days()){
            optimalRequirement = this.plant.getSt4_or();

        } else if(noDays >= (this.plant.getSt1_days()+ this.plant.getSt2_days()) & noDays < (this.plant.getSt1_days()+ this.plant.getSt2_days()+ this.plant.getSt3_days())){
            optimalRequirement = this.plant.getSt3_or();
        }
        else if(noDays >= this.plant.getSt1_days() & noDays < (this.plant.getSt1_days()+ this.plant.getSt2_days())){
            optimalRequirement = this.plant.getSt2_or();
        }
        else if (noDays >=0 & noDays < this.plant.getSt1_days()){
            optimalRequirement = this.plant.getSt1_or();
        } else {
            optimalRequirement=0;
        }

        return optimalRequirement;
    }


    /**
     * Method gets basic requirement for a plot object based on where it is within its growth cycle on a particular date
     * @param days
     * @return
     */
    public double getBasic(int days, LocalDate date){

        double basicRequirement = 0.0;
        LocalDate planted = this.datePlanted;

        int noDays = (int)planted.until(date.plusDays(days), ChronoUnit.DAYS);

        if(noDays >= (this.plant.getSt1_days()+this.plant.getSt2_days()+this.plant.getSt3_days()) &  noDays <= this.plant.getSt1_days() + this.plant.st2_days + this.plant.st3_days + this.plant.st4_days){
            basicRequirement = this.plant.getSt4_br();

        } else if(noDays >= (this.plant.getSt1_days()+ this.plant.getSt2_days()) & noDays < (this.plant.getSt1_days()+ this.plant.getSt2_days()+ this.plant.getSt3_days())){
            basicRequirement = this.plant.getSt3_br();
        }
        else if(noDays >= this.plant.getSt1_days() & noDays < (this.plant.getSt1_days()+ this.plant.getSt2_days())){
            basicRequirement = this.plant.getSt2_br();
        }
        else if(noDays >= 0 & noDays <this.plant.getSt1_days()) {
            basicRequirement = this.plant.getSt1_br();
        }

        return basicRequirement;
    }


    /**
     * method get the stage prioirty value for plot based on where it is in its growth stage
     * @param days
     * @return
     */
    public int getStagePriority(int days, LocalDate date){

        int stagePriority = 0;
        LocalDate planted = this.datePlanted;

        int noDays = (int)planted.until(date.plusDays(days), ChronoUnit.DAYS);

        if(noDays >= (this.plant.getSt1_days()+this.plant.getSt2_days()+this.plant.getSt3_days()) & noDays <= (this.plant.getSt1_days() + this.plant.getSt2_days() + this.plant.getSt3_days() + this.plant.getSt4_days())){
            stagePriority=1;

        } else if(noDays >= (this.plant.getSt1_days()+ this.plant.getSt2_days()) & noDays < (this.plant.getSt1_days()+ this.plant.getSt2_days()+ this.plant.getSt3_days())){
            stagePriority = 3;
        }
        else if(noDays >= this.plant.getSt1_days() & noDays < (this.plant.getSt1_days()+ this.plant.getSt2_days())){
            stagePriority = 2;
        }
        else  if(noDays >= 0 & noDays< this.plant.getSt1_days()){
            stagePriority = 4;
        } else{
            stagePriority = 0;
        }


        return stagePriority;
    }

    /**
     * Method that returns an integer value to show priority of the plot based on stage of plot and plot priority
     * @param days
     * @return int
     */
    public double getPlotPriorityValue(int days, LocalDate date){

        double stagePrioirty = this.getStagePriority(days, date);
        double plotPriority;
        if(stagePrioirty==0){
            plotPriority=0;

        } else {
            plotPriority=this.getPriority();
        }
        return stagePrioirty + plotPriority;
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

    public double getPriority() {
        return this.priority;
    }

    public void setPriority(double priorityvalue){
        this.priority=priorityvalue;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName){
        this.name=newName;
    }

    public int getId() {
        return this.id;
    }





    public static void main(String[] args){

        try {
            Plot a = Database.createPlot("Radish 2", 12, 7, 2017);

            System.out.println(a.getName());
            System.out.println(a.getPlant().getName());




        } catch(SQLException e){

        }
    }

}
