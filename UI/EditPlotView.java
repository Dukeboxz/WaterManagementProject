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
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;



/**
 * Class to create UI and action for to allow user to edit the details of individual plot
 * Author Stephen Jackson
 */

public class EditPlotView {

    private Plot plot;
    private Stage stage;
    private User user;
    private Garden garden;

    public EditPlotView(Plot p , Stage s, User u, Garden g) {
        this.plot = p;
        this.stage = s;
        this.user = u;
        this.garden = g;

    }

    //Method to set up UI scene and action
    public void setUp() {

        this.getStage().setTitle("Edit Plot");

        // create alternate page to edit plot details


        // Set up layout of Scene
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



        //Set up elements of UI to edit the plot
        javafx.scene.control.Label editPlotName = new javafx.scene.control.Label("Plot Name");
        javafx.scene.control.TextField plotNameTextField = new javafx.scene.control.TextField();
        plotNameTextField.setText(plot.getName());

        ComboBox plantTypes = new ComboBox();

        javafx.scene.control.Label editPlantedLabel = new javafx.scene.control.Label("Planted with:");
        Map<Integer, String> mapOfPlantTypes = Database.getPlantTypesInMap();
        int planttypeCounter = 0;
        for(Map.Entry<Integer, String> entry : mapOfPlantTypes.entrySet()){
            plantTypes.getItems().add(planttypeCounter, entry.getValue());

        }

        Map<String, Plant> mapOfPlants = new TreeMap<>();
        ComboBox plantsOfType = new ComboBox();
        plantsOfType.setValue(plot.getPlant().getName());

        //change plant options dependent on type selected
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
        DatePicker theDatePlanted = new DatePicker(plot.getDatePlanted());


        javafx.scene.control.Label editSizeLabel = new javafx.scene.control.Label("Size of plot: ");
        javafx.scene.control.TextField sizeField = new javafx.scene.control.TextField(Double.toString(plot.getSize()));

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
            if(entry.getKey().equals(plot.getPriority())){
                thePrioirty.getSelectionModel().select(counter);
            }
            counter++;
        }

        javafx.scene.control.Button editPlotBackButton = new javafx.scene.control.Button("Back");

        editPlotBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                GardenAndPlotView back = new GardenAndPlotView(EditPlotView.this.getStage(), EditPlotView.this.getUser(), EditPlotView.this.getGarden());
                back.launch();
            }
        });

        // check user rights and show edit button if allowed
        javafx.scene.control.Button saveEditButton = new javafx.scene.control.Button("Save Edit");
        if(this.getGarden().getUserEditRights()==false){
            saveEditButton.setVisible(false);
        }

        // Action to save changes made to plot
        saveEditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String plotName = plotNameTextField.getText().trim();
                boolean nameExists = false;
                for(Plot p : EditPlotView.this.getGarden().getPlots()){
                    if(p.getName().equals(plotName)){
                        nameExists=true;
                    }
                }
                if(nameExists==false) {
                    if (plotNameTextField.getText().length() < 1 || plantsOfType.getValue().equals(null) || sizeField.getText().length() <= 0) {

                        if (plotNameTextField.getText().length() < 1) {
                            Alert enterName = new Alert(Alert.AlertType.ERROR);
                            enterName.setContentText("Please Enter Name");
                            enterName.show();
                            plotNameTextField.setStyle("-fx-border-color: Red");
                        }
                        if (plantsOfType.getValue().equals(null)) {
                            Alert enterPlant = new Alert(Alert.AlertType.ERROR);
                            enterPlant.setContentText("Please choose plant");
                            plantsOfType.setStyle("-fx-border-color: Red");
                        }
                        if (sizeField.getText().length() < 1) {
                            Alert noSize = new Alert(Alert.AlertType.ERROR);
                            noSize.setContentText("Please enter relevant size");
                            noSize.show();
                        }

                    } else {
                        String newPlotName = plotNameTextField.getText();

                        String plantName = plantsOfType.getValue().toString().trim();
                        System.out.println(plantName);
                        Plant thePlant = null;
                        if (plantName.equals(plot.getPlant().getName().trim())) {
                            thePlant = plot.getPlant();

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

                        newPlotSize = (sqm.isSelected() == true) ? newPlotSize : newPlotSize * 0.092903;
                        newPlotSize = (newPlotSize < 1) ? 1 : newPlotSize;

                        LocalDate theLocalDate = theDatePlanted.getValue();

                        String thePriorityString = thePrioirty.getValue().toString();

                        double priorityValue = 0;
                        for (Map.Entry<Double, String> entry : priorityMap.entrySet()) {
                            if (entry.getValue().equals(thePriorityString)) {
                                priorityValue = entry.getKey();
                            }
                        }

                        int id = plot.getId();
                        int numberOfPlants = 1;

                        numberOfPlants = (int) newPlotSize * thePlant.getNumberPerMeter();


                        for (Plot gPlots : garden.getPlots()) {
                            if (id == gPlots.getId()) {
                                gPlots.setName(newPlotName);
                                plot.setName(newPlotName);
                                gPlots.setPlant(thePlant);
                                plot.setPlant(thePlant);
                                gPlots.setSize(newPlotSize);
                                plot.setSize(newPlotSize);
                                gPlots.setNoOfPlants(numberOfPlants);
                                plot.setNoOfPlants(numberOfPlants);
                                gPlots.setPriority(priorityValue);
                                plot.setPriority(priorityValue);

                            }
                        }
                        System.out.println("New Plot name= " + newPlotName + "\n" +
                                "The new plant= " + thePlant.getName() + "\n" +
                                " The new planted date= " + theLocalDate + "\n" +
                                "New size of Plot= " + newPlotSize + "\n" +
                                "New plot priority= " + priorityValue + "\n" +
                                "The plant id= " + thePlant.getid());

                        Database.updatePlot(plot.getId(), newPlotName, newPlotSize, thePlant.getid(), theLocalDate, priorityValue);

                        PlotView next = new PlotView(EditPlotView.this.getPlot(), EditPlotView.this.getStage(), EditPlotView.this.getUser(), garden);
                        next.launch();

                    }
                } else {
                    Alert nameNotValid = new Alert(Alert.AlertType.ERROR);
                    nameNotValid.setHeaderText("Plot Name Already Exists");
                    nameNotValid.show();
                }
            }
        });


        // Button to remove plot - Only showed if user has edit rights
        Button removePlot = new Button("Remove Plot");
        if(this.getGarden().getUserEditRights()==false){
            removePlot.setVisible(false);
        }

        // sets action to remove current plot
        removePlot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert checkRemoval = new Alert(Alert.AlertType.CONFIRMATION);
                checkRemoval.setHeaderText("Plot to be removed");
                checkRemoval.setContentText("Do you wish to proceed?");

                Optional<ButtonType> result = checkRemoval.showAndWait();
                if(result.get() == ButtonType.OK){
                    ArrayList<Plot> toBeRemoved = new ArrayList<>();
                    for(Plot thePlot : garden.getPlots() ){

                        if(thePlot.getId()==plot.getId()){

                            toBeRemoved.add(thePlot);
                            Database.deletePlot(thePlot.getId());


                        }
                    }

                   EditPlotView.this.getGarden().getPlots().removeAll(toBeRemoved);
                    GardenAndPlotView next = new GardenAndPlotView(EditPlotView.this.getStage(), EditPlotView.this.getUser(), EditPlotView.this.getGarden());
                    next.launch();





                } else {

                }
            }
        });





        // set up layout of edit plot scene
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
        editPlotPane.add(removePlot, 5, 7);



        this.getStage().setScene(editPlotScene);



    }

    //Getter and Setter
    public Plot getPlot() {
        return plot;
    }

    public Stage getStage() {
        return stage;
    }

    public User getUser() {
        return user;
    }

    public Garden getGarden() {
        return garden;
    }

    //launch method to create UI
    public void launchEditPlotView() {

        setUp();

        this.getStage().show();
}




}
