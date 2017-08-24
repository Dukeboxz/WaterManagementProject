package UI;

import Optimizer.Database;
import Optimizer.Garden;
import Optimizer.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GardenSelectionView {

    private Stage s;
    private User u;

    public GardenSelectionView(Stage stage, User user){
        this.s=stage;
        this.u=user;
    }

    public Stage getS() {
        return s;
    }

    public User getU() {
        return u;
    }

    public void setUp() {

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


        java.util.List<Garden> gardenList = Database.getUsersGardens(this.getU().getId());

        this.getS().setTitle("Garden Selection ");


        // Garden Selection Page
        javafx.scene.control.Label gardenLabel = new javafx.scene.control.Label();
        String  a = " Welcome " + this.getU().getName() + "  please select your garden  or create a new one ";
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
                AddGardenView next = new AddGardenView(GardenSelectionView.this.getU(), GardenSelectionView.this.getS());
                next.launch();
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
                                GardenAndPlotView next = new GardenAndPlotView(GardenSelectionView.this.getS(), GardenSelectionView.this.getU(), h);
                                next.launch();
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

                                GardenAndPlotView next = new GardenAndPlotView(GardenSelectionView.this.getS(), GardenSelectionView.this.getU(), h);
                                next.launch();

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

        this.getS().setScene(garden);
    }



    public void launch(){

        setUp();

        this.getS().show();

    }
}
