import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

import static java.awt.Color.getColor;


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
        gardenGrid.setId("gardengrid");
        Scene garden = new Scene(gardenGrid ,500, 500);


       gardenGrid.getStylesheets().add("/res/StyleSheet.css");

        gardenGrid.setVgap(5);
        gardenGrid.setHgap(5);
        gardenGrid.setGridLinesVisible(false);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(10);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(35);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(15);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(10);
        gardenGrid.getRowConstraints().addAll(row2, row1, row1, row1, row1, row1, row1);
        gardenGrid.getColumnConstraints().addAll(col1, col2, col1, col2, col1);


        java.util.List<Garden> gardenList = Database.getUsersGardens(user.getId());

        stage.setTitle("Garden Selection ");


        // Garden Selection Page
        javafx.scene.control.Label gardenLabel = new javafx.scene.control.Label();
        String  a = " Welcome" + user.getName() + "  please select your garden  or create a new one ";
        gardenLabel.setText(a);
        gardenLabel.setWrapText(true);
        gardenLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
       gardenLabel.setTextFill(Color.WHITE);
        javafx.scene.control.Label selectGarden = new javafx.scene.control.Label("Existing Garden");
       selectGarden.setTextFill(Color.WHITE);
        selectGarden.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        javafx.scene.control.Button load = new javafx.scene.control.Button("Load");
        javafx.scene.control.Button createNewGarden = new javafx.scene.control.Button("Create new Garden");


        createNewGarden.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addGarden(user, stage);
            }
        });


        ComboBox usersGardens = new ComboBox();
        usersGardens.setMaxSize(Double.MAX_VALUE, 40);
        usersGardens.setVisibleRowCount(7);
        ComboBox gardensCanView = new ComboBox();
        gardensCanView.setMaxSize(Double.MAX_VALUE, 40);
        gardensCanView.setVisible(false);



        ToggleGroup gardenPermissions = new ToggleGroup();

        RadioButton viewAndEdit = new RadioButton("Show gardens I can edit");
        viewAndEdit.setWrapText(true);
        viewAndEdit.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        viewAndEdit.setTextFill(Color.WHITE);
        RadioButton justView = new RadioButton("Show gardens I can only view");
        justView.setWrapText(true);
        justView.setTextFill(Color.WHITE);
        justView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        viewAndEdit.setToggleGroup(gardenPermissions);
        justView.setToggleGroup(gardenPermissions);
        viewAndEdit.setSelected(true);



        for(Garden g : gardenList){

            if(g.getUserEditRights()==true) {
                usersGardens.getItems().add(g.getName());
            } else {
                gardensCanView.getItems().add(g.getName());
            }

        }
        viewAndEdit.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1==false){
                    usersGardens.setVisible(false);
                    gardensCanView.setVisible(true);
                } else{
                    usersGardens.setVisible(true);
                    gardensCanView.setVisible(false);
                }
            }
        });

