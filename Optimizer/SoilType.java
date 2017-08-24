package Optimizer;

/**
 * Created by stephen on 10/07/17.
 * class to create object to store soil information
 */
public class SoilType {

    int id;
    String name;
    double value;

    public SoilType(int id, String name, double value){
        this.id=id;
        this.name=name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }



    @Override
    public String toString() {
        return "The soilType id is " + id + "\n" +
                "The name is " + name + "\n" +
                "The value is " + getValue();
    }
}
