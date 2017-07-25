import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephen on 15/06/17.
 * class to create Garden Object
 */
public class Garden {
   private int gardenID;
     private String name;
     private ArrayList<Plot> plots;
    private int userID;
     private String location;
     boolean userEditRights;

    public Garden(int id, String name, ArrayList<Plot> plots, int userID, String location, Boolean editRights) {
        this.gardenID=id;
        this.name= name;
        this.plots= new ArrayList<>();
        this.plots=plots;
        this.userID = userID;
        this.location=location;
        this.userEditRights=editRights;
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

    public String getLocation() { return this.location;}

    public boolean getUserEditRights() { return  this.userEditRights;}

}
