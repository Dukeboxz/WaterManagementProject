package UI;

import Optimizer.Database;
import Optimizer.Garden;
import Optimizer.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LinkedUserView {

    private Stage stage;
    private User use;
    private Garden g;

    public LinkedUserView(Stage s, User u, Garden g){
        this.stage=s;
        this.use= u;
        this.g=g;
    }

    public Stage getStage() {
        return stage;
    }

    public User getUse() {
        return use;
    }

    public Garden getG() {
        return g;
    }

    public void setUp(){

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
                GardenAndPlotView next = new GardenAndPlotView(LinkedUserView.this.getStage(), LinkedUserView.this.getUse(), g);
                next.launch();

            }
        });



        linkUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String the = userNameInput.getText();
                System.out.println("*" + the + "*");

                    if (Database.userNameExists(the)) {
                        int linkedUserid = Database.getUserIDBasedOnName(the);
                        if(viewOnly.isSelected()) {
                            Database.insertNewUserGarden(g.getGardenID(), linkedUserid, false);
                        } else {
                            Database.insertNewUserGarden(g.getGardenID(), linkedUserid, true);
                        }


                        GardenAndPlotView next = new GardenAndPlotView(LinkedUserView.this.getStage(), LinkedUserView.this.getUse(), g);
                        next.launch();
                    } else {
                        Alert noName = new Alert(Alert.AlertType.ERROR);
                        noName.setContentText("User Name does not exist");
                        noName.show();
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



        this.getStage().setScene(linkedUserScene);


    }

    public void launch(){
        setUp();

        this.stage.show();
    }
}
