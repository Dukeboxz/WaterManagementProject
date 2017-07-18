import com.jom.DoubleMatrixND;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sun.reflect.generics.tree.Tree;

import javax.swing.*;
import java.awt.*;
import java.awt.Button;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


/**
 * Created by stephen on 29/06/17.
 * Class to create UI for program using javaFX
 */
public class View  extends Application{

    String userName, email, password;
    Stage theStage;
    Scene welcomePage, userEntry, loginEntry;
    public static User theUser = new User();
    Garden theGarden;
    private static javafx.scene.control.Label gardenLabel;


    /**
     * method to create the garden selection scene in UI
     * @param stage
     * @param user
     * @throws SQLException
     */
    public static void setGardenScene(Stage stage, User user) throws SQLException {

        GridPane gardenGrid = new GridPane();
        Scene garden = new Scene(gardenGrid ,500, 500);

        java.util.List<Garden> gardenList = Database.getUsersGardens(user.getId());




        // Garden Selection Page
        javafx.scene.control.Label gardenLabel = new javafx.scene.control.Label();
        String  a = " the name is " + user.getName();
        gardenLabel.setText(a);
        javafx.scene.control.Label selectGarden = new javafx.scene.control.Label("Existing Garden");
        javafx.scene.control.Button load = new javafx.scene.control.Button("Load");
        javafx.scene.control.Button createNewGarden = new javafx.scene.control.Button("Create new Garden");
        createNewGarden.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addGarden(user, stage);
            }
        });


        ComboBox usersGardens = new ComboBox();
        int we = user.getId();



        for(Garden g : gardenList){
            usersGardens.getItems().add(g.getName());
        }


        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(usersGardens.getValue().equals(null)){
                    Alert noGarden = new Alert(Alert.AlertType.ERROR);
                    noGarden.setContentText("Please select a garden");
                } else {
                    String chosenGarden = usersGardens.getValue().toString();
                    for(Garden h : gardenList){
                        if(h.getName().equals(chosenGarden)){
                            setGardenAndPlotScene(stage, user, h);
                        }
                    }

                }



            }
        });
        gardenGrid.setAlignment(Pos.CENTER);
        gardenGrid.add(gardenLabel, 0, 1);
        gardenGrid.add(createNewGarden, 0, 2);
        gardenGrid.add(usersGardens, 0, 4);
        gardenGrid.add(selectGarden, 0, 3);
        gardenGrid.add(load, 1, 4);

        stage.setScene(garden);
    }

    /**
     * Method to create UI to allow user to enter garden
     * @param user
     * @param s
     */
    public static void addGarden(User user, Stage s){

        s.setTitle("Add New Garden");
        GridPane addGardenPane = new GridPane();
        Scene addGardenScene = new Scene(addGardenPane, 600 , 500);

        javafx.scene.control.Label createGardenLabel = new javafx.scene.control.Label("Garden Name");
        javafx.scene.control.TextField gardenNameText = new javafx.scene.control.TextField();

        javafx.scene.control.Label locationLabel = new javafx.scene.control.Label("Location Of Garden Type first 3 letters");


        ComboBox<String> potentialLoctions = new ComboBox();
        potentialLoctions.setEditable(true);

        Map<String, String> possibleLocations  = new TreeMap<>();
        int counter = 0;

        potentialLoctions.setOnAction((event) -> {
            System.out.println("Event Triggered");
            if(potentialLoctions.getValue()!=null) {
                String new_val = potentialLoctions.getValue();
                if (new_val.length() >= 3 & new_val.length() < 15) {

                    TreeMap<String, String> temp = WeatherData.getPotentialLocations(new_val);
                    temp.entrySet().forEach((e) -> possibleLocations.put(e.getKey(), e.getValue()));

                    ObservableMap<String, String> obsMap = FXCollections.observableMap(temp);

                    potentialLoctions.getItems().clear();
                    potentialLoctions.getItems().addAll(obsMap.keySet());


                }
            }
        });




        javafx.scene.control.Button createNewGardenButton = new javafx.scene.control.Button("Create Garden");
        createNewGardenButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                boolean allOK = true;
                String locationRef = null;
                String location = null;

                if(gardenNameText.getText().length()< 1){

                    Alert noText = new Alert(Alert.AlertType.ERROR);
                    noText.setContentText("PLEASE ENTER NAME");
                    noText.show();

                    gardenNameText.setStyle("-fx-border-color: red; -fx-border-width: 2px");

                }
                else{
                    String gardenName = gardenNameText.getText().trim();
                    System.out.println("*"+ gardenName+"*");
                    String theGardenLocation = potentialLoctions.getValue().trim();

                    if(possibleLocations.containsKey(theGardenLocation)){
                        allOK=true;



                        for(Map.Entry<String, String> entry : possibleLocations.entrySet()){
                            if(theGardenLocation.equals(entry.getKey())){
                                location = entry.getKey();
                                locationRef = entry.getValue();
                            }
                        }



                    } else {
                        Alert noText = new Alert(Alert.AlertType.ERROR);
                        noText.setContentText("Location not recognised");
                        noText.show();

                        gardenNameText.setStyle("-fx-border-color: red; -fx-border-width: 2px");

                        allOK = false;
                    }

                    //TODO
                    // change database method to reflect new garden object

                    Database.createNewGarden(gardenName, user, location, locationRef);

                    Garden createdGarden = Database.userGardenReturn(gardenName, user);
                    setGardenAndPlotScene(s,user,createdGarden );


                }
            }
        });

        javafx.scene.control.Button backButon = new javafx.scene.control.Button("Back");
        backButon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    setGardenScene(s, user);
                } catch(SQLException e){

                }
            }
        });

        addGardenPane.setAlignment(Pos.CENTER);
        addGardenPane.add(createGardenLabel, 1, 1);
        addGardenPane.add(gardenNameText, 2, 1);
        addGardenPane.add(createNewGardenButton, 1, 4);
        addGardenPane.add(locationLabel, 1, 3   );
        addGardenPane.add(potentialLoctions, 2, 3);

        addGardenPane.add(backButon, 3, 4);


        s.setScene(addGardenScene);


    }


    /**
     * Sets up UI elements for showing plots in garden and allow users to add more plots and select optimsie
     * @param stage
     * @param user
     * @param g
     */
    public static void setGardenAndPlotScene(Stage stage, User user, Garden g ){

        GridPane gardenPlotPane = new GridPane();
        Scene gardenPlotScene = new Scene(gardenPlotPane, 500, 500);
        ArrayList<Plot> gPlots = g.getPlots();
        int counter = 1;

        for(int i = 0; i < g.getPlots().size(); i++){
            javafx.scene.control.Button plotButton = new javafx.scene.control.Button(g.getPlots().get(i).getName());
            gardenPlotPane.add(plotButton, 1, i+1);
            counter++;

            plotButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    for(Plot p : gPlots){
                        if(plotButton.getText().equals(p.getName())){
                            plotView(p, stage, user, g );
                        }
                    }

                }
            });
        }

        Slider waterSlider = new Slider();
        waterSlider.setMin(0.0);
        waterSlider.setMax(10000.0);
        waterSlider.setBlockIncrement(200.00 );
        waterSlider.setMinorTickCount(200);
        waterSlider.setMajorTickUnit(1000);
        waterSlider.setShowTickLabels(true);

        Slider daySlider = new Slider();
        daySlider.setMin(0  );
        daySlider.setMax(100);
        daySlider.setBlockIncrement(1);
        daySlider.setMinorTickCount(1);
        daySlider.setMajorTickUnit(20);
        daySlider.setShowTickLabels(true);

        javafx.scene.control.Label dayLabel = new javafx.scene.control.Label();
        dayLabel.setText(Double.toString(1));
        javafx.scene.control.Label dayTitle = new javafx.scene.control.Label("Number of days");

        javafx.scene.control.Label waterLabel = new javafx.scene.control.Label();
        waterLabel.setText(Double.toString(waterSlider.getValue()));
        javafx.scene.control.Label waterTitle = new javafx.scene.control.Label("Amount of water");

        daySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number old_val, Number  new_val) {
                dayLabel.setText(String.format("%.0f", new_val));
            }
        });

        waterSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number old_val  , Number new_val) {
                waterLabel.setText(String.format("%.0f",new_val));

            }
        });
        javafx.scene.control.TextField waterText = new javafx.scene.control.TextField();
        javafx.scene.control.Button optimise = new javafx.scene.control.Button("OPTIMISE");

        // date picker
        DatePicker optimiseStartDate = new DatePicker(LocalDate.now());


        //  include weather toggle and its impact on date picker
        RadioButton includeWeatherToggle = new RadioButton("Include Weather");

        LocalDate dateSelected;

        if(includeWeatherToggle.isSelected()){

            dateSelected= LocalDate.now();

        } else {

            dateSelected=optimiseStartDate.getValue();

        }

        includeWeatherToggle.setSelected(false);
        includeWeatherToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1==true){
                    optimiseStartDate.setVisible(false);
                    Alert weatherToggled = new Alert(Alert.AlertType.WARNING);
                    weatherToggled.setContentText("Using wether means" + "\n" + "optimisation must be done from todays date");
                    weatherToggled.show();
                } else{
                    optimiseStartDate.setVisible(true);
                }
            }
        });



        optimise.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int days = Integer.parseInt(dayLabel.getText());
                //double water = Double.parseDouble(waterLabel.getText());
                double water = Double.parseDouble(waterText.getText());


                Optimiser opObject = new Optimiser(g, days, water,dateSelected, false );
                optimisationScene(opObject, stage);

            }
        });


        //gardenPlotPane.add(waterSlider, 2, 2, 2, 1);

        gardenPlotPane.add(daySlider, 2, 4);
        gardenPlotPane.add(waterText, 2,2);
        gardenPlotPane.add(waterTitle, 3, 2);
        //gardenPlotPane.add(waterLabel, 3, 2);
        gardenPlotPane.add(dayLabel, 3, 4);
        gardenPlotPane.add(includeWeatherToggle, 2 , 5);
        gardenPlotPane.add(optimiseStartDate, 2, 6);
        gardenPlotPane.add(optimise, 2, 10);



        javafx.scene.control.Button addPlot = new javafx.scene.control.Button("Add Plot");
        addPlot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    addPlot(stage, g, user);
                } catch(SQLException e) {

                }
            }
        });
        gardenPlotPane.add(addPlot, 1, counter);



        stage.setTitle("Garden ");
        stage.setScene(gardenPlotScene);


    }

    // method to create scene that shows information on that plot
    public static  void plotView(Plot pl, Stage s, User u, Garden p){

        s.setTitle(pl.getName());

        GridPane plotViewPane = new GridPane();
        Scene plotViewScene = new Scene(plotViewPane, 500, 600);
        javafx.scene.control.Label plotNameLabel = new javafx.scene.control.Label("Plot Name:");
        javafx.scene.control.Label plotNameText = new javafx.scene.control.Label(pl.getName());
        javafx.scene.control.Label plantTypeLabel = new javafx.scene.control.Label("Planted with:");
        javafx.scene.control.Label plantTypeText = new javafx.scene.control.Label(pl.getPlant().getName());
        javafx.scene.control.Label plantedLabel = new javafx.scene.control.Label("Planted On: ");
        javafx.scene.control.Label plantedLabelText = new javafx.scene.control.Label(pl.getDatePlanted().toString());

        javafx.scene.control.Button back = new javafx.scene.control.Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setGardenAndPlotScene(s, u , p);
            }
        });

        plotViewPane.add(plotNameLabel, 1, 1);
        plotViewPane.add(plotNameText, 2, 1);
        plotViewPane.add(plantTypeLabel, 1, 2);
        plotViewPane.add(plantTypeText, 2, 2);
        plotViewPane.add(plantedLabel, 1, 3);
        plotViewPane.add(plantedLabelText, 2, 3);

        plotViewPane.add(back, 5, 5);


        s.setScene(plotViewScene);

    }

    /**
     *  Method to create UI that shows results of optimisation
     * @param o
     * @param s
     */

    public static void optimisationScene(Optimiser o, Stage s){

        Stage secondStage = new Stage();

        FlowPane opGrid = new FlowPane();


        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days");
        yAxis.setLabel("Water mm^3");

        LineChart<Number, Number> waterChart = new LineChart<Number, Number>(xAxis, yAxis);
        waterChart.setTitle("Water Usage");

        XYChart.Series optimalUse = new XYChart.Series();
        optimalUse.setName("Optimal");
        double[] opP = o.getOptimisationPoints();
        for(int i = 0 ; i < opP.length; i++){
            optimalUse.getData().add(new XYChart.Data<>(i, opP[i]));


        }

        double[] arg = o.decisionByDay(o.optimize());
        XYChart.Series decionUse = new XYChart.Series();
        decionUse.setName("Water to use");
        for(int i = 0; i < arg.length; i++){
            decionUse.getData().add(new XYChart.Data<>(i , arg[i]));
        }

        double[][] plotsOptimal = o.optimize();
        for(int i = 0; i < o.getGarden().getPlots().size(); i++){

            XYChart.Series plotOptimalSeries = new XYChart.Series();
           // double[] plotOptimalValues = new double[o.getDays()];
            for(int j = 0; j < o.getDays(); j++){
              //  plotOptimalValues[j] =
                double value = plotsOptimal[i][j];
                plotOptimalSeries.getData().add(new XYChart.Data<>(j, value));

            }


            waterChart.getData().add(plotOptimalSeries);
        }

        waterChart.getData().add(optimalUse);
        waterChart.getData().add(decionUse);

        Scene opScene = new Scene(waterChart, 1000, 600);



        secondStage.setTitle("Optimisation Result");
        secondStage.setScene(opScene);
        // s.setTitle("Optimisation Result");
        // s.setScene(opScene);
        secondStage.show();

    }

    /**
     * Creates UI to add plots to garden
     * @param s
     * @param g
     * @param u
     * @throws SQLException
     */
    public static void addPlot(Stage s, Garden g, User u) throws SQLException{

        GridPane addPlotPane = new GridPane();
        Scene addPlotScene = new Scene(addPlotPane, 500, 600);
        s.setTitle("Add Plot");

        javafx.scene.control.Label name = new javafx.scene.control.Label("Name");
        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField();

        javafx.scene.control.Label howBigPlot = new javafx.scene.control.Label("How big is plot?");
        javafx.scene.control.TextField sizeValue = new javafx.scene.control.TextField();
        ToggleGroup areaValues = new ToggleGroup();
        RadioButton sqm = new RadioButton("Sqm");
        sqm.setToggleGroup(areaValues);
        sqm.setSelected(true);
        RadioButton sqft = new RadioButton("Sqft");
        sqft.setToggleGroup(areaValues);


        javafx.scene.control.Label environmentLabel = new javafx.scene.control.Label("Environment");
        ComboBox<String> environmentSelect = new ComboBox<>();

        environmentSelect.getItems().add(0, "Normal");
        environmentSelect.getItems().add(1, "Raised Beds");
        environmentSelect.getItems().add(2, "Polytunnel");
        environmentSelect.getSelectionModel().selectFirst();


        javafx.scene.control.Label soilSelectTitle = new javafx.scene.control.Label("SoilType");
        HashMap obs = Database.getSoilVariables();
        ArrayList<SoilType> soilTypesList = Database.returnSoilTypeList();

        ComboBox<String> soilSelect = new ComboBox<>();
        for(SoilType st : soilTypesList){
            soilSelect.getItems().add(st.getName());
        }


        soilSelect.getSelectionModel().select(3);

        javafx.scene.control.Label plotPrioirityTitle = new javafx.scene.control.Label("Importance of plot crop");
        ComboBox<String> plotPriorty = new ComboBox<>();
        plotPriorty.getItems().add(0, "High");
        plotPriorty.getItems().add(1, "Normal");
        plotPriorty.getItems().add(2, "Low");
        plotPriorty.getSelectionModel().select(1);


        javafx.scene.control.Label plantLabel = new javafx.scene.control.Label("What are you planting?");
        ComboBox<String> plantType = new ComboBox();
        ComboBox plant = new ComboBox();

        plantType.getItems().add(0, "1 Vegetable");
        plantType.getItems().add(1, "2 Fruit");
        plantType.getItems().add(2, "3 Herbs");
        //plantType.getSelectionModel().select(0 );

        HashMap<Integer, String> plantsInUser = new HashMap<>();

       plantType.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
               int type = Integer.parseInt(plantType.getValue().toString().substring(0,1));
               plantsInUser.clear();
               HashMap<Integer, String> plantNames = Database.returnPlantDetails(type);
               plant.getItems().clear();
               plantNames.forEach((k,v)->{plantsInUser.put(k, v);
               plant.getItems().add(v);});



           }
       });




        DatePicker plantedDate = new DatePicker(LocalDate.now());


        // code to add plot after button is pushed
        javafx.scene.control.Button addPlotButton = new javafx.scene.control.Button("Add Plot ");
        addPlotButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

