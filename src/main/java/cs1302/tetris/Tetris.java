package cs1302.tetris;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.GridPane;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import java.util.Random;

/**
 * The logic and structure for a standard game of Tetris. Includes
 * the enumeration for the Tetrominoes and all the rules that the game
 * must abide by.
 */
public class Tetris extends Application {

    /**
     * Enumerates the different piece types for the game.
     */
    public enum Tetrominoe {
        NoShape, S, Z, L, ML, Sqr, Line, T
    } //Tetrominoe enum

    private VBox board;
    private VBox score;
    private HBox container;
    private int width;
    private int height;
    private Rectangle base;
    //private Rectangle two;
    //private Rectangle three;
    //private Rectangle four;
    private GridPane gp;
    private Timeline tl;
    private int numLines = 0;
    private boolean playing = true;
    private Tetrominoe piece;
    private Tetrominoe[] shapes;
    private Random rand = new Random();

    /**
     * The start method for the application.
     */
    public void start(Stage stage) {
        board = new VBox();
        score = new VBox();
        container = new HBox();
        width = 10;
        height = 20;
        base = null;
        //two = null;
        //three = null;
        //four = null;
        gp = new GridPane();
        //tl = new Timeline();
        shapes = Tetrominoe.values();
        randomShape(shapes);
        defaultStart();

        while (playing) {
            playing = false;
        }
        board.getChildren().add(gp);
        container.getChildren().addAll(score, board);

        Scene scene = new Scene(container);
        stage.sizeToScene();
        stage.setMaxWidth(500);
        stage.setMaxHeight(480);
        stage.setTitle("Tetris");
        stage.setScene(scene);
        stage.show();

    } //start

    /**
     * Creates the shape for the current piece out of four
     * new rectangle objects.
     */
    public void makeShape() {
        int style = piece.ordinal();
        if (style == 1) { //S-shape block
            for (int i = 6; i < 8; i++) {
                base = new Rectangle(1, 1, Color.LIGHTGREEN);
                addShape(0, i, base);
            }
            for (int i = 5; i < 7; i++) {
                base = new Rectangle(1, 1, Color.LIGHTGREEN);
                addShape(1, i, base);
            }
        } else if (style == 2) { //Z-shape block
            for (int i = 5; i < 7; i++) {
                base = new Rectangle(1, 1, Color.RED);
                addShape(0, i, base);
            }
            for (int i = 6; i < 8; i++) {
                base = new Rectangle(1, 1, Color.RED);
                addShape(1, i, base);
            }
        } else if (style == 3) {
            for (int i = 5; i < 8; i++) {
                base = new Rectangle(1, 1, Color.ORANGE);
                addShape(0, i, base);
            }
            for (int i = 5; i < 6; i++) {
                base = new Rectangle(1, 1, Color.ORANGE);
                addShape(1, i, base);
            }
        }

    } //makeShape

    /**
     * Adds created shape to the grid.
     * @param row the row of insertion
     * @param col the column of insertion
     * @param add the rectangle to add to the grid
     */
    public void addShape(int row, int col, Rectangle add) {

    } //addShape

    /**
     * Tests to see if the piece can continue moving down/other
     * directions.
     */
    public boolean testMove() {
        return true;

    } //testMove

    /**
     * Moves the piece to the left.
     */
    public void updateLeft() {

    } //updateLeft

    /**
     * Moves the piece to the right.
     */
    public void updateRight() {

    } //updateRight

    /**
     * Rotates left.
     */
    public void rotateLeft() {

    } //rotateLeft

    /**
     * Rotates right.
     */
    public void rotateRight() {

    } //rotateRight

    /**
     * Moves the piece down one row of the pane; designed
     * to be looped indefinitely as a part of the animation.
     */
    public void moveDown() {

    } //moveDown

    /**
     * Scans the board from the bottom up to see how many rows
     * are full of rectangles.
     */
    public void checkBoard() {

    } //checkBoard

    /**
     * Gets a random shape from a passed array and assigns it to piece.
     * @param options the list of shapes to choose from
     */
    public void randomShape(Tetrominoe[] options) {
        int num = Math.abs(rand.nextInt()) % 7 + 1;
        piece = options[num];
    } //randomShape

    /**
     * The default setup for the beginning of the game.
     */
    public void defaultStart() {
        for (int i = 0; i < height; i++) {
            for (int j = 1; j < width; j++) {
                gp.add(new Rectangle(1, 1, Color.WHITE), j, i);
            }
        }
    } //defaultStart
} //Tetris
