package Optimizer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;


/**
 * Created by stephen on 19/06/17.
 * Class that creates and then solves the optimisation problem.
 * Calls the MatLabFunction class to access MatLabs Linprog functinality to provide solution to problem
 */
public class Optimiser  extends Observable{

    private Garden garden;
    public static int days;
    private double waterAvailable;
    LocalDate dateSelected;
    boolean withWeather;

    Map<String, ArrayList<Double>> optimalMap;
    Map<String, ArrayList<Double>> basicMap;




    public Optimiser(Garden garden,
                     int days,
                     double water,
                     LocalDate selectedDate,
                     boolean withWeather
                     ) {

        this.garden = garden;
        this.days = days;
        this.waterAvailable = water;
        this.dateSelected = selectedDate;
        this.withWeather = withWeather;
        this.optimalMap = createOptimalMap(this.withWeather);
        this.basicMap = createBasicMap(this.withWeather);

    }


    //Getters and Setters
    public Garden getGarden() {
        return this.garden;
    }

    public int getDays() {
        return this.days;
    }

    public LocalDate getDateSelected() {
        return this.dateSelected;
    }

    public double getWaterAvailable() {
        return this.waterAvailable;
    }

    public Map<String, ArrayList<Double>> getOptimalMap() {
        return this.optimalMap;
    }

    public Map<String, ArrayList<Double>> getBasicMap() {
        return this.basicMap;
    }

    public boolean getWithWeather(){
        return this.withWeather;
    }



    public void setGarden(Garden garden) {
        this.garden = garden;
        setChanged();
        notifyObservers();
    }

    public static void setDays(int days) {
        Optimiser.days = days;
    }

    public void setWaterAvailable(double waterAvailable) {
        this.waterAvailable = waterAvailable;
    }

    public void setDateSelected(LocalDate dateSelected) {
        this.dateSelected = dateSelected;
    }

    public void setWithWeather(boolean withWeather) {
        this.withWeather = withWeather;
    }



    public void setOptimalMap(Map<String, ArrayList<Double>> optimalMap) {
        this.optimalMap = optimalMap;
    }

    public void setBasicMap(Map<String, ArrayList<Double>> basicMap) {
        this.basicMap = basicMap;
    }

    /**
     * Method create a matrix showing optimal water requirments for each plot on a day to day basis
     *
     * @return double[][]
     */
    public double[][] createOptimalMatrix() {
        Map<String, ArrayList<Double>> theOptimalMap = this.getOptimalMap();
        double[][] matrix = new double[this.garden.getPlots().size()][this.getDays()];
        double total = 0.0;

        int rowCounter = 0;
        for (Map.Entry<String, ArrayList<Double>> entry : theOptimalMap.entrySet()) {
            ArrayList<Double> temp = entry.getValue();
            for (int i = 0; i < temp.size(); i++) {
                matrix[rowCounter][i] = temp.get(i);
            }
            rowCounter++;
        }


        // System.out.println("OPTIMAL total sum= " + total);
        return matrix;
    }


    /**
     * Create optimal matrix using Map data structure
      * @param withWeather
     * @return
     */
    public Map<String, ArrayList<Double>> createOptimalMap(boolean withWeather) {


        Map<String, ArrayList<Double>> theMap = new TreeMap<>();


        if (withWeather == false) {

            for (int i = 0; i < this.getGarden().getPlots().size(); i++) {
                Plot a = this.garden.getPlots().get(i);
                String theName = a.getName();
                ArrayList<Double> theArrayList = new ArrayList<>();
                double soil = a.getSoil();
                double environment = a.getEnvironment();
                int numPlants = a.getNoOfPlants();
                for (int j = 0; j < this.getDays(); j++) {

                    double dayOptimalRequirment = (a.getOptimal(j + 1, this.dateSelected) * (double) numPlants) * soil * environment;
                    theArrayList.add(dayOptimalRequirment);
                }

                theMap.put(theName, theArrayList);
            }
        } else {
            ArrayList<WeatherObject> weather = WeatherData.getWeatherData(this.getGarden().getLocation().trim());
            for (int i = 0; i < this.getGarden().getPlots().size(); i++) {
                Plot a = this.garden.getPlots().get(i);
                String theName = a.getName();
                ArrayList<Double> theArrayList = new ArrayList<>();
                double soil = a.getSoil();
                double environment = a.getEnvironment();
                int numPlants = a.getNoOfPlants();
                for (int j = 0; j < this.getDays(); j++) {

                    double tempValue = 1;

                    if (j < 10 & j < this.getDays()) {
                        int temp = weather.get(j).getHighTemp();
                        if (temp > 20 & temp <= 25) {
                            tempValue = 1.05;
                        } else if (temp > 25 & temp < 30) {
                            tempValue = 1.10;
                        } else if (temp >= 30) {
                            tempValue = 1.15;
                        } else {
                            tempValue = 1;
                        }
                    }
                    double optimalPerPlant;
                    if (j < 10 & j < this.getDays()) {
                        optimalPerPlant = a.getOptimal(j + 1, dateSelected) - weather.get(j).rainInInches;
                        if (optimalPerPlant < 1) {
                            optimalPerPlant = 1;
                        }
                    } else {
                        optimalPerPlant = a.getOptimal(j + 1, dateSelected);
                    }


                    double dayOptimalRequirment = (optimalPerPlant * (double) numPlants) * soil * environment * tempValue;
                    theArrayList.add(dayOptimalRequirment);
                }

                theMap.put(theName, theArrayList);
            }

        }

        return theMap;
    }

