package UI;

import Optimizer.Garden;
import Optimizer.Optimiser;
import Optimizer.Plot;
import Optimizer.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;



/**
 * Class that allow users to set up garden for optimization calulation
 *
 */

public class GardenAndPlotView {

    private Stage stage;
    private User user;
    private Garden g;

    public GardenAndPlotView(Stage stage, User user, Garden g){
        this.stage=stage;
        this.user=user;
        this.g=g;

    }

    // getters and setters
    public Stage getStage() {
        return stage;
    }

    public User getUser() {
        return user;
    }

    public Garden getG() {
        return g;
    }

    /**
     * Method to set up UI and Actions
     */
    public void setUp(){

        // set up layout of UI
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



        // create element that adds a list of plots of the garden on to the page
        VBox thePlots = new VBox();

        thePlots.setSpacing(5);

        ScrollPane scroller = new ScrollPane();
        scroller.setContent(thePlots);
        scroller.setFitToHeight(false);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroller.setId("plotScroler");
        gardenPlotPane.add(scroller, 0, 2, 1, 3);



        if(!this.getG().getPlots().isEmpty()) {
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
                                PlotView newPlotView = new PlotView(p, GardenAndPlotView.this.getStage(),
                                        GardenAndPlotView.this.getUser(), GardenAndPlotView.this.getG());
                                newPlotView.launch();
                                // plotView(p, stage, user, g);
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
        ComboBox waterMeasurement = new ComboBox();
        waterMeasurement.getItems().add("In^3");
        waterMeasurement.getItems().add("Litres");
        waterMeasurement.getItems().add("Gallons");
        waterMeasurement.getSelectionModel().select(0);

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



//        Tooltip mathmaticalTip = new Tooltip();
//        mathmaticalTip.setText("Selecting this option will run a mathmatical algorithm through matlab.  To use it you must have matlab installed on your system");
//        mathmaticalTip.wrapTextProperty().setValue(true);
//
//        Tooltip simpleTip = new Tooltip();
//        simpleTip.setText("This uses simple algorithm.  Select this if you do not have access to matlab. ");
//
//
//
//
//        optimise.setMaxWidth(Double.MAX_VALUE);
//        optimise.setMaxHeight(50);
//
//        ToggleGroup algorithms = new ToggleGroup();
//        RadioButton math = new RadioButton("Use mathmatical algorithm");
//        math.wrapTextProperty().setValue(true);
//        math.setTooltip(mathmaticalTip);
//        math.setSelected(true);
//        math.setToggleGroup(algorithms);
//        math.setStyle("-fx-background-color: white");
//
//        RadioButton simple = new RadioButton("Use Simple Algorithm");
//        simple.setTooltip(simpleTip);
//        simple.setToggleGroup(algorithms);
//        simple.setWrapText(true);
//        simple.setStyle("-fx-background-color: white");


        /**
         * On optimization action the method creates an Optimiser Object that creates an Optimization scene
         */
        optimise.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                boolean weatherActive;
                if(includeWeatherToggle.isSelected()){
                    weatherActive = true;
                } else {
                    weatherActive= false;
                }
                int days = Integer.parseInt(dayLabel.getText());
                if (g.getPlots().size() > 0) {

//                    boolean mathmaticalValue = true;
//                    if(math.isSelected()){
//                        mathmaticalValue=true;
//                    } else {
//                        mathmaticalValue=false;
//                    }

                    if (days > 1) {

                        if (!waterText.getText().equals(null) & waterText.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {

                            double water = Double.parseDouble(waterText.getText());
                            String measurement = waterMeasurement.getValue().toString().trim();
                            System.out.println("Measurement " + "*"+measurement+"*");
                            if(measurement.equals("Litres")){
                                water =  water*61.0237;
                            } else if(measurement.equals("Gallons")){
                                water = water*277.419;
                            }else {
                                water = water;
                            }
                            System.out.println("Water Avaialable " + water);

                            if (days > 1 & water > 1) {
                                //double water = Double.parseDouble(waterLabel.getText());

                                LocalDate theDate = optimiseStartDate.getValue();
                                System.out.println("The Weather is Active = " + weatherActive);

                                Optimiser opObject = new Optimiser(g, days, water, theDate, weatherActive);
                                //optimisationScene(opObject, stage);
                                OptimizationSceneView opPage = new OptimizationSceneView(opObject, GardenAndPlotView.this.getStage(), GardenAndPlotView.this.getUser(), measurement);
                                opObject.getGarden().addObserver(opPage);
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

        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GardenSelectionView back = new GardenSelectionView(GardenAndPlotView.this.getStage(), GardenAndPlotView.this.getUser());
                back.launch();
            }
        });


        //gardenPlotPane.add(waterSlider, 2, 2, 2, 1);
        gardenPlotPane.setGridLinesVisible(false);

        gardenPlotPane.add(waterText, 1,2);
        gardenPlotPane.add(waterTitle, 1, 1);
        gardenPlotPane.add(waterMeasurement, 2, 2);

        gardenPlotPane.add(daySlider, 1, 3, 2, 1);

        //gardenPlotPane.add(waterLabel, 3, 2);
        gardenPlotPane.add(dayLabel, 1, 4);
        gardenPlotPane.add(dayTitle, 1, 4);


        gardenPlotPane.setHalignment(dayTitle, HPos.LEFT);
        gardenPlotPane.setHalignment(dayLabel, HPos.RIGHT);



        gardenPlotPane.add(includeWeatherToggle, 1 , 5);
        gardenPlotPane.add(optimiseStartDate, 1, 6);
        gardenPlotPane.add(optimise, 1, 7);

        gardenPlotPane.add(back, 0, 8);






        javafx.scene.control.Button addPlot = new javafx.scene.control.Button("Add Plot");


        if(g.getUserEditRights()==false){
            addPlot.setVisible(false);
        }

        addPlot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                   AddPlotView next = new AddPlotView(GardenAndPlotView.this.getStage(), GardenAndPlotView.this.getUser(), GardenAndPlotView.this.getG());
                   next.launch();


            }
        });
        javafx.scene.control.Button linkUserButton = new javafx.scene.control.Button(" Link Another User to this Garden");
        linkUserButton.setWrapText(true);
        linkUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                LinkedUserView next = new LinkedUserView(GardenAndPlotView.this.getStage(), GardenAndPlotView.this.getUser(), GardenAndPlotView.this.getG());
                next.launch();


            }
        });

        gardenPlotPane.add(addPlot, 0, 5);
        gardenPlotPane.add(linkUserButton, 0,7);



        this.getStage().setTitle("Optimise Water Supply");
        this.getStage().setScene(gardenPlotScene);


    }

    /**
     * Method launches the GardenAndPlot UI element
     */
    public void launch() {

        setUp();

        this.stage.show();

    }
}