//        viewAndEdit.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
//                if(t1==false){
//                    usersGardens.getItems().clear();
//                    for(Garden g : gardenList){
//                        if(g.getUserEditRights()==false){
//                            usersGardens.getItems().add(g.getName());
//                        }
//                    }
//                } else {
//                    if(!usersGardens.getItems().isEmpty()) {
//                        usersGardens.getItems().clear();
//                    }
//                    for(Garden g : gardenList){
//                        if(g.getUserEditRights()==true){
//                            usersGardens.getItems().add(g.getName());
//                        }
//                    }
//                }
//            }
//        });






        load.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent actionEvent) {

                if(viewAndEdit.isSelected()) {
                    if (usersGardens.getSelectionModel().isEmpty()) {
                        Alert noGarden = new Alert(Alert.AlertType.ERROR);
                        noGarden.setContentText("Please select a garden");
                        usersGardens.setStyle("-fx-border-width: 2.5; -fx-border-color: red");
                    } else {
                        String chosenGarden = usersGardens.getValue().toString();
                        for (Garden h : gardenList) {
                            if (h.getName().equals(chosenGarden)) {
                                setGardenAndPlotScene(stage, user, h);
                            }
                        }

                    }
                } else {
                    if (gardensCanView.getSelectionModel().isEmpty()) {
                        Alert noGarden = new Alert(Alert.AlertType.ERROR);
                        noGarden.setContentText("Please select a garden");
                        usersGardens.setStyle("-fx-border-width: 2.5; -fx-border-color: red");
                    } else {
                        String chosenGarden = gardensCanView.getValue().toString();
                        for (Garden h : gardenList) {
                            if (h.getName().equals(chosenGarden)) {
                                setGardenAndPlotScene(stage, user, h);
                            }
                        }


                    }

                }

            }
        });
        gardenGrid.setAlignment(Pos.CENTER);
        gardenGrid.setVgap(10 );
        gardenGrid.setHgap(10);
        gardenGrid.setPadding(new javafx.geometry.Insets(2, 2, 2, 2));
        gardenGrid.add(gardenLabel, 1, 1, 3, 1);
        gardenGrid.add(createNewGarden, 1, 2);
        gardenGrid.add(viewAndEdit, 1, 4);

        gardenGrid.add(usersGardens, 1, 5);
        gardenGrid.add(justView, 3,4);
        gardenGrid.add(gardensCanView,3, 5);
        gardenGrid.add(selectGarden, 1, 3);
        gardenGrid.add(load, 1, 6);

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
        Scene addGardenScene = new Scene(addGardenPane, 500 , 500);

        addGardenPane.setStyle("-fx-background-image: url(" + "/res/Classic-KellyGreen.jpg" + ")");

        addGardenPane.setHgap(5);
        addGardenPane.setVgap(5);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(5);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(40);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(10);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(5);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(30);

        addGardenPane.getColumnConstraints().addAll(col1, col2, col3, col2, col3, col1);
        addGardenPane.getRowConstraints().addAll(row1, row2, row2, row2, row1);


        javafx.scene.control.Label createGardenLabel = new javafx.scene.control.Label("Garden Name");
        createGardenLabel.setTextFill(Color.WHITE);
        createGardenLabel.setStyle("-fx-font-size: 150%");
        javafx.scene.control.TextField gardenNameText = new javafx.scene.control.TextField();

        javafx.scene.control.Label locationLabel = new javafx.scene.control.Label("Location Of Garden Type first 3 letters");
        locationLabel.setWrapText(true);
        locationLabel.setTextFill(Color.WHITE);

        locationLabel.setStyle("-fx-font-size: 150%");


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
                  //  createdGarden.setUserEditRights(true);

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
        addGardenPane.add(gardenNameText, 3, 1);

        addGardenPane.add(locationLabel, 1, 2   );
        addGardenPane.add(potentialLoctions, 3, 2);


        addGardenPane.add(createNewGardenButton, 3, 3);
        addGardenPane.add(backButon, 1, 3);


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

        gardenPlotPane.setStyle("-fx-background-image: url(" + "/res/waterGrass.jpg" + ")");
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(20);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(10);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(20);
        gardenPlotPane.getColumnConstraints().addAll(col1, col2, col3);
        gardenPlotPane.getRowConstraints().addAll(row1, row1, row1, row1, row1, row1, row1, row2, row1);
        gardenPlotPane.setHgap(5);
        gardenPlotPane.setVgap(5);

        gardenPlotScene.getStylesheets().add("/res/StyleSheet.css");


        ArrayList<Plot> gPlots = g.getPlots();


        int counter = 1;

        javafx.scene.control.Label plotsLabel = new javafx.scene.control.Label("PLOTS");
        plotsLabel.setTextFill(Color.BLACK);
        gardenPlotPane.add(plotsLabel, 0, 1);




        VBox thePlots = new VBox();

        thePlots.setSpacing(5);

        ScrollPane scroller = new ScrollPane();
        scroller.setContent(thePlots);
        scroller.setFitToHeight(false);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroller.setId("plotScroler");
        gardenPlotPane.add(scroller, 0, 2, 1, 3);



        if(!g.getPlots().isEmpty()) {
            for (int i = 0; i < g.getPlots().size(); i++) {
                javafx.scene.control.Button plotButton = new javafx.scene.control.Button(g.getPlots().get(i).getName());
                plotButton.setWrapText(true);
                plotButton.setId("green");



                plotButton.setMaxWidth(Double.MAX_VALUE);
                plotButton.setMinHeight(20);


                plotButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        for (Plot p : gPlots) {
                            if (plotButton.getText().equals(p.getName())) {
                                plotView(p, stage, user, g);
                            }
                        }

                    }
                });
                thePlots.getChildren().add(plotButton);
            }
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
        dayLabel.setTextFill(Color.BLACK);
        dayLabel.setText(Integer.toString(1));
        javafx.scene.control.Label dayTitle = new javafx.scene.control.Label("Number of days");
        dayTitle.setTextFill(Color.BLACK);
