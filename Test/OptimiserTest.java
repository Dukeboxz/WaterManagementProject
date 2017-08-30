

import Optimizer.Database;
import Optimizer.Garden;
import Optimizer.Optimiser;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OptimiserTest {

    double[][] desired = new double[][] {{250, 250, 250}, {250, 250, 250}};
    double[][] basic = new double[][] {{150, 150, 150}, {150, 150, 150}};
    double[][] priority = new double[][]{{2,2,4}, {5, 5, 7}};

    Garden testGarden = Database.createGarden(1, true);

    Optimiser test = new Optimiser(testGarden, 3, 600, LocalDate.now(), false);



    @Test
    public void test1(){


        double[][] result = test.createOptimalMatrix();
        double[][] priority = test.createPriorityMatrix();
        double[][] basic = test.createBasicMatrix();





        for(int i = 0; i < desired.length; i++) {
            for(int j = 1 ; j < desired[0].length; j++){
                if(priority[i][j]>priority[i][j-1]){
                    assertTrue(result[i][j] >result[i][j-1]);
                }
            }
        }

    }

    @Test
    public void test2(){

        double[] optimal = test.decisionByDay(test.optimizeForMap());
        double[] desired = test.getOptimisationPoints();
        double[] basic = test.getBasicPoints();

        for(int i =0; i < optimal.length; i++){
            assertTrue(optimal[i]<= desired[i] & desired[i] >= basic[i]);
        }

    }

}