package cs1302.tetris;

import cs1302.tetris.Shape.Tetrominoe;
import javafx.animation.Timeline;
import java.awt.Graphics;
import java.awt.Color;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The grid that the game is played on, as in,
 * the main window the player has as to what is going
 * on.
 */
public class Board {// extends Application{

    private final int WIDTH = 10;
    private final int HEIGHT = 20;

    private boolean fallen;
    private Timeline anime;
    private VBox board;
    private VBox status;
    private HBox container;
    private Tetrominoe[] shapes;
    private int x;
    private int y;
    private Shape dropPiece;
    private Shape nextPiece;
    private Scene scene;
/*
    /**
     * The start method for the application, displays the board
     * and stuff.
     * @param stage the stage to add stuff to and display

    public void start(Stage stage) {
        fallen = false;
        //anime = new Timeline();
        board = new VBox();
        status = new VBox();
        container = new HBox();
        x = 0;
        y = 0;
        shapes = new Tetrominoe[WIDTH * HEIGHT];
        dropPiece = new Shape();
        scene = new Scene();

        clear();

        container.getChildren().addAll(status, board);
        scene.getChildren().add(container);

        stage.setTitle("Tetris");
        stage.setScene(scene);
        stage.show();

    } //start
*/
    public Board() {
    }
    /**
     * Creates a new piece and begins dropping it.
     */
    public void create() {
        dropPiece.randomShape();
        x = this.WIDTH / 2 + 1;
        y = this.HEIGHT - 1;

    } //create

    /**
     * Moves the piece down a line.
     */
    public void down() {
        for (int i = 0; i < 4; i++) {
            //int x =
        }
    } //down

    /**
     * Attempts to move the piece.
     */
    public boolean tryMove(Shape piece, int testX, int testY) {
        for (int i = 0; i < 4; i++) {
            int x = testX + piece.getX(i);
            int y = testY + piece.getY(i);

            if (x < 0 || x >= this.WIDTH || y < 0 || y >= this.HEIGHT) {
                return false;
            }

            if (shape(x, y) != Tetrominoe.None) {
                return false;
            }
        }

        dropPiece = piece;
        this.x = x;
        this.y = y;

        return true;

    } //tryMove

    /**
     * Draws the shapes present in the board.
     *
     */
    public void draw(Graphics grphs, int x, int y, Tetrominoe style) {
        int shape = style.ordinal();
        Color c;
        Color[] options = {
            new Color(0, 0, 0), new Color(144, 238, 144), new Color(100,0,0),
            new Color(0,75,100), new Color(58, 44, 86), new Color(100,100,0),
            new Color(0,0,55), new Color(100,75,0)
        };
        c = options[shape];

        grphs.setColor(c);
        grphs.fillRect(x + 1, y + 1, 1, 1);

    } //draw

    /**
     * Returns the shape currently located at the index on the board.
     * @param x the x location of the piece
     * @param y the y location of the piece
     * @return the joint location in the array given the params
     */
    public Tetrominoe shape(int x, int y) {
        return shapes[y * WIDTH + x];
    } //shape

    /**
     * Clears the board by filling all values with empty shapes.
     */
    public void clear() {
        for (int i = 0; i < this.HEIGHT * this.WIDTH; i++) {
            shapes[i] = Tetrominoe.None;
        }
    } //clear




} //Board
