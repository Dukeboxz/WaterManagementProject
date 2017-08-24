package Optimizer;


/*
* Class tht creates and modifies enviroment objects
 */
public class Environment {

    private int id;
    private String name;
    private double value;

    public Environment(int id, String name, double value) {
        this.id = id;
        this.name = name;
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
}
