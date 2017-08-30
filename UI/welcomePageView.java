package UI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Page to display UI an elements for welcome page
 */

public class welcomePageView {

    Stage primaryStage;

    public welcomePageView(Stage primaryStage){
        this.primaryStage=primaryStage;
    }

    public Stage getPrimaryStage(){
        return this.primaryStage;
    }

    public void setUp(){

        //welcome Page
        GridPane welcomePage = new GridPane();
        Scene welcomePageScene = new Scene(welcomePage, 300 , 200);
        //primaryStage.setResizable(false);

        welcomePage.setStyle("-fx-background-image: url(" +
                "/res/cougette.jpg" + ")");


        primaryStage.setTitle("Welcome");

        // Welcome Page set up
        welcomePage.setAlignment(Pos.CENTER);
        welcomePage.setVgap(10);
        welcomePage.setHgap(10);
        welcomePage.setPadding(new javafx.geometry.Insets(2, 2, 2, 2));


        javafx.scene.control.Label welcome = new javafx.scene.control.Label();
        welcome.setText("Welcome ");
        welcome.setWrapText(true);
        welcome.setTextFill(Color.WHITE);
        welcome.setFont(Font.font("Verdanna", FontWeight.BOLD, 20));

        javafx.scene.control.Button login = new javafx.scene.control.Button("Login");

        javafx.scene.control.Button newUser = new javafx.scene.control.Button("Create New User");
        newUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                NewUserEntryView next = new NewUserEntryView(primaryStage);
                next.launch();
            }
        });
        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                LoginView newLoginView = new LoginView(primaryStage);
                newLoginView.launchLogin();
            }
        });

        login.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        newUser.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(60);
        ColumnConstraints outCol = new ColumnConstraints();
        outCol.setPercentWidth(20);
        welcomePage.getColumnConstraints().addAll(outCol, col, outCol);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(20);
        welcomePage.getRowConstraints().addAll(row, row, row, row, row);



//        BackgroundImage welcomePageImage = new BackgroundImage(new Image("/res/cougette.jpg", 300, 300, true, true),
//                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
//
//
//        Background welcomeBackground = new Background(welcomePageImage);



        // welcomePage.setBackground(new Background(welcomePageImage));
        welcomePage.add(welcome, 1,1, 2,1 );
        welcomePage.add(login,  1,2, 1, 1);
        welcomePage.add(newUser, 1,3, 1, 1);










        this.getPrimaryStage().setScene(welcomePageScene);

    }



    public void launch(){
        setUp();

        this.getPrimaryStage().show();
    }
}
