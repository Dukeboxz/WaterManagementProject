package UI;

import Optimizer.Database;
import Optimizer.User;
import Optimizer.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.sql.SQLException;

public class NewUserEntryView {

    private Stage stage;

    public NewUserEntryView(Stage s){
        this.stage=s;
    }

    public Stage getStage(){
        return this.stage;
    }

    public void setUp(){

        GridPane grid = new GridPane();
        Scene newUserScene = new Scene(grid, 400 , 500);

        grid.setStyle("-fx-background-image: url(" + "/res/greenHouse.jpg" + ")");
        this.getStage().setTitle("New User Entry");
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



        javafx.scene.control.TextField user = new javafx.scene.control.TextField(" Name");
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
                        userNameAlert.setContentText(" Name not long enough");
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


                            GardenSelectionView next = new GardenSelectionView(NewUserEntryView.this.getStage(), temp);
                            next.launch();


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
                View.welcomePage(stage);

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



        this.getStage().setScene(newUserScene);



    }

    public void launch(){
        setUp();

        this.getStage().show();
    }
}
