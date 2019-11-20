package cs1302.mancala;

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

    //the pits of the game
    private int p1 = 4;
    private int p2 = 4;
    private int p3 = 4;
    private int p4 = 4;
    private int p5 = 4;
    private int p6 = 4;
    private int p7 = 4;
    private int p8 = 4;
    private int p9 = 4;
    private int p10 = 4;
    private int p11 = 4;
    private int p12 = 4;
    //player stores for their collected pieces(points)
    private int store1 = 0;
    private int store2 = 0;

    @Override
    public void start(Stage stage) {
        GridPane board = new GridPane();
        board.setPadding(new Insets(10, 10, 10, 10));
        board.setHgap(6);
        board.setVgap(6);

        //player 1's pits
        Circle pit1 = new Circle(20, 20, 70);
        Circle pit2 = new Circle(20, 20, 70);
        Circle pit3 = new Circle(20, 20, 70);
        Circle pit4 = new Circle(20, 20, 70);
        Circle pit5 = new Circle(20, 20,70);
        Circle pit6 = new Circle(20, 20, 70);
        //player 2's pits
        Circle pit7 = new Circle(20, 20, 70);
        Circle pit8 = new Circle(20, 20, 70);
        Circle pit9 = new Circle(20, 20, 70);
        Circle pit10 = new Circle(20, 20, 70);
        Circle pit11 = new Circle(20, 20, 70);
        Circle pit12 = new Circle(20, 20, 70);

        //both players stores
        Rectangle p1Store = new Rectangle(110.0, 300.0);
        p1Store.setArcWidth(110.0);
        p1Store.setArcHeight(110.0);
        Rectangle p2Store = new Rectangle(110.0, 300.0);
        p2Store.setArcWidth(110.0);
        p2Store.setArcHeight(110.0);

        p1Store.setId("rect");
        p2Store.setId("rect");
        pit1.setId("circ");
        pit2.setId("circ");
        pit3.setId("circ");
        pit4.setId("circ");
        pit5.setId("circ");
        pit6.setId("circ");
        pit7.setId("circ");
        pit8.setId("circ");
        pit9.setId("circ");
        pit10.setId("circ");
        pit11.setId("circ");
        pit12.setId("circ");

        //adds rudimentary objects to the gridpane
//        board.add(p1Store, 0, 0);
//        board.add(p2Store, 0, 7);

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


        HBox Hbox = new HBox();

        Hbox.getChildren().addAll(p1Store, board, p2Store);
        Hbox.setAlignment(Pos.CENTER);

        //StackPane root = new StackPane();
        Hbox.setId("pane");
        Scene scene = new Scene(Hbox);
        scene.getStylesheets().addAll("file:src/main/java/cs1302/mancala/style.css");

		stage.setTitle("Mancala");
		stage.setScene(scene);
		//stage.sizeToScene();
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

} //Mancala
