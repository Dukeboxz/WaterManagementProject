package UI;

import Optimizer.Database;
import Optimizer.User;
import Optimizer.View;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LoginView  extends Application{

    Stage s;


    public LoginView(Stage s) {


        this.s = s;

    }

    public Stage getS(){
        return this.s;
    }

    /**
     * Method to set up elements of scene
     */
    public void setUp(){

        GridPane loginPageGrid = new GridPane();
        Scene loginEntry = new Scene(loginPageGrid, 500, 300);
        loginPageGrid.setStyle("-fx-background-image: url(" + "/res/wildFlowers.jpg"+")");



        // login Entry set up

        javafx.scene.control.Label loginLabel = new javafx.scene.control.Label("LOGIN");
        loginLabel.setStyle("-fx-font-size: 100%");
        loginLabel.setTextFill(Color.WHITE);


        javafx.scene.control.TextField loginEntryField = new javafx.scene.control.TextField("userName");
        javafx.scene.control.Label password = new javafx.scene.control.Label("Password");
        password.setTextFill(Color.WHITE);
        password.setStyle("-fx-font-size: 120%");
        PasswordField thePasswordField = new PasswordField();
        thePasswordField.setPromptText("Password");
        thePasswordField.setOnKeyPressed( e -> {
            if(e.getCode()== KeyCode.ENTER){
                String theUserName = loginEntryField.getText();
                String thePassword =  thePasswordField.getText();
                if( Database.userNameAndPasswordCheck(theUserName, thePassword)) {


                    try {
                        User temp = Database.createUser(theUserName);
                        GardenSelectionView next = new GardenSelectionView(s, temp );
                        next.launch();




                    } catch (SQLException f){

                    }


                } else {


                    Alert userAlreadyExistsAlert = new Alert(Alert.AlertType.ERROR);
                    userAlreadyExistsAlert.setContentText("Not Valid Optimizer.User");
                    userAlreadyExistsAlert.show();



                }
            }
        });

        javafx.scene.control.Button loginButton = new javafx.scene.control.Button("Login");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String theUserName = loginEntryField.getText();
                String thePassword =  thePasswordField.getText();
                try{
                    if( Database.userNameAndPasswordCheck(theUserName, thePassword)) {


                        User temp = Database.createUser(theUserName);


                        GardenSelectionView next = new GardenSelectionView(s, temp);
                        next.launch();





                    } else {


                        Alert userAlreadyExistsAlert = new Alert(Alert.AlertType.ERROR);
                        userAlreadyExistsAlert.setContentText("Not Valid Optimizer.User");
                        userAlreadyExistsAlert.show();



                    }
                }
                catch(SQLException e) {
                    System.out.println("SQl ERROR LOGIN");
                    e.printStackTrace();
                }
            }
        });

        javafx.scene.control.Button backButton = new javafx.scene.control.Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                welcomePageView back = new welcomePageView(LoginView.this.getS());
                back.launch();
            }
        });

        loginPageGrid.setGridLinesVisible(false);
        loginPageGrid.setPadding(new javafx.geometry.Insets(2, 2,2, 2));
        loginPageGrid.setAlignment(Pos.CENTER);
        loginPageGrid.setHgap(5);
        loginPageGrid.setVgap(5);
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(60);
        ColumnConstraints outCol = new ColumnConstraints();
        outCol.setPercentWidth(20);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(20);
        loginPageGrid.getColumnConstraints().addAll(outCol, col, outCol);
        loginPageGrid.getRowConstraints().addAll(row, row, row, row, row);

        loginPageGrid.setPadding(new javafx.geometry.Insets(5  , 5, 5 ,5 ));
        loginPageGrid.add(loginLabel,1,0);
        loginPageGrid.add(loginEntryField, 1,1);
        loginPageGrid.add(password, 1, 2);
        loginPageGrid.add(thePasswordField, 1, 3);
        loginPageGrid.add(loginButton, 1, 4);
        loginButton.setAlignment(Pos.CENTER_LEFT);



        loginPageGrid.add(backButton, 1, 4);

        GridPane.setHalignment(backButton, HPos.RIGHT);

        backButton.setMaxSize(140, Double.MAX_VALUE);
        loginButton.setMaxSize(140, Double.MAX_VALUE);
        this.s.setTitle("LOGIN");
        this.s.setScene(loginEntry);

    }

    public void launchLogin(){


        setUp();
        this.s.show();

    }

    public void start(Stage primaryStage){

        LoginView test = new LoginView(primaryStage);
        test.launchLogin();

    }

    public static void main(String[] args){


        launch(args);
    }
    }