    /**
     * Method creates matrix to show basic water requirments for each plot on a day by day basis
     *
     * @return
     */
    public double[][] createBasicMatrix() {

        Map<String, ArrayList<Double>> basicMap = this.getBasicMap();
        double[][] matrix = new double[this.getGarden().getPlots().size()][this.getDays()];

        int rowCounter = 0;
        for (Map.Entry<String, ArrayList<Double>> entry : basicMap.entrySet()) {
            ArrayList<Double> temp = entry.getValue();
            for (int i = 0; i < temp.size(); i++) {
                matrix[rowCounter][i] = temp.get(i);
            }
            rowCounter++;
        }


        return matrix;
    }

    /**
     * Method that creates maps data structure with basic values for each plot on day by day basis
     * @param withWeather
     * @return
     */
    public Map<String, ArrayList<Double>> createBasicMap(boolean withWeather) {


        Map<String, ArrayList<Double>> theMap = new TreeMap<>();
        ArrayList<WeatherObject> weather = WeatherData.getWeatherData(this.getGarden().getLocation().trim());


        if (withWeather == false) {
            for (int i = 0; i < this.getGarden().getPlots().size(); i++) {
                Plot a = this.garden.getPlots().get(i);
                String theName = a.getName();
                double soil = a.getSoil();
                double environment = a.getEnvironment();
                int numPlants = a.getNoOfPlants();
                ArrayList<Double> theArrayList = new ArrayList<>();
                for (int j = 0; j < this.getDays(); j++) {

                    double dayBasicRequirement = (a.getBasic(j + 1, this.dateSelected) * (double) numPlants) * soil * environment;

                    theArrayList.add(dayBasicRequirement);


                }
                theMap.put(theName, theArrayList);
            }
        } else {

            for (int i = 0; i < this.getGarden().getPlots().size(); i++) {
                Plot a = this.garden.getPlots().get(i);
                String theName = a.getName();
                ArrayList<Double> theArrayList = new ArrayList<>();
                double soil = a.getSoil();
                double environment = a.getEnvironment();
                int numPlants = a.getNoOfPlants();
                for (int j = 0; j < this.getDays(); j++) {

                    double tempValue = 1;

                    if (j < 10 & j < this.getDays()) {
                        int temp = weather.get(j).getHighTemp();
                        if (temp > 20 & temp <= 25) {
                            tempValue = 1.05;
                        } else if (temp > 25 & temp < 30) {
                            tempValue = 1.10;
                        } else if (temp >= 30) {
                            tempValue = 1.15;
                        } else {
                            tempValue = 1;
                        }
                    }
                    double basicPerPlant;
                    if (j < 10 & j < this.getDays()) {
                        basicPerPlant = a.getBasic(j + 1, dateSelected) - weather.get(j).rainInInches;
                        if (basicPerPlant < 1) {
                            basicPerPlant = 1;
                        }
                    } else {
                        basicPerPlant = a.getBasic(j + 1, dateSelected);
                    }


                    double dayBasicRequirment = (basicPerPlant * (double) numPlants) * soil * environment * tempValue;
                    theArrayList.add(dayBasicRequirment);
                }

                theMap.put(theName, theArrayList);
            }

        }

        return theMap;
    }

    /**
     * Method creates matrix of priority weightings
     *
     * @return double[][]
     */
    public double[][] createPriorityMatrix() {
        double[][] priortyMatrix = new double[this.garden.getPlots().size()][this.getDays()];

        for (int i = 0; i < this.getGarden().getPlots().size(); i++) {
            Plot a = this.getGarden().getPlots().get(i);

            for (int j = 0; j < this.getDays(); j++) {

                priortyMatrix[i][j] = a.getPlotPriorityValue(j, this.dateSelected);


                // System.out.print(priortyMatrix[i][j] + " ");
            }
            // System.out.println("\n");
        }

        return priortyMatrix;
    }

    public double[][] createzeroMatrix() {
        double[][] zeroMatrix = new double[this.getGarden().getPlots().size()][this.getDays()];

        for(int i = 0 ; i < zeroMatrix.length; i++){
            for(int j = 0 ; j < zeroMatrix[0].length; j++){
                zeroMatrix[i][j] = 0;
            }
        }


        return zeroMatrix;
    }


