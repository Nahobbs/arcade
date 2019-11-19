package cs1302.mancala;

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

        //player 1's pits
        Circle pit1 = new Circle(20, 20, 50);
        Circle pit2 = new Circle(20, 20, 50);
        Circle pit3 = new Circle(20, 20, 50);
        Circle pit4 = new Circle(20, 20, 50);
        Circle pit5 = new Circle(20, 20, 50);
        Circle pit6 = new Circle(20, 20, 50);
        //player 2's pits
        Circle pit7 = new Circle(20, 20, 50);
        Circle pit8 = new Circle(20, 20, 50);
        Circle pit9 = new Circle(20, 20, 50);
        Circle pit10 = new Circle(20, 20, 50);
        Circle pit11 = new Circle(20, 20, 50);
        Circle pit12 = new Circle(20, 20, 50);

        //both players stores
        Rectangle p1Store = new Rectangle(150.0f, 75.0f, 80.0f, 100.0f);
        Rectangle p2Store = new Rectangle(150.0f, 75.0f, 80.0f, 100.0f);

} //mancala
