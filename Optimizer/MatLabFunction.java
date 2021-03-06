package Optimizer;

import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import waterOp.Water;

import java.time.LocalDate;

/*
Class that uses function contaimed within the MatLab Java Builder Library to call created MatLab functions and then send return data back to Optimizer Class
Author Stephen Jackson
MatLab code created by Dr Morteza Azad University of Birmingham and then modified by Stephen Jackson
 */

public class MatLabFunction {

    private double[][] optimalMatrix;
    private double[][] basicMatrix;
    private double[][] priorityMatrix;
    private double waterAvailable;


    public MatLabFunction(double[][] optimalMatrix,
                          double[][] basicMatrix,
                          double[][] priorityMatrix,
                          double waterAvailable) {

        this.optimalMatrix=optimalMatrix;
        this.basicMatrix=basicMatrix;
        this.priorityMatrix=priorityMatrix;
        this.waterAvailable=waterAvailable;




    }

    // Getters and Setters
    public double[][] getOptimalMatrix() {
        return optimalMatrix;
    }

    public double[][] getBasicMatrix() {
        return basicMatrix;
    }

    public double[][] getPriorityMatrix() {
        return priorityMatrix;
    }

    public double getWaterAvailable() {
        return waterAvailable;
    }


    // Method that calls MatLab function
    public double[][] callMatLabFunction() {

//        try {
//            System.setProperty("java.library.path", "/home/stephen/IdeaProjects/MSCproject/out/artifacts/lib/libmwmclmcrrt.so.9.2");
//            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
//            fieldSysPath.setAccessible(true);
//            fieldSysPath.set(null, null);
//        }catch (NoSuchFieldException e){
//
//        } catch (IllegalAccessException f){
//
//        }

        System.out.println(System.getProperty("java.library.path"));

        Object[] input = new Object[4];
        Object[] output = new Object[this.getOptimalMatrix().length*this.getOptimalMatrix()[0].length];
        double[][] solutionMatrix = new double[this.getOptimalMatrix().length][this.getOptimalMatrix()[0].length];
        try{
             Water theClass=new Water();
            input[0] = this.getOptimalMatrix();
            input[1] = this.getBasicMatrix();
            input[2] = this.getPriorityMatrix();
            input[3] = this.getWaterAvailable();

            // get result from matlab function
            output= theClass.waterOp(1, input);
            //get results and turn back to double[][] matrix
            MWNumericArray resultSet = (MWNumericArray)output[0];
            solutionMatrix = (double[][])resultSet.toDoubleArray();


            theClass.dispose();
            resultSet.dispose();

        } catch (MWException f){
            f.printStackTrace();
        }

        return solutionMatrix;
    }


   public static void main(String[] args)
   {

//        Garden testGarden = Database.createGarden(2, true);
//
//        Optimiser optimiserTest = new Optimiser(testGarden, 10 , 4500, LocalDate.now(), false, true);
//
//       MatLabFunction functionTest = new MatLabFunction(optimiserTest.createOptimalMatrix(), optimiserTest.createBasicMatrix(), optimiserTest.createPriorityMatrix(), optimiserTest.getWaterAvailable());
//
//        double[][] sol = functionTest.callMatLabFunction();
  }


}