    /**
     * Main method of the class
     * Takes input matrix and then creates a of the matlab function
     * @return Map<String, ArrayList<Double>>
     */
    public Map<String, ArrayList<Double>> optimizeForMap() {

        double[][] optimal = this.createOptimalMatrix();
        double[][] basic = this.createBasicMatrix();
        double[][] priority = this.createPriorityMatrix();
        Map<String, ArrayList<Double>> solutionMap = new TreeMap<>();
        double[][] solutionMatrix = new double[this.getGarden().getPlots().size()][this.getDays()];



            double basicTotal = 0;
            for (int i = 0; i < basic.length; i++) {
                for (int j = 0; j < basic[0].length; j++) {
                    basicTotal += basic[i][j];
                }
            }


            if (basicTotal > this.getWaterAvailable()) {

                double[][] zero = this.createzeroMatrix();
                MatLabFunction theFunction = new MatLabFunction(optimal, zero, priority, this.getWaterAvailable());
                solutionMatrix = theFunction.callMatLabFunction();

            } else {

                MatLabFunction theFunction = new MatLabFunction(optimal, basic, priority, this.getWaterAvailable());
                solutionMatrix = theFunction.callMatLabFunction();
            }


        int rowCounter = 0;
        for (Map.Entry<String, ArrayList<Double>> entry : this.getOptimalMap().entrySet()) {
            String name = entry.getKey();

            ArrayList<Double> temp = new ArrayList<>();
            for (int i = 0; i < solutionMatrix[0].length; i++) {
                temp.add(solutionMatrix[rowCounter][i]);
            }

            solutionMap.put(name, temp);
            rowCounter++;
        }


        return solutionMap;


    }

    /**
     *  returns totals of each plot optimal on a day by day basis
     * @param theMap
     * @return
     */
    public double[] decisionByDay(Map<String, ArrayList<Double>> theMap) {
        double[] days = new double[this.getDays()];

        for(Map.Entry<String, ArrayList<Double>> entry : theMap.entrySet()){
            ArrayList<Double> temp = entry.getValue();
            for(int i = 0; i < temp.size(); i++) {
                days[i]+=temp.get(i);
            }
        }

        return days;

    }


    /**
     *
     *  returns desired totals for all plots each day
     * @return double[]
     */
    public double[] getOptimisationPoints(){
       double[][] opMatrix = this.createOptimalMatrix();
       double[] totals = new double[this.getDays()];
       for(int i = 0 ; i < opMatrix[0].length; i++) {
           double dayTotal = 0;
           for (int j = 0; j < opMatrix.length; j++) {
                dayTotal+=opMatrix[j][i];
           }
           totals[i] = dayTotal;
       }

       return totals;
    }

    /**
     * returns basic total on a day by day basis
     * @return
     */
    public double[] getBasicPoints() {
        double[][]basicMatrix = this.createBasicMatrix();
        double[] totals = new double[this.getDays()];
        for(int i = 0; i < basicMatrix[0].length; i++){
            double dayTotal = 0;
            for(int j = 0 ; j < basicMatrix.length; j++){
                dayTotal+=basicMatrix[j][i];
            }
            totals[i]=dayTotal;
        }

        return totals;
    }


    /**
     * Main method used for testing
     *
     * @param args
     */
    public static void main(String[] args) {

        System.out.println(System.getProperty("java.library.path"));


        Garden testGarden = Database.createGarden(1, true);

        Optimiser optimiserTest = new Optimiser(testGarden, 20, 4500, LocalDate.now(), false);
        Optimiser optimiserWithWeather = new Optimiser(testGarden, 20, 4500, LocalDate.now(), true);

        double[][] testMatrix = optimiserTest.createOptimalMatrix();
        double[][] basicMatrix = optimiserTest.createBasicMatrix();
        double[][] weatherOptimal = optimiserWithWeather.createOptimalMatrix();
        double[][] weatherBasic = optimiserWithWeather.createBasicMatrix();


        Map<String, ArrayList<Double>> testMap = optimiserTest.optimizeForMap();
        Map<String, ArrayList<Double>> testWeatherMap = optimiserWithWeather.optimizeForMap();

        double Optotal = 0;
        for (int i = 0; i < testMatrix.length; i++) {
            for (int j = 0; j < testMatrix[0].length; j++) {
                System.out.printf("|%6.2f", testMatrix[i][j]);
                Optotal += testMatrix[i][j];
            }

            System.out.println("\n");
        }
        System.out.println("Optimal= " + Optotal);

        double OptotalWithWeather = 0;
        for (int i = 0; i < testMatrix.length; i++) {
            for (int j = 0; j < testMatrix[0].length; j++) {
                System.out.printf("|%6.2f", weatherOptimal[i][j]);
                Optotal += weatherOptimal[i][j];
            }

            System.out.println("\n");
        }
        System.out.println("Opt with weather= " + OptotalWithWeather);

    }
}