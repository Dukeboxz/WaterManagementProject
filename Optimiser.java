import java.sql.SQLException;
import com.jom.*;
import com.sun.org.apache.xpath.internal.SourceTree;


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





    public Optimiser(Garden garden,
                     int days,
                     double water){

        this.garden=garden;
        this.days=days;
        this.waterAvailable=water;
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
        double[][] matrix = new double[this.garden.plots.size()][this.getDays()];
        double total = 0.0;

        for(int i = 0; i < this.getGarden().plots.size(); i++){
            Plot a = this.garden.getPlots().get(i);
            double soilAndEnvironment = a.getEnvironment()+ a.getSoil();
            int numPlants = a.getNoOfPlants();
            for(int j = 0; j < this.getDays(); j++){

                double dayOptimalRequirment = (a.getOptimal(j + 1)*(double)numPlants)*soilAndEnvironment;
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

    /**
     * Method creates matrix to show basic water requirments for each plot on a day by day basis
     * @return
     */
    public double[][] createBasicMatrix() {
        double[][] matrix = new double[this.garden.plots.size()][this.getDays()];
        double total = 0.0;

        for(int i = 0; i < this.getGarden().plots.size(); i++){
            Plot a = this.garden.getPlots().get(i);
            double soilAndEnvironment = a.getEnvironment()+ a.getSoil();
            int numPlants = a.getNoOfPlants();
            for(int j = 0; j < this.getDays(); j++){

                double dayBasicRequirement = (a.getBasic(j + 1)*(double)numPlants)*soilAndEnvironment;
                matrix[i][j] = dayBasicRequirement;
                // System.out.print(a.getBasic(j) + " ");
                total+= dayBasicRequirement;

                System.out.print(matrix[i][j] + " ");



            }
            System.out.print("\n");
        }

        System.out.println(" BASIC TOTAL SUM= " + total);

        return matrix;
    }

    /**
     * Method creates matrix of priority weightings
     * @return
     */
    public int[][] createPriorityMatrix() {
        int[][] priortyMatrix = new int[this.garden.plots.size()][this.getDays()];

        for(int i = 0; i < this.getGarden().plots.size(); i++){
            Plot a = this.getGarden().getPlots().get(i);
            int plotPriority = a.getPriority();
            for(int j = 0; j < this.getDays(); j ++){
                priortyMatrix[i][j]= plotPriority+a.getStagePriority(j + 1);
                System.out.print(priortyMatrix[i][j] + " ");
            }
            System.out.println("\n");
        }

        return priortyMatrix;
    }

    /**
     * Method for decison variable matrix
     */
    public double[][] decisionMatrix(){
        double[][] decisionMatrix = new double[this.getGarden().plots.size()][this.getDays()];
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

                System.out.print(decisionMatrix[i][j] + " ");




            }
            System.out.print("\n");



        }
        System.out.println("Decison Matrix total = " + total);
        return decisionMatrix;
    }


    /**
     * Method that creates matrix based on objective function
     */
    public double[][] objectiveFunctionMatrix() {
        double[][] objectiveMatrix = new double[this.garden.plots.size()][this.getDays()];
        double[][] optiamalMatrix = this.createOptimalMatrix();
        double[][] decisonMatrix = this.decisionMatrix();

        for(int i = 0; i < objectiveMatrix.length; i++){
            for(int j = 0; j <objectiveMatrix[i].length; j++){
                double difference = optiamalMatrix[i][j] - decisonMatrix[i][j];
                objectiveMatrix[i][j] = difference;
                System.out.print(objectiveMatrix[i][j] + " ");
            }
            System.out.println("\n");
        }


        return objectiveMatrix;
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

    // method that undertakes optimisation and returns solution using JOM methods
    public double[][] optimize(){

        // create optimisation problem object - JOM
        OptimizationProblem op = new OptimizationProblem();

        // add decision variable with lower and upper limit based on optimal and basic levels - JOM
        op.addDecisionVariable("y", false, new int[]{this.getGarden().getPlots().size(), this.getDays()},
                new DoubleMatrixND(this.createBasicMatrix()), new DoubleMatrixND(this.createOptimalMatrix()));

        // input parameter basic level matrix - JOM
        op.setInputParameter("z", new DoubleMatrixND(this.createBasicMatrix()));

        // input parameter optimal level matrix - JOM
        op.setInputParameter("x", new DoubleMatrixND(this.createOptimalMatrix()));

        // set objective function - JOM
        op.setObjectiveFunction("minimize", "sum(x-y)");


        // add constraint JOM
        double water = this.getWaterAvailable();
        String waterAvailable = String.valueOf(water);
        String expression = "sum(sum(y,2),1)<="+waterAvailable;
        op.addConstraint(expression, "a");


        // set an initial solution using a decison matrix - JOM
        op.setInitialSolution("y", new DoubleMatrixND(this.decisionMatrix()));

        // call solver - JOM
        op.solve("glpk", "solverLibraryName", "libglpk.so.36");

        if(!op.solutionIsOptimal()) throw new RuntimeException("Not optimal solution");
        // check solver
        String result = SolverTester.check_glpk("libglpk.so.36");
        System.out.println(" the result is " + result);


        // get solution and print out = JOM `
        double[][] solMatrix = new double[this.garden.getPlots().size()][this.getDays()];
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



        return solMatrix;
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
     * Main method used for testing
     * @param args
     */
    public static void main(String[] args){

        try {
            Garden testGarden = Database.createGarden(2);

            Optimiser test = new Optimiser(testGarden, 31, 10000);

            double[][] temp = test.optimize();

            double[] decisionPoints = new double[test.getDays()];
            for(int i = 0 ; i < test.getDays(); i++){
                double dayToal = 0;
                for (int j = 0; j < temp.length; j++){

                    //System.out.print(temp[j][i] + " ");
                    dayToal+= temp[j][i];
                    System.out.print(dayToal + " ");
                }
               System.out.println("\n");

                decisionPoints[i]= dayToal;


            }

            System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            int counter = 0;
            double total = 0;
          double[] optimalPoints = test.getOptimisationPoints();
            for(double d : optimalPoints){
                System.out.print(" " + Math.floor(d));
                total+= d;
                counter++;
            }

            System.out.println();
            System.out.println("The counter for optimal points is " + counter);
            System.out.println("The total for optimal is " + total);

            counter = 0;
            total = 0;
            for(int i = 0 ; i < decisionPoints.length; i++){
                System.out.print(" " + Math.floor(decisionPoints[i]));
                counter++;
                total += decisionPoints[i];
            }
            System.out.println();
            System.out.println("The counter for decison points is " + counter);
            System.out.println("The decision for optimal is " + total);

            System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
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


        }




    }
}
