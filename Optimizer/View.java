package Optimizer;

import UI.EditPlotView;
import UI.LoginView;
import UI.OptimizationSceneView;
import UI.welcomePageView;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

import static java.awt.Color.getColor;


/**
 * Created by stephen on 29/06/17.
 * main method of application
 * Launches a JavaFX application
 */
public class View  extends Application{

    String userName, email, password;
    Stage theStage;
    Scene welcomePage, userEntry, loginEntry;
    public static User theUser = new User();
    Garden theGarden;
    private static javafx.scene.control.Label gardenLabel;




    /**
     * start method that opens up applications welcome page
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        System.setProperty("javafx.userAgentStylesheetUrl1", "caspian");


        welcomePageView  next = new welcomePageView(primaryStage);
        next.launch();






    }


    // main method with launch method to start javafx
    public static  void main(String[] args)

    {
       // System.setProperty("java.library.path","/home/stephen/IdeaProjects/MSCproject/out/artifacts/lib/libmwmclmcrrt.so.9.2" );
        System.out.println(System.getProperty("java.library.path"));
        launch(args);
    }


}