//        dayLabel.setAlignment(Pos.BASELINE_RIGHT);
//        dayTitle.setAlignment(Pos.BASELINE_LEFT);



        javafx.scene.control.Label waterLabel = new javafx.scene.control.Label();
        waterLabel.setTextFill(Color.BLACK);
        waterLabel.setText(Double.toString(waterSlider.getValue()));
        javafx.scene.control.Label waterTitle = new javafx.scene.control.Label("Amount of water");
        waterTitle.setWrapText(true);

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
        includeWeatherToggle.setTextFill(Color.BLACK);
       includeWeatherToggle.setMaxWidth(200);
       includeWeatherToggle.setStyle("-fx-font-size: 150%");

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

        Tooltip mathmaticalTip = new Tooltip();
        mathmaticalTip.setText("Selecting this option will run a mathmatical algorithm through matlab.  To use it you must have matlab installed on your system");
        mathmaticalTip.wrapTextProperty().setValue(true);

        Tooltip simpleTip = new Tooltip();
        simpleTip.setText("This uses simple algorithm.  Select this if you do not have access to matlab. ");




        optimise.setMaxWidth(Double.MAX_VALUE);
        optimise.setMaxHeight(50);

        ToggleGroup algorithms = new ToggleGroup();
        RadioButton math = new RadioButton("Use mathmatical algorithm");
        math.wrapTextProperty().setValue(true);
        math.setTooltip(mathmaticalTip);
        math.setSelected(true);
        math.setToggleGroup(algorithms);
        math.setStyle("-fx-background-color: white");

        RadioButton simple = new RadioButton("Use Simple Algorithm");
        simple.setTooltip(simpleTip);
        simple.setToggleGroup(algorithms);
        simple.setWrapText(true);
        simple.setStyle("-fx-background-color: white");



        /// optimise action
        optimise.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int days = Integer.parseInt(dayLabel.getText());
                if (g.getPlots().size() > 0) {

                    boolean mathmaticalValue = true;
                    if(math.isSelected()){
                        mathmaticalValue=true;
                    } else {
                        mathmaticalValue=false;
                    }

                    if (days > 1) {

                        if (!waterText.getText().equals(null) & waterText.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {

                            double water = Double.parseDouble(waterText.getText());
                            if (days > 1 & water > 0) {
                                //double water = Double.parseDouble(waterLabel.getText());

                                LocalDate theDate = optimiseStartDate.getValue();

                                Optimiser opObject = new Optimiser(g, days, water, theDate, weatherActive, mathmaticalValue);
                                //optimisationScene(opObject, stage);
                                OptimizationSceneView opPage = new OptimizationSceneView(opObject, stage);
                                opPage.launchOptimizer();
                            }

                        } else {
                            Alert noWaterEntered = new Alert(Alert.AlertType.WARNING);
                            noWaterEntered.setContentText("Please enter valid water type");
                            noWaterEntered.show();
                        }
                    } else {
                        Alert notEnoughDaysAlert = new Alert(Alert.AlertType.ERROR);
                        notEnoughDaysAlert.setContentText("Need more than one day for optimization");
                        notEnoughDaysAlert.show();
                    }


                } else {
                    Alert noPlots = new Alert(Alert.AlertType.ERROR);
                    noPlots.setContentText("Please add plots to optimize");
                    noPlots.show();
                }


            }
        });


        //gardenPlotPane.add(waterSlider, 2, 2, 2, 1);
        gardenPlotPane.setGridLinesVisible(false);

        gardenPlotPane.add(waterText, 1,2);
        gardenPlotPane.add(waterTitle, 1, 1);

        gardenPlotPane.add(daySlider, 1, 3, 2, 1);

        //gardenPlotPane.add(waterLabel, 3, 2);
        gardenPlotPane.add(dayLabel, 1, 4);
        gardenPlotPane.add(dayTitle, 1, 4);


        gardenPlotPane.setHalignment(dayTitle, HPos.LEFT);
        gardenPlotPane.setHalignment(dayLabel, HPos.RIGHT);



        gardenPlotPane.add(includeWeatherToggle, 1 , 5);
        gardenPlotPane.add(optimiseStartDate, 1, 6);
        gardenPlotPane.add(optimise, 1, 7);
        gardenPlotPane.add(math, 1, 8);
        gardenPlotPane.add(simple, 2, 8);






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
        javafx.scene.control.Button linkUserButton = new javafx.scene.control.Button(" Link Another User to this Garden");
        linkUserButton.setWrapText(true);
        linkUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                linkUserView(stage, user, g);
            }
        });

        gardenPlotPane.add(addPlot, 0, 5);
        gardenPlotPane.add(linkUserButton, 0,7);



        stage.setTitle("Optimise Water Supply");
        stage.setScene(gardenPlotScene);


    }


    public static void linkUserView(Stage stage, User user, Garden g){

        stage.setTitle("Linked New User");
        GridPane linkedUserPane = new GridPane();
        Scene linkedUserScene = new Scene(linkedUserPane, 400, 300);

        linkedUserPane.setHgap(5);
        linkedUserPane.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(5);
        linkedUserPane.getColumnConstraints().addAll(col2, col1, col1, col1, col2);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(20);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(10);
        linkedUserPane.getRowConstraints().addAll(row2, row1, row1, row1, row1, row2);



        linkedUserPane.setStyle("-fx-background-image: url(" + "/res/Classic-KellyGreen.jpg" +")");

        javafx.scene.control.Label addNewUserLabel = new javafx.scene.control.Label("Add UserName of User you would like to add to your garden");
        addNewUserLabel.setTextFill(Color.WHITE);
        addNewUserLabel.setWrapText(true);

        javafx.scene.control.Label nameLabel = new javafx.scene.control.Label("UserName");
        nameLabel.setTextFill(Color.WHITE);
        javafx.scene.control.TextField userNameInput = new javafx.scene.control.TextField();
        javafx.scene.control.Label permissionLabel = new javafx.scene.control.Label("Permission Rights");
        permissionLabel.setTextFill(Color.WHITE);
        permissionLabel.setWrapText(true);

        ToggleGroup permissions = new ToggleGroup();

        RadioButton viewAndEdit = new RadioButton("View and Edit");
        viewAndEdit.setTextFill(Color.WHITE);
        RadioButton viewOnly = new RadioButton("View Only");
        viewOnly.setTextFill(Color.WHITE);
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



        linkedUserPane.add(addNewUserLabel, 1,1,3,1);
        linkedUserPane.add(nameLabel, 1, 2);
        linkedUserPane.add(userNameInput, 2, 2, 2, 1);
        linkedUserPane.add(permissionLabel, 1, 3, 1, 1);
        linkedUserPane.add(viewAndEdit, 2, 3);
        linkedUserPane.add(viewOnly ,3, 3);
        linkedUserPane.add(linkUser, 2, 4);
        linkedUserPane.add(back, 1, 4);



        stage.setScene(linkedUserScene);

    }



    // method to create scene that shows information on that plot
    public static  void plotView(Plot pl, Stage s, User u, Garden p){

        s.setTitle(pl.getName());

        GridPane plotViewPane = new GridPane();
        Scene plotViewScene = new Scene(plotViewPane, 400, 200);
        plotViewPane.setStyle("-fx-background-color: #75d153");


        plotViewPane.setHgap(5);
        plotViewPane.setVgap(5);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(35);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(10);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(10);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(15);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(5);

        plotViewPane.getColumnConstraints().addAll(col2, col1, col2, col1,  col2);
        plotViewPane.getRowConstraints().addAll(row1, row2, row2, row2, row2, row2,row3, row2, row3);

        javafx.scene.control.Label plotNameLabel = new javafx.scene.control.Label("Plot Name:");
        javafx.scene.control.Label plotNameText = new javafx.scene.control.Label(pl.getName());
        plotNameText.setWrapText(true);
        javafx.scene.control.Label plantTypeLabel = new javafx.scene.control.Label("Planted with:");
        javafx.scene.control.Label plantTypeText = new javafx.scene.control.Label(pl.getPlant().getName());
        plantTypeText.setWrapText(true);
        plantTypeText.setMinWidth(Region.USE_PREF_SIZE);
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



        javafx.scene.control.Button editPlot = new javafx.scene.control.Button("Edit/Remove Plot ");


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
        plotViewPane.add(plotNameText, 3, 1);
        plotViewPane.add(plantTypeLabel, 1, 2);
        plotViewPane.add(plantTypeText, 3, 2);
        plotViewPane.add(plantedLabel, 1, 3);
        plotViewPane.add(plantedLabelText, 3, 3);
        plotViewPane.add(sizeOfPlotLabel, 1, 4);
        plotViewPane.add(sizeofPlotText, 3, 4);
        plotViewPane.add(priorityLabel, 1 , 5);
        plotViewPane.add(priorityText, 3, 5);

        plotViewPane.add(back, 1, 7);
        plotViewPane.add(editPlot, 3, 7);



        s.setScene(plotViewScene);

    }

    /**
     * set up scene that allows user to edit the plot
     */
    public static void editPlotScene(Plot pl, Stage s, User u , Garden p ) {

     s.setTitle("Edit Plot");

        // create alternate page to edit plot details



        GridPane editPlotPane = new GridPane();
        Scene editPlotScene = new Scene(editPlotPane, 400, 400 );
        editPlotPane.setStyle("-fx-background-color: #75d153");

        editPlotPane.setVgap(5);
        editPlotPane.setHgap(5);


        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        col1.setPercentWidth(30);
        col2.setPercentWidth(2);
        col3.setPercentWidth(3);
        editPlotPane.getColumnConstraints().addAll(col2, col1, col3, col1, col3, col1,col2);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(10);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(15);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(5);

        editPlotPane.getRowConstraints().addAll(row1, row2, row2, row2, row2, row2,row3, row2, row3);




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
        plotPriorityEditLabel.setWrapText(true);
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

                   if(plotNameTextField.getText().length() < 1){
                       Alert enterName = new Alert(Alert.AlertType.ERROR);
                       enterName.setContentText("Please Enter Name");
                       enterName.show();
                       plotNameTextField.setStyle("-fx-border-color: Red");
                   }
                   if(plantsOfType.getValue().equals(null)){
                       Alert enterPlant = new Alert(Alert.AlertType.ERROR);
                       enterPlant.setContentText("Please choose plant");
                       plantsOfType.setStyle("-fx-border-color: Red");
                   }
                   if(sizeField.getText().length() <1){
                       Alert noSize = new Alert(Alert.AlertType.ERROR);
                       noSize.setContentText("Please enter relevant size");
                       noSize.show();
                   }

                }

                else{
                    String newPlotName = plotNameTextField.getText();

                    String plantName = plantsOfType.getValue().toString().trim();
                    System.out.println(plantName);
                    Plant thePlant = null;
                    if(plantName.equals(pl.getPlant().getName().trim())){
                        thePlant=pl.getPlant();

                    } else {
                        for (Map.Entry<String, Plant> entry : mapOfPlants.entrySet()) {

                            if (entry.getKey().toString().trim().equals(plantName)) {
                                thePlant = entry.getValue();

                            }
                        }
                    }
//                    Alert testAlert = new Alert(Alert.AlertType.INFORMATION);
//                    testAlert.setContentText(newPlotName + "*" + plantsOfType.getValue().toString().trim()+"*" + " " +"*" + pl.getPlant().getName()+"*");
//                    testAlert.show();

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
                    int numberOfPlants = 1;

                      numberOfPlants = (int) newPlotSize * thePlant.getNumberPerMeter();


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
                            "New plot priority= " + priorityValue + "\n" +
                             "The plant id= " + thePlant.getid());

                     Database.updatePlot(pl.getId(), newPlotName,newPlotSize,  thePlant.getid(), theLocalDate, priorityValue);

                    plotView(pl, s, u, p);
                }
            }
        });






        editPlotPane.add(editPlotName, 1, 1);
        editPlotPane.add(plotNameTextField, 3, 1);
        editPlotPane.add(editPlantedLabel, 1, 2 );

        editPlotPane.add(plantTypes, 3, 2);
        editPlotPane.add(plantsOfType, 5, 2, 2, 1);
        editPlotPane.add(editPlantedOn, 1, 3);
        editPlotPane.add(theDatePlanted, 3,3);
        editPlotPane.add(editSizeLabel, 1, 4);
        editPlotPane.add(sizeField, 3, 4);
        editPlotPane.add(sqm, 5 ,4);
        editPlotPane.setHalignment(sqm, HPos.LEFT);
        editPlotPane.add(sqft, 5, 4);
        editPlotPane.setHalignment(sqft, HPos.RIGHT);
        editPlotPane.add(plotPriorityEditLabel, 1, 5);
        editPlotPane.add(thePrioirty, 3, 5);
        editPlotPane.add(editPlotBackButton, 1, 7);
        editPlotPane.add(saveEditButton, 3, 7);



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

        opGrid.setHgap(5);
       opGrid.setVgap(5);



        final NumberAxis xAxis = new NumberAxis(0, o.getDays() -1, 1);
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLowerBound(0.1);
        xAxis.setLabel("Days");
        yAxis.setLabel("Water inches^3");


        LineChart<Number, Number> waterChart = new LineChart<Number, Number>(xAxis, yAxis);
        waterChart.setTitle("Water Usage");

        XYChart.Series optimalUse = new XYChart.Series();
        optimalUse.setName("Desired");
        double[] opP = o.getOptimisationPoints();
        double optimalTotal = 0;
        for(int i = 0 ; i < opP.length; i++){
            optimalTotal+=opP[i];
        }
        for(int i = 0 ; i < opP.length; i++){


            optimalUse.getData().add(new XYChart.Data<>(i, opP[i]));


        }




        Map<String, ArrayList<Double>> optimisedInMap = o.optimizeForMap();
        Map<String, ArrayList<Double>> optimalPerPlot = o.getOptimalMap();
        Map<String, ArrayList<Double>> basicPerPlot = o.getBasicMap();

        double[] arg = o.decisionByDay(optimisedInMap);
        double decisionTotal = 0;

        for(int i = 0 ; i < arg.length; i++){
            decisionTotal+=arg[i];
        }

        double waterLeft = (o.getWaterAvailable() - optimalTotal) < 0 ? 0 :(o.getWaterAvailable()-optimalTotal);
        XYChart.Series decionUse = new XYChart.Series();
        decionUse.setName("Optimal");
        for(int i = 0; i < arg.length; i++){
            decionUse.getData().add(new XYChart.Data<>(i , arg[i]));
        }

        double[] basicPoints = o.getBasicPoints();
        XYChart.Series basicPointsSeries = new XYChart.Series();
        basicPointsSeries.setName("Basic");
        for(int i = 0; i < basicPoints.length; i++){
            basicPointsSeries.getData().add(new XYChart.Data<>(i, basicPoints[i]));
        }


        int plotCounter = 0;
        int colCounter = 0;

        ButtonBar descionVariables = new ButtonBar();
        descionVariables.setButtonMinWidth(40);
        ScrollPane decisionScroler = new ScrollPane(descionVariables);
        decisionScroler.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        decisionScroler.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        opGrid.add(decisionScroler, 0,6, 6, 1 );
        for(Map.Entry<String, ArrayList<Double>> entry: optimisedInMap.entrySet()){
            String thePlotName = entry.getKey();

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

           descionVariables.getButtons().add(newButton);

        }

        plotCounter++;

        ButtonBar optimalButttonBar = new ButtonBar();
        optimalButttonBar.setButtonMinWidth(40);


        optimalButttonBar.setMaxHeight(Double.MAX_VALUE);
        ScrollPane scroler = new ScrollPane(optimalButttonBar);
        scroler.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroler.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
       // scroler.setPadding(new Insets(10 , 10 ,10 , 10));
       opGrid.add(scroler, 0 , 8 , 6,1 );
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

            RadioButton newButton = new RadioButton("Show  desired for: " + thePlotName);
            newButton.setWrapText(true);


            if(columnCounter > 5){
                columnCounter=0;
                plotCounter++;
            }
            optimalButttonBar.getButtons().add(newButton);
           // opGrid.add(newButton, columnCounter, 6 + plotCounter);
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



        ButtonBar basicButtonBar = new ButtonBar();
        basicButtonBar.setButtonMinWidth(40);
        ScrollPane basicScrollPane = new ScrollPane(basicButtonBar);
        basicScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        basicScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        opGrid.add(basicScrollPane, 0 ,10, 6, 1);
        for(Map.Entry<String, ArrayList<Double>> entry: basicPerPlot.entrySet()) {
            String thePlotName = entry.getKey();
            XYChart.Series newBasicSeries = new XYChart.Series();
            newBasicSeries.setName(thePlotName);
            int counter = 0 ;

            for(Double d : entry.getValue()){
                newBasicSeries.getData().add(new XYChart.Data<>(counter, d));
                counter++;
            }

            RadioButton newBasicButton = new RadioButton("Show Basic for: " + thePlotName);
            newBasicButton.setWrapText(true);
            basicButtonBar.getButtons().add(newBasicButton);
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
        waterChart.getData().add(basicPointsSeries);


        RadioButton showOptimal = new RadioButton("show Desired");
        showOptimal.setSelected(true);
        RadioButton showUsage = new RadioButton("Show Optimal");
        showUsage.setSelected(true);
        RadioButton showBasic = new RadioButton("Show Basic");
        showBasic.setSelected(true);

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

        showBasic.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1==false){
                    waterChart.getData().remove(basicPointsSeries);
                } else {
                    waterChart.getData().add(basicPointsSeries);
                }
            }
        });



        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(9);

        ColumnConstraints col = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(16);


        col.setPercentWidth(20);
        opGrid.getColumnConstraints().addAll(col, col2, col2, col2, col2, col2);

        opGrid.add(waterChart, 0, 0,5, 4);
        opGrid.add(showOptimal, 1, 5);
        opGrid.add(showUsage, 2, 5);
        opGrid.add(showBasic, 3, 5);




        Slider dayChooseSlide = new Slider();
        dayChooseSlide.setPadding(new javafx.geometry.Insets( 20 , 20 , 20 , 20));
        dayChooseSlide.setMax(o.getDays()-1);
        opGrid.add(dayChooseSlide, 0, 11, 5, 1);


        System.out.println(plotCounter + 6);


        ArrayList<javafx.scene.control.Label> labels = new ArrayList<>();

        int theRowCounter = 0;
        int rowIndex = 12;
        int theColumnConter = 0;

        for(Map.Entry<String, ArrayList<Double>> entry:optimisedInMap.entrySet()){

            javafx.scene.control.Label plotText = new javafx.scene.control.Label("water needed in " + entry.getKey());
            plotText.setWrapText(true);


            if(theRowCounter > 5){
                theRowCounter=0;
                theColumnConter+=2;
            }
            opGrid.add(plotText, theColumnConter, rowIndex + theRowCounter);
            javafx.scene.control.Label plotLabel = new javafx.scene.control.Label();
            plotLabel.setId(entry.getKey());
            labels.add(plotLabel);
            opGrid.add(plotLabel, theColumnConter+1, rowIndex + theRowCounter);

            theRowCounter++;



        }
        DecimalFormat df = new DecimalFormat("#.##");
        Label waterLeftLabel = new Label( " Water left= " + df.format(waterLeft));
        waterLeftLabel.setWrapText(true);
        opGrid.add(waterLeftLabel, 1, 18);





        dayChooseSlide.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int n = t1.intValue();
                for(Map.Entry<String, ArrayList<Double>> entry : optimisedInMap.entrySet()){
                    for(javafx.scene.control.Label l : labels){
                        if(l.getId().equals(entry.getKey())){
                            DecimalFormat df = new DecimalFormat("#.##");
                            double value = entry.getValue().get(n);
                            l.setText(df.format(value));
                        }
                    }

                }

            }
        });


        Scene opScene = new Scene(opGrid, 1000, 1000);



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
        Scene addPlotScene = new Scene(addPlotPane, 400, 500);
        s.setTitle("Add Plot");
        addPlotPane.setStyle("-fx-background-image: url(" + "/res/Classic-KellyGreen.jpg" + ")");

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(3);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(2);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(10);

        addPlotPane.getColumnConstraints().addAll(col2, col1, col3, col1, col3, col1, col2);
        addPlotPane.getRowConstraints().addAll(row1, row1, row1, row1, row1, row1, row1, row1, row1, row1);

        javafx.scene.control.Label name = new javafx.scene.control.Label("Name");
        name.setTextFill(Color.WHITE);
        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField();
        nameField.textProperty().addListener(((observableValue, s1, t1) -> {
            if(t1.length() > 25) nameField.setText(s1); }));

        javafx.scene.control.Label howBigPlot = new javafx.scene.control.Label("How big is plot?");
        howBigPlot.setWrapText(true);
        howBigPlot.setTextFill(Color.WHITE);
        javafx.scene.control.TextField sizeValue = new javafx.scene.control.TextField();
        ToggleGroup areaValues = new ToggleGroup();
        RadioButton sqm = new RadioButton("Sqm");
        sqm.setToggleGroup(areaValues);
        sqm.setSelected(true);
        sqm.setTextFill(Color.WHITE);
        RadioButton sqft = new RadioButton("Sqft");
        sqft.setToggleGroup(areaValues);
        sqft.setTextFill(Color.WHITE);


        javafx.scene.control.Label environmentLabel = new javafx.scene.control.Label("Environment");
        environmentLabel.setTextFill(Color.WHITE);
        ComboBox<String> environmentSelect = new ComboBox<>();

        environmentSelect.getItems().add(0, "Normal");
        environmentSelect.getItems().add(1, "Raised Beds");
        environmentSelect.getItems().add(2, "Polytunnel");
        environmentSelect.getSelectionModel().selectFirst();


        javafx.scene.control.Label soilSelectTitle = new javafx.scene.control.Label("SoilType");
        soilSelectTitle.setTextFill(Color.WHITE);
        HashMap obs = Database.getSoilVariables();
        ArrayList<SoilType> soilTypesList = Database.returnSoilTypeList();

        ComboBox<String> soilSelect = new ComboBox<>();
        for(SoilType st : soilTypesList){
            soilSelect.getItems().add(st.getName());
        }


        soilSelect.getSelectionModel().select(2);

        javafx.scene.control.Label plotPrioirityTitle = new javafx.scene.control.Label("Importance of plot crop");
        plotPrioirityTitle.setTextFill(Color.WHITE);
        plotPrioirityTitle.setWrapText(true);
        ComboBox<String> plotPriorty = new ComboBox<>();
        plotPriorty.getItems().add(0, "High");
        plotPriorty.getItems().add(1, "Normal");
        plotPriorty.getItems().add(2, "Low");
        plotPriorty.getSelectionModel().select(1);


        javafx.scene.control.Label plantLabel = new javafx.scene.control.Label("What are you planting?");
        plantLabel.setTextFill(Color.WHITE);
        plantLabel.setWrapText(true);
        ComboBox<String> plantType = new ComboBox();
        ComboBox plant = new ComboBox();
        plant.setValue("plant");

        plantType.getItems().add(0, "1 Vegetable");
        plantType.getItems().add(1, "2 Fruit");
        plantType.getItems().add(2, "3 Herbs");
        //plantType.getSelectionModel().select(0 );

        Map<String, Integer> plantsInUser = new TreeMap<>();

       plantType.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
               int type = Integer.parseInt(plantType.getValue().toString().substring(0,1));
               plantsInUser.clear();
               TreeMap<String, Integer> plantNames = Database.returnPlantDetails(type);

               plant.getItems().clear();
               plantNames.forEach((k,v)->{plantsInUser.put(k, v);
               plant.getItems().add(k);});



           }
       });




        Label datePlantedLabel = new Label("Date planted");
        datePlantedLabel.setTextFill(Color.WHITE);
        DatePicker plantedDate = new DatePicker(LocalDate.now());


        // code to add plot after button is pushed
        javafx.scene.control.Button addPlotButton = new javafx.scene.control.Button("Add Plot ");
        addPlotButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {



                // name of plot
                String ThePlotsName = nameField.getText();
                if(ThePlotsName.length() > 0  & sizeValue.getText().matches("[0-9]{1,13}(\\.[0-9]*)?") & !plant.getValue().toString().equals("plant")) {
                    // plot size
                    double thePlotSize;
                    if (sqm.isSelected()) {
                        thePlotSize = Double.parseDouble(sizeValue.getText());
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
                    if (selectedPlant.equals(null)) {
                        Alert noPlantAlert = new Alert(Alert.AlertType.ERROR);
                        noPlantAlert.setContentText("please select a plant");
                    } else {
                        for (Map.Entry<String, Integer> entry : plantsInUser.entrySet()) {
                            if (entry.getKey().equals(selectedPlant)) {
                                plantID = entry.getValue();
                            } else {

                            }
                        }

                    }
                    Plant plotsPlant = new Plant();
                    try {
                        plotsPlant = Database.createPlant(plantID);
                    } catch (SQLException e) {

                    }
                    // number of plants in plot
                    int number = ((plotsPlant.getNumberPerMeter() * (int) thePlotSize) < 1) ? 1 : (plotsPlant.getNumberPerMeter() * (int) thePlotSize);


                    // soil value
                    String soilStringValue = soilSelect.getValue();
                    double TheSoilValue = 0.0;
                    int soilID = 0;
                    for (SoilType st : soilTypesList) {
                        if (st.getName().equals(soilStringValue)) {
                            TheSoilValue = st.getValue();
                            soilID = st.getId();
                        }
                    }


                    // environment value
                    double theEnvironmentValue = 0.0;
                    int environmentID = 0;
                    if (environmentSelect.getValue().toString().equals("Polytunnel")) {
                        theEnvironmentValue = 1.25;
                        environmentID = 1;

                    } else if (environmentSelect.getValue().toString().equals("Raised Beds")) {
                        theEnvironmentValue = 1.05;
                        environmentID = 2;
                    } else {
                        theEnvironmentValue = 1.0;
                        environmentID = 3;
                    }

                    // priority
                    int thePriorityValue = 0;
                    int priorityID = 0;
                    if (plotPriorty.getValue().toString().equals("High")) {
                        thePriorityValue = 3;

                    } else if (plotPriorty.getValue().toString().equals("Low")) {
                        thePriorityValue = 1;
                    } else {
                        thePriorityValue = 2;
                    }

                    System.out.println( "plant id = " + plotsPlant.getid() + "/n"  + "The plot name is " + ThePlotsName + "\n" + "The plot size is " + thePlotSize + "\n" +
                            "It was planted on " + ThedatePlanted + " Day= " + day + "month= " + month + "year= " + year + "\n" +
                            "its size is " + thePlotSize + " the plant is " + plotsPlant.getName() + "\n" +
                            " the number of plants is is " + number + "\n" + " the soil value is " + TheSoilValue + "\n" +
                            " the environment value is " + theEnvironmentValue + "\n" + "the priority value is " + thePriorityValue
                    );


                    if (Database.insertNewPlot(ThePlotsName, thePlotSize, plotsPlant.getid(), g.getGardenID(), soilID, environmentID, day, month, year, thePriorityValue) == true) {

                        try {
                            Plot theNewPlot = Database.createPlot(ThePlotsName, day, month, year);
                            g.getPlots().add(theNewPlot);
                            setGardenAndPlotScene(s, u, g);
                        } catch (SQLException e) {

                        }


                    } else {
                        Alert writeFailure = new Alert(Alert.AlertType.ERROR);
                        writeFailure.setContentText("Failed To Write to Database ");
                        writeFailure.show();
                    }
                } else {
                    if(ThePlotsName.length() < 1) {
                        Alert pleaseEnterName = new Alert(Alert.AlertType.ERROR);
                        pleaseEnterName.setContentText("Please enter a plot name");
                        pleaseEnterName.show();
                    }
                    else if(plant.getValue().equals("plant")){
                        Alert noPlantSelect = new Alert(Alert.AlertType.ERROR);
                        noPlantSelect.setContentText("Please select plant");
                        noPlantSelect.show();
                    }
                    else {

                        Alert enterRightData = new Alert(Alert.AlertType.ERROR);
                        enterRightData.setContentText("Please enter valid size value");
                        enterRightData.show();

                    }
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




        addPlotPane.add(name, 1, 1);
        addPlotPane.add(nameField, 3, 1);
        addPlotPane.add(environmentLabel, 1, 2);
        addPlotPane.add(environmentSelect, 3, 2, 3, 1);
        addPlotPane.add(soilSelectTitle, 1, 3);
        addPlotPane.add(soilSelect, 3, 3, 3, 1);

        addPlotPane.add(plotPrioirityTitle, 1, 4);
        addPlotPane.add(plotPriorty, 3, 4);

        addPlotPane.add(plantLabel, 1, 5);
        addPlotPane.add(plantType, 3, 5);
        addPlotPane.add(plant, 5, 5);

        addPlotPane.add(datePlantedLabel, 1, 7);
        addPlotPane.add(plantedDate, 3, 7);

        addPlotPane.add( howBigPlot, 1, 8);
        addPlotPane.add(sizeValue, 3, 8);
        addPlotPane.add(sqft, 5 , 8);
        addPlotPane.setHalignment(sqft, HPos.RIGHT);
        addPlotPane.add(sqm, 5, 8);
        addPlotPane.setHalignment(sqm, HPos.LEFT);


        addPlotPane.add(addPlotButton, 3, 9    );

        addPlotPane.add(backButton, 1, 9);

        s.setScene(addPlotScene);
    }

    /**
     * Login page creation
     * @param s
     */
    public static void login(Stage s){

        GridPane loginPageGrid = new GridPane();
        Scene loginEntry = new Scene(loginPageGrid, 500, 300);
        loginPageGrid.gridLinesVisibleProperty().setValue(false);
        loginPageGrid.setGridLinesVisible(false);
        loginPageGrid.setStyle("-fx-background-image: url(" + "/res/wildFlowers.jpg"+")");
        System.out.println(loginPageGrid.isGridLinesVisible());


        // login Entry set up

        javafx.scene.control.Label loginLabel = new javafx.scene.control.Label("LOGIN");
        loginLabel.setStyle("-fx-font-size: 100%");
        loginLabel.setTextFill(Color.WHITE);
        

        javafx.scene.control.TextField loginEntryField = new javafx.scene.control.TextField("userName");
        javafx.scene.control.Label password = new javafx.scene.control.Label("Password");
        password.setTextFill(Color.WHITE);
        password.setStyle("-fx-font-size: 120%");
        PasswordField thePasswordField = new PasswordField();
        thePasswordField.setPromptText("Password");
       thePasswordField.setOnKeyPressed( e -> {
           if(e.getCode()== KeyCode.ENTER){
               String theUserName = loginEntryField.getText();
               String thePassword =  thePasswordField.getText();
               if( Database.userNameAndPasswordCheck(theUserName, thePassword)) {


                   try {
                       User temp = Database.createUser(theUserName);

                       setGardenScene(s, temp);


                   } catch (SQLException f){

                   }


               } else {


                   Alert userAlreadyExistsAlert = new Alert(Alert.AlertType.ERROR);
                   userAlreadyExistsAlert.setContentText("Not Valid User");
                   userAlreadyExistsAlert.show();



               }
           }
       });

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

        javafx.scene.control.Button backButton = new javafx.scene.control.Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                welcomePage(s);
            }
        });


        loginPageGrid.setPadding(new javafx.geometry.Insets(2, 2,2, 2));
        loginPageGrid.setAlignment(Pos.CENTER);
        loginPageGrid.setHgap(5);
        loginPageGrid.setVgap(5);
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(60);
        ColumnConstraints outCol = new ColumnConstraints();
        outCol.setPercentWidth(20);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(20);
        loginPageGrid.getColumnConstraints().addAll(outCol, col, outCol);
        loginPageGrid.getRowConstraints().addAll(row, row, row, row, row);

        loginPageGrid.setPadding(new javafx.geometry.Insets(5  , 5, 5 ,5 ));
        loginPageGrid.add(loginLabel,1,0);
        loginPageGrid.add(loginEntryField, 1,1);
        loginPageGrid.add(password, 1, 2);
        loginPageGrid.add(thePasswordField, 1, 3);
        loginPageGrid.add(loginButton, 1, 4);
        loginButton.setAlignment(Pos.CENTER_LEFT);



        loginPageGrid.add(backButton, 1, 4);

       GridPane.setHalignment(backButton, HPos.RIGHT);

       backButton.setMaxSize(140, Double.MAX_VALUE);
       loginButton.setMaxSize(140, Double.MAX_VALUE);
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

                LoginView newLoginView = new LoginView(primaryStage);
                newLoginView.launchLogin();
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
        Scene newUserScene = new Scene(grid, 400 , 500);

        grid.setStyle("-fx-background-image: url(" + "/res/greenHouse.jpg" + ")");
        s.setTitle("New User Entry");
        grid.setHgap(5);
        grid.setVgap(5);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(60);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(20);
        RowConstraints outer = new RowConstraints();
        outer.setPercentHeight(10);
        RowConstraints inner = new RowConstraints();
        inner.setPercentHeight(20);
        grid.getColumnConstraints().addAll(col2, col1, col2);
        grid.getRowConstraints().addAll(outer, inner, inner, inner, inner, outer);



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
        grid.setPadding(new javafx.geometry.Insets(2, 2, 2, 2));
        grid.add(user,1,1);
        user.setMaxSize(Double.MAX_VALUE, 50);
        grid.add(emailentry, 1, 2);
        emailentry.setMaxSize(Double.MAX_VALUE, 50);
        grid.add(pass, 1, 3);
        pass.setMaxSize(Double.MAX_VALUE, 50);
        grid.add(submit, 1, 4);
        submit.setMaxHeight(40);
        grid.add(backButton, 1, 4);
        submit.setMaxHeight(40);

        grid.setHalignment(backButton, HPos.RIGHT);

        ;



        s.setScene(newUserScene);


    }

    // main method with launch method to start javafx
    public static  void main(String[] args)

    {
       // System.setProperty("java.library.path","/home/stephen/IdeaProjects/MSCproject/out/artifacts/lib/libmwmclmcrrt.so.9.2" );
        System.out.println(System.getProperty("java.library.path"));
        launch(args);
    }


}
