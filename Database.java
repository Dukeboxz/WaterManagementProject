import com.jom.DoubleMatrixND;
import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.application.Application;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by stephen on 15/06/17.
 * Class that has methods that access the database created for the project
 */
public class Database {

    static Connection conn = null;
    static Statement stmt = null;

    /**
     * Method to check if user name exists
     * @param userName
     * @return
     * @throws SQLException
     */
    public static boolean userNameExists(String userName) throws  SQLException{

        boolean doesExist = true;
        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }
        System.out.println("working");

        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e){ }

        String checkUserNameString = "SELECT id FROM users WHERE name = ?";
        PreparedStatement checkedUserName = conn.prepareStatement(checkUserNameString);
        checkedUserName.setString(1, userName);

        try {
            ResultSet r = checkedUserName.executeQuery();
            if(!r.next()){
                doesExist = false;
            }

        } catch (SQLException missing) {

        }
        conn.close();
        return doesExist;
    }

    /**
     * Create new User object based on name
     * @param name
     * @return
     * @throws SQLException
     */
    public static User createUser(String name) throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("driver");
        }
        System.out.println("working");

        String user = "stephen";
        String password = "Keyb0ard";

        conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);

        String createUserString = "SELECT * FROM users WHERE name=?";
        PreparedStatement createUserStatement = conn.prepareStatement(createUserString);
        createUserStatement.setString(1, name);

        ResultSet rs = createUserStatement.executeQuery();

        int id =0;
        String theName = "";
        String theEmail = "";
        String thePassword = "";

        while(rs.next()){
            id = rs.getInt("id");
            theName = rs.getString("name");
            theEmail = rs.getString("email");
            thePassword = rs.getString("password");
        }

        return new User(id, theName, theEmail, thePassword );

    }


    /**
     * Get soil type value based on soil type id
     * @param soilTypeID
     * @return
     * @throws SQLException
     */
    public static double getSoilTypeValue(int soilTypeID) throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }
        System.out.println("working");

        String user = "stephen";
        String password = "Keyb0ard";

        conn= DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);

        if(conn!=null) {
            System.out.println("Connection ");
        }
        String getSoilString = "SELECT * FROM soilType WHERE id=?";

        PreparedStatement getSoil = conn.prepareStatement(getSoilString);
        getSoil.setInt(1, soilTypeID);

        ResultSet r = getSoil.executeQuery();

        double soilValue=0;
        String name = null;

        while(r.next()) {

            name = r.getString("name");
            soilValue = r.getDouble("value");

        }
        conn.close();

        return soilValue;



    }


    /**
     * Method get environmental value based on environmentid
     * @param environmentid
     * @return
     * @throws SQLException
     */
    public static double getEnvironmentValue(int environmentid) throws SQLException{

        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }
        System.out.println("working");

        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e) {

        }
        if(conn!=null) {
            System.out.println("Connection ");
        }

        String getEnvironmentString = " SELECT value FROM Environment WHERE id=?";

        PreparedStatement getEnvironment = conn.prepareStatement(getEnvironmentString);
        getEnvironment.setInt(1,environmentid);


        ResultSet r = getEnvironment.executeQuery();
        double value = 0.0;

        while(r.next()){
            value = r.getDouble("value");
        }

        conn.close();

        return  value;


    }

    /**
     * creates Plant object based on plant id
     * @param plantid
     * @return
     * @throws SQLException
     */
    public static Plant createPlant(int plantid) throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }
        System.out.println("working");

        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e) {

        }
        if(conn!=null) {
            System.out.println("Connection ");
        }

        String getPlantsString = " SELECT * FROM plants WHERE id=?";

        PreparedStatement getPlants = conn.prepareStatement(getPlantsString);
        getPlants.setInt(1,plantid);


        ResultSet r = getPlants.executeQuery();

        String name = null;
        int id=0, st1Days=0;
        int  st2Days = 0, st3Days = 0, st4Days = 0, numPerMeter = 0;
        double st1OW = 0.0, st1BW= 0.0, st2OW = 0.0, st2BW= 0.0, st3OW = 0.0,
                st3BW= 0.0, st4OW= 0.0, st4BW = 0.0;




        while(r.next()){

            id = r.getInt("id");
            name = r.getString("name");
            st1Days = r.getInt("st1_days");
            st1OW = r.getDouble("st1_ow");
            st1BW = r.getDouble("st1_bw");
            st2Days = r.getInt("st2_days");
            st2OW= r.getDouble("st2_ow");
            st2BW= r.getDouble("st2_bw");
            st3Days= r.getInt("st3_days");
            st3OW= r.getDouble("st3_ow");
            st3BW= r.getDouble("st3_bw");
            st4Days= r.getInt("st4_days");
            st4OW = r.getDouble("st4_ow");
            st4BW= r.getDouble("st4_bw");
            numPerMeter= r.getInt("plants_persqm");


        }

        System.out.println(name + " " + st2Days + " " + st1OW +" " + st1BW);

        Plant plant = new Plant(id, name, st1Days, st1OW, st1BW, st2Days, st2OW, st2BW, st3Days, st3OW, st3BW,
                st4Days, st4OW, st4BW, numPerMeter);

        conn.close();

        return  plant;




    }


    /**
     * Create Plot object based on plot id
     * @param plotID
     * @return
     * @throws SQLException
     */
    public static Plot createPlot(int plotID) throws SQLException  {

        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }
        System.out.println("working");

        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e) {

        }
        if(conn!=null) {
            System.out.println("Connection ");
        }

        String createPlotString = " SELECT * from plots WHERE id=?";

        String name ="";
        double size = 0.0;
        int day = 0;
        int month = 0 ;
        int year = 0;

        int plantID = 0;
        int soilID = 0;
        int environmentID = 0;
        int priority = 1;

        try {
          PreparedStatement getPlot =  conn.prepareStatement(createPlotString);
          getPlot.setInt(1, plotID);

          ResultSet r = getPlot.executeQuery();


          while(r.next()){
              name = r.getString("name");
              size = r.getDouble("size");
              day = r.getInt("day_planted");
              month = r.getInt("month_planted");
              year = r.getInt("year_planted");
              soilID= r.getInt("soiltypeid");
              environmentID = r.getInt("environmentid");
              plantID = r.getInt("plantid");
              priority = r.getInt("priority");




          }
        }

        catch(SQLException e){

        }

        LocalDate datePlanted = LocalDate.of(year, month, day);


            Plant plant = createPlant(plantID);

            Double soilValue = getSoilTypeValue(soilID);
            double environmentValue = getEnvironmentValue(environmentID);
            int number = plant.getNumberPerMeter()* (int)size;
            Plot plot = new Plot(name, size, datePlanted, plant,  number, soilValue, environmentValue, priority);



        conn.close();

        return plot;





    }

    /**
     * Create hashmap of soil variables for using in showing option in UI
     * @return
     * @throws SQLException
     */
    public static HashMap<String, Double> getSoilVariables() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }
        System.out.println("working");

        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e) {

        }

        HashMap<String, Double> soilNameAndValues = new HashMap<>();

        String getSoilVairablesString = "SELECT * FROM soiltype";
        PreparedStatement getSoilVariablePS = conn.prepareStatement(getSoilVairablesString);



        ResultSet rs = getSoilVariablePS.executeQuery();

        double correctValue = 0.0;

        while(rs.next()){
            String name = rs.getString("name").trim();
            double soilValue = rs.getDouble("value");
            soilNameAndValues.put(name, soilValue);


        }

        conn.close();

        return soilNameAndValues;
    }

    /**
     * Method to return list of soilTypes objects
     * @return List<SoilType></SoilType>
     */
    public static ArrayList<SoilType> returnSoilTypeList() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }
        System.out.println("working");

        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e) {

        }

        ArrayList<SoilType> theSoilTypes = new ArrayList<>();

        String returnSoilTypeList = "SELECT * FROM soiltype;";
        try {
            PreparedStatement returnSoilTypePS = conn.prepareStatement(returnSoilTypeList);
            ResultSet rs = returnSoilTypePS.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double value = rs.getDouble("value");

                SoilType soilTypeObject = new SoilType(id, name, value);

                theSoilTypes.add(soilTypeObject);

            }
        } catch(SQLException e){

        }


        return theSoilTypes;

    }

    /**
     * creates hash map of plant names and id based on planttype
     * @param plantType
     * @return
     */
    public static HashMap<Integer, String> returnPlantDetails(int plantType) {

        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }


        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e) {

        }

        HashMap<Integer, String> plantDetails = new HashMap<>();
        try {
            String returnPlantDetailsString = "SELECT id, name FROM plants where type=?;";
            PreparedStatement returnPlantDetailsPS = conn.prepareStatement(returnPlantDetailsString);
            returnPlantDetailsPS.setInt(1, plantType);



            ResultSet rs = returnPlantDetailsPS.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                plantDetails.put(id, name);

            }

        } catch(SQLException e){

        }
        try{
            conn.close();
        } catch(SQLException b){

        }


        return plantDetails;

    }

    /**
     * returns a garden object based on user and garden name
     * @param name
     * @param gardenUser
     * @return
     */
    public static Garden userGardenReturn(String name, User gardenUser){
        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }


        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e) {

        }
        int gardenid = 0;
        String gardenName = null;
        int gardenUserID = 0;
        ArrayList<Plot> plots = new ArrayList<>();
        String gardenLocation = null;

        try{
        String userGardenReturnString = "Select * FROM garden WHERE name=? and id=?;";
        PreparedStatement userGardenReturnPS = conn.prepareStatement(userGardenReturnString);
        userGardenReturnPS.setString(1, name);
        userGardenReturnPS.setInt(2, gardenUser.getId());



        ResultSet rs =  userGardenReturnPS.executeQuery();

         gardenid = rs.getInt("id");
         gardenName = rs.getString("name");
        gardenUserID = rs.getInt("userid");
          gardenLocation = rs.getString("location");


        conn.close();

        }
        catch(SQLException e){

            }

            return new Garden(gardenid, gardenName, plots,  gardenUserID, gardenLocation);
    }

    /**
     * creates new garden in database
     * @param Gname
     * @param gardenUser
     */
    public static  void createNewGarden(String Gname, User gardenUser, String location, String locationReference){

        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }


        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e) {

        }

        try{

            String createNewGardenString = "INSERT INTO garden(name, userid, location, location_reference) VALUES(?, ?, ?, ?);";
            PreparedStatement createNewGardenPS = conn.prepareStatement(createNewGardenString);
            createNewGardenPS.setString(1, Gname);
            createNewGardenPS.setInt(2, gardenUser.getId());
            createNewGardenPS.setString(3, location);
            createNewGardenPS.setString(4, locationReference);

            createNewGardenPS.executeUpdate();
        } catch (SQLException c){

            c.printStackTrace();

        }
        try {
            conn.close();
        } catch(SQLException f){

        }
    }


    /**
     * create Garden object from database information using gardenID
     * @param gardenID
     * @return
     * @throws SQLException
     */
    public static Garden createGarden(int gardenID) throws SQLException{
        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }


        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e) {

        }
        if(conn!=null) {
            System.out.println("Connection ");
        }

        String createGardenString = "SELECT id FROM plots WHERE gardenid=?";
        PreparedStatement createGarden = conn.prepareStatement(createGardenString);
        createGarden.setInt(1,gardenID);

        String gardenNameString = "Select * FROM garden where id=?";
        PreparedStatement gardenname = conn.prepareStatement(gardenNameString);
        gardenname.setInt(1, gardenID);

        ResultSet r = createGarden.executeQuery();
        ResultSet n = gardenname.executeQuery();

        String name=null;
        int gardensid = 0;
        int userID = 0;
        String gardenLocation = null;

        ArrayList<Plot> gardenPlots = new ArrayList<>();



        while(n.next()){
             gardensid = n.getInt("id");
            name = n.getString("name");
            userID = n.getInt("userid");
            gardenLocation = n.getString("location");
        }


        while(r.next()){

           int plotid = r.getInt("id");

            Plot nextPlot = createPlot(plotid);

            gardenPlots.add(nextPlot);


        }
        Garden newGarden = new Garden(gardensid, name, gardenPlots, userID, gardenLocation);

        conn.close();

        return newGarden;

    }

    /**
     * Inserts new user into database
     * @param userName
     * @param email
     * @param passWord
     * @return
     * @throws SQLException
     */
    public static boolean insertUserNameANDPassword(String userName, String email,  String passWord) throws SQLException{

        boolean userNameNotUsed = true;

        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }


        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e) {

        }

        String checkuserNameString = "SELECT name FROM users";
        PreparedStatement checkUserName = conn.prepareStatement(checkuserNameString);

        ResultSet rs1 = checkUserName.executeQuery();
        String nameFromDatabase = "";

        while(rs1.next()){
            nameFromDatabase = rs1.getString("name");
            System.out.println(nameFromDatabase + " " + userName);
            if(nameFromDatabase==userName){
                System.out.println("FALSE");
                userNameNotUsed = false;
                System.out.println("User Name Already in Use");
                break;
            }
        }

        if(userNameNotUsed==true){
            String insertNewUserString = "INSERT INTO users(name, email, password) VALUES(?, ?, ?)";
            PreparedStatement insertNewUser = conn.prepareStatement(insertNewUserString);
            insertNewUser.setString(1, userName);
            insertNewUser.setString(2, email);
            insertNewUser.setString(3, passWord);

            insertNewUser.executeUpdate();
        }

        conn.close();
        return userNameNotUsed;




    }
    // Method for getting garden Names

    public static List<Garden> getUsersGardens(int userID ) throws  SQLException{
        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }


        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e) {

        }

        String getUserGardenNamesString = "SELECT id FROM garden where userid = ?";
        PreparedStatement getUsersGardenNames = conn.prepareStatement(getUserGardenNamesString);
        getUsersGardenNames.setInt(1, userID);

        ResultSet rs = getUsersGardenNames.executeQuery();

        String name = "";
        ArrayList<Garden> nameList = new ArrayList<>();

        while(rs.next()){
            int id = rs.getInt("id");

            nameList.add(createGarden(id));
        }

        conn.close();
        return nameList;

    }

    // method to insert new plot in database
    public static boolean insertNewPlot(String name, double size, int plantID, int gardenID, int soilTypeID, int environmentID,
                              int dayPlanted, int monthPlanted, int yearPlanted,  int priority) {

        boolean inserted = true;

        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }


        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/mscproject", user, password);
        } catch(SQLException e) {

        }

        String insertNewPlotString = "INSERT INTO plots(name, size, plantid, gardenid, soiltypeid, environmentid, " +
                "day_planted, month_planted, year_planted, priority) VALUES(?, ?,?,?,?,?,?,?,?, ?)";

        try {
            PreparedStatement insertNewPlotPS = conn.prepareStatement(insertNewPlotString);
            insertNewPlotPS.setString(1, name);
            insertNewPlotPS.setDouble(2, size);
            insertNewPlotPS.setInt(3, plantID);
            insertNewPlotPS.setInt(4, gardenID);
            insertNewPlotPS.setInt(5, soilTypeID);
            insertNewPlotPS.setInt(6, environmentID);
            insertNewPlotPS.setInt(7, dayPlanted);
            insertNewPlotPS.setInt(8, monthPlanted);
            insertNewPlotPS.setInt(9, yearPlanted);
            insertNewPlotPS.setInt(10, priority);

            insertNewPlotPS.executeUpdate();

            conn.close();
        } catch(SQLException e){
            inserted = false;
            System.out.println("The plot was not inserted");
        }

        return inserted;

    }

    /**
     * Main method used for testing in development
     * @param args
     */
    public static void main(String[] args) {

        String one = "Stephen";
        String two = "Stephen";

       // System.out.println(one.equals(two));

        try {
           // System.out.println(userNameExists("MyUserName"));
            //System.out.println(createUser("MyUserName").getPassword());
//            User test = createUser("PekingBroth");
//            System.out.println(test.getPassword());
//            System.out.println(test.getId());
//
//            List<Garden> test = new ArrayList<>();
//            test = getUsersGardens(2);
//
//           for(Garden n : test){
//               List<Plot> garden = n.getPlots();
//               for( Plot np : garden) {
//                   System.out.println(np.getName());
//               }

            User u = createUser("Stephen");

            createNewGarden("specialGarden", u, "Birmingham", "Test");

            List<SoilType> test = returnSoilTypeList();

            for(SoilType a : test){
                System.out.println(a);
                System.out.println();
            }



           // System.out.println(u.getPassword());

           // insertNewPlot("My Tomatoe Plot", 5, 4, 3, 2, 1, 10 , 5,  2017, 2);


       }

        catch ( SQLException r){}

//        try {
//           Database.insertUserNameANDPassword("Stephen", "sjjjjj@Jackson.com", "boop");
//
//
//        } catch( SQLException e) {
//            e.printStackTrace();
//            System.out.println("Not working");
//
//        }
    }
}
