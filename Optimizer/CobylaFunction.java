package Optimizer;

import con.cureos.numerics.Calcfc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CobylaFunction {


    private double rhobeg;
    private double rhoend;
    private int iprint;
    private int maxfun ;
    private double[][] desired;
    private double[][] basic;
    private double[][] priority;
    private double waterAvaiable;

    public CobylaFunction(double[][] desired,
                          double[][] basic,
                          double[][] priority,
                          double waterAvaiable){
        this.rhobeg = 0.5;
        this.rhoend = 1.0e6;
        this.iprint = 1;
        this.maxfun = 3500;

        this.desired=desired;
        this.basic=basic;
        this.priority=priority;
        this.waterAvaiable=waterAvaiable;
    }

    public double[][] getDesired() {
        return desired;
    }

    public double[][] getBasic() {
        return basic;
    }

    public double[][] getPriority() {
        return priority;
    }

    public double getWaterAvaiable() {
        return waterAvaiable;
    }

    public double[][] optimize(){
        double[] upper = new double[this.getDesired().length*this.getDesired()[0].length];
        double[] lower = new double[this.getBasic().length*this.getBasic()[0].length];
        double[] priority = new double[this.getPriority().length*this.getPriority()[0].length];

        int counter=0;
        double desiredTotal = 0;
        for(int i = 0; i < this.getDesired().length; i++){
            for(int j = 0 ; j <this.getDesired()[0].length; j++){
                upper[counter] = this.getDesired()[i][j];
                lower[counter]= this.getBasic()[i][j];
                priority[counter] = this.getPriority()[i][j];
                counter++;
                desiredTotal+= this.getDesired()[i][j];
            }
        }

        if(this.getWaterAvaiable() > desiredTotal){
            return this.getDesired();
        } else {



            Calcfc calcfc = new Calcfc() {
                @Override
                public double Compute(int n, int m, double[] x, double[] con) {
                    double total = 0;
                    for(double b: x){
                        total+=b;
                    }
                    con[0] = CobylaFunction.this.getWaterAvaiable() - total;

                    int conCounter = 1;
                    for(int i = 0; i < x.length; i++){
                        con[conCounter]=x[i]-lower[i];

                        con[conCounter+1]=upper[i]-x[i];
                        con[conCounter+2] = x[i]-x[i];
                        conCounter=conCounter+3;

                    }

                    MathContext ms = new MathContext(4, RoundingMode.HALF_UP);
                    BigDecimal objective =  new BigDecimal(0.0, ms);

                    for(int i = 0; i < x.length; i++) {
                        MathContext mc = new MathContext(4);
                        double doubleUpper = upper[i];
                        double doubleX = x[i];
                        double priorityDouble = priority[i];

                        double initalValue = (upper[i] - x[i]) * priority[i];
                        BigDecimal upper = new BigDecimal(doubleUpper, ms);


                        BigDecimal xBD = new BigDecimal(doubleX, ms);

                        BigDecimal prioirty = new BigDecimal(priorityDouble, ms);

                        BigDecimal theFirstValue = upper.subtract(xBD);

                        BigDecimal theSecondValue = theFirstValue.multiply(prioirty);

                        objective = objective.add(theSecondValue);


                    }


                    return objective.doubleValue();

//52

                }
            };

            return desired;
        }
    }
}
