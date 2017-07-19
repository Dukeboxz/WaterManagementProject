import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.jom.*;
import com.sun.org.apache.xpath.internal.SourceTree;

import javax.swing.*;


/**
 * Created by stephen on 19/06/17.
 * Class that creates and then solves the optimisation problem.
 * Uses methods within the Java OPtimiser Modeler Authored by Professor Pablo Pavon Marino University of Cartagena(Spain)
 * Method from this library will be inidcated as JOM methods in comments
 */
public class Optimiser  {

    private Garden garden;
    public static int days;
     private double waterAvailable;
     LocalDate dateSelected;
     boolean withWeather;





    public Optimiser(Garden garden,
                     int days,
                     double water,
                     LocalDate selectedDate,
                     boolean withWeather){

        this.garden=garden;
        this.days=days;
        this.waterAvailable=water;
        this.dateSelected=selectedDate;
        this.withWeather=withWeather;
    }

    //@Override
    public boolean eval_f(int a, double[] b, boolean c, double[] d) {

        return true;
    }

    public Garden getGarden(){
        return this.garden;
    }

    public int getDays() {
        return this.days;
    }

    public double getWaterAvailable(){
        return this.waterAvailable;
    }


    /**
     * Method create a matrix showing optimal water requirments for each plot on a day to day basis
     * @return double[][]
     */
    public double[][] createOptimalMatrix(){
        double[][] matrix = new double[this.garden.getPlots().size()][this.getDays()];
        double total = 0.0;

        for(int i = 0; i < this.getGarden().getPlots().size(); i++){
            Plot a = this.garden.getPlots().get(i);
            double soilAndEnvironment = a.getEnvironment()+ a.getSoil();
            int numPlants = a.getNoOfPlants();
            for(int j = 0; j < this.getDays(); j++){

                double dayOptimalRequirment = (a.getOptimal(j + 1, this.dateSelected)*(double)numPlants)*soilAndEnvironment;
                matrix[i][j] =  dayOptimalRequirment;
                total += dayOptimalRequirment;

                //System.out.print(a.getOptimal(j) + " ");

               // System.out.print(matrix[i][j] + " ");



            }
          //  System.out.print("\n");
        }


       // System.out.println("OPTIMAL total sum= " + total);
        return matrix;
    }

