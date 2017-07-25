import com.joptimizer.functions.PDQuadraticMultivariateRealFunction;
import com.joptimizer.optimizers.JOptimizer;

import javax.swing.*;
import java.beans.Expression;
import java.sql.SQLException;
import java.time.LocalDate;
import com.jom.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by stephen on 14/06/17.
 * Class to test ideas out in will not form part of final product
 */
public class Scratch extends Application {

    public static Stage theStage;

    public static double[][] waterOptimisation(Optimiser optimiser){
        double[][] a = optimiser.createOptimalMatrix();
        double total = 0;
        for(int i = 0 ; i < optimiser.getGarden().getPlots().size(); i++){
            for(int j = 0; j < optimiser.getDays(); j++){
                total+= a[i][j];
            }
        }
        System.out.println("The total THAT IS " + total);
        System.out.println(optimiser.getWaterAvailable());
        double waterAvaialbe = optimiser.getWaterAvailable();
        if(total <= waterAvaialbe){
            System.out.println("More Water");
            return a;
        } else {
            return getBestUse(optimiser) ;
        }
    }

    public static boolean isEnough(double[][] a, double waterAmount){
        System.out.println("is enough running");
        double total = 0;
        for(int i = 0; i <a.length; i++){
            for(int j = 0 ; j < a[i].length; j++){
                total += a[i][j];
            }
        }

        System.out.println("Total is " + total);
        if (total <= waterAmount) {

            return true;
        } else {
            return false;
        }
    }

    public static  double[][] getBestUse(Optimiser o) {

        System.out.println("Running best Use");
        double x = 100;
        double[][] priorityMatrix = o.createPriorityMatrix();
        double[][] optimal = o.createOptimalMatrix();
        double[][] newMatrix = new double[o.getGarden().getPlots().size()][o.getDays()];
        for(int i = 0; i < newMatrix.length; i++){
            for(int j = 0; j < newMatrix[i].length; j++) {
                newMatrix[i][j]=0;
            }
        }
        boolean flag=false;

        while(flag==false & x > 5) {
            System.out.println("whiel loop ruuning ");
            double total =0;
            System.out.println(x);
            for (int i = 0; i < o.getGarden().getPlots().size(); i++) {
                for (int j = 0; j < o.getDays(); j++) {

                    double priority = priorityMatrix[i][j];
                    double reductionAmount;

                    if (priority == 7) {
                        reductionAmount = (x - 1) / 100;
                    } else if (priority == 6) {
                        reductionAmount = (x - 2) / 100;
                    } else if (priority == 5) {
                        reductionAmount = (x - 3) / 100;
                    } else if (priority == 4) {
                        reductionAmount = (x - 4) / 100;
                    } else if (priority == 3) {
                        reductionAmount = (x - 5) / 100;
                    } else if (priority == 2) {
                        reductionAmount = (x - 6) / 100;
                    } else {
                        reductionAmount = 1;
                    }

                    newMatrix[i][j] = optimal[i][j] * reductionAmount;
                    total+= newMatrix[i][j];

                   // System.out.printf("|%6.2f", newMatrix[i][j]);

                }
               // System.out.println("\n");
            }

            for(int i = 0 ; i < o.getGarden().getPlots().size(); i++){
                for(int j = 0 ; j < o.getDays(); j++){
                    System.out.printf("|%6.2f", newMatrix[i][j]);
                }
                System.out.println("\n");
            }
            System.out.println(isEnough(newMatrix, o.getWaterAvailable()));
            if(isEnough(newMatrix, o.getWaterAvailable())){
                flag=true;
            }

            x = x - 5;
        }

        System.out.println("while stopped");
        return newMatrix;





    }

    public void start(Stage primaryStage){

        theStage = primaryStage;

        try{

            Garden testGarden = Database.createGarden(2 ,true);


            Optimiser test = new Optimiser(testGarden, 40, 16000, LocalDate.now(), false);

            double[][] theResult = waterOptimisation(test);
            double[][] theOptimal = test.createOptimalMatrix();
            double[] resultTotals = new double[test.getDays()];
            double[] optimalTotals = new double[test.getDays()];

            for(int i = 0 ; i < test.getDays() ; i++){
                double totalO = 0;
                double totalR = 0;
                for(int j = 0 ; j < test.getGarden().getPlots().size(); j++){
                    totalO+= theOptimal[j][i];
                    totalR+= theResult[j][i];
                }

                resultTotals[i]=totalR;
                optimalTotals[i]=totalO;
            }

            GridPane opGrid = new GridPane();

            final NumberAxis xAxis = new NumberAxis(0, test.getDays(), 1);
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Days");
            yAxis.setLabel("Water inches^3");
            LineChart<Number, Number> waterChart = new LineChart<Number, Number>(xAxis, yAxis);
            waterChart.setTitle("Water Usage");

            XYChart.Series optimalUse = new XYChart.Series();
            optimalUse.setName("Optimal");
            XYChart.Series results = new XYChart.Series();
            results.setName("Results");

            for(int i = 0 ; i < resultTotals.length; i++){
                optimalUse.getData().add(new XYChart.Data<>(i, optimalTotals[i]));
                results.getData().add(new XYChart.Data<>(i, resultTotals[i]));
            }

            waterChart.getData().add(optimalUse);
           waterChart.getData().add(results);

            opGrid.add(waterChart,1 , 1);

            Scene opScene = new Scene(opGrid, 1000, 600);
            primaryStage.setScene(opScene);
            primaryStage.show();

        } catch(SQLException e) {

        }


    }

    public static void main(String[] args) {

        System.out.println(Math.floorDiv(10, 4));

        try {
            Garden testGarden = Database.createGarden(2, true);


            Optimiser test = new Optimiser(testGarden, 40, 16000, LocalDate.now(), false);

            double[][] theResult = waterOptimisation(test);
            double[][] theOptimal = test.createOptimalMatrix();

            System.out.println("RESULT");
            double resultTotal = 0;
            for(int i = 0 ; i < theResult.length ; i++) {
                for(int j = 0 ; j < theResult[i].length; j++){
                    System.out.printf("|%6.2f", theResult[i][j]);
                    resultTotal+= theResult[i][j];
                }
                System.out.println("\n");
            }
            System.out.println("The result total is " + resultTotal);
            System.out.println();
            double total = 0;
            System.out.println("OPTIMAL");
            for(int i = 0 ; i < theResult.length ; i++) {
                for(int j = 0 ; j < theResult[i].length; j++){
                    System.out.printf("|%6.2f", theOptimal[i][j]);
                    total+=theOptimal[i][j];
                }
                System.out.println("\n");
            }

            System.out.println("optimal total is " + total);

            int numElement = test.getDays()* test.getGarden().getPlots().size();
            double whatleft = test.getWaterAvailable() - resultTotal;
            System.out.println("what left = " + whatleft);
            double split = (double)numElement/whatleft;
            System.out.println(split);




         // launch(args);
        }catch(SQLException e){

        }

    }
}
