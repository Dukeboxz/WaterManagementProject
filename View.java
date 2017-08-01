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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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

import static java.awt.Color.getColor;
import static java.awt.Color.red;


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

        stage.setTitle("Garden Selection ");


        // Garden Selection Page
        javafx.scene.control.Label gardenLabel = new javafx.scene.control.Label();
        String  a = " Welcome  " + user.getName().trim() + "\n" + " please select your garden or create a new one ";
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


        ToggleGroup gardenPermissions = new ToggleGroup();

        RadioButton viewAndEdit = new RadioButton("Show gardens I can edit");
        RadioButton justView = new RadioButton("Show gardens I can only view");
        viewAndEdit.setToggleGroup(gardenPermissions);
        justView.setToggleGroup(gardenPermissions);
        viewAndEdit.setSelected(true);

        for(Garden g : gardenList){

            if(g.getUserEditRights()==true) {
                usersGardens.getItems().add(g.getName());
            }

        }

        viewAndEdit.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1==false){
                    usersGardens.getItems().clear();
                    for(Garden g : gardenList){
                        if(g.getUserEditRights()==false){
                            usersGardens.getItems().add(g.getName());
                        }
                    }
                } else {
                    if(!usersGardens.getItems().isEmpty()) {
                        usersGardens.getItems().clear();
                    }
                    for(Garden g : gardenList){
                        if(g.getUserEditRights()==true){
                            usersGardens.getItems().add(g.getName());
                        }
                    }
                }
            }
        });






        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(usersGardens.getSelectionModel().isEmpty()){
                    Alert noGarden = new Alert(Alert.AlertType.ERROR);
                    noGarden.setContentText("Please select a garden");
                  usersGardens.setStyle("-fx-border-width: 2.5; -fx-border-color: red");
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
        gardenGrid.setVgap(10 );
        gardenGrid.setHgap(10);
        gardenGrid.setPadding(new javafx.geometry.Insets(10, 10, 50, 10));
        gardenGrid.add(gardenLabel, 0, 1);
        gardenGrid.add(createNewGarden, 0, 2);
        gardenGrid.add(viewAndEdit, 0, 4);
        gardenGrid.add(justView, 1, 4);
        gardenGrid.add(usersGardens, 0, 5);
        gardenGrid.add(selectGarden, 0, 3);
        gardenGrid.add(load, 0, 6);

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



        // create new garden after button is pressed
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

                    if(gardenName.length() >20){

                        Alert noText = new Alert(Alert.AlertType.ERROR);
                        noText.setContentText("GARDEN NAME TOO LONG");
                        noText.show();

                        gardenNameText.setStyle("-fx-border-color: red; -fx-border-width: 2px");

                    }

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
     * Sets up UI elements for showing plots in garden and allow users to add more plots  and to link other users to garden and select optimsie
     * @param stage
     * @param user
     * @param g
     */
    public static void setGardenAndPlotScene(Stage stage, User user, Garden g ){

        GridPane gardenPlotPane = new GridPane();
        Scene gardenPlotScene = new Scene(gardenPlotPane, 500, 500);
        stage.setTitle("Optimse Your Water Supply");
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
            System.out.println("SHOULD NOT HAPPEN");

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

        boolean weatherActive;
        if(optimiseStartDate.isVisible()){
            weatherActive = false;
        } else {
            weatherActive= true;
        }

        optimise.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int days = Integer.parseInt(dayLabel.getText());
                //double water = Double.parseDouble(waterLabel.getText());
                double water = Double.parseDouble(waterText.getText());
                LocalDate theDate = optimiseStartDate.getValue();



                Optimiser opObject = new Optimiser(g, days, water,theDate, weatherActive);
                optimisationScene(opObject, stage);

            }
        });


        //gardenPlotPane.add(waterSlider, 2, 2, 2, 1);

        gardenPlotPane.add(daySlider, 2, 4, 3, 1);
        gardenPlotPane.add(waterText, 2,2);
        gardenPlotPane.add(waterTitle, 3, 2);
        //gardenPlotPane.add(waterLabel, 3, 2);
        gardenPlotPane.add(dayLabel, 5, 4);
        gardenPlotPane.add(includeWeatherToggle, 2 , 5);
        gardenPlotPane.add(optimiseStartDate, 2, 6);
        gardenPlotPane.add(optimise, 2, 10);



        javafx.scene.control.Button addPlot = new javafx.scene.control.Button("Add Plot");


        if(g.getUserEditRights()==false){
            addPlot.setVisible(false);
        }

        addPlot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    addPlot(stage, g, user);
                } catch(SQLException e) {

                }
            }
        });
        javafx.scene.control.Button linkUserButton = new javafx.scene.control.Button(" Linked User to Garden");
        linkUserButton.setWrapText(true);
        linkUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                linkUserView(stage, user, g);
            }
        });

        gardenPlotPane.add(addPlot, 1, counter);
        gardenPlotPane.add(linkUserButton, 4,10 );



        stage.setTitle("Garden ");
        stage.setScene(gardenPlotScene);


    }

    public static void linkUserView(Stage stage, User user, Garden g){

        stage.setTitle("Linked New User");
        GridPane linkedUserPane = new GridPane();
        Scene linkedUserScene = new Scene(linkedUserPane, 400, 400);

        javafx.scene.control.Label addNewUserLabel = new javafx.scene.control.Label("Add UserName of User you would like to add to your garden");

        javafx.scene.control.Label nameLabel = new javafx.scene.control.Label("UserName");
        javafx.scene.control.TextField userNameInput = new javafx.scene.control.TextField();
        javafx.scene.control.Label permissionLabel = new javafx.scene.control.Label("What permissions would you like them to have?");

        ToggleGroup permissions = new ToggleGroup();

        RadioButton viewAndEdit = new RadioButton("View and Edit");
        RadioButton viewOnly = new RadioButton("View Only");
        viewAndEdit.setToggleGroup(permissions);
        viewOnly.setToggleGroup(permissions);
        viewOnly.setSelected(true);

        javafx.scene.control.Button linkUser = new javafx.scene.control.Button("Link New User");
        javafx.scene.control.Button back = new javafx.scene.control.Button("Back");

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setGardenAndPlotScene(stage, user, g);
            }
        });



        linkUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String the = userNameInput.getText();
                System.out.println("*" + the + "*");
                try {
                    if (Database.userNameExists(the)) {
                        int linkedUserid = Database.getUserIDBasedOnName(the);
                        if(viewOnly.isSelected()) {
                            Database.insertNewUserGarden(g.getGardenID(), linkedUserid, false);
                        } else {
                            Database.insertNewUserGarden(g.getGardenID(), linkedUserid, true);
                        }

                        setGardenAndPlotScene(stage, user, g);
                    } else {
                        Alert noName = new Alert(Alert.AlertType.ERROR);
                        noName.setContentText("User Name does not exist");
                        noName.show();
                    }
                }
                catch(SQLException f){

                    }

            }
        });


        linkedUserPane.setHgap(5);
        linkedUserPane.setVgap(5);
        ColumnConstraints a = new ColumnConstraints();

        linkedUserPane.add(addNewUserLabel, 1,1,4,1);
        linkedUserPane.add(nameLabel, 1, 2);
        linkedUserPane.add(userNameInput, 3, 2);
        linkedUserPane.add(permissionLabel, 1, 3, 2, 1);
        linkedUserPane.add(viewAndEdit, 3, 3);
        linkedUserPane.add(viewOnly ,4, 3);
        linkedUserPane.add(linkUser, 1, 4);
        linkedUserPane.add(back, 3, 4);



        stage.setScene(linkedUserScene);

    }



    // method to create scene that shows information on that plot
    public static  void plotView(Plot pl, Stage s, User u, Garden p){

        s.setTitle(pl.getName());

        GridPane plotViewPane = new GridPane();
        Scene plotViewScene = new Scene(plotViewPane, 500, 500);
        javafx.scene.control.Label plotNameLabel = new javafx.scene.control.Label("Plot Name:");
        javafx.scene.control.Label plotNameText = new javafx.scene.control.Label(pl.getName());
        javafx.scene.control.Label plantTypeLabel = new javafx.scene.control.Label("Planted with:");
        javafx.scene.control.Label plantTypeText = new javafx.scene.control.Label(pl.getPlant().getName());
        javafx.scene.control.Label plantedLabel = new javafx.scene.control.Label("Planted On: ");
        javafx.scene.control.Label plantedLabelText = new javafx.scene.control.Label(pl.getDatePlanted().toString());
        javafx.scene.control.Label sizeOfPlotLabel = new javafx.scene.control.Label("The size of plot ");

        javafx.scene.control.Label sizeofPlotText = new javafx.scene.control.Label(Double.toString(pl.getSize()) + "sqm");

        String priority;
        double pValue = pl.getPriority();
        if(pValue==3){
            priority="High";

        } else if(pValue==2){
            priority ="Medium";
        } else {
            priority="Low";
        }

        javafx.scene.control.Label priorityLabel = new javafx.scene.control.Label("Priority of Plot");
        javafx.scene.control.Label priorityText = new javafx.scene.control.Label(priority);





        javafx.scene.control.Button back = new javafx.scene.control.Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setGardenAndPlotScene(s, u , p);
            }
        });



        javafx.scene.control.Button editPlot = new javafx.scene.control.Button("Edit Plot ");


        if(p.getUserEditRights()==false){
            editPlot.setVisible(false);
        }

        editPlot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                editPlotScene(pl, s, u, p);
            }
        });



        plotViewPane.add(plotNameLabel, 1, 1);
        plotViewPane.add(plotNameText, 2, 1);
        plotViewPane.add(plantTypeLabel, 1, 2);
        plotViewPane.add(plantTypeText, 2, 2);
        plotViewPane.add(plantedLabel, 1, 3);
        plotViewPane.add(plantedLabelText, 2, 3);
        plotViewPane.add(sizeOfPlotLabel, 1, 4);
        plotViewPane.add(sizeofPlotText, 2, 4);
        plotViewPane.add(priorityLabel, 1 , 5);
        plotViewPane.add(priorityText, 2, 5);

        plotViewPane.add(back, 5, 7);
        plotViewPane.add(editPlot, 2, 7);



        s.setScene(plotViewScene);

    }

    /**
     * set up scene that allows user to edit the plot
     */
    public static void editPlotScene(Plot pl, Stage s, User u , Garden p ) {

     s.setTitle("Edit Plot");

        // create alternate page to edit plot details

        GridPane editPlotPane = new GridPane();
        Scene editPlotScene = new Scene(editPlotPane, 500, 500 );

        javafx.scene.control.Label editPlotName = new javafx.scene.control.Label("Plot Name");
        javafx.scene.control.TextField plotNameTextField = new javafx.scene.control.TextField();
        plotNameTextField.setText(pl.getName());

        ComboBox plantTypes = new ComboBox();

        javafx.scene.control.Label editPlantedLabel = new javafx.scene.control.Label("Planted with:");
        Map<Integer, String> mapOfPlantTypes = Database.getPlantTypesInMap();
        int planttypeCounter = 0;
        for(Map.Entry<Integer, String> entry : mapOfPlantTypes.entrySet()){
            plantTypes.getItems().add(planttypeCounter, entry.getValue());

        }

        Map<String, Plant> mapOfPlants = new TreeMap<>();
        ComboBox plantsOfType = new ComboBox();
        plantsOfType.setValue(pl.getPlant().getName());

        plantTypes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String type = plantTypes.getValue().toString();

                plantsOfType.getItems().clear();
                mapOfPlantTypes.forEach((k,v) -> {

                    if(type.equals(v)){
                        int typeId =k;
                        Map<String, Plant> plantMap = Database.returnPlantsInMap(typeId);



                        if(plantsOfType.getItems().isEmpty()){
                            mapOfPlants.clear();
                            for(Map.Entry<String, Plant> entry : plantMap.entrySet()){
                                plantsOfType.getItems().add(entry.getKey());
                                mapOfPlants.put(entry.getKey(), entry.getValue());
                            }
                        } else {
                            mapOfPlants.clear();
                            plantsOfType.getItems().clear();
                            for(Map.Entry<String, Plant> entry : plantMap.entrySet()){
                                plantsOfType.getItems().add(entry.getKey());
                                mapOfPlants.put(entry.getKey(), entry.getValue());
                            }

                        }

                    }
                });
            }
        });



        javafx.scene.control.Label editPlantedOn = new javafx.scene.control.Label("Planted On: ");
        DatePicker theDatePlanted = new DatePicker(pl.getDatePlanted());


        javafx.scene.control.Label editSizeLabel = new javafx.scene.control.Label("Size of plot: ");
        javafx.scene.control.TextField sizeField = new javafx.scene.control.TextField(Double.toString(pl.getSize()));
        ToggleGroup size = new ToggleGroup();
        RadioButton sqm = new RadioButton("sqm");
        sqm.setSelected(true);
        sqm.setToggleGroup(size);
        RadioButton sqft = new RadioButton("sqft");
        sqft.setToggleGroup(size);


        javafx.scene.control.Label plotPriorityEditLabel = new javafx.scene.control.Label("Priority of plot:");
        Map<Double, String> priorityMap = new TreeMap<>();
        priorityMap.put(3.0, "High");
        priorityMap.put(2.0, "Medium");
        priorityMap.put(1.0, "Low");

        ComboBox thePrioirty = new ComboBox();
        int counter = 0;
        for(Map.Entry entry : priorityMap.entrySet()){
            thePrioirty.getItems().add(counter, entry.getValue());
            if(entry.getKey().equals(pl.getPriority())){
                thePrioirty.getSelectionModel().select(counter);
            }
            counter++;
        }

        javafx.scene.control.Button editPlotBackButton = new javafx.scene.control.Button("Back");

        editPlotBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                plotView(pl,s, u , p);
            }
        });

        javafx.scene.control.Button saveEditButton = new javafx.scene.control.Button("Save Edit");

        saveEditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(plotNameTextField.getText().length() < 1 || plantsOfType.getValue().equals(null) || sizeField.getText().length() <=0){

                } else{
                    String newPlotName = plotNameTextField.getText();
                    String plantName = plantsOfType.getValue().toString();
                    Plant thePlant = null;
                    for(Map.Entry<String, Plant> entry : mapOfPlants.entrySet()){
                        if(entry.getKey().equals(plantName)) {
                            thePlant=entry.getValue();
                        }
                    }

                    double newPlotSize = Double.parseDouble(sizeField.getText());

                    newPlotSize= (sqm.isSelected()==true)? newPlotSize : newPlotSize*0.092903;
                    newPlotSize= (newPlotSize < 1) ? 1 : newPlotSize;

                    LocalDate theLocalDate = theDatePlanted.getValue();

                    String thePriorityString = thePrioirty.getValue().toString();
                    double priorityValue = 0;
                    for(Map.Entry<Double, String> entry : priorityMap.entrySet()){
                        if(entry.getValue().equals(thePriorityString)){
                            priorityValue=entry.getKey();
                        }
                    }

                    int id = pl.getId();
                    int numberOfPlants = (int)newPlotSize* thePlant.getNumberPerMeter();

                    for(Plot gPlots : p.getPlots()){
                        if(id==gPlots.getId()){
                            gPlots.setName(newPlotName);
                            pl.setName(newPlotName);
                            gPlots.setPlant(thePlant);
                            pl.setPlant(thePlant);
                            gPlots.setSize(newPlotSize);
                            pl.setSize(newPlotSize);
                            gPlots.setNoOfPlants(numberOfPlants);
                            pl.setNoOfPlants(numberOfPlants);
                            gPlots.setPriority(priorityValue);
                            pl.setPriority(priorityValue);

                        }
                    }
                    System.out.println("New Plot name= " + newPlotName + "\n" +
                            "The new plant= " + thePlant.getName() + "\n" +
                            " The new planted date= " + theLocalDate + "\n" +
                            "New size of Plot= " + newPlotSize + "\n" +
                            "New plot priority= " + priorityValue);

                    //  Database.updatePlot(pl.getId(), newPlotName,newPlotSize,  thePlant.getid(), theLocalDate, priorityValue);

                    plotView(pl, s, u, p);
                }
            }
        });






        editPlotPane.add(editPlotName, 1, 1);
        editPlotPane.add(plotNameTextField, 2, 1);
        editPlotPane.add(editPlantedLabel, 1, 2 );

        editPlotPane.add(plantTypes, 2, 2);
        editPlotPane.add(plantsOfType, 3, 2, 2, 1);
        editPlotPane.add(editPlantedOn, 1, 3);
        editPlotPane.add(theDatePlanted, 2,3);
        editPlotPane.add(editSizeLabel, 1, 4);
        editPlotPane.add(sizeField, 2, 4);
        editPlotPane.add(sqm, 3 ,4);
        editPlotPane.add(sqft, 4, 4);
        editPlotPane.add(plotPriorityEditLabel, 1, 5);
        editPlotPane.add(thePrioirty, 2, 5);
        editPlotPane.add(editPlotBackButton, 2, 6);
        editPlotPane.add(saveEditButton, 4, 6);



       s.setScene(editPlotScene);



    }

    /**
     *  Method to create UI that shows results of optimisation
     * @param o
     * @param s
     */

    public static void optimisationScene(Optimiser o, Stage s){

        Stage secondStage = new Stage();


        GridPane opGrid = new GridPane();

        opGrid.setHgap(50);
       opGrid.setVgap(5);


        final NumberAxis xAxis = new NumberAxis(0, o.getDays(), 1);
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days");
        yAxis.setLabel("Water inches^3");


        LineChart<Number, Number> waterChart = new LineChart<Number, Number>(xAxis, yAxis);
        waterChart.setTitle("Water Usage");

        XYChart.Series optimalUse = new XYChart.Series();
        optimalUse.setName("Optimal");
        double[] opP = o.getOptimisationPoints();
        for(int i = 0 ; i < opP.length; i++){

            System.out.println("The day is " + i  + " and the value is " + opP[i]);
            optimalUse.getData().add(new XYChart.Data<>(i, opP[i]));


        }

        double[] arg = o.decisionByDay(o.optimize(0));
        XYChart.Series decionUse = new XYChart.Series();
        decionUse.setName("Water to use");
        for(int i = 0; i < arg.length; i++){
            decionUse.getData().add(new XYChart.Data<>(i , arg[i]));
        }


        Map<String, ArrayList<Double>> optimisedInMap = o.optimizeForMap();
        Map<String, ArrayList<Double>> optimalPerPlot = o.createOptimalMap();
        Map<String, ArrayList<Double>> basicPerPlot = o.returnBasicInMap();
        int plotCounter = 0;
        int colCounter = 0;
        for(Map.Entry<String, ArrayList<Double>> entry: optimisedInMap.entrySet()){
            String thePlotName = entry.getKey();
            System.out.println(entry.getKey());
            XYChart.Series newDecisionSeries = new XYChart.Series();
            newDecisionSeries.setName(thePlotName);
            int counter = 0;
            for(Double d : entry.getValue()){

                newDecisionSeries.getData().add(new XYChart.Data<>(counter, d));
                counter++;

            }
            waterChart.getData().add(newDecisionSeries);
            RadioButton newButton = new RadioButton(" Show " + thePlotName);
            newButton.setWrapText(true);
            if(colCounter> 5){
                colCounter=1;
                plotCounter++;
            }
           opGrid.add(newButton, colCounter, 6 +  plotCounter);
            colCounter++;

           newButton.setSelected(true);
           newButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
               @Override
               public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                   if(t1==false){
                       waterChart.getData().remove(newDecisionSeries);
                   } else {
                       waterChart.getData().add(newDecisionSeries);
                   }
               }
           });



        }
        System.out.println("PC aget optimal = " + plotCounter);
        plotCounter++;

        int columnCounter = 0;
        for(Map.Entry<String, ArrayList<Double>> entry : optimalPerPlot.entrySet()){
            String thePlotName = entry.getKey();
            XYChart.Series newOptimalSeries = new XYChart.Series();
            newOptimalSeries.setName(thePlotName);
            int counter = 0;

            for(Double d : entry.getValue()) {
                newOptimalSeries.getData().add(new XYChart.Data<>(counter, d));
                counter++;
            }

            RadioButton newButton = new RadioButton("Show Optimal for plot: " + thePlotName);
            newButton.setWrapText(true);

            System.out.println(" column counter before is " + columnCounter);
            if(columnCounter > 5){
                columnCounter=0;
                plotCounter++;
            }
            opGrid.add(newButton, columnCounter, 6 + plotCounter);
            columnCounter++;

            newButton.setSelected(false);
            newButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    if(t1==true){
                        waterChart.getData().add(newOptimalSeries);
                    } else{
                        waterChart.getData().remove(newOptimalSeries);
                    }
                }
            });
        }

        plotCounter++;
        columnCounter=0;
        for(Map.Entry<String, ArrayList<Double>> entry: basicPerPlot.entrySet()) {
            String thePlotName = entry.getKey();
            XYChart.Series newBasicSeries = new XYChart.Series();
            newBasicSeries.setName(thePlotName);
            int counter = 0 ;

            for(Double d : entry.getValue()){
                newBasicSeries.getData().add(new XYChart.Data<>(counter, d));
                counter++;
            }

            RadioButton newBasicButton = new RadioButton("Show Basic for plot: " + thePlotName);
            newBasicButton.setWrapText(true);
            if(columnCounter>5){
                columnCounter=0;
                plotCounter++;
            }
            opGrid.add(newBasicButton, columnCounter,  6 + plotCounter);
            columnCounter++;
            newBasicButton.setSelected(false);
            newBasicButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    if(t1==true){
                        waterChart.getData().add(newBasicSeries);
                    } else{
                        waterChart.getData().remove(newBasicSeries);
                    }
                }
            });
        }

        waterChart.getData().add(optimalUse);
        waterChart.getData().add(decionUse);


        RadioButton showOptimal = new RadioButton("show Optimal");
        showOptimal.setSelected(true);
        RadioButton showUsage = new RadioButton("Show Total Water Usage");
        showUsage.setSelected(true);

        showOptimal.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1 == false){
                    waterChart.getData().remove(optimalUse);
                } else{
                    waterChart.getData().add(optimalUse);
                }
            }
        });

        showUsage.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1==false){
                    waterChart.getData().remove(decionUse);
                } else{
                    waterChart.getData().add(decionUse);
                }
            }
        });





        ColumnConstraints col = new ColumnConstraints();


        col.setPercentWidth(20);
        opGrid.getColumnConstraints().addAll(col, col, col , col, col);

        opGrid.add(waterChart, 0, 0,5, 4);
        opGrid.add(showOptimal, 1, 5);
        opGrid.add(showUsage, 2, 5);

        plotCounter++;


        Slider dayChooseSlide = new Slider();
        dayChooseSlide.setPadding(new javafx.geometry.Insets( 20 , 20 , 20 , 20));
        dayChooseSlide.setMax(o.getDays());
        opGrid.add(dayChooseSlide, 0, plotCounter+ 6, 5, 1);

        plotCounter++;
        System.out.println(plotCounter + 6);


        ArrayList<javafx.scene.control.Label> labels = new ArrayList<>();

        for(Map.Entry<String, ArrayList<Double>> entry:optimisedInMap.entrySet()){

            javafx.scene.control.Label plotText = new javafx.scene.control.Label("water needed in " + entry.getKey());
            plotText.setWrapText(true);
            opGrid.add(plotText, 0, plotCounter +6);
            javafx.scene.control.Label plotLabel = new javafx.scene.control.Label();
            plotLabel.setId(entry.getKey());
            labels.add(plotLabel);
            opGrid.add(plotLabel, 2, plotCounter +6);
            plotCounter++;
            System.out.println(plotCounter);
        }



        dayChooseSlide.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int n = t1.intValue();
                for(Map.Entry<String, ArrayList<Double>> entry : optimisedInMap.entrySet()){
                    for(javafx.scene.control.Label l : labels){
                        if(l.getId().equals(entry.getKey())){

                            l.setText(entry.getValue().get(n).toString());
                        }
                    }

                }

            }
        });


        Scene opScene = new Scene(opGrid, 1000, 800);



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



               if(Database.insertNewPlot(ThePlotsName, thePlotSize, plotsPlant.getid(), g.getGardenID(), soilID, environmentID, day, month, year, thePriorityValue )==true) {

                   try {
                       Plot theNewPlot = Database.createPlot(ThePlotsName, day, month, year);
                       g.getPlots().add(theNewPlot);
                       setGardenAndPlotScene(s, u, g);
                   }catch(SQLException e){

                   }


               } else {
                   Alert writeFailure = new Alert(Alert.AlertType.ERROR);
                   writeFailure.setContentText("Failed To Write to Database ");
                   writeFailure.show();
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
        javafx.scene.control.Label password = new javafx.scene.control.Label("Password");
        PasswordField thePasswordField = new PasswordField();
        javafx.scene.control.Button loginButton = new javafx.scene.control.Button("Login");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String theUserName = loginEntryField.getText();
                String thePassword =  thePasswordField.getText();
                try{
                    if( Database.userNameAndPasswordCheck(theUserName, thePassword)) {

                        System.out.println(loginEntryField.getText());
                        System.out.println(Database.userNameExists(loginEntryField.getText()));
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

        loginPageGrid.setGridLinesVisible(true);
        loginPageGrid.setAlignment(Pos.CENTER);
        loginPageGrid.setHgap(5);
        loginPageGrid.setVgap(5);
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(60);
        ColumnConstraints outCol = new ColumnConstraints();
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(25);
        loginPageGrid.getColumnConstraints().addAll(outCol, col, outCol);

        loginPageGrid.setPadding(new javafx.geometry.Insets(5  , 5, 5 ,5 ));
        loginPageGrid.add(loginLabel,1,0);
        loginPageGrid.add(loginEntryField, 1,1);
        loginPageGrid.add(password, 1, 2);
        loginPageGrid.add(thePasswordField, 1, 3);
        loginPageGrid.add(loginButton, 1, 4);

        s.setTitle("LOGIN");
        s.setScene(loginEntry);
    }


    /**
     * start method that opens up applications welcome page
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        System.setProperty("javafx.userAgentStylesheetUrl1", "caspian");


        welcomePage(primaryStage);





    }

    public static void welcomePage(Stage primaryStage) {

        //welcome Page
        GridPane welcomePage = new GridPane();
        Scene welcomePageScene = new Scene(welcomePage, 300 , 200);
        //primaryStage.setResizable(false);

        welcomePage.setStyle("-fx-background-image: url(" +
                "/res/cougette.jpg" + ")");


        primaryStage.setTitle("Welcome");

        // Welcome Page set up
        welcomePage.setAlignment(Pos.CENTER);
        welcomePage.setVgap(10);
        welcomePage.setHgap(10);
       welcomePage.setPadding(new javafx.geometry.Insets(2, 2, 2, 2));


        javafx.scene.control.Label welcome = new javafx.scene.control.Label();
        welcome.setText("Welcome ");
        welcome.setWrapText(true);
        welcome.setTextFill(Color.WHITE);
        welcome.setFont(Font.font("Verdanna", FontWeight.BOLD, 20));

        javafx.scene.control.Button login = new javafx.scene.control.Button("Login");

        javafx.scene.control.Button newUser = new javafx.scene.control.Button("Create New User");
        newUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                newUserEntry(primaryStage);
            }
        });
        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                login(primaryStage);
            }
        });

        login.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        newUser.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(60);
        ColumnConstraints outCol = new ColumnConstraints();
        outCol.setPercentWidth(20);
        welcomePage.getColumnConstraints().addAll(outCol, col, outCol);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(20);
        welcomePage.getRowConstraints().addAll(row, row, row, row, row);



//        BackgroundImage welcomePageImage = new BackgroundImage(new Image("/res/cougette.jpg", 300, 300, true, true),
//                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
//
//
//        Background welcomeBackground = new Background(welcomePageImage);



       // welcomePage.setBackground(new Background(welcomePageImage));
        welcomePage.add(welcome, 1,1, 2,1 );
        welcomePage.add(login,  1,2, 1, 1);
        welcomePage.add(newUser, 1,3, 1, 1);










        primaryStage.setScene(welcomePageScene);
        primaryStage.show();
    }

    public  static void newUserEntry(Stage s) {

        GridPane grid = new GridPane();
        Scene newUserScene = new Scene(grid, 400 , 400);


        javafx.scene.control.TextField user = new javafx.scene.control.TextField("User Name");
        javafx.scene.control.TextField emailentry = new javafx.scene.control.TextField("email");
        PasswordField pass = new PasswordField();
        pass.setPromptText("Password at least 6 characters");
        javafx.scene.control.Button submit = new javafx.scene.control.Button("Submit");

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String userName = user.getText();
                 String email = emailentry.getText().trim();
                 String password = pass.getText();


                if(Database.userNameNotExist(userName.trim())) {


                    if (userName.length() < 6) {
                        Alert userNameAlert = new Alert(Alert.AlertType.ERROR);
                        userNameAlert.setContentText("User Name not long enough");
                        userNameAlert.showAndWait();

                    } else if (password.length() < 6) {
                        Alert passwordAlter = new Alert(Alert.AlertType.ERROR);
                        passwordAlter.setContentText("Password is not long enough");
                        passwordAlter.showAndWait();


                    } else if (!email.toUpperCase().matches("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}")) {
                        Alert emailBadFormat = new Alert(Alert.AlertType.ERROR);
                        emailBadFormat.setContentText("Email not correct format");
                        emailBadFormat.show();
                    } else {
                        try {
                            Database.insertUserNameANDPassword(userName, email, password);
                            Alert inputSuccess = new Alert((Alert.AlertType.CONFIRMATION));
                            inputSuccess.setContentText("Data Entered Successfully");

                            User temp = Database.createUser(userName);

                            setGardenScene(s, temp);


                            //theStage.setScene(garden);


                        } catch (SQLException e) {
                            Alert entryFailureAlert = new Alert(Alert.AlertType.WARNING);
                            entryFailureAlert.show();
                            e.printStackTrace();
                        }
                    }
                } else {
                    Alert nameExists = new Alert(Alert.AlertType.WARNING);
                    nameExists.setContentText("Name Already Exists");
                    nameExists.show();
                }
            }
        });

        javafx.scene.control.Button backButton = new javafx.scene.control.Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                welcomePage(s);
            }
        });

        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new javafx.geometry.Insets(50, 50, 50, 50));
        grid.add(user,0,0);
        grid.add(emailentry, 0, 1);
        grid.add(pass, 0, 2);
        grid.add(submit, 0, 3);
        grid.add(backButton, 1, 3);



        s.setScene(newUserScene);


    }

    // main method with launch method to start javafx
    public static  void main(String[] args) {
        launch(args);
    }


}
