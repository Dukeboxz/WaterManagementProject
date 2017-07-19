import com.joptimizer.functions.PDQuadraticMultivariateRealFunction;
import com.joptimizer.optimizers.JOptimizer;

import javax.swing.*;
import java.beans.Expression;
import java.sql.SQLException;
import java.time.LocalDate;
import com.jom.*;

/**
 * Created by stephen on 14/06/17.
 * Class to test ideas out in will not form part of final product
 */
public class Scratch {

    public static void main(String[] args) {

       try {
           Garden testGarden = Database.createGarden(2);


           Optimiser test = new Optimiser(testGarden, 31, 9050, LocalDate.now(), false);

           Optimiser test2 = new Optimiser(testGarden, 31, 9000, LocalDate.now(), true);

           OptimizationProblem op = new OptimizationProblem();

           com.jom.Expression c = new OptimizationProblem().parseExpression("[7;1;4;;5;2;6]");
         // System.out.println("value of e: " + c.evaluate());
           System.out.println("c(1,2) = " +  op.parseExpression("c(0,0)").evaluate());


           // Optimiser test = new Optimiser(testGarden, 31, 9500, LocalDate.now()), false);


           double[][] p = new double[][]{{1., 0.4}, {0.4, 1.}};
           PDQuadraticMultivariateRealFunction objectiveFunction = new PDQuadraticMultivariateRealFunction(p, null, 0);


       } catch(SQLException e){

       }
    }

}
