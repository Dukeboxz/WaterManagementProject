package Optimizer;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by stephen on 15/06/17.
 * Class that has static methoda that access the database and return required information that it then sends to other classes
 * in required datastructures
 */
public class Database {

    static Connection conn = null;
    static Statement stmt = null;
    static String host = "//localhost/mscproject";
    static String thePassword = "Keyb0ard";
    static String theUserName = "stephen";

    /**
     * Method to check if user name exists
     * @param userName
     * @return
     * @throws SQLException
     */
    public static boolean userNameExists(String userName) {


        boolean doesExist = false;
        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("driver");
        }
        System.out.println("working");

        String user = "stephen";
        String password = "Keyb0ard";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql:"+host, theUserName, thePassword);
        } catch(SQLException e){ }

        try {
        String checkUserNameString = "SELECT name FROM users";
        PreparedStatement checkedUserName = conn.prepareStatement(checkUserNameString);

        String userElemnt = null;



            ResultSet r = checkedUserName.executeQuery();
            while(r.next() & doesExist==false){
                userElemnt = r.getString("name");

                userElemnt.trim();
                System.out.println("**" + userElemnt + "**");


                if(userElemnt.equals(userName)){
                    doesExist=true;
                }
            }

            conn.close();
        } catch (SQLException missing) {

        }

        return doesExist;
    }

    /**
     * Create new Optimizer.User object based on name
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
     * creates Optimizer.Plant object based on plant id
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
        int id=0, type=0, st1Days=0;
        int  st2Days = 0, st3Days = 0, st4Days = 0, numPerMeter = 0;
        double st1OW = 0.0, st1BW= 0.0, st2OW = 0.0, st2BW= 0.0, st3OW = 0.0,
                st3BW= 0.0, st4OW= 0.0, st4BW = 0.0;




        while(r.next()){

            id = r.getInt("id");
            name = r.getString("name");
            type = r.getInt("type");
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



        Plant plant = new Plant(id, name, type,  st1Days, st1OW, st1BW, st2Days, st2OW, st2BW, st3Days, st3OW, st3BW,
                st4Days, st4OW, st4BW, numPerMeter);

        conn.close();

        return  plant;




    }

    /**
     * Method that returns map of all plants within the database as map
     * @return Map<String, Plant> key is the name of the plant
     */
    public static Map<String, Plant> returnMapOfAllPlants() {
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

        String returnPlantMapStrng = "SELECT * FROM plants;";

        Map<String, Plant> thePlantMap = new TreeMap<>();

        String name = null;
        int id=0, type=0, st1Days=0;
        int  st2Days = 0, st3Days = 0, st4Days = 0, numPerMeter = 0;
        double st1OW = 0.0, st1BW= 0.0, st2OW = 0.0, st2BW= 0.0, st3OW = 0.0,
                st3BW= 0.0, st4OW= 0.0, st4BW = 0.0;

        try {
            PreparedStatement returnPlantMapPS = conn.prepareStatement(returnPlantMapStrng);

            ResultSet r = returnPlantMapPS.executeQuery();

            while(r.next()){

                id = r.getInt("id");
                name = r.getString("name");
                type = r.getInt("type");
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


                Plant plant = new Plant(id, name, type,  st1Days, st1OW, st1BW, st2Days, st2OW, st2BW, st3Days, st3OW, st3BW,
                        st4Days, st4OW, st4BW, numPerMeter);


                thePlantMap.put(name, plant);
            }


            conn.close();
        } catch (SQLException e){

        }

        return thePlantMap;
    }


    /**
     * Create Optimizer.Plot object based on name and date
     * @param
     * @return
     * @throws SQLException
     */
    public static Plot createPlot(String plotName, int dayPlanted, int monthPlanted, int yearPlanted) throws SQLException  {

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

        String createPlotString = " SELECT * from plots WHERE name=? AND day_planted=? AND month_planted=? AND year_planted=? ";

        int id = 0;
        String name ="";
        double size = 0.0;
        int day = 0;
        int month = 0 ;
        int year = 0;

        int plantID = 0;
        int soilID = 0;
        int environmentID = 0;
        double priority = 1;

        try {
          PreparedStatement getPlot =  conn.prepareStatement(createPlotString);
          getPlot.setString(1, plotName);
          getPlot.setInt(2, dayPlanted);
          getPlot.setInt(3, monthPlanted);
          getPlot.setInt(4, yearPlanted);

          ResultSet r = getPlot.executeQuery();


          while(r.next()){
              id = r.getInt("id");
              name = r.getString("name");
              size = r.getDouble("size");
              day = r.getInt("day_planted");
              month = r.getInt("month_planted");
              year = r.getInt("year_planted");
              soilID= r.getInt("soiltypeid");
              environmentID = r.getInt("environmentid");
              plantID = r.getInt("plantid");
              priority = r.getDouble("priority");




          }
        }

        catch(SQLException e){

        }

        LocalDate datePlanted = LocalDate.of(year, month, day);


            Plant plant = createPlant(plantID);

            Double soilValue = getSoilTypeValue(soilID);
            double environmentValue = getEnvironmentValue(environmentID);
            int number = plant.getNumberPerMeter()* (int)size;
            Plot plot = new Plot(id, name, size, datePlanted, plant,  number, soilValue, environmentValue, priority);



        conn.close();

        return plot;





    }

    /**
     * Create Optimizer.Plot object based on name and date
     * @param
     * @return
     * @throws SQLException
     */
    public static Plot createPlotBasedOnId(int plotid) throws SQLException  {

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

        String createPlotString = " SELECT * from plots WHERE  id=?";

        int id = 0;
        String name ="";
        double size = 0.0;
        int day = 0;
        int month = 0 ;
        int year = 0;

        int plantID = 0;
        int soilID = 0;
        int environmentID = 0;
        double priority = 1;

        try {
            PreparedStatement getPlot =  conn.prepareStatement(createPlotString);
            getPlot.setInt(1, plotid);

            ResultSet r = getPlot.executeQuery();


            while(r.next()){
                id = r.getInt("id");
                name = r.getString("name");
                size = r.getDouble("size");
                day = r.getInt("day_planted");
                month = r.getInt("month_planted");
                year = r.getInt("year_planted");
                soilID= r.getInt("soiltypeid");
                environmentID = r.getInt("environmentid");
                plantID = r.getInt("plantid");
                priority = r.getDouble("priority");




            }
        }

        catch(SQLException e){

        }

        LocalDate datePlanted = LocalDate.of(year, month, day);


        Plant plant = createPlant(plantID);

        Double soilValue = getSoilTypeValue(soilID);
        double environmentValue = getEnvironmentValue(environmentID);
        int number = plant.getNumberPerMeter()* (int)size;
        Plot plot = new Plot(id, name, size, datePlanted, plant,  number, soilValue, environmentValue, priority);



        conn.close();

        return plot;





    }


    /**
     * Create hashmap of soil variables for using in showing option in UI
     * @return
     * @throws SQLException
     */
    public static HashMap<String, Double> getSoilVariables()  {
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
        try {

            String getSoilVairablesString = "SELECT * FROM soiltype";
            PreparedStatement getSoilVariablePS = conn.prepareStatement(getSoilVairablesString);


            ResultSet rs = getSoilVariablePS.executeQuery();

            double correctValue = 0.0;

            while (rs.next()) {
                String name = rs.getString("name").trim();
                double soilValue = rs.getDouble("value");
                soilNameAndValues.put(name, soilValue);


            }

            conn.close();
        } catch (SQLException e){

        }

        return soilNameAndValues;
    }

    /**
     * Method to return list of soilTypes objects
     * @return List<Optimizer.SoilType></Optimizer.SoilType>
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
    public static TreeMap<String, Integer> returnPlantDetails(int plantType) {

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

        TreeMap<String, Integer> plantDetails = new TreeMap<>();
        try {
            String returnPlantDetailsString = "SELECT id, name FROM plants where type=? ORDER BY name;";
            PreparedStatement returnPlantDetailsPS = conn.prepareStatement(returnPlantDetailsString);
            returnPlantDetailsPS.setInt(1, plantType);



            ResultSet rs = returnPlantDetailsPS.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println(name);
                plantDetails.put(name, id);

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
        boolean edit =false;
        Garden theGarden=null;
        try{


        String UserGardenReturnString = "SELECT * FROM garden WHERE name=? AND userid=?;";
        PreparedStatement userGardenReturnPS = conn.prepareStatement(UserGardenReturnString);
        userGardenReturnPS.setString(1, name);
        userGardenReturnPS.setInt(2, gardenUser.getId());

        ResultSet rs = userGardenReturnPS.executeQuery();

        while(rs.next()){
            gardenid = rs.getInt("id");

        }
        theGarden = createGarden(gardenid, true);

        }
        catch(SQLException e){

        }





        return theGarden;
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


            String getNewGardenID = "SELECT id FROM garden WHERE name=? AND userid=?;";
            PreparedStatement getNewGardenIDPS = conn.prepareStatement(getNewGardenID);
            getNewGardenIDPS.setString(1, Gname);
            getNewGardenIDPS.setInt(2, gardenUser.getId());

            ResultSet rs = getNewGardenIDPS.executeQuery();

            int id = 0;

            while(rs.next()) {
                id = rs.getInt("id");
            }

            insertNewUserGarden(id, gardenUser.getId(), true );
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
    public static Garden createGarden(int gardenID, boolean edit) {
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


        String name=null;
        int gardensid = 0;
        int userID = 0;
        String gardenLocation = null;
        ArrayList<Plot> gardenPlots = new ArrayList<>();

        try {
        String createGardenString = "SELECT id FROM plots WHERE gardenid=?";
        PreparedStatement createGarden = conn.prepareStatement(createGardenString);
        createGarden.setInt(1,gardenID);

        String gardenNameString = "Select * FROM garden where id=?";
        PreparedStatement gardenname = conn.prepareStatement(gardenNameString);
        gardenname.setInt(1, gardenID);









            ResultSet r = createGarden.executeQuery();
            ResultSet n = gardenname.executeQuery();


            while (n.next()) {
                gardensid = n.getInt("id");
                name = n.getString("name");
                userID = n.getInt("userid");
                gardenLocation = n.getString("location_reference");


            }


            while (r.next()) {

                int plotid = r.getInt("id");

                Plot nextPlot = createPlotBasedOnId(plotid);

                gardenPlots.add(nextPlot);


            }

            System.out.println("Not sorted");
            for (Plot p : gardenPlots) {
                System.out.println(p.getName() + " id  " + p.id);
            }

            Collections.sort(gardenPlots);

            System.out.println("after sort");
            for (Plot p : gardenPlots) {
                System.out.println(p.getName() + " id " + p.id);
            }

            conn.close();
        }catch(SQLException e){

        }


        Garden newGarden = new Garden(gardensid, name, gardenPlots, userID, gardenLocation, edit);



        return newGarden;

    }

    /**
     *
     *
     */

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
            nameFromDatabase = rs1.getString("name").trim();
            System.out.println(nameFromDatabase + " " + userName);
            if(nameFromDatabase.equals(userName)){
                System.out.println("FALSE");
                userNameNotUsed = false;
                System.out.println("Optimizer.User Name Already in Use");
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

    /**
     * Method to retrieve list of users gardens basen on their id
     * returns results in List of Garden Objects
     * @param userID
     * @return List<Garden></Garden>
     */
    public static List<Garden> getUsersGardens(int userID ) {
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
        ArrayList<Garden> nameList = new ArrayList<>();
        try {
            String getUserGardenNamesString = "SELECT * FROM gardenusers where userid = ?";
            PreparedStatement getUsersGardenNames = conn.prepareStatement(getUserGardenNamesString);
            getUsersGardenNames.setInt(1, userID);

            ResultSet rs = getUsersGardenNames.executeQuery();




            while (rs.next()) {
                int id = rs.getInt("gardenid");
                boolean edit = rs.getBoolean("editrights");

                nameList.add(createGarden(id, edit));
            }

            conn.close();

        }catch(SQLException e){

        }

        return nameList;
    }

    /**
     * method to insert new plot in database
     * @param name
     * @param size
     * @param plantID
     * @param gardenID
     * @param soilTypeID
     * @param environmentID
     * @param dayPlanted
     * @param monthPlanted
     * @param yearPlanted
     * @param priority
     * @return boolean
     */
    public static boolean insertNewPlot(String name, double size, int plantID, int gardenID, int soilTypeID, int environmentID,
                              int dayPlanted, int monthPlanted, int yearPlanted,  double priority) {

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
            insertNewPlotPS.setDouble(10, priority);

            insertNewPlotPS.executeUpdate();

            conn.close();
        } catch(SQLException e){
            inserted = false;
            System.out.println("The plot was not inserted");
        }

        return inserted;

    }

    /**
     * Method that checks to see if a user name exists in the database
     * @param userName
     * @return
     */
    public static boolean userNameNotExist(String userName){

        boolean nameExists = true;
        ;

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
        try {
            String checkuserNameString = "SELECT name FROM users";
            PreparedStatement checkUserName = conn.prepareStatement(checkuserNameString);

            ResultSet rs1 = checkUserName.executeQuery();
            String nameFromDatabase = "";

            while (rs1.next()) {
                nameFromDatabase = rs1.getString("name").trim();
                System.out.println(nameFromDatabase + " " + userName);
                if (nameFromDatabase.equals(userName)) {
                    System.out.println("FALSE");
                    nameExists = false;
                    System.out.println("Optimizer.User Name Already in Use");
                    break;
                }
            }

            conn.close();
        }catch(SQLException e){

        }


        return nameExists;

    }

    /**
     * Method to insert a new garden into the database
     * @param gardenid
     * @param userid
     * @param edit
     */
    public static void insertNewUserGarden(int gardenid, int userid, boolean edit){

        System.out.println("Insert new Optimizer.Garden Running");

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

        try {
            String insertGardenUserString = "INSERT INTO gardenusers Values(?, ? ,?);";

            PreparedStatement insertGardenUserPS = conn.prepareStatement(insertGardenUserString);
            insertGardenUserPS.setInt(1, gardenid);
            insertGardenUserPS.setInt(2, userid);
            insertGardenUserPS.setBoolean(3, edit);

            insertGardenUserPS.executeUpdate();

            conn.close();
        } catch (SQLException e){

            e.printStackTrace();
        }

    }

    /**
     * Method that access database and returns user name based on their id
     * @param name
     * @return
     */
    public static int getUserIDBasedOnName(String name){
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
        int theID = 0;
        try{
            System.out.println("*" + name + "*");
            String returnIDString = "SELECT * FROM users WHERE name=?;";
            PreparedStatement returnIDPS = conn.prepareStatement(returnIDString);
            returnIDPS.setString(1, name);

            ResultSet rs = returnIDPS.executeQuery();

            while(rs.next()) {
                theID = rs.getInt("id");
            }

        } catch(SQLException e ){
            e.printStackTrace();

        }

        return theID;
    }

    /**
     * Method that returns map of plants based on their plant type
     */
    public static Map<String, Plant> returnPlantsInMap(int plantType){

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



        Map<String, Plant> theMap = new TreeMap<>();

        try {
            String returnPlantinMapString = "SELECT * FROM plants WHERE type=?;";

            PreparedStatement returnPlantinMapStringPS = conn.prepareStatement(returnPlantinMapString);
            returnPlantinMapStringPS.setInt(1, plantType);

            String plantName = null;
            int plantId = 0;

            ResultSet rs = returnPlantinMapStringPS.executeQuery();
            while(rs.next()){
                plantName = rs.getString("name");
                plantId = rs.getInt("id");

                Plant newPlant = createPlant(plantId);

                theMap.put(plantName, newPlant);

            }

            conn.close();

        } catch (SQLException e) {

        }

        return theMap;

    }

    /**
     * Method that returns plant types in Map datastructure
     * @return Map
     */
    public static Map<Integer, String> getPlantTypesInMap() {

        Map<Integer, String> theMap = new TreeMap<>();

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

            String getPlantTypesInMapString = "SELECT * FROM planttypes;";
            PreparedStatement getPlantTypesInMapPS = conn.prepareStatement(getPlantTypesInMapString);

            String name = null;
            int id = 0;

            ResultSet rs = getPlantTypesInMapPS.executeQuery();

            while(rs.next()){
                id = rs.getInt("id");
                name = rs.getString("name");

                theMap.put(id, name);
            }

            conn.close();

        }catch(SQLException e){

        }

        return theMap;

    }

    /**
     * Method updates plot details within the database
     * @param plotId
     * @param plotName
     * @param size
     * @param plantid
     * @param datePlanted
     * @param priority
     */
    public static void updatePlot(int plotId, String plotName, double size, int plantid,  LocalDate datePlanted, double priority ){

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

        try {

            String updatePlotString = "UPDATE plots set name=?, size=?, plantid=?, day_planted=?, month_planted=?, year_planted=? , priority=? WHERE id=?;";
            PreparedStatement updatePlotPS = conn.prepareStatement(updatePlotString);
            updatePlotPS.setString(1, plotName);
            updatePlotPS.setDouble(2, size);
            updatePlotPS.setInt(3, plantid);
            updatePlotPS.setInt(4, datePlanted.getDayOfMonth());
            updatePlotPS.setInt(5, datePlanted.getMonthValue());
            updatePlotPS.setInt(6, datePlanted.getYear());
            updatePlotPS.setDouble(7, priority);
            updatePlotPS.setInt(8, plotId);

            updatePlotPS.executeUpdate();

            conn.close();

        } catch(SQLException e){

        }

    }

    /*
     method checks the database to see if given user values match the username and passoword in database
     */
    public static boolean userNameAndPasswordCheck(String userName, String thePassword){
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

        boolean areCorrect=false;

        try {
          String userNameAndPasswordString = "SELECT * FROM users WHERE name=? AND password=?;";
            PreparedStatement userNameAndPasswordPS = conn.prepareStatement(userNameAndPasswordString);
            userNameAndPasswordPS.setString(1, userName);
            userNameAndPasswordPS.setString(2, thePassword);

            ResultSet rs = userNameAndPasswordPS.executeQuery();




            if(!rs.next()) {
                areCorrect=false;

            } else {
                areCorrect=true;
            }

            conn.close();
        } catch(SQLException e){

        }

        return areCorrect;
    }

    /**
     * Removes plot from database
     * @param plotid
     */
    public static void deletePlot(int plotid){

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

        try {
            String deletePlotString = "DELETE FROM plots WHERE id=?;";
            PreparedStatement deletePlotPS = conn.prepareStatement(deletePlotString);
            deletePlotPS.setInt(1, plotid);

            deletePlotPS.executeUpdate();

            conn.close();

        }catch (SQLException e){
            System.out.println("Database Delete Operation Failed");
        }

    }

    /**
     * Method that gets details from environment table in database and then create and array list of Environment objects
     * @return
     */
    public static ArrayList<Environment> returnEnvironmentList() {

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

        ArrayList<Environment> theArrayList = new ArrayList<>();

        try
        {
            String EnvListString = "SELECT * FROM environment;";
            PreparedStatement envListPS = conn.prepareStatement(EnvListString);

            ResultSet rs = envListPS.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double value = rs.getDouble("value");

                Environment env = new Environment(id, name, value);
                theArrayList.add(env);
            }

            conn.close();

        } catch (SQLException e) {

        }

    return theArrayList;
    }




    /**
     * Main method used for testing in development
     * @param args
     */
    public static void main(String[] args) {

        System.out.println(userNameExists("Stephen"));

//        try {
//            User a = createUser("Stephen");
//            createNewGarden("TestGarden4", a, "Birmingham", "00000.54.03534");
//            Garden test = userGardenReturn("TestGarden3", a);
//
//            System.out.println(test.getUserEditRights());
//
//            System.out.println(test.getName());
//            System.out.println(test.getUserEditRights());
//
//
//
//        } catch(SQLException e){
//
//        }


            // System.out.println(userNameExists("MyUserName"));
            //System.out.println(createUser("MyUserName").getPassword());
//            Optimizer.User test = createUser("PekingBroth");
//            System.out.println(test.getPassword());
//            System.out.println(test.getId());
//
//            List<Optimizer.Garden> test = new ArrayList<>();
//            test = getUsersGardens(2);
//
//           for(Optimizer.Garden n : test){
//               List<Optimizer.Plot> garden = n.getPlots();
//               for( Optimizer.Plot np : garden) {
//                   System.out.println(np.getName());
//               }

            //createGarden(2);

          //  System.out.println(getUserIDBasedOnName("Optimizer.User Name"));


            // System.out.println(u.getPassword());

            // insertNewPlot("My Tomatoe Optimizer.Plot", 5, 4, 3, 2, 1, 10 , 5,  2017, 2);





//        try {
//           Optimizer.Database.insertUserNameANDPassword("Stephen", "sjjjjj@Jackson.com", "boop");
//
//
//        } catch( SQLException e) {
//            e.printStackTrace();
//            System.out.println("Not working");
//
//        }
    }
}
