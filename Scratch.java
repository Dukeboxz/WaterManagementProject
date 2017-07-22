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

            for(int i = 0; i < 20 ; i++) {

                System.out.println(Math.floorDiv(i, 4)); // for rows
                System.out.println();
                System.out.println(Math.floorMod(i, 4)); // for columns
            }

        } catch (SQLException e) {

        }

    }
}
