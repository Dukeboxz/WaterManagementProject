import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephen on 15/06/17.
 * class to create Garden Object
 */
public class Garden {
    int gardenID;
    String name;
    ArrayList<Plot> plots;
    int userID;

    public Garden(int id, String name, ArrayList<Plot> plots, int userID) {
        this.gardenID=id;
        this.name= name;
        this.plots= new ArrayList<>();
        this.plots=plots;
        this.userID = userID;
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<Plot> getPlots(){
        return this.plots;
    }

    public int getGardenID() {
        return this.gardenID;
    }

    public int getUserID() {
        return this.getUserID();
    }

}
