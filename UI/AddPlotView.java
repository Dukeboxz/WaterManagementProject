package UI;

import Optimizer.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class to create UI and actions that allow users to add new plots to their garden
 */

public class AddPlotView {

    private Stage stage;
    private User user;
    private Garden garden;

    public AddPlotView(Stage stage, User user, Garden garden){
        this.garden=garden;
        this.stage=stage;
        this.user=user;
    }

    //Getters and Setters
    public Stage getStage() {
        return stage;
    }

    public User getUser() {
        return user;
    }

    public Garden getGarden() {
        return garden;
    }


    /**
     * Method to set up UI and Actions
     */
    public  void setUp(){

        //set up layout of scene
        GridPane addPlotPane = new GridPane();
        Scene addPlotScene = new Scene(addPlotPane, 400, 500);
        this.getStage().setTitle("Add Plot");
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

        // add elements to allow user to add new plot to garden
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
                String ThePlotsName = nameField.getText().trim();
                boolean samename = false;

                for (Plot p : AddPlotView.this.getGarden().getPlots()) {
                    if (p.getName().equals(ThePlotsName)) {
                        samename = true;
                    }
                }
                if (samename == false) {
                    if (ThePlotsName.length() > 0 & sizeValue.getText().matches("[0-9]{1,13}(\\.[0-9]*)?") & !plant.getValue().toString().equals("plant")) {
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

                        System.out.println("plant id = " + plotsPlant.getid() + "/n" + "The plot name is " + ThePlotsName + "\n" + "The plot size is " + thePlotSize + "\n" +
                                "It was planted on " + ThedatePlanted + " Day= " + day + "month= " + month + "year= " + year + "\n" +
                                "its size is " + thePlotSize + " the plant is " + plotsPlant.getName() + "\n" +
                                " the number of plants is is " + number + "\n" + " the soil value is " + TheSoilValue + "\n" +
                                " the environment value is " + theEnvironmentValue + "\n" + "the priority value is " + thePriorityValue
                        );


                        if (Database.insertNewPlot(ThePlotsName, thePlotSize, plotsPlant.getid(), AddPlotView.this.getGarden().getGardenID(), soilID, environmentID, day, month, year, thePriorityValue) == true) {

                            try {
                                Plot theNewPlot = Database.createPlot(ThePlotsName, day, month, year);
                                AddPlotView.this.getGarden().getPlots().add(theNewPlot);
                                GardenAndPlotView next = new GardenAndPlotView(AddPlotView.this.getStage(), AddPlotView.this.getUser(), AddPlotView.this.getGarden());
                                next.launch();

                            } catch (SQLException e) {

                            }


                        } else {
                            Alert writeFailure = new Alert(Alert.AlertType.ERROR);
                            writeFailure.setContentText("Failed To Write to Optimizer.Database ");
                            writeFailure.show();
                        }
                    } else {
                        if (ThePlotsName.length() < 1) {
                            Alert pleaseEnterName = new Alert(Alert.AlertType.ERROR);
                            pleaseEnterName.setContentText("Please enter a plot name");
                            pleaseEnterName.show();
                        } else if (plant.getValue().equals("plant")) {
                            Alert noPlantSelect = new Alert(Alert.AlertType.ERROR);
                            noPlantSelect.setContentText("Please select plant");
                            noPlantSelect.show();
                        } else {

                            Alert enterRightData = new Alert(Alert.AlertType.ERROR);
                            enterRightData.setContentText("Please enter valid size value");
                            enterRightData.show();

                        }
                    }

                } else {
                    Alert alreadyExists = new Alert(Alert.AlertType.ERROR);
                    alreadyExists.setHeaderText("Plot Name Already Exists");
                    alreadyExists.show();
                }
            }
        });

        // back button action moves back to previouse scene
        javafx.scene.control.Button backButton = new javafx.scene.control.Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                GardenAndPlotView next = new GardenAndPlotView(AddPlotView.this.getStage(), AddPlotView.this.getUser(), AddPlotView.this.getGarden());
                next.launch();
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

        this.getStage().setScene(addPlotScene);


    }

    /**
     * Method that launches the UI for this part of the application
     */
    public void launch() {
        setUp();

        this.getStage().show();
    }
}
