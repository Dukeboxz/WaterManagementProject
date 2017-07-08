/**
 * Created by stephen on 14/06/17.
 * class to create Plant objects
 */
public class Plant {

    String name;
    int st1_days, st2_days, st3_days, st4_days;
    double st1_or, st2_or, st3_or, st4_or;
    double st1_br, st2_br, st3_br, st4_br;
    int numberPerMeter;


    public Plant(){

    }

    public Plant(String name,
                 int st1Days,
                 double st1OR,
                 double st1BR,
                 int st2Days,
                 double st2OR,
                 double st2BR,
                 int st3Days,
                 double st3OR,
                 double st3_br,
                 int st4Days,
                 double st4OR,
                 double st4BR,
                 int numberPerMeter ){
        this.name=name;
        this.st1_days=st1Days;
        this.st1_or=st1OR;
        this.st1_br=st1BR;

        this.st2_days=st2Days;
        this.st2_or=st2OR;
        this.st2_br=st2BR;

        this.st3_days= st3Days;
        this.st3_or=st3OR;
        this.st3_br=st3_br;

        this.st4_days=st4Days;
        this.st4_or=st4OR;
        this.st4_br=st4BR;

        this.numberPerMeter=numberPerMeter;


    }

    public int getSt1_days() {
        return st1_days;
    }

    public int getSt2_days() {
        return st2_days;
    }

    public int getSt3_days() {
        return st3_days;
    }

    public int getSt4_days() {
        return st4_days;
    }

    public double getSt1_or() {
        return st1_or;
    }

    public double getSt2_or() {
        return st2_or;
    }

    public double getSt3_or() {
        return st3_or;
    }

    public double getSt4_or() {
        return st4_or;
    }

    public double getSt1_br() {
        return st1_br;
    }

    public double getSt2_br() {
        return st2_br;
    }

    public double getSt3_br() {
        return st3_br;
    }

    public double getSt4_br() {
        return st4_br;
    }

    public int getNumberPerMeter() {
        return numberPerMeter;
    }

    public String getName() {

        return name;
    }
}
