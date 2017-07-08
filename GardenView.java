/**
 * Created by stephen on 29/06/17.
 */

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GardenView extends Application {



    public static void main(String[] args) {
       // launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Garden");
        Button btn = new Button();

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

       Label dayLabel = new Label();
       dayLabel.setText(Double.toString(1));
       Label dayTitle = new Label("Number of days");

        Label waterLabel = new Label();
        waterLabel.setText(Double.toString(waterSlider.getValue()));
        Label waterTitle = new Label("Amount of water");

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


        btn.setText("Optimise");

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Hey Up World ");
                System.out.println(waterLabel);

            }
        });


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(btn, 0, 1);
        grid.getColumnConstraints().add(new ColumnConstraints(400));
        grid.add(waterTitle, 0, 2);
        grid.add(waterSlider, 0, 3);
        grid.add(waterLabel, 1, 3);
        grid.add(dayTitle, 0, 4);
        grid.add(daySlider, 0, 5);
        grid.add(dayLabel, 1, 5);
        primaryStage.setScene(new Scene(grid, 500, 300));
        primaryStage.show();
    }

}
