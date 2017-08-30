package Optimizer;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PlotTest {

        Plant testPlant = new Plant(36, "TestPlant", 1, 5, 1.5, 1.0, 5,
                2.5, 2.0, 5, 3.5, 3.0, 5, 4.5, 4.0, 1);


    Plot testPlot = new Plot(9999, "Test PLot", 5, LocalDate.now(), testPlant, 1, 1.0, 1.0, 1.0  );

    @Test
    public void test1() {

       assertEquals(3.0,  testPlot.getBasic(11, LocalDate.now()));
    }

    @Test
    public void test2(){
        assertEquals(0.0, testPlot.getBasic(23, LocalDate.now()) );
    }

    @Test
    public void test3() {
        LocalDate datePlanted = LocalDate.now().plusDays(10);
        assertEquals(3.5, testPlot.getOptimal(3, datePlanted ));
    }

    @Test
    public void test4() {

       assertEquals(5, testPlot.getPlotPriorityValue(2, LocalDate.now()));
    }

    @Test
    public void test5() {
        testPlot.setPriority(2.0);

        assertEquals(6, testPlot.getPlotPriorityValue(2, LocalDate.now()));
    }

    @Test
    public void test6() {
        assertEquals(0, testPlot.getPlotPriorityValue(50, LocalDate.now()));
    }

}
