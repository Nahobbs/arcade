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
import javafx.collections.ObservableList;
import javafx.scene.Node;

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
    private Rectangle white;
    //private Rectangle two;
    //private Rectangle three;
    //private Rectangle four;
    private GridPane gp;
    private Timeline tl;
    private int numLines = 0;
    private boolean playing = true;
    private Tetrominoe piece;
    private Tetrominoe[] shapes;
    private Color[] colors = {
        Color.LIGHTGREEN, Color.RED, Color.ORANGE, Color.NAVY, Color.YELLOW,
        Color.LIGHTCYAN, Color.MEDIUMPURPLE
    };
    private Random rand = new Random();
    private int s = 30;

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
        white = new Rectangle(s, s, Color.WHITE);
        white.setStroke(Color.BLACK);
        //two = null;
        //three = null;
        //four = null;
        gp = new GridPane();
        gp.setGridLinesVisible(true);
        //tl = new Timeline();
        shapes = Tetrominoe.values();
        randomShape(shapes);
        defaultStart();

        while (playing) {
            playing = false;
        }
        turn();
        board.getChildren().add(gp);
        container.getChildren().addAll(score, board);

        Scene scene = new Scene(container);
        stage.sizeToScene();
        stage.setMaxWidth(550);
        stage.setMaxHeight(550);
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
                base = new Rectangle(s, s, Color.LIGHTGREEN);
                base.setStroke(Color.BLACK);
                addShape(0, i, base);
            }
            for (int i = 5; i < 7; i++) {
                base = new Rectangle(s, s, Color.LIGHTGREEN);
                base.setStroke(Color.BLACK);
                addShape(1, i, base);
            }
        } else if (style == 2) { //Z-shape block
            for (int i = 5; i < 7; i++) {
                base = new Rectangle(s, s, Color.RED);
                base.setStroke(Color.BLACK);
                addShape(0, i, base);
            }
            for (int i = 6; i < 8; i++) {
                base = new Rectangle(s, s, Color.RED);
                base.setStroke(Color.BLACK);
                addShape(1, i, base);
            }
        } else if (style == 3) {
            for (int i = 5; i < 8; i++) {
                base = new Rectangle(s, s, Color.ORANGE);
                base.setStroke(Color.BLACK);
                addShape(0, i, base);
            }
            for (int i = 5; i < 6; i++) {
                base = new Rectangle(s, s, Color.ORANGE);
                base.setStroke(Color.BLACK);
                addShape(1, i, base);
            }
        } else if (style == 4) {
            for (int i = 5; i < 8; i++) {
                base = new Rectangle(s, s, Color.NAVY);
                base.setStroke(Color.BLACK);
                addShape(0, i, base);
            }
            for (int i = 7; i < 8; i++) {
                base = new Rectangle(s, s, Color.NAVY);
                base.setStroke(Color.BLACK);
                addShape(1, i, base);
            }
        } else if (style == 5) {
            for (int i = 6; i < 8; i++) {
                base = new Rectangle(s, s, Color.YELLOW);
                base.setStroke(Color.BLACK);
                addShape(0, i, base);
            }
            for (int i = 6; i < 8; i++) {
                base = new Rectangle(s, s, Color.YELLOW);
                base.setStroke(Color.BLACK);
                addShape(1, i, base);
            }
        } else if (style == 6) {
            for (int i = 5; i < 9; i++) {
                base = new Rectangle(s, s, Color.LIGHTCYAN);
                base.setStroke(Color.BLACK);
                addShape(0, i, base);
            }
        } else if (style == 7) {
            for (int i = 5; i < 8; i++) {
                base = new Rectangle(s, s, Color.MEDIUMPURPLE);
                base.setStroke(Color.BLACK);
                addShape(0, i, base);
            }
            for (int i = 6; i < 7; i++) {
                base = new Rectangle(s, s, Color.MEDIUMPURPLE);
                base.setStroke(Color.BLACK);
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
        if (testMove(row, col)) {
            gp.add(add, col, row);
        }
    } //addShape

    /**
     * Tests to see if the piece can continue moving down/other
     * directions.
     */
    public boolean testMove(int row, int col) {
        Rectangle spot = getRect(row, col);
        if (spot.getFill() == Color.WHITE && row < 20 && row >= 0 && col < 10 && col >= 0) {
            return true;
        } else {
            return false;
        }

    } //testMove

    /**
     * Moves the piece to the left.
     */
    public void updateLeft() {
        for (int i = 0; i < 4; i++) {
            int row = findR(i);
            int col = findC(i);
            if (testMove(row, col - 1)) {
                addShape(row, col - 1, getRect(row, col));
                addShape(row, col, white);
            }
        }

    } //updateLeft

    /**
     * Moves the piece to the right.
     */
    public void updateRight() {
        for (int i = 0; i < 4; i++) {
            int row = findR(i);
            int col = findC(i);
            if (testMove(row, col + 1)) {
                addShape(row, col + 1, getRect(row, col));
                addShape(row, col, white);
            }
        }

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
     * Finds the current piece on the board and returns it's location.
     * @param n the number of the rectangle in the piece to find and move
     * @return the rectangle at the specified location
     */
    public int findR(int n) {
        Color find = colors[piece.ordinal()];
        int count = 0;
        int result = 0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                if (getRect(i, j).getFill() == find) {
                    count++;
                    if (count == n) {
                        result = i;
                        count++;
                    }
                }
            }
        }
        return result;

    } //findR

    /**
     * Finds the current piece on the board and returns it's location.
     * @param n the number of the rectangle in the piece to find and move
     * @return the rectangle at the specified location
     */
    public int findC(int n) {
        Color find = colors[piece.ordinal()];
        int count = 0;
        int result = 0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                if (getRect(i, j).getFill() == find) {
                    count++;
                    if (count == n) {
                        result = j;
                        count++;
                    }
                }
            }
        }
        return result;

    } //findC

    /**
     * Scans the board from the bottom up to see how many rows
     * are full of rectangles.
     */
    public void checkBoard() {
        for (int i = 20; i >= 0; i--) {
            int count = 0;
            for (int j = 0; j < 10; j++) {
                Rectangle test = getRect(i, j);
                if (test.getFill() != Color.WHITE) {
                    count++;
                }
            }
            if (count == 10) {
                clearRow(i);
                shiftDown(i);
            }
        }

    } //checkBoard

    /**
     * Clears a row from the board and increases the number of lines removed
     * counter.
     * @param row the row to clear
     */
    public void clearRow(int row) {
        for (int i = 0; i < 10; i++) {
            Rectangle fill = new Rectangle(s, s, Color.WHITE);
            fill.setStroke(Color.BLACK);
            addShape(row, i, fill);
        }
    } //clearRow

    /**
     * Shifts all the squares above a row that has been cleared down one.
     * @param row the row to start from
     */
    public void shiftDown(int row) {
        for (int i = row; i >= 0; i--) {
            for (int j = 0; j < 10; j++) {
                if (getRect(i - 1, j).getFill() != Color.WHITE) {
                    addShape(i, j, getRect(i - 1, j));
                }
            }
        }
    } //shiftDown

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
                Rectangle fill = new Rectangle(s, s, Color.WHITE);
                fill.setStroke(Color.BLACK);
                gp.add(fill, j, i);
            }
        }
    } //defaultStart

    /**
     * Gets a child from the GridPane to compare to other elements.
     * @param row the row to examine
     * @param col the column to examine
     * @return the rectangle at the given point
     */
    public Rectangle getRect (int row, int column) {
        return (Rectangle) gp.getChildren().get(row * 20 + column);
    } //getRect

    /**
     * A turn in the game.
     */
    public void turn() {
        randomShape(shapes);
        makeShape();
    } //turn

} //Tetris
