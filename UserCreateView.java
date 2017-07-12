/**
 * Created by stephen on 29/06/17.
 */

import com.sun.xml.internal.bind.v2.TODO;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.*;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.awt.Color.red;

public class UserCreateView extends Application {

    String userName, email, password;
    Stage theStage;
    Scene welcomePage, userEntry, loginEntry;
    public static User theUser = new User();
    Garden theGarden;
    private static  Label gardenLabel;


    public static void setGardenScene(Stage stage, User user) throws SQLException{

        GridPane gardenGrid = new GridPane();
        Scene garden = new Scene(gardenGrid ,500, 500);

        List<Garden> gardenList = Database.getUsersGardens(user.getId());




            // Garden Selection Page
        Label gardenLabel = new Label();
        String  a = " the name is " + user.getName();
        gardenLabel.setText(a);
        Label selectGarden = new Label("Existing Garden");
        Button load = new Button("Load");
        Button createNewGarden = new Button("Create new Garden");
        createNewGarden.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addGarden(user, stage);
            }
        });


        ComboBox usersGardens = new ComboBox();
        int we = user.getId();



            for(Garden g : gardenList){
                usersGardens.getItems().add(g.getName());
            }


        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(usersGardens.getValue().equals(null)){
                    Alert noGarden = new Alert(Alert.AlertType.ERROR);
                    noGarden.setContentText("Please select a garden");
                } else {
                    String chosenGarden = usersGardens.getValue().toString();
                    for(Garden h : gardenList){
                        if(h.getName().equals(chosenGarden)){
                            setGardenAndPlotScene(stage, user, h);
                        }
                    }

                }



            }
        });
        gardenGrid.setAlignment(Pos.CENTER);
        gardenGrid.add(gardenLabel, 0, 1);
        gardenGrid.add(createNewGarden, 0, 2);
        gardenGrid.add(usersGardens, 0, 4);
        gardenGrid.add(selectGarden, 0, 3);
        gardenGrid.add(load, 1, 4);

        stage.setScene(garden);
    }

    public static void addGarden(User user, Stage s){

        s.setTitle("Add New Garden");
        GridPane addGardenPane = new GridPane();
        Scene addGardenScene = new Scene(addGardenPane, 500 , 500);

        Label createGardenLabel = new Label("Garden Name");
        TextField gardenNameText = new TextField();
        Button createNewGardenButton = new Button("Create Garden");
        createNewGardenButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(gardenNameText.getText().length()< 1){

                    Alert noText = new Alert(Alert.AlertType.ERROR);
                    noText.setContentText("PLEASE ENTER NAME");
                    noText.show();

                    gardenNameText.setStyle("-fx-border-color: red; -fx-border-width: 2px");

                }
                else{
                    String gardenName = gardenNameText.getText().trim();
                    System.out.println("*"+ gardenName+"*");

                    Database.createNewGarden(gardenName, user);

                    Garden createdGarden = Database.userGardenReturn(gardenName, user);




                        setGardenAndPlotScene(s,user,createdGarden );


                }
            }
        });

        addGardenPane.setAlignment(Pos.CENTER);
        addGardenPane.add(createGardenLabel, 1, 1);
        addGardenPane.add(gardenNameText, 2, 1);
        addGardenPane.add(createNewGardenButton, 1, 2);

        s.setScene(addGardenScene);


    }

    public static void setGardenAndPlotScene(Stage stage, User user, Garden g ){

        GridPane gardenPlotPane = new GridPane();
        Scene gardenPlotScene = new Scene(gardenPlotPane, 500, 500);
        ArrayList<Plot> gPlots = g.getPlots();
        int counter = 1;

        for(int i = 0; i < g.getPlots().size(); i++){
            Button plotButton = new Button(g.getPlots().get(i).getName());
            gardenPlotPane.add(plotButton, 1, i+1);
            counter++;

            plotButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    for(Plot p : gPlots){
                        if(plotButton.getText().equals(p.getName())){
                            plotView(p, stage, user, g );
                        }
                    }

                }
            });
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
        TextField waterText = new TextField();
        Button optimise = new Button("OPTIMISE");
        LocalDate dateSelected = LocalDate.now();
        optimise.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int days = Integer.parseInt(dayLabel.getText());
                //double water = Double.parseDouble(waterLabel.getText());
                double water = Double.parseDouble(waterText.getText());
                Optimiser opObject = new Optimiser(g, days, water, dateSelected );
                optimisationScene(opObject, stage);

            }
        });


        //gardenPlotPane.add(waterSlider, 2, 2, 2, 1);

        gardenPlotPane.add(daySlider, 2, 4);
        gardenPlotPane.add(waterText, 2,2);
        gardenPlotPane.add(waterTitle, 3, 2);
        //gardenPlotPane.add(waterLabel, 3, 2);
        gardenPlotPane.add(dayLabel, 3, 4);
        gardenPlotPane.add(optimise, 2, 6);



        Button addPlot = new Button("Add Plot");
        addPlot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    addPlot(stage, g, user);
                } catch(SQLException e) {

                }
            }
        });
        gardenPlotPane.add(addPlot, 1, counter);



        stage.setTitle("Garden ");
        stage.setScene(gardenPlotScene);


    }

    // method to create scene that shows information on that plot
    public static  void plotView(Plot pl, Stage s, User u, Garden p){

        s.setTitle(pl.getName());

        GridPane plotViewPane = new GridPane();
        Scene plotViewScene = new Scene(plotViewPane, 500, 600);
        Label plotNameLabel = new Label("Plot Name:");
        Label plotNameText = new Label(pl.getName());
        Label plantTypeLabel = new Label("Planted with:");
        Label plantTypeText = new Label(pl.getPlant().getName());
        Label plantedLabel = new Label("Planted On: ");
        Label plantedLabelText = new Label(pl.getDatePlanted().toString());

        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setGardenAndPlotScene(s, u , p);
            }
        });

        plotViewPane.add(plotNameLabel, 1, 1);
        plotViewPane.add(plotNameText, 2, 1);
        plotViewPane.add(plantTypeLabel, 1, 2);
        plotViewPane.add(plantTypeText, 2, 2);
        plotViewPane.add(plantedLabel, 1, 3);
        plotViewPane.add(plantedLabelText, 2, 3);

        plotViewPane.add(back, 5, 5);


        s.setScene(plotViewScene);

    }

    public static void optimisationScene(Optimiser o, Stage s){

        Stage secondStage = new Stage();

        FlowPane opGrid = new FlowPane();


        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days");
        yAxis.setLabel("Water mm^3");

        LineChart<Number, Number> waterChart = new LineChart<Number, Number>(xAxis, yAxis);
        waterChart.setTitle("Water Usage");

        XYChart.Series optimalUse = new XYChart.Series();
        optimalUse.setName("Optimal");
        double[] opP = o.getOptimisationPoints();
        for(int i = 0 ; i < opP.length; i++){
            optimalUse.getData().add(new XYChart.Data<>(i, opP[i]));


        }

        double[] arg = o.decisionByDay(o.optimize());
        XYChart.Series decionUse = new XYChart.Series();
        decionUse.setName("Water to use");
        for(int i = 0; i < arg.length; i++){
            decionUse.getData().add(new XYChart.Data<>(i , arg[i]));
        }

        waterChart.getData().add(optimalUse);
        waterChart.getData().add(decionUse);

        Scene opScene = new Scene(waterChart, 1000, 600);



        secondStage.setTitle("Optimisation Result");
        secondStage.setScene(opScene);
       // s.setTitle("Optimisation Result");
       // s.setScene(opScene);
        secondStage.show();

    }

    public static void addPlot(Stage s, Garden g, User u) throws SQLException{

        GridPane addPlotPane = new GridPane();
        Scene addPlotScene = new Scene(addPlotPane, 500, 600);
        s.setTitle("Add Plot");

        Label name = new Label("Name");
        TextField nameField = new TextField();

        Label howBigPlot = new Label("How big is plot?");
        TextField sizeValue = new TextField();
        RadioButton sqm = new RadioButton("Sqm");
        RadioButton sqft = new RadioButton("Sqft");


        Label environmentLabel = new Label("Environment");
        ComboBox<String> environmentSelect = new ComboBox<>();

        environmentSelect.getItems().add(0, "Normal");
        environmentSelect.getItems().add(1, "Raised beds");
        environmentSelect.getItems().add(2, "Polytunnel");
        environmentSelect.getSelectionModel().selectFirst();


        Label soilSelectTitle = new Label("SoilType");
        HashMap obs = Database.getSoilVariables();
        ComboBox<String> soilSelect = new ComboBox<>();

        obs.forEach((k,v )-> soilSelect.getItems().add(k.toString()) );
        soilSelect.getSelectionModel().select(3);

        Label plotPrioirityTitle = new Label("Importance of plot crop");
        ComboBox<String> plotPriorty = new ComboBox<>();
        plotPriorty.getItems().add(0, "High");
        plotPriorty.getItems().add(1, "Normal");
        plotPriorty.getItems().add(2, "Low");
        plotPriorty.getSelectionModel().select(1);


        Label plantLabel = new Label("What are you planting?");
        ComboBox plantType = new ComboBox();
        ComboBox plant = new ComboBox();

        plantType.getItems().add(0, "1 Vegetable");
        plantType.getItems().add(1, "2 Fruit");
        plantType.getItems().add(2, "3 Herbs");

        plantType.setOnAction((event) -> {

            int type = Integer.parseInt(plantType.getValue().toString().substring(0,1));

            HashMap<Integer, String> plantNames = Database.returnPlantDetails(type);

            plant.getItems().clear();
            for(Map.Entry<Integer, String> e : plantNames.entrySet()){
                plant.getItems().add(e.getValue());
            }



        });

        DatePicker plantedDate = new DatePicker(LocalDate.now());

        Button addPlotButton = new Button("Add Plot ");



        addPlotPane.add(name, 5, 1);
        addPlotPane.add(nameField, 6, 1);
        addPlotPane.add(environmentLabel, 5, 2);
        addPlotPane.add(environmentSelect, 6, 2, 2, 1);
        addPlotPane.add(soilSelectTitle, 5, 3);
        addPlotPane.add(soilSelect, 6, 3);

        addPlotPane.add(plotPrioirityTitle, 5, 4);
        addPlotPane.add(plotPriorty, 6, 4);

        addPlotPane.add(plantLabel, 5, 5);
        addPlotPane.add(plantType, 6, 5);
        addPlotPane.add(plant, 7, 5);

        addPlotPane.add(plantedDate, 6, 7);

        addPlotPane.add( howBigPlot, 4, 8);
        addPlotPane.add(sizeValue, 5, 8);
        addPlotPane.add(sqft, 6 , 8);
        addPlotPane.add(sqm, 7, 8);



        s.setScene(addPlotScene);
    }

    public static void login(Stage s){

        GridPane loginPageGrid = new GridPane();
        Scene loginEntry = new Scene(loginPageGrid, 500, 300);

        // login Entry set up

        Label loginLabel = new Label("LOGIN");
        TextField loginEntryField = new TextField("userName");
        Button loginButton = new Button("Login");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String theUserName = loginEntryField.getText();
                try{
                    if(  true) {
                        System.out.println();
                        User temp = Database.createUser(theUserName);

                        setGardenScene(s, temp);


                    } else {
                        Alert userAlreadyExistsAlert = new Alert(Alert.AlertType.ERROR);
                        userAlreadyExistsAlert.setContentText("Not Valid User");
                        userAlreadyExistsAlert.show();
                    }
                }
                catch(SQLException e) {
                    System.out.println("SQl ERROR LOGIN");
                    e.printStackTrace();
                }
            }
        });


        loginPageGrid.setAlignment(Pos.CENTER);
        loginPageGrid.setPadding(new Insets(200, 50, 200 ,50 ));
        loginPageGrid.add(loginLabel,1,0);
        loginPageGrid.add(loginEntryField, 1,1);
        loginPageGrid.add(loginButton, 1, 2);

        s.setTitle("LOGIN");
        s.setScene(loginEntry);
    }


    public static  void main(String[] args) {
        launch(args);
    }




    @Override
    public void start(Stage primaryStage) {


        theStage = primaryStage;



        //welcome Page
        GridPane welcomePage = new GridPane();
        Scene welcomePageScene = new Scene(welcomePage, 500 , 500);

        // user Entry
        GridPane grid = new GridPane();
        Scene userEntry = new Scene(grid, 500, 300 );





        // Welcome Page set up
        welcomePage.setAlignment(Pos.CENTER);
        welcomePage.setPadding(new Insets(50, 50, 50, 50));
        Label welcome = new Label();
        welcome.setText("Welcome to the Garden App");
        Button login = new Button("Login");

        Button newUser = new Button("Create New User");
        newUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                theStage.setScene(userEntry);
            }
        });
        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                login(primaryStage);
            }
        });
        welcomePage.add(welcome, 1,0);
        welcomePage.add(login,  1,1);
        welcomePage.add(newUser, 1,2);







        // new user entry scene
        TextField user = new TextField("User Name");
        TextField emailentry = new TextField("email");
        TextField pass = new TextField("Password");
        Button submit = new Button("Submit");

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                userName = user.getText();
                email = emailentry.getText();
                password = pass.getText();

                if(userName.length() < 6){
                    Alert userNameAlert = new Alert(Alert.AlertType.ERROR);
                    userNameAlert.setContentText("User Name not long enough");
                    userNameAlert.showAndWait();

                } else if(password.length() < 6) {
                    Alert passwordAlter = new Alert(Alert.AlertType.ERROR);
                    passwordAlter.setContentText("Password is not long enough");
                    passwordAlter.showAndWait();

                } else{
                    try{
                    Database.insertUserNameANDPassword(userName, email, password);
                    Alert inputSuccess = new Alert((Alert.AlertType.CONFIRMATION));
                    inputSuccess.setContentText("Data Entered Successfully");

                        User temp = Database.createUser(userName);
                        theStage.setTitle("Garden");
                        setGardenScene(theStage, temp );


                   //theStage.setScene(garden);


                }catch (SQLException e){
                        Alert entryFailureAlert = new Alert(Alert.AlertType.WARNING);
                        entryFailureAlert.show();

                    }
                }
            }
        });

        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(50, 50, 50, 50));
        grid.add(user,0,0);
        grid.add(emailentry, 0, 1);
        grid.add(pass, 0, 2);
        grid.add(submit, 0, 3);







        theStage.setTitle("New User Entry");
        theStage.setScene(welcomePageScene);
        theStage.show();
    }
}