//                PLot object =  (String name,
//                double size,
//                LocalDate datePlanted,
//                Plant plant,
//                int number,
//                double soil,
//                double environment,
//                int priority

                // name of plot
                String ThePlotsName = nameField.getText();
                // plot size
                double thePlotSize;
                if(sqm.isSelected()){
                    thePlotSize= Double.parseDouble(sizeValue.getText());
                } else {
                    thePlotSize = Double.parseDouble(sizeValue.getText()) * 0.092903;
                }
                // date planted
                LocalDate ThedatePlanted = plantedDate.getValue();
                int day = ThedatePlanted.getDayOfMonth();
                int month = ThedatePlanted.getMonthValue();
                int year = ThedatePlanted.getYear();

                // plant in plot
                String selectedPlant = plant.getValue().toString();
                int plantID = 0;
                if(selectedPlant.equals(null)){
                    Alert noPlantAlert = new Alert(Alert.AlertType.ERROR);
                    noPlantAlert.setContentText("please select a plant");
                } else {
                    for (Map.Entry<Integer, String> entry : plantsInUser.entrySet()) {
                        if(entry.getValue().equals(selectedPlant)){
                            plantID = entry.getKey();
                        }
                        else{
                            System.out.println("Something went wrong");
                        }
                    }

                }
                Plant plotsPlant = new Plant();
                try {
                     plotsPlant = Database.createPlant(plantID);
                } catch(SQLException e){

                }
                // number of plants in plot
                int number = ((plotsPlant.getNumberPerMeter()*(int)thePlotSize) < 1) ? 1 : (plotsPlant.getNumberPerMeter()*(int)thePlotSize);


                // soil value
                String soilStringValue = soilSelect.getValue();
               double TheSoilValue = 0.0;
               int soilID = 0;
                for(SoilType st : soilTypesList){
                    if(st.getName().equals(soilStringValue)){
                        TheSoilValue=st.getValue();
                        soilID = st.getId();
                    }
                }


                // environment value
                double theEnvironmentValue = 0.0;
                int environmentID = 0;
                if(environmentSelect.getValue().toString().equals("Polytunnel")){
                    theEnvironmentValue= 1.25;
                    environmentID = 1;

                } else if(environmentSelect.getValue().toString().equals("Raised Beds")) {
                    theEnvironmentValue = 1.05;
                    environmentID = 2;
                } else{
                    theEnvironmentValue = 1.0;
                    environmentID = 3;
                }

                // priority
                int thePriorityValue = 0;
                int priorityID = 0;
                if(plotPriorty.getValue().toString().equals("High")){
                    thePriorityValue = 3;

                } else if(plotPriorty.getValue().toString().equals("Low")){
                    thePriorityValue = 1;
                } else {
                    thePriorityValue = 2;
                }

                System.out.println("The plot name is " + ThePlotsName + "\n" + "The plot size is " + thePlotSize + "\n" +
                 "It was planted on " + ThedatePlanted + " Day= " + day + "month= " + month + "year= " + year + "\n" +
                "its size is " + thePlotSize + " the plant is " + plotsPlant.getName() + "\n" +
                        " the number of plants is is " + number + "\n" +  " the soil value is " + TheSoilValue + "\n" +
                        " the environment value is " + theEnvironmentValue + "\n" + "the priority value is " + thePriorityValue
                );

                Plot theNewPlot = new Plot(ThePlotsName, thePlotSize, ThedatePlanted, plotsPlant, number, TheSoilValue, theEnvironmentValue, thePriorityValue);
                g.getPlots().add(theNewPlot);

               if(Database.insertNewPlot(ThePlotsName, thePlotSize, plotsPlant.getid(), g.getGardenID(), soilID, environmentID, day, month, year, thePriorityValue )==true) {
                    setGardenAndPlotScene(s, u, g);

               }


            }
        });

        javafx.scene.control.Button backButton = new javafx.scene.control.Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setGardenAndPlotScene(s, u, g);
            }
        });




        addPlotPane.add(name, 5, 1);
        addPlotPane.add(nameField, 6, 1);
        addPlotPane.add(environmentLabel, 5, 2);
        addPlotPane.add(environmentSelect, 6, 2, 2, 1);
        addPlotPane.add(soilSelectTitle, 5, 3);
        addPlotPane.add(soilSelect, 6, 3);

        addPlotPane.add(plotPrioirityTitle, 5, 4);
        addPlotPane.add(plotPriorty, 6, 4);

        addPlotPane.add(plantLabel, 5, 5);
        addPlotPane.add(plantType, 6, 5);
        addPlotPane.add(plant, 7, 5);

        addPlotPane.add(plantedDate, 6, 7);

        addPlotPane.add( howBigPlot, 4, 8);
        addPlotPane.add(sizeValue, 5, 8);
        addPlotPane.add(sqft, 6 , 8);
        addPlotPane.add(sqm, 7, 8);


        addPlotPane.add(addPlotButton, 5, 10    );

        addPlotPane.add(backButton, 7, 12);

        s.setScene(addPlotScene);
    }

    /**
     * Login page creation
     * @param s
     */
    public static void login(Stage s){

        GridPane loginPageGrid = new GridPane();
        Scene loginEntry = new Scene(loginPageGrid, 500, 300);

        // login Entry set up

        javafx.scene.control.Label loginLabel = new javafx.scene.control.Label("LOGIN");
        javafx.scene.control.TextField loginEntryField = new javafx.scene.control.TextField("userName");
        javafx.scene.control.Button loginButton = new javafx.scene.control.Button("Login");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String theUserName = loginEntryField.getText();
                try{
                    if(  true) {
                        System.out.println();
                        User temp = Database.createUser(theUserName);

                        setGardenScene(s, temp);


                    } else {
                        Alert userAlreadyExistsAlert = new Alert(Alert.AlertType.ERROR);
                        userAlreadyExistsAlert.setContentText("Not Valid User");
                        userAlreadyExistsAlert.show();
                    }
                }
                catch(SQLException e) {
                    System.out.println("SQl ERROR LOGIN");
                    e.printStackTrace();
                }
            }
        });


        loginPageGrid.setAlignment(Pos.CENTER);
        loginPageGrid.setPadding(new javafx.geometry.Insets(200, 50, 200 ,50 ));
        loginPageGrid.add(loginLabel,1,0);
        loginPageGrid.add(loginEntryField, 1,1);
        loginPageGrid.add(loginButton, 1, 2);

        s.setTitle("LOGIN");
        s.setScene(loginEntry);
    }


    /**
     * start method that opens up applications welcome page
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {


        theStage = primaryStage;



        //welcome Page
        GridPane welcomePage = new GridPane();
        Scene welcomePageScene = new Scene(welcomePage, 500 , 500);

        // user Entry
        GridPane grid = new GridPane();
        Scene userEntry = new Scene(grid, 500, 300 );





        // Welcome Page set up
        welcomePage.setAlignment(Pos.CENTER);
        welcomePage.setPadding(new javafx.geometry.Insets(50, 50, 50, 50));
        javafx.scene.control.Label welcome = new javafx.scene.control.Label();
        welcome.setText("Welcome to the Garden App");
        javafx.scene.control.Button login = new javafx.scene.control.Button("Login");

        javafx.scene.control.Button newUser = new javafx.scene.control.Button("Create New User");
        newUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                theStage.setScene(userEntry);
            }
        });
        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                login(primaryStage);
            }
        });
        welcomePage.add(welcome, 1,0);
        welcomePage.add(login,  1,1);
        welcomePage.add(newUser, 1,2);







        // new user entry scene
        javafx.scene.control.TextField user = new javafx.scene.control.TextField("User Name");
        javafx.scene.control.TextField emailentry = new javafx.scene.control.TextField("email");
        javafx.scene.control.TextField pass = new javafx.scene.control.TextField("Password");
        javafx.scene.control.Button submit = new javafx.scene.control.Button("Submit");

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                userName = user.getText();
                email = emailentry.getText();
                password = pass.getText();

                if(userName.length() < 6){
                    Alert userNameAlert = new Alert(Alert.AlertType.ERROR);
                    userNameAlert.setContentText("User Name not long enough");
                    userNameAlert.showAndWait();

                } else if(password.length() < 6) {
                    Alert passwordAlter = new Alert(Alert.AlertType.ERROR);
                    passwordAlter.setContentText("Password is not long enough");
                    passwordAlter.showAndWait();

                } else{
                    try{
                        Database.insertUserNameANDPassword(userName, email, password);
                        Alert inputSuccess = new Alert((Alert.AlertType.CONFIRMATION));
                        inputSuccess.setContentText("Data Entered Successfully");

                        User temp = Database.createUser(userName);
                        theStage.setTitle("Garden");
                        setGardenScene(theStage, temp );


                        //theStage.setScene(garden);


                    }catch (SQLException e){
                        Alert entryFailureAlert = new Alert(Alert.AlertType.WARNING);
                        entryFailureAlert.show();

                    }
                }
            }
        });

        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new javafx.geometry.Insets(50, 50, 50, 50));
        grid.add(user,0,0);
        grid.add(emailentry, 0, 1);
        grid.add(pass, 0, 2);
        grid.add(submit, 0, 3);







        theStage.setTitle("New User Entry");
        theStage.setScene(welcomePageScene);
        theStage.show();
    }

    // main method with launch method to start javafx
    public static  void main(String[] args) {
        launch(args);
    }


}
