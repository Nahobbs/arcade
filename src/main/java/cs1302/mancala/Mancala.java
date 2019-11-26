package cs1302.mancala;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;

public class Mancala extends Application {

    //girdpane for pits
    private static GridPane board = new GridPane();

    //initial ints that represent the amount of stones in a pit or store
    //the pits of the game
    private static int p1 = 4;
    private static int p2 = 4;
    private static int p3 = 4;
    private static int p4 = 4;
    private static int p5 = 4;
    private static int p6 = 4;
    private static int p7 = 4;
    private static int p8 = 4;
    private static int p9 = 4;
    private static int p10 = 4;
    private static int p11 = 4;
    private static int p12 = 4;
    //ints for the player stores for their collected pieces(points)
    private static int store1 = 0;
    private static int store2 = 0;

    //both players stores shapes
    private static Rectangle p1Store = new Rectangle(110.0, 300.0);
    private static StackPane s1 = new StackPane();
    private static Rectangle p2Store = new Rectangle(110.0, 300.0);
    private static StackPane s2 = new StackPane();

    //player 1 pit buttons
    private static Button pit1 = new Button("" + p1);
    private static Button pit2 = new Button("" + p2);
    private static Button pit3 = new Button("" + p3);
    private static Button pit4 = new Button("" + p4);
    private static Button pit5 = new Button("" + p5);
    private static Button pit6 = new Button("" + p6);
    //player 2 pit buttons
    private static Button pit7 = new Button("" + p7);
    private static Button pit8 = new Button("" + p8);
    private static Button pit9 = new Button("" + p9);
    private static Button pit10 = new Button("" + p10);
    private static Button pit11 = new Button("" + p11);
    private static Button pit12 = new Button("" + p12);


    @Override
    public void start(Stage stage) {
        board.setPadding(new Insets(10, 10, 10, 10));
        board.setHgap(6);
        board.setVgap(6);

        setIds();
        addShapes();

        HBox Hbox = new HBox();

        //overlays the number of points on the player stores
        setPoints(store1, s1, p1Store);
        setPoints(store2, s2, p2Store);
        Hbox.getChildren().addAll(s1, board, s2);
        Hbox.setAlignment(Pos.CENTER);

        //StackPane root = new StackPane();
        Hbox.setId("pane");
        Scene scene = new Scene(Hbox);
        scene.getStylesheets().addAll("file:src/main/java/cs1302/mancala/style.css");

		stage.setTitle("Mancala");
		stage.setScene(scene);
        stage.setWidth(1150);
		stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
    } //Mancala

    public static void main(String[] args) {
        try {
            Application.launch(Mancala.class, args);
        } catch (UnsupportedOperationException e) {
            System.out.println(e);
            System.err.println("If this is a DISPLAY problem, then your X server connection");
            System.err.println("has likely timed out. This can generally be fixed by logging");
            System.err.println("out and logging back in.");
            System.exit(1);
        } // try
    } // main

    public static void setIds() {
        p1Store.setId("rect");
        p2Store.setId("rect");
    } //setIds

    public static void addShapes() {
        //adds p1 pits
        board.add(pit1, 0, 0);
        board.add(pit2, 1, 0);
        board.add(pit3, 2, 0);
        board.add(pit4, 3, 0);
        board.add(pit5, 4, 0);
        board.add(pit6, 5, 0);
        //adds p2 pits
        board.add(pit7, 0, 1);
        board.add(pit8, 1, 1);
        board.add(pit9, 2, 1);
        board.add(pit10, 3, 1);
        board.add(pit11, 4, 1);
        board.add(pit12, 5, 1);
    } //addShapes

    /**
     * overlays player points on their store.
     */
    public static void setPoints(int points, StackPane sp, Rectangle rect) {
        Text score = new Text("" + points);
        score.setId("text");
        sp.getChildren().addAll(rect, score);
    } //setPoints

} //Mancala
