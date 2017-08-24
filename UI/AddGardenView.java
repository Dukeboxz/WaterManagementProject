package UI;

import Optimizer.Database;
import Optimizer.Garden;
import Optimizer.User;
import Optimizer.WeatherData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class AddGardenView {

    private User user;
    private Stage stage;

    public AddGardenView(User u, Stage s){

        this.user=u;
        this.stage=s;

    }

    public User getUser() {
        return user;
    }

    public Stage getStage() {
        return stage;
    }

    public void setUp(){

        this.getStage().setTitle("Add New Garden");
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

                    GardenAndPlotView next = new GardenAndPlotView(AddGardenView.this.getStage(), AddGardenView.this.getUser(), createdGarden);
                    next.launch();



                }
            }
        });

        javafx.scene.control.Button backButon = new javafx.scene.control.Button("Back");
        backButon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                    GardenSelectionView next = new GardenSelectionView(AddGardenView.this.getStage(), AddGardenView.this.getUser());
                    next.launch();

            }
        });

        addGardenPane.setAlignment(Pos.CENTER);
        addGardenPane.add(createGardenLabel, 1, 1);
        addGardenPane.add(gardenNameText, 3, 1);

        addGardenPane.add(locationLabel, 1, 2   );
        addGardenPane.add(potentialLoctions, 3, 2);


        addGardenPane.add(createNewGardenButton, 3, 3);
        addGardenPane.add(backButon, 1, 3);


        this.getStage().setScene(addGardenScene);



    }

    public void launch(){
        setUp();

        this.stage.show();
    }
}