    public double[][] createOptimalWithWeather() {
        double[][] matrix = new double[this.garden.getPlots().size()][this.getDays()];
        ArrayList<WeatherObject> weather = WeatherData.getWeatherData(this.getGarden().getLocation().trim());
        double total = 0.0;

        for(int i = 0; i < this.getGarden().getPlots().size(); i++){
            Plot a = this.garden.getPlots().get(i);
            double soilAndEnvironment = a.getEnvironment()+ a.getSoil();
            int numPlants = a.getNoOfPlants();
            for(int j = 0; j < this.getDays(); j++){
                double tempValue = 1;

                if(j < 10 & j < this.getDays()) {
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
                if(j < 10 & j < this.getDays()) {
                    optimalPerPlant = a.getOptimal(j + 1, dateSelected) - weather.get(j).rainInInches;
                    if (optimalPerPlant < 1) {
                        optimalPerPlant = 1;
                    }
                } else{
                    optimalPerPlant = a.getOptimal(j+ 1, dateSelected);
                }


                double dayOptimalRequirment = (optimalPerPlant*(double)numPlants)*soilAndEnvironment * tempValue;
                matrix[i][j] =  dayOptimalRequirment;
                total += dayOptimalRequirment;

                //System.out.print(a.getOptimal(j) + " ");

                // System.out.print(matrix[i][j] + " ");



            }
            //  System.out.print("\n");
        }



        return  matrix;



    }

    /**
     * Method creates matrix to show basic water requirments for each plot on a day by day basis
     * @return
     */
    public double[][] createBasicMatrix() {
        double[][] matrix = new double[this.garden.getPlots().size()][this.getDays()];

        double total = 0.0;

        for(int i = 0; i < this.getGarden().getPlots().size(); i++){
            Plot a = this.garden.getPlots().get(i);
            double soilAndEnvironment = a.getEnvironment()+ a.getSoil();
            int numPlants = a.getNoOfPlants();
            for(int j = 0; j < this.getDays(); j++){

                double dayBasicRequirement = (a.getBasic(j + 1 , this.dateSelected)*(double)numPlants)*soilAndEnvironment;
                matrix[i][j] = dayBasicRequirement;
                // System.out.print(a.getBasic(j) + " ");
                total+= dayBasicRequirement;

               // System.out.print(matrix[i][j] + " ");



            }
           // System.out.print("\n");
        }

//        System.out.println(" BASIC TOTAL SUM= " + total);

        return matrix;
    }

    public double[][] createBasicMatrixWithWeather() {
        double[][] matrix = new double[this.getGarden().getPlots().size()][this.getDays()];
        ArrayList<WeatherObject> weather = WeatherData.getWeatherData(this.getGarden().getLocation().trim());
        double total = 0.0;

        for(int i = 0; i < this.getGarden().getPlots().size(); i++){
            Plot a = this.garden.getPlots().get(i);
            double soilAndEnvironment = a.getEnvironment()+ a.getSoil();
            int numPlants = a.getNoOfPlants();
            for(int j = 0; j < this.getDays(); j++){

                double tempValue = 1;

                if(j < 10 & j < this.getDays()) {
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
                if(j < 10 & j < this.getDays()) {
                    basicPerPlant = a.getBasic(j + 1, this.dateSelected) - weather.get(j).rainInInches;
                    if (basicPerPlant < 1) {
                        basicPerPlant = 1;
                    }
                } else{
                    basicPerPlant = a.getBasic(j+ 1, this.dateSelected);
                }


                double dayOptimalRequirment = (basicPerPlant*(double)numPlants)*soilAndEnvironment * tempValue;
                matrix[i][j] =  dayOptimalRequirment;
                total += dayOptimalRequirment;



            }
            System.out.print("\n");
        }

      //  System.out.println(" BASIC  WITH WEATHER TOTAL SUM= " + total);

        return matrix;

    }

    /**
     * Method creates matrix of priority weightings
     * @return
     */
    public int[][] createPriorityMatrix() {
        int[][] priortyMatrix = new int[this.garden.getPlots().size()][this.getDays()];

        for(int i = 0; i < this.getGarden().getPlots().size(); i++){
            Plot a = this.getGarden().getPlots().get(i);
            int plotPriority = a.getPriority();
            for(int j = 0; j < this.getDays(); j ++){
                priortyMatrix[i][j]= plotPriority+a.getStagePriority(j + 1, this.dateSelected);
               // System.out.print(priortyMatrix[i][j] + " ");
            }
           // System.out.println("\n");
        }

        return priortyMatrix;
    }

    /**
     * Method for decison variable matrix
     */
    public double[][] decisionMatrix(){
        double[][] decisionMatrix = new double[this.getGarden().getPlots().size()][this.getDays()];
        double[][] optiamalMatrix = this.createOptimalMatrix();
        double[][] baseMatrix = this.createBasicMatrix();
        int[][] priorityMatrix = this.createPriorityMatrix();
        double total = 0.0;

        for(int i = 0; i < optiamalMatrix.length; i++ ){
            for( int j = 0; j <optiamalMatrix[i].length; j++){
                double difference = optiamalMatrix[i][j] - baseMatrix[i][j];
                double weightIncrements = difference/7.0;
                double weighting = (double)priorityMatrix[i][j];
                double base = baseMatrix[i][j];
                decisionMatrix[i][j]= base + weighting*weightIncrements;
                total+= base + weightIncrements*weighting;

               // System.out.print(decisionMatrix[i][j] + " ");




            }
            //System.out.print("\n");



        }
        //System.out.println("Decison Matrix total = " + total);
        return decisionMatrix;
    }


    /**
     * method to create points showing water used for whole garden on a day by day basis
     * @return
     */
    public double[] getOptimisationPoints() {
        double[][] temp = this.createOptimalMatrix();
        double[] optimalPoints = new double[this.getDays()];

        for(int i = 0; i < this.getDays(); i++){
            double dayTotal = 0;
            for(int j = 0; j < this.garden.getPlots().size(); j++ ){
                dayTotal += temp[j][i];

            }
            optimalPoints[i] = dayTotal;
        }

        return optimalPoints;
    }

    //

    // method that undertakes optimisation and returns solution using JOM methods
    public double[][] optimize(){



            // create optimisation problem object - JOM
            OptimizationProblem op = new OptimizationProblem();

        if(this.withWeather==false) {

            // add decision variable with lower and upper limit based on optimal and basic levels - JOM
            op.addDecisionVariable("y", false, new int[]{this.getGarden().getPlots().size(), this.getDays()},
                    new DoubleMatrixND(this.createBasicMatrix()), new DoubleMatrixND(this.createOptimalMatrix()));

            // input parameter basic level matrix - JOM
            op.setInputParameter("z", new DoubleMatrixND(this.createBasicMatrix()));

            // input parameter optimal level matrix - JOM
            op.setInputParameter("x", new DoubleMatrixND(this.createOptimalMatrix()));

            op.setInputParameter("w", new DoubleMatrixND(this.decisionMatrix()));


        } else {


            // add decision variable with lower and upper limit based on optimal and basic levels - JOM
            op.addDecisionVariable("y", false, new int[]{this.getGarden().getPlots().size(), this.getDays()},
                    new DoubleMatrixND(this.createBasicMatrixWithWeather()), new DoubleMatrixND(this.createBasicMatrixWithWeather()));

            // input parameter basic level matrix - JOM
            op.setInputParameter("z", new DoubleMatrixND(this.createBasicMatrixWithWeather()));

            // input parameter optimal level matrix - JOM
            op.setInputParameter("x", new DoubleMatrixND(this.createOptimalWithWeather()));




        }

        // set objective function - JOM
        op.setObjectiveFunction("maximize", "sum(sum(z+y) - sum(x-y))");


        // add constraint JOM
        double water = this.getWaterAvailable();
        String waterAvailable = String.valueOf(water);
        String expression = "sum(sum(y,2),1)<=" + waterAvailable;
        op.addConstraint(expression, "a");


        // set an initial solution using a decison matrix - JOM
       // op.setInitialSolution("y", new DoubleMatrixND(this.decisionMatrix()));


        // call solver - JOM
        double[][] solMatrix = new double[this.garden.getPlots().size()][this.getDays()];
        try {
            op.solve("glpk", "solverLibraryName", "libglpk.so.36");

            if (!op.solutionIsOptimal()) throw new RuntimeException("Not optimal solution");
            // check solver
            String result = SolverTester.check_glpk("libglpk.so.36");
            System.out.println(" the result is " + result);

            // get solution and print out = JOM `

            DoubleMatrixND sol = op.getPrimalSolution("y");
            double total = 0.0;
           // System.out.println("SOLUTION SOLUTION ================================================================");
            for (int a1 = 0; a1 < this.garden.getPlots().size(); a1++) {
                for (int b1 = 0; b1 < this.getDays(); b1++) {

                    double temp = sol.get(new int[]{a1, b1});
                  //  System.out.print(temp + " ");
                    solMatrix[a1][b1] = temp;
                    total +=  sol.get(new int[]{a1, b1});
                }

                //System.out.println("\n");


            }
            //System.out.println("solution total is " + total);
            //System.out.println("====================================================================================");





        } catch(JOMException e){
            System.out.println("Cannot give a solution ");
            //JOptionPane.showMessageDialog(null, "Cannot find solution reducing basic by 5%");

            solMatrix=optimizeForDrought(5);


        }

        finally {

            return solMatrix;

        }


    }

    public double[] decisionByDay(double[][] d){
        double[] days = new double[d[0].length];
        double dayTotal = 0;
        for(int i = 0; i < this.getDays(); i++){

            dayTotal = 0;
            for(int j = 0; j < this.garden.getPlots().size(); j++){
                dayTotal += d[j][i];
            }

            days[i]= dayTotal;
        }

        return days;

    }

    /**
     * create new basic matrix for drought conditions
     * @return
     */
    public double[][] createLowerBasicMatrix(double lowerPercentage ){

        double[][] currentBasicMatrix = createBasicMatrix();

        double[][] lowerMatrix =  new double[this.getGarden().getPlots().size()][this.getDays()];



        double percentageLowerVariable = 1 - (lowerPercentage/100);


        double total = 0;
        for(int i = 0; i < this.getGarden().getPlots().size(); i++){
            for (int j = 0 ; j< this.getDays(); j++){

                    double temp = currentBasicMatrix[i][j]*percentageLowerVariable;
                     lowerMatrix[i][j] = temp;
                System.out.print(lowerMatrix[i][j] + " ");
                     total += temp;
            }
            System.out.println("\n");
        }

        System.out.println("The total for this drought iteration is " + total);
        return lowerMatrix;
    }




    public double[][] optimizeForDrought(int x ){

        System.out.println("drought optimisation running");

        // create optimisation problem object - JOM
        OptimizationProblem op = new OptimizationProblem();

        if(this.withWeather==false) {

            // add decision variable with lower and upper limit based on optimal and basic levels - JOM
            op.addDecisionVariable("y", false, new int[]{this.getGarden().getPlots().size(), this.getDays()},
                    new DoubleMatrixND(this.createLowerBasicMatrix(x)), new DoubleMatrixND(this.createOptimalMatrix()));

            // input parameter basic level matrix - JOM
            op.setInputParameter("z", new DoubleMatrixND(this.createBasicMatrix()));

            // input parameter optimal level matrix - JOM
            op.setInputParameter("x", new DoubleMatrixND(this.createOptimalMatrix()));

            op.setInputParameter("w", new DoubleMatrixND(this.decisionMatrix()));


        } else {


            // add decision variable with lower and upper limit based on optimal and basic levels - JOM
            op.addDecisionVariable("y", false, new int[]{this.getGarden().getPlots().size(), this.getDays()},
                    new DoubleMatrixND(this.createLowerBasicMatrix(x)), new DoubleMatrixND(this.createBasicMatrixWithWeather()));

            // input parameter basic level matrix - JOM
            op.setInputParameter("z", new DoubleMatrixND(this.createBasicMatrixWithWeather()));

            // input parameter optimal level matrix - JOM
            op.setInputParameter("x", new DoubleMatrixND(this.createOptimalWithWeather()));




        }


        // set objective function - JOM
        op.setObjectiveFunction("maximize", "sum(sum(z+y) - sum(x-y))");;


        // add constraint JOM
        double water = this.getWaterAvailable();
        String waterAvailable = String.valueOf(water);
        String expression = "sum(sum(y,2),1)<="+waterAvailable;
        op.addConstraint(expression, "a");


        // set an initial solution using a decison matrix - JOM
        op.setInitialSolution("y", new DoubleMatrixND(this.decisionMatrix()));

        // call solver - JOM

        double[][] solMatrix = new double[this.garden.getPlots().size()][this.getDays()];
        try {
            op.solve("glpk", "solverLibraryName", "libglpk.so.36");

            if (!op.solutionIsOptimal()) throw new RuntimeException("Not optimal solution");
            // check solver
            String result = SolverTester.check_glpk("libglpk.so.36");
            System.out.println(" the result is " + result);

            // get solution and print out = JOM `

            DoubleMatrixND sol = op.getPrimalSolution("y");
            double total = 0.0;
            System.out.println("SOLUTION SOLUTION ================================================================");
            for (int a1 = 0; a1 < this.garden.getPlots().size(); a1++) {
                for (int b1 = 0; b1 < this.getDays(); b1++) {

                    double temp = sol.get(new int[]{a1, b1});
                    System.out.print(temp + " ");
                    solMatrix[a1][b1] = temp;
                    total +=  sol.get(new int[]{a1, b1});
                }

                System.out.println("\n");


            }
            System.out.println("solution total is " + total);
            System.out.println("====================================================================================");


        } catch(JOMException e){



            System.out.println("Cannot give a solution Running drought with basic reduced " + x + " percent");
            if(x < 100) {
                solMatrix = optimizeForDrought(x + 5);
            } else {
                JOptionPane.showMessageDialog(null, "No solution can be found ");
            }
        }





        return solMatrix;
    }





    /**
     * Main method used for testing
     * @param args
     */
    public static void main(String[] args){

        try {
            Garden testGarden = Database.createGarden(2);

            for(int i = 0; i < testGarden.getPlots().size(); i++){
                testGarden.getPlots().get(i).setDatePlanted(LocalDate.now());
            }




            Optimiser test = new Optimiser(testGarden, 30,  9000, LocalDate.now(), false);

            Optimiser test2 = new Optimiser(testGarden, 31, 9000, LocalDate.now(), true);


            BufferedWriter out = new BufferedWriter(new FileWriter("matrix.txt"));

//
            double[][] temp = test.createOptimalMatrix();
            double total = 0;

            out.write("optimal Matrix made up of base optimal need * soiland Env ");
            out.newLine();
            for(int i = 0; i < test.getGarden().getPlots().size(); i++){



                for(int j = 0 ; j < test.getDays(); j++){


                   // System.out.printf("|%6.2f   ", temp[i][j]);
                    out.write(String.format("|%6.2f", temp[i][j]));
                    total+= temp[i][j];
                }

                //System.out.println("\n");
                out.newLine();
                out.newLine();

            }
            out.write("The Optimal Matrix total is " + total);
            out.newLine();
            System.out.println("The optinmal matrix total is " + total );
            out.newLine();

            out.write("Basic Matrix made up of basic water * soil & Environment ");
            out.newLine();
            double[][] temp2 = test.createBasicMatrix();
            double dayToal = 0;
            for(int i = 0 ; i < test.getGarden().getPlots().size(); i++){
                int stagePriority = 0;
                for (int j = 0; j <  test.getDays(); j++){

                    stagePriority =  test.getGarden().getPlots().get(i).getStagePriority(j,LocalDate.now().plusDays(j));
                    System.out.print(" " + temp2[i][j] +  " ");

                    out.write(String.format("|%6.2f", temp2[i][j]));

                   // System.out.print( "ST = " + stagePriority);
                    dayToal+= temp2[i][j];
                   // System.out.print(dayToal + " ");
                }
               System.out.println("\n");
                out.newLine();

               //decisionPoints[i]= dayToal;


            }
            out.write("basic matrix total is " + dayToal);

            out.newLine();
//
         System.out.println("solution total with weather is " + dayToal);

            out.newLine();
            out.write("Solution Matrix ");
            out.newLine();
            double[][] temp3 = test.createBasicMatrix();
            double basicToal = 0;
            for(int i = 0 ; i < test.getGarden().getPlots().size(); i++){

                for (int j = 0; j <  test.getDays(); j++){

                    System.out.print(temp3[i][j] + " ");

                    basicToal+= temp3[i][j];
                    out.write(String.format("|%6.2f", temp3[i][j]));
                    // System.out.print(dayToal + " ");
                }
                System.out.println("\n");
                out.newLine();

                // decisionPoints[i]= dayToal;


            }
            out.write("The solution total is " + basicToal);
            out.newLine();
            System.out.println("The basic with  total is " + basicToal);
            out.newLine();
            out.write("Difference between Optimal and Decision ");
            out.newLine();
            double[][] less = new double[test.getGarden().getPlots().size()][test.getDays()];
            for(int i = 0 ; i < test.getGarden().getPlots().size(); i++){
                for(int j = 0 ; j < test.getDays(); j++){
                    if(temp2[i][j]< temp[i][j]){
                        double value = (temp[i][j] - temp2[i][j])/temp[i][j] * 100;
                        less[i][j] = value;
                    } else{
                        less[i][j] = 0;
                    }
                }
            }
            for(int i = 0 ; i < test.getGarden().getPlots().size(); i++){
                for(int j = 0 ; j < test.getDays(); j++){
                    System.out.print(less[i][j] + " ");
                    out.write(String.format("|%6.2f", less[i][j]));

                }
                out.newLine();
                System.out.println("\n");
            }
            out.newLine();

            double[][] weightings = test.decisionMatrix() ;
            out.write("Decion values given as starting point  Made up of for difference between Optimal and Basic matric elements * by Prioirty Weightings ");
            out.newLine();
            for(int i = 0 ; i < test.getGarden().getPlots().size(); i++) {
                for(int j = 0 ; j < test.getDays(); j++){
                    out.write(String.format("|%6.2f", weightings[i][j]));
                }
                out.newLine();
            }

            out.newLine();
            out.write("Priority Weightings ");
            out.newLine();

            int[][] prior = test.createPriorityMatrix();

            for(int i = 0 ; i < test.getGarden().getPlots().size(); i++) {
                for(int j = 0 ; j < test.getDays(); j++){
                    out.write(String.format("|%-6d", prior[i][j]));
                }
                out.newLine();
            }
            out.write(" optimal Water Needs =   made up of water need per plant * number of plant ");
            out.newLine();

            double[][] baseOptimalNeed = new double[test.getGarden().getPlots().size()][test.getDays()];

            for(int i = 0 ; i < test.getGarden().getPlots().size(); i++) {
                for (int j = 0; j < test.getDays(); j++) {
                   double value =  test.getGarden().getPlots().get(i).getOptimal(j, test.dateSelected.plusDays(j));
                   baseOptimalNeed[i][j] = value;
                }


            }

            for(int i = 0 ; i < test.getGarden().getPlots().size(); i++) {
                for (int j = 0; j < test.getDays(); j++) {
                    out.write(String.format("|%6.2f", baseOptimalNeed[i][j]));

                }

                out.newLine();
            }
            out.newLine();

//            out.write(" Basic Water need made up of water need per plant * number of plants ");
            out.newLine();

//            double[][] baseBasic = new double[test.getGarden().getPlots().size()][test.getDays()];
//
//            for(int i = 0 ; i < test.getGarden().getPlots().size(); i++) {
//                for (int j = 0; j < test.getDays(); j++) {
//                    double value =  test.getGarden().getPlots().get(i).getBasic(j, test.dateSelected.plusDays(j)) *
//                            test.getGarden().getPlots().get(i).getNoOfPlants();
//                    baseOptimalNeed[i][j] = value;
//                }
//
//
//            }
//
//            for(int i = 0 ; i < test.getGarden().getPlots().size(); i++) {
//                for (int j = 0; j < test.getDays(); j++) {
//                    out.write(String.format("|%6.4f", baseBasic[i][j]));
//
//                }
//
//                out.newLine();
//            }

            out.newLine();
            out.write("Soil and Environment multiplications ");

            double[][] soilAndEnv = new double[test.getGarden().getPlots().size()][test.getDays()];

            out.newLine();

            for(int i = 0 ; i < test.getGarden().getPlots().size(); i++) {
                for (int j = 0; j < test.getDays(); j++) {
                    double value =  test.getGarden().getPlots().get(i).getSoil() + test.getGarden().getPlots().get(i).getEnvironment() *
                            test.getGarden().getPlots().get(i).getNoOfPlants();
                    soilAndEnv[i][j] = value;
                }


            }

            for(int i = 0 ; i < test.getGarden().getPlots().size(); i++) {
                for (int j = 0; j < test.getDays(); j++) {
                    out.write(String.format("|%6.2f", soilAndEnv[i][j]));

                }

                out.newLine();
            }








            out.close();


//            System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
//            int counter = 0;
//            double total = 0;
//          double[] optimalPoints = test.getOptimisationPoints();
//            for(double d : optimalPoints){
//                System.out.print(" " + Math.floor(d));
//                total+= d;
//                counter++;
//            }
//
//            System.out.println();
//            System.out.println("The counter for optimal points is " + counter);
//            System.out.println("The total for optimal is " + total);
//
//            counter = 0;
//            total = 0;
//            for(int i = 0 ; i < decisionPoints.length; i++){
//                System.out.print(" " + Math.floor(decisionPoints[i]));
//                counter++;
//                total += decisionPoints[i];
//            }
//            System.out.println();
//            System.out.println("The counter for decison points is " + counter);
//            System.out.println("The decision for optimal is " + total);
//
//            System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
//            System.out.println("OPTIMAL MATRIX");
//            double[][] matrix = test.createOptimalMatrix();
//            System.out.println();
//            System.out.println("BASIC MATRIX");
//            double[][] basicM = test.createBasicMatrix();
//            System.out.println();
//            System.out.println("PRIORITY MATRIX");
//            test.createPriorityMatrix();
//            System.out.println();
//            System.out.println("DECISION VARIABLE MATRIX ");
//            test.decisionMatrix();
//            System.out.println();
//            System.out.println("OBJECTIVE FUNCTION MATRIX");
//            test.objectiveFunctionMatrix();


        }catch(SQLException e){


        } catch(IOException f){

        }




    }
}
