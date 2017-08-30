package UI;

import Optimizer.Garden;
import Optimizer.Plot;
import Optimizer.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

/**
 * Class to create UI and action to allow user to view details of a particular plot
 * Author Stephen Jackson
 */

public class PlotView {

    private Plot plot;
    private Stage stage;
    private User user;
    private Garden garden;

    public PlotView(Plot plot, Stage stage, User user, Garden garden) {
        this.plot = plot;
        this.stage = stage;
        this.user = user;
        this.garden = garden;
    }

    //Getters and Setters
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

    //Method to set up UI and actions
    public void setUp() {

        // set up layout to UI
        this.getStage().setTitle(this.getPlot().getName());

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
        javafx.scene.control.Label plotNameText = new javafx.scene.control.Label(this.getPlot().getName());
        plotNameText.setWrapText(true);
        javafx.scene.control.Label plantTypeLabel = new javafx.scene.control.Label("Planted with:");
        javafx.scene.control.Label plantTypeText = new javafx.scene.control.Label(this.getPlot().getPlant().getName());
        plantTypeText.setWrapText(true);
        plantTypeText.setMinWidth(Region.USE_PREF_SIZE);
        javafx.scene.control.Label plantedLabel = new javafx.scene.control.Label("Planted On: ");
        javafx.scene.control.Label plantedLabelText = new javafx.scene.control.Label(this.getPlot().getDatePlanted().toString());
        javafx.scene.control.Label sizeOfPlotLabel = new javafx.scene.control.Label("The size of plot ");

        javafx.scene.control.Label sizeofPlotText = new javafx.scene.control.Label(Double.toString(this.getPlot().getSize()) + "sqm");


        String priority;
        double pValue = this.getPlot().getPriority();
        if(pValue==3){
            priority="High";

        } else if(pValue==2){
            priority ="Medium";
        } else {
            priority="Low";
        }

        javafx.scene.control.Label priorityLabel = new javafx.scene.control.Label("Priority of Plot");
        javafx.scene.control.Label priorityText = new javafx.scene.control.Label(priority);




        //button to go back
        javafx.scene.control.Button back = new javafx.scene.control.Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                GardenAndPlotView back = new GardenAndPlotView(PlotView.this.getStage(), PlotView.this.getUser(), PlotView.this.getGarden());
                back.launch();

            }
        });



        javafx.scene.control.Button editPlot = new javafx.scene.control.Button("Edit Plot ");



        if(this.getGarden().getUserEditRights()==false){
            editPlot.setVisible(false);
        }

        editPlot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                EditPlotView next = new EditPlotView(PlotView.this.getPlot(), PlotView.this.getStage(), PlotView.this.getUser(),
                        PlotView.this.getGarden());
                next.launchEditPlotView();


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



        this.getStage().setScene(plotViewScene);


    }


    // Method to launch UI
    public void launch(){
        setUp();

        this.getStage().show();
    }
}
