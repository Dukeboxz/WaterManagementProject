package UI;

import Optimizer.*;
import com.sun.javafx.binding.StringFormatter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.time.LocalDate;
import java.util.*;

/**
 * Class to produce UI for the optimization part of the application
 * Author Stephen Jackson
 */

public class OptimizationSceneView implements Observer {

    private Optimiser op;
    private Stage s;
    private Stage secondStage;
    private Optimiser copy;
    private User user;
    private boolean resultsSaved;
    private String waterUnits;

    public OptimizationSceneView(Optimiser op, Stage theStage, User user, String waterUnits) {
        this.op = op;
        this.s = theStage;

        this.secondStage = new Stage();
        this.user=user;



        this.copy = op;

        this.resultsSaved=false;
        this.waterUnits=waterUnits;


    }


    /*
    Method that sets up the elements for UI
     */
    public void setUp(){


        //Set Up Grid and new scene
        GridPane opGrid = new GridPane();
        getSecondStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {

            }
        });
        opGrid.getStylesheets().add("/res/StyleSheet.css");

        opGrid.setHgap(5);
        opGrid.setVgap(5);


        Scene opScene = new Scene(opGrid, 1000, 1000);

        //Data to be used

        Map<String, ArrayList<Double>> optimisedInMap = this.getOp().optimizeForMap();
        Map<String, ArrayList<Double>> optimalPerPlot = this.getOp().getOptimalMap();
        Map<String, ArrayList<Double>> basicPerPlot = this.getOp().getBasicMap();
        double[] opP = this.getOp().getOptimisationPoints();
        double[] arg = this.getOp().decisionByDay(optimisedInMap);



        for(Map.Entry<String, ArrayList<Double>> ent : optimisedInMap.entrySet()){
            System.out.println(ent.getKey());
            ArrayList<Double> r = ent.getValue();
            System.out.println(r.get(0) + " " + r.get(1));
        }


        //set up graph
        final NumberAxis xAxis = new NumberAxis(0, this.getOp().getDays() -1, 1);
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLowerBound(0.1);

        xAxis.setLabel("Days");
        yAxis.setLabel("Water inches^3");


        LineChart<Number, Number> waterChart = new LineChart<Number, Number>(xAxis, yAxis);
        waterChart.setTitle("Water Usage");

        XYChart.Series optimalUse = new XYChart.Series();
        optimalUse.setName("Desired");

        double optimalTotal = 0;
        for(int i = 0 ; i < opP.length; i++){
            optimalTotal+=opP[i];
        }
        for(int i = 0 ; i < opP.length; i++){


            optimalUse.getData().add(new XYChart.Data<>(i, opP[i]));


        }






        double decisionTotal = 0;

        for(int i = 0 ; i < arg.length; i++){
            decisionTotal+=arg[i];
        }


        double waterLeft = (this.getOp().getWaterAvailable() - optimalTotal) < 0 ? 0 :(this.getOp().getWaterAvailable()-optimalTotal);
        if(this.getWaterUnits().equals("Litres")){

            waterLeft = waterLeft*0.0163871;


        } else if(this.getWaterUnits().equals("Gallons")){

            waterLeft = waterLeft*0.00360465;

        } else {

            waterLeft=waterLeft;

        }


        XYChart.Series decionUse = new XYChart.Series();
        decionUse.setName("Optimal");
        for(int i = 0; i < arg.length; i++){
            decionUse.getData().add(new XYChart.Data<>(i , arg[i]));
        }

        // Chart Basic Points
        double[] basicPoints = this.getOp().getBasicPoints();
        XYChart.Series basicPointsSeries = new XYChart.Series();
        basicPointsSeries.setName("Basic");
        for(int i = 0; i < basicPoints.length; i++){
            basicPointsSeries.getData().add(new XYChart.Data<>(i, basicPoints[i]));
        }


        int plotCounter = 0;
        int colCounter = 0;

        // Create Button bar to store radio buttons to show series for each plot
        ButtonBar descionVariables = new ButtonBar();
        descionVariables.setButtonMinWidth(40);
        ScrollPane decisionScroler = new ScrollPane(descionVariables);
        decisionScroler.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        decisionScroler.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // set up optimised chart series
        opGrid.add(decisionScroler, 0,6, 6, 1 );
        ArrayList<XYChart.Series> optimisedSeries = new ArrayList<>();
        for(Map.Entry<String, ArrayList<Double>> entry: optimisedInMap.entrySet()){
            String thePlotName = entry.getKey();

            XYChart.Series newDecisionSeries = new XYChart.Series();
            newDecisionSeries.setName("Optimal for "+thePlotName);
            int counter = 0;
            for(Double d : entry.getValue()){

                newDecisionSeries.getData().add(new XYChart.Data<>(counter, d));
                counter++;

            }


            optimisedSeries.add(newDecisionSeries);




        }

        // set up desired chart series
        ArrayList<XYChart.Series> desiredSeries = new ArrayList<>();
        for(Map.Entry<String, ArrayList<Double>> entry : optimalPerPlot.entrySet()){
            String thePlotName = entry.getKey();
            XYChart.Series newDesiredSeries = new XYChart.Series();
            newDesiredSeries.setName("Desired for "+thePlotName);
            int counter = 0;

            for(Double d : entry.getValue()) {
                newDesiredSeries.getData().add(new XYChart.Data<>(counter, d));
                counter++;
            }

            desiredSeries.add(newDesiredSeries);
        }

        //set up basic chart series
        ArrayList<XYChart.Series> basicSeries = new ArrayList<>();
        for(Map.Entry<String, ArrayList<Double>> entry: basicPerPlot.entrySet()) {
            String thePlotName = entry.getKey();
            XYChart.Series newBasicSeries = new XYChart.Series();
            newBasicSeries.setName("Basic for "+thePlotName);
            int counter = 0 ;

            for(Double d : entry.getValue()){
                newBasicSeries.getData().add(new XYChart.Data<>(counter, d));
                counter++;
            }

            basicSeries.add(newBasicSeries);

        }


        // associate optimized, basic and desired  series for each plot with radio button
            this.getOp().getOptimalMap().forEach((k,v) -> {

                RadioButton newButton = new RadioButton(" Show " + k);
                newButton.setWrapText(true);


                newButton.setSelected(false);
                newButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                        if (t1 == true) {
                            for(XYChart.Series series : optimisedSeries){
                                if(series.getName().equals("Optimal for "+k) ) {
                                    waterChart.getData().add(series);
                                }
                            }
                            for(XYChart.Series series : desiredSeries){
                                if(series.getName().equals("Desired for "+k)){
                                    waterChart.getData().add(series);
                                }
                            }
                            for(XYChart.Series series : basicSeries){
                                if(series.getName().equals("Basic for " + k)){
                                    waterChart.getData().add(series);
                                }
                            }

                        } else {

                            ArrayList<XYChart.Series> toBeRemoved = new ArrayList<>();
                            for(XYChart.Series series : waterChart.getData()){
                                if(series.getName().equals("Optimal for "+k) || series.getName().equals("Desired for " + k) || series.getName().equals("Basic for " + k)){
                                    toBeRemoved.add(series);
                                }


                            }
                            waterChart.getData().removeAll(toBeRemoved);

                        }
                    }
                });

                descionVariables.getButtons().add(newButton);
            });





        plotCounter++;



        // Add date series for total desired. basic and optimal
        waterChart.getData().add(optimalUse);
        waterChart.getData().add(decionUse);
        waterChart.getData().add(basicPointsSeries);


        // Radio Button to show desired, optimal and basic series on chart
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



        // UI layout
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
        dayChooseSlide.setMax(this.getOp().getDays()-1);
        opGrid.add(dayChooseSlide, 0, 11, 5, 1);




        // set up elements to allow user to do what if scenario
        GridPane testGrid = new GridPane();

        Label whatIfLabel = new Label("Test new plot");

        TextField testPlotName = new TextField("Plot Name");
        Label plantType = new Label("Plant type");
        ComboBox plantTypes = new ComboBox();
        ComboBox plantsOfType = new ComboBox();
        plantsOfType.setValue("Plants");

        Map<Integer, String> thePlantTypes = Database.getPlantTypesInMap();
        Map<String, Plant> allThePlants = Database.returnMapOfAllPlants();
        thePlantTypes.forEach((k,v) -> {
            plantTypes.getItems().add(v);
        });

        plantTypes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String thePlantTypeSelected = plantTypes.getValue().toString();

                thePlantTypes.forEach((k,v) -> {
                    if(v.equals(thePlantTypeSelected)) {
                        int plantTypeId = k;
                        if(plantsOfType.getItems().isEmpty()) {
                            allThePlants.forEach((x, y) -> {
                                if(y.getType()==plantTypeId){
                                    plantsOfType.getItems().add(x);
                                }

                            });
                        } else {
                            plantsOfType.getItems().removeAll();
                            allThePlants.forEach((key, value) -> {
                                if(value.getType()==plantTypeId){
                                    plantsOfType.getItems().add(key);
                                }
                            });

                        }
                    }
                });


            }
        });

        // What if - plot size
        Label sizePlotLabel = new Label("Size");
        TextField sizeOfPlot = new TextField();
        sizeOfPlot.setText("size of plot");

        // What if - Plot soil Type
        Label soilLabel = new Label("Soil Type");
        ArrayList<SoilType> soilTypes = Database.returnSoilTypeList();
        ComboBox ComboBoxSoilTypes = new ComboBox();
        for(SoilType s: soilTypes) {
            ComboBoxSoilTypes.getItems().add(s.getName());
        }
        ComboBoxSoilTypes.getSelectionModel().select(2);

        //  What if - environment values
        Label envLable = new Label("Environment");
        ArrayList<Environment> env = Database.returnEnvironmentList();
        ComboBox envTypes = new ComboBox();
        for(Environment e: env){
            envTypes.getItems().add(e.getName());
        }
        envTypes.getSelectionModel().select(2);

        //What if -  priority
        Label priorityLabel = new Label("Priority");
        ComboBox priorityBox = new ComboBox();
        priorityBox.getItems().add("High");
        priorityBox.getItems().add("Normal");
        priorityBox.getItems().add("Low");
        priorityBox.getSelectionModel().select(1);



        //What if - plot size
        ToggleGroup sizes = new ToggleGroup();
        RadioButton sqm = new RadioButton("sqm");
        sqm.setSelected(true);
        sqm.setToggleGroup(sizes);
        RadioButton sqft = new RadioButton("sqft");
        sqft.setToggleGroup(sizes);

        //What if - date planted
        Label theDateLabel = new Label("Date \n Planted");
        DatePicker theTestDate = new DatePicker(LocalDate.now());

        //What if - buttons to initate actions for scenrios
        Button test = new Button("Test");
        Button removeTest = new Button("Remove Test Plots");
        Button saveTest = new Button("Save Test Plots to Garden");

        // When button pressed add new plot to the optimization series
        test.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                OptimizationSceneView.this.setCopy(OptimizationSceneView.this.getOp());
                if(testPlotName.getText().length() > 0) {

                    if (sizeOfPlot.getText().length() > 0 & sizeOfPlot.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                        if (plantsOfType.getValue().toString().equals("Plants")) {
                            Alert enterPlantType = new Alert(Alert.AlertType.ERROR);
                            enterPlantType.setHeaderText("Please select plant");
                            enterPlantType.show();
                        } else {
                            Plant newPlant = null;
                            for (Map.Entry<String, Plant> entry : allThePlants.entrySet()) {
                                if (entry.getKey().equals(plantsOfType.getValue().toString())) {
                                    newPlant = entry.getValue();
                                }
                            }

                            double size = sqm.isSelected() ? Double.valueOf(sizeOfPlot.getText()) : Double.valueOf(sizeOfPlot.getText()) * 0.092903;

                            double soilTypeValue = 0;
                            String soilTypeString = ComboBoxSoilTypes.getValue().toString();
                            System.out.println("*" + soilTypeString + "*");
                            for(SoilType s : soilTypes){
                                if(s.getName().equals(soilTypeString)){
                                    soilTypeValue=s.getValue();
                                }
                            }
                            assert soilTypeValue > 0;
                            double envValue = 0;
                            String envString = envTypes.getValue().toString();
                            for(Environment e: env){
                                if(envString.equals(e.getName())){
                                    envValue=e.getValue();
                                }
                            }
                            double priorityValue =1;
                            String priorityString = priorityBox.getValue().toString();
                            if (priorityString.equals("High")) {
                                priorityValue=3.0;
                            } else if(priorityString.equals("Medium")) {
                                priorityValue=2.0;
                            } else {
                                priorityValue=1.0;
                            }



                            double numberOfPlants = (size * newPlant.getNumberPerMeter()) < 0 ? 1 : (size * newPlant.getNumberPerMeter());
                            int noOfPlants = (int) numberOfPlants;

                            Plot newPlot = new Plot(999999, testPlotName.getText(), size, theTestDate.getValue(), newPlant, noOfPlants, soilTypeValue, envValue, priorityValue);

                            Garden tmp = op.getGarden();
                            ArrayList<Plot> tmpPlotList = tmp.getPlots();
                            tmpPlotList.add(newPlot);

                            for(Plot p : tmpPlotList){
                                System.out.println(p.getName());
                                System.out.println(p.getPlant().getSt1_or());
                                System.out.println();
                            }
                            op.getGarden().setPlots(tmpPlotList);


                        }


                    } else {
                        Alert relevantSize = new Alert(Alert.AlertType.ERROR);
                        relevantSize.setHeaderText("Size data not numeric");
                        relevantSize.show();
                    }
                } else {
                    Alert noName = new Alert(Alert.AlertType.ERROR);
                    noName.setHeaderText("Test plot must have name");
                }
            }
        });

        // After button pressed test plots are removed from optimization scene
        removeTest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<Plot> plotList = op.getGarden().getPlots();
                ArrayList<Plot> plotsToBeRemoved = new ArrayList<>();
                for(Plot p : plotList){
                    if(p.getId() > 99999){
                        plotsToBeRemoved.add(p);
                    }
                }
                plotList.removeAll(plotsToBeRemoved);
                op.getGarden().setPlots(plotList);
            }
        });


        // After button pressed test plots are saved to the garden and database updated
        saveTest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                ArrayList<Plot> plotList = OptimizationSceneView.this.getOp().getGarden().getPlots();
                ArrayList<Plot> addedPlots = new ArrayList<>();

                for(Plot p: plotList){
                    if (p.getId()> 9999){
                        addedPlots.add(p);
                    }
                }

                LocalDate selectedDate = theTestDate.getValue();


                for(Plot np : addedPlots){

                    int soilId = 0;
                    for(SoilType e : soilTypes){
                        if(np.getSoil()==e.getValue()){
                            soilId=e.getId();
                        }
                    }
                    assert soilId >0 ;

                    int environmentID = 0;
                    for(Environment e: env){
                        if(np.getEnvironment()==e.getValue()){
                            environmentID=e.getId();
                        }
                    }
                    assert environmentID >0;


                    Database.insertNewPlot(np.getName(), np.getSize(), np.getPlant().getid(),
                            OptimizationSceneView.this.getOp().getGarden().getGardenID(), soilId,environmentID ,
                            selectedDate.getDayOfMonth(), selectedDate.getMonthValue(), selectedDate.getYear(), np.getPriority());
                }

                Garden newGarden = Database.createGarden(OptimizationSceneView.this.getOp().getGarden().getGardenID(),
                        OptimizationSceneView.this.getOp().getGarden().getUserEditRights());

                OptimizationSceneView.this.getOp().setGarden(newGarden);

            }
        });

        //Set up layout of what if part of page
        testGrid.setHgap(5);
        testGrid.setVgap(5);
        testGrid.add(whatIfLabel, 1, 1, 1, 1);
        testGrid.add(testPlotName, 2, 1);
        testGrid.add(plantType, 1, 2);
        testGrid.add(plantTypes, 2, 2);
        testGrid.add(plantsOfType, 3, 2);
        testGrid.add(sizePlotLabel, 1, 3);
        testGrid.add(sizeOfPlot, 2, 3);
        testGrid.add(sqm, 3, 3);
        testGrid.setHalignment(sqm, HPos.LEFT);
        testGrid.add(sqft,3, 3);
        testGrid.setHalignment(sqft, HPos.RIGHT);
        testGrid.add(theDateLabel, 1, 4);
        testGrid.add(theTestDate, 2, 4);
        testGrid.add(soilLabel,4, 1 );
        testGrid.add(ComboBoxSoilTypes, 5, 1);
        testGrid.add(envLable, 4, 3);
        testGrid.add(envTypes, 5, 3);
        testGrid.add(priorityLabel, 4, 4);
        testGrid.add(priorityBox, 5, 4);


        testGrid.add(test, 1, 5);
        testGrid.add(removeTest, 2, 5);
        testGrid.add(saveTest, 3, 5);


        opGrid.add(testGrid, 0, 7, 5, 4);

        testGrid.setId("testGrid");

        //End of what if scenario set up
        //



        // Save result Button that exports results to file
        Button exportResults = new Button("Export Results");

        exportResults.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                exportToFile(optimisedInMap);

            }
        });


        //back button

        Button backButton = new Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(OptimizationSceneView.this.getResultsSaved()==true) {
                    GardenAndPlotView back = new GardenAndPlotView(OptimizationSceneView.this.getS(), OptimizationSceneView.this.getUser(), OptimizationSceneView.this.getOp().getGarden());
                    back.launch();

                } else {
                    ArrayList<Plot> plotsToBeRemoved = new ArrayList<>();
                    for(Plot p : OptimizationSceneView.this.getOp().getGarden().getPlots()){
                        if(p.getId() > 9999){
                            plotsToBeRemoved.add(p);
                        }
                    }
                    OptimizationSceneView.this.getOp().getGarden().getPlots().removeAll(plotsToBeRemoved);

                    GardenAndPlotView back = new GardenAndPlotView(OptimizationSceneView.this.getS(), OptimizationSceneView.this.getUser(), OptimizationSceneView.this.getOp().getGarden());
                    back.launch();
                }
            }
        });
        opGrid.add(exportResults, 5, 2);
        opGrid.add(backButton, 5, 3);



        // Add labels to show daily need per plot
        ArrayList<javafx.scene.control.Label> labels = new ArrayList<>();

        int theRowCounter = 0;
        int rowIndex = 12;
        int theColumnConter = 0;

        for(Map.Entry<String, ArrayList<Double>> entry:optimisedInMap.entrySet()){

            javafx.scene.control.Label plotText = new javafx.scene.control.Label("water needed in " + entry.getKey());
            plotText.setWrapText(true);


            if(theRowCounter > 4){
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

        // label to show water that is left
        DecimalFormat df = new DecimalFormat("#.##");
        Label waterLeftLabel = new Label( " Water left= " + df.format(waterLeft) + " " + this.getWaterUnits());
        waterLeftLabel.setWrapText(true);
        opGrid.add(waterLeftLabel, 1, 18);




        // Slider that user moves to show different days
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





        // set scene
        this.getS().setTitle("Optimisation Result");
        this.getS().setScene(opScene);



    }

    public Optimiser getOp() {
        return op;
    }

    public void setOp(Optimiser o ){
        this.op=o;
    }

    public void setCopy(Optimiser a){
        this.copy=a;
    }

    public Stage getS() {
        return s;
    }

    public Optimiser getCopy() {
        return copy;
    }

    public Stage getSecondStage() {
        return secondStage;
    }

    public User getUser() { return user;}

    public boolean getResultsSaved() {
        return this.resultsSaved;
    }

    public String getWaterUnits(){
        return waterUnits;
    }

    public void setResultsSaved(Boolean saved){
        this.resultsSaved=saved;
    }


    public void launchOptimizer() {



        setUp();

        this.getS().show();



       

    }


    public void update(Observable obj, Object arg){

        Garden newGarden = (Garden)obj;

        Optimiser newOp = new Optimiser(newGarden, this.getOp().getDays(), this.getOp().getWaterAvailable(), this.getOp().getDateSelected(), this.getOp().getWithWeather());
        this.setOp(newOp);

        setUp();

        this.getS().show();


    }

    public void exportToFile(Map<String, ArrayList<Double>> desiredMap) {

        Stage anotherStage = new Stage();

        try {

//            FileChooser fc = new FileChooser();
//            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files(*.txt)", "*.txt");
//            fc.getExtensionFilters().add(extFilter);

            TextInputDialog fileNameInput = new TextInputDialog();
            fileNameInput.setTitle("File Name");
            fileNameInput.setHeaderText("Set File Path and Name");
            fileNameInput.setContentText("Enter file path and name");

            Optional<String> result = fileNameInput.showAndWait();
            String filePath = null;
            if(result.isPresent()){
                filePath = result.get();
            }


            File toSaveFile = new File(filePath);

            if (toSaveFile != null) {

                FileWriter fr = new FileWriter(toSaveFile);

                BufferedWriter out = new BufferedWriter(fr);


                out.write("Optimal water usage for " + this.op.getGarden().getName() +
                " between dates of " + this.op.getDateSelected() + " and "  + this.op.getDateSelected().plusDays(this.op.getDays()) );
                out.newLine();
                out.newLine();
                for(Map.Entry<String, ArrayList<Double>> entry : desiredMap.entrySet()){
                    out.write(entry.getKey());
                    out.newLine();
                    ArrayList<Double> temp = entry.getValue();
                    for(double d : temp){
                        out.write(String.format("|%6.2f", d));
                    }
                    out.newLine();
                }

                for(int i = 0; i < this.getOp().getDays(); i++){

                    LocalDate theDate = this.getOp().getDateSelected().plusDays(i);
                    out.write( theDate.getDayOfMonth()+"/" + theDate.getMonthValue()+ "  ");
                }

                out.newLine();
                out.close();
            }


        } catch (IOException e) {
            Alert failToSave = new Alert(Alert.AlertType.WARNING);
            failToSave.setHeaderText("Failed To Save File");
            failToSave.show();


        }
    }
}




