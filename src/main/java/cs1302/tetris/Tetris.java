package cs1302.tetris;

import javafx.application.Platform;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.GridPane;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.paint.Color;
import java.util.Random;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Group;
import javafx.util.Duration;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.scene.paint.Paint;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

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
    private GridPane gp;
    private Timeline tl, inner;
    private Rectangle[] current = new Rectangle[4];
    private Rectangle[][] grid = new Rectangle[20][10];
    private int numLines = 0;
    private boolean playing = true, dropping = true, spawn = false, done = false;
    private Tetrominoe piece;
    private Tetrominoe[] shapes;
    private Color[] colors = {
        Color.WHITE, Color.LIGHTGREEN, Color.RED, Color.ORANGE, Color.NAVY,
        Color.YELLOW, Color.CYAN, Color.MEDIUMPURPLE
    };
    private Random rand = new Random();
    private int s = 30;
    private int[][] coords = new int[4][2];
    private Timer timer;
    private TimerTask drop;
    private boolean turned = false;
    private int assBlast = 0; //variable for dealing with rotating L, J, and T piece

    /**
     * The start method for the application.
     * @param stage the stage to add things to
     */
    public void start(Stage stage) {
        init(stage);
        EventHandler<ActionEvent> game = event -> {
            if (!spawn) {
                randomShape(shapes);
                done = false;
                makeShape();
                assBlast = 0;
                turned = false;
                spawn = true;
            }
            moveDown();
            if (done) {
                checkBoard();
                done = false;
                spawn = false;
            }
        };
        KeyFrame fuck = new KeyFrame(Duration.seconds(1), game);
        tl.getKeyFrames().add(fuck);
        tl.play();

        container.setOnKeyPressed(move());
        container.getChildren().addAll(score, board);
        Scene scene = new Scene(container);
        stage.sizeToScene();
        stage.setMaxWidth(550);
        stage.setMaxHeight(700);
        stage.setTitle("Tetris");
        stage.setScene(scene);
        stage.show();
        container.requestFocus();

    } //start

    /**
     * Creates the KeyEvents that occur when the buttons for the game
     * are pushed.
     * @return the key event handler
     */
    public EventHandler<? super KeyEvent> move() {
        return event -> {
            switch (event.getCode()) {
            case LEFT:
                updateLeft();
                break;
            case RIGHT:
                updateRight();
                break;
            case DOWN:
                moveDown();
                break;
            case R:
                rotate();
                break;
            default:
            }
        };
    } //move

    /**
     * Cleans out the grid.
     */
    public void clean() {
        Rectangle[][] g2 = new Rectangle[20][10];
        grid = g2;
    } //clean

    /**
     * Initializes everything in one method.
     */
    public void init(Stage stage) {
        board = new VBox();
        score = new VBox();
        makeStatus(stage);
        container = new HBox();
        width = 10;
        height = 20;
        base = new Rectangle(s, s);
        white = new Rectangle(s, s, Color.WHITE);
        white.setStroke(Color.BLACK);
        gp = new GridPane();
        board.getChildren().add(gp);
        tl = new Timeline();
        tl.setCycleCount(Animation.INDEFINITE);
        tl.setAutoReverse(true);
        inner = new Timeline();
        inner.setCycleCount(10);
        inner.setAutoReverse(true);
        defaultStart();
        shapes = Tetrominoe.values();
        clean();
    } //init

    /**
     * Makes the status part of the board.
     */
    public void makeStatus(Stage stage) {
        Text tetris = new Text("TETRIS");
        tetris.setUnderline(true);
        Label clears = new Label("Lines Cleared: " + numLines);
        Button quit = new Button("Quit");
        EventHandler<ActionEvent> exit = event -> {
            stage.close();
        };
        quit.setOnAction(exit);
        score.getChildren().addAll(tetris, clears, quit);
    } //makeStatus

    /**
     * Takes in the row, column, and count of makeshape and adds it
     * to the grid.
     * @param row the row of the new shape
     * @param col the column of the new shape
     * @param count the count from makeShape
     */
    public void genShape(int row, int col, int count) {
        int style = piece.ordinal();
        Rectangle r = new Rectangle(s, s, colors[style]);
        r.setStroke(Color.BLACK);
        coords[count][0] = row;
        coords[count][1] = col;
        current[count] = r;
        addShape(row, col, r);
    } //genShape

    /**
     * Creates the shape for the current piece out of four
     * new rectangle objects.
     */
    public void makeShape() {
        int style = piece.ordinal();
        int count = 0;
        if (style == 1) { //S-shape block
            for (int i = 6; i < 8; i++) {
                genShape(0, i, count);
                count++;
            }
            for (int i = 5; i < 7; i++) {
                genShape(1, i, count);
                count++;
            }
        } else if (style == 2) { //Z-shape block
            for (int i = 5; i < 7; i++) {
                genShape(0, i, count);
                count++;
            }
            for (int i = 6; i < 8; i++) {
                genShape(1, i, count);
                count++;
            }
        } else if (style == 3) { //L-shape block
            for (int i = 5; i < 8; i++) {
                genShape(0, i, count);
                count++;
            }
            for (int i = 5; i < 6; i++) {
                genShape(1, i, count);
                count++;
            }
        } else {
            makeShape2(style);
        }
    } //makeShape

    /**
     * The continuation of makeShape, forced into existence by the method
     * length requirement of checkstyle.
     * @param style the integer value to check for if it wasn't found at first
     */
    public void makeShape2(int style) {
        int count = 0;
        if (style == 4) { //Mirror L-shape block
            for (int i = 5; i < 8; i++) {
                genShape(0, i, count);
                count++;
            }
            for (int i = 7; i < 8; i++) {
                genShape(1, i, count);
                count++;
            }
        } else if (style == 5) { //Square shape block
            for (int i = 6; i < 8; i++) {
                genShape(0, i, count);
                count++;
            }
            for (int i = 6; i < 8; i++) {
                genShape(1, i, count);
                count++;
            }
        } else if (style == 6) { //Line block
            for (int i = 5; i < 9; i++) {
                genShape(0, i, count);
                count++;
            }
        } else if (style == 7) { //T-shape block
            for (int i = 5; i < 8; i++) {
                genShape(0, i, count);
                count++;
            }
            for (int i = 6; i < 7; i++) {
                genShape(1, i, count);
                count++;
            }
        }
    } //makeShape2

    /**
     * Adds created shape to the grid.
     * @param row the row of insertion
     * @param col the column of insertion
     * @param add the rectangle to add to the grid
     */
    public void addShape(int row, int col, Rectangle add) {
        gp.add(add, col, row);
    } //addShape

    /**
     * Tests to see if the piece can continue moving down/other
     * directions.
     */
    public boolean testMove(int row, int col) {
        if (grid[row][col] == null && row < 20 && row >= 0 && col < 10 && col >= 0) {
            return true;
        } else {
            return false;
        }

    } //testMove

    /**
     * Moves the piece to the left.
     */
    public void updateLeft() {
        //System.out.println("left");
        for (int i = 0; i < 4; i++) {
            Rectangle r = new Rectangle(s, s, current[i].getFill());
            r.setStroke(Color.BLACK);
            Rectangle w = new Rectangle(s, s, white.getFill());
            w.setStroke(Color.BLACK);
            int row = coords[i][0];
            int col = coords[i][1];
            if (testMove(row, col - 1)) {
                //remove(row, col - 1);
                addShape(row, col - 1, r);
                //remove(row, col);
                addShape(row, col, w);
                coords[i][1] = col - 1;
            }
        }

    } //updateLeft

    /**
     * Moves the piece to the right.
     */
    public void updateRight() {
        for (int i = 3; i >= 0; i--) {
            Rectangle r = new Rectangle(s, s, current[i].getFill());
            r.setStroke(Color.BLACK);
            Rectangle w = new Rectangle(s, s, white.getFill());
            w.setStroke(Color.BLACK);
            int row = coords[i][0];
            int col = coords[i][1];
            if (testMove(row, col + 1)) {
                //remove(row, col + 1);
                addShape(row, col + 1, r);
                //remove(row, col);
                addShape(row, col, w);
                coords[i][1] = col + 1;
            }
        }

    } //updateRight

    /**
     * Initiates gameover.
     */
    public void gameOver() {
        playing = false;
    }

    /**
     * Rotates the piece.
     */
    public void rotate() {
        int style = piece.ordinal();
        switch (style) {
        case 1:
            rotateS();
            break;
        case 2:
            rotateZ();
            break;
        case 3:
            rotateL();
            break;
        case 4:
            rotateJ();
            break;
        case 5:
            break;
        case 6:
            rotateLine();
            break;
        case 7:
            rotateT();
            break;
        default:
            break;
        }
    } //rotateLeft

    /**
     * Makes a new Rectangle for the rotations.
     * @param fill the fill for the new rectangle
     */
    public Rectangle eRect(Paint fill) {
        Rectangle r = new Rectangle(s, s, fill);
        r.setStroke(Color.BLACK);
        return r;
    } //eRect


    /**
     * Rotates an S-Piece.
     */
    public void rotateS() {
        for (int i = 0; i < 4; i++) {
            if (i != 3) {
                int row = coords[i][0];
                int col = coords[i][1];
                addShape(row, col, eRect(white.getFill()));
            }
        }
        if (!turned) {
            int row = coords[3][0];
            int col = coords[3][1];
            addShape(row - 1, col, eRect(current[3].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row, col + 1, eRect(current[3].getFill()));
            coords[1][0] = row;
            coords[1][1] = col + 1;
            addShape(row + 1, col + 1, eRect(current[3].getFill()));
            coords[2][0] = row + 1;
            coords[2][1] = col + 1;
            turned = false;
        } else {
            int row = coords[3][0];
            int col = coords[3][1];
            addShape(row, col - 1, eRect(current[3].getFill()));
            coords[0][0] = row;
            coords[0][1] = col - 1;
            addShape(row - 1, col, eRect(current[3].getFill()));
            coords[1][0] = row - 1;
            coords[1][1] = col;
            addShape(row - 1, col + 1, eRect(current[3].getFill()));
            coords[2][0] = row - 1;
            coords[2][1] = col + 1;
            turned = true;
        }
    } //rotateS

    /**
     * Rotates an S-Piece.
     */
    public void rotateZ() {
        for (int i = 0; i < 4; i++) {
            if (i != 2) {
                int row = coords[i][0];
                int col = coords[i][1];
                addShape(row, col, eRect(white.getFill()));
            }
        }
        if (!turned) {
            int row = coords[2][0];
            int col = coords[2][1];
            addShape(row - 1, col + 1, eRect(current[2].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col + 1;
            addShape(row, col + 1, eRect(current[2].getFill()));
            coords[1][0] = row;
            coords[1][1] = col + 1;
            addShape(row + 1, col, eRect(current[2].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col;
            turned = false;
        } else {
            int row = coords[3][0];
            int col = coords[3][1];
            addShape(row - 1, col - 1, eRect(current[2].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col - 1;
            addShape(row - 1, col, eRect(current[2].getFill()));
            coords[1][0] = row - 1;
            coords[1][1] = col;
            addShape(row, col + 1, eRect(current[2].getFill()));
            coords[3][0] = row;
            coords[3][1] = col + 1;
            turned = true;
        }
    } //rotateZ

    /**
     * Rotates a line.
     */
    public void rotateLine() {
        for (int i = 0; i < 4; i++) {
            if (i != 1) {
                int row = coords[i][0];
                int col = coords[i][1];
                addShape(row, col, eRect(white.getFill()));
            }
        }
        if (!turned) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row - 1, col + 1, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[2][0] = row + 1;
            coords[2][1] = col;
            addShape(row + 2, col, eRect(current[1].getFill()));
            coords[3][0] = row + 2;
            coords[3][1] = col;
            turned = false;
        } else {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[0][0] = row;
            coords[0][1] = col - 1;
            addShape(row, col + 1, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col + 1;
            addShape(row, col + 2, eRect(current[1].getFill()));
            coords[3][0] = row;
            coords[3][1] = col + 2;
            turned = true;
        }
    } //rotateLine

    /**
     * Rotates the T-piece.
     */
    public void rotateT() {
        for (int i = 0; i < 4; i++) {
            if (i != 1) {
                int row = coords[i][0];
                int col = coords[i][1];
                addShape(row, col, eRect(white.getFill()));
            }
        }
        if (assBlast == 0) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col - 1;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col;
            assBlast++;
        } else if (assBlast == 1) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col - 1;
            addShape(row, col + 1, eRect(current[1].getFill()));
            coords[3][0] = row;
            coords[3][1] = col + 1;
            assBlast++;
        } else {
            rotateT2();
        }
    } //rotateT

    /**
     * Rotates the T-piece.
     */
    public void rotateT2() {
        if (assBlast == 2) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row, col + 1, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col + 1;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col;
            assBlast++;
        } else if (assBlast == 3) {
            int row = coords[3][0];
            int col = coords[3][1];
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[0][0] = row;
            coords[0][1] = col - 1;
            addShape(row, col + 1, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col + 1;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col;
            assBlast = 0;
        }
    } //rotateT2


    /**
     * Rotates the L-piece.
     */
    public void rotateL() {
        for (int i = 0; i < 4; i++) {
            if (i != 1) {
                int row = coords[i][0];
                int col = coords[i][1];
                addShape(row, col, eRect(white.getFill()));
            }
        }
        if (assBlast == 0) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[2][0] = row + 1;
            coords[2][1] = col;
            addShape(row + 1, col + 1, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col + 1;
            assBlast++;
        } else if (assBlast == 1) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[0][0] = row;
            coords[0][1] = col - 1;
            addShape(row, col + 1, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col + 1;
            addShape(row - 1, col + 1, eRect(current[1].getFill()));
            coords[3][0] = row - 1;
            coords[3][1] = col + 1;
            assBlast++;
        } else {
            rotateL2();
        }
    } //rotateL

    /**
     * Rotates the L-piece.
     */
    public void rotateL2() {
        if (assBlast == 2) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[0][0] = row + 1;
            coords[0][1] = col;
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[2][0] = row - 1;
            coords[2][1] = col;
            addShape(row - 1, col - 1, eRect(current[1].getFill()));
            coords[3][0] = row - 1;
            coords[3][1] = col - 1;
            assBlast++;
        } else if (assBlast == 3) {
            int row = coords[3][0];
            int col = coords[3][1];
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[0][0] = row;
            coords[0][1] = col - 1;
            addShape(row, col + 1, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col + 1;
            addShape(row + 1, col - 1, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col - 1;
            assBlast = 0;
        }
    } //rotateL2

    /**
     * Rotates the J-piece.
     */
    public void rotateJ() {
        for (int i = 0; i < 4; i++) {
            if (i != 1) {
                int row = coords[i][0];
                int col = coords[i][1];
                addShape(row, col, eRect(white.getFill()));
            }
        }
        if (assBlast == 0) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[2][0] = row + 1;
            coords[2][1] = col;
            addShape(row - 1, col + 1, eRect(current[1].getFill()));
            coords[3][0] = row - 1;
            coords[3][1] = col + 1;
            assBlast++;
        } else if (assBlast == 1) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[0][0] = row;
            coords[0][1] = col - 1;
            addShape(row, col + 1, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col + 1;
            addShape(row - 1, col - 1, eRect(current[1].getFill()));
            coords[3][0] = row - 1;
            coords[3][1] = col - 1;
            assBlast++;
        } else {
            rotateJ2();
        }
    } //rotateJ

    /**
     * Rotates the J-piece.
     */
    public void rotateJ2() {
        if (assBlast == 2) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[0][0] = row + 1;
            coords[0][1] = col;
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[2][0] = row - 1;
            coords[2][1] = col;
            addShape(row + 1, col - 1, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col - 1;
            assBlast++;
        } else if (assBlast == 3) {
            int row = coords[3][0];
            int col = coords[3][1];
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[0][0] = row;
            coords[0][1] = col - 1;
            addShape(row, col + 1, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col + 1;
            addShape(row + 1, col + 1, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col + 1;
            assBlast = 0;
        }
    } //rotateJ2

    /**
     * Moves the piece down one row of the pane; designed
     * to be looped indefinitely as a part of the animation.
     */
    public void moveDown() {
        for (int i = 3; i >= 0; i--) {
            Rectangle r = new Rectangle(s, s, current[i].getFill());
            r.setStroke(Color.BLACK);
            Rectangle w = new Rectangle(s, s, white.getFill());
            w.setStroke(Color.BLACK);
            int row = coords[i][0];
            int col = coords[i][1];
            if (!done && row + 1 < 20 && testMove(row + 1, col)) {
                addShape(row + 1, col, r);
                addShape(row, col, w);
                coords[i][0] = row + 1;
            } else {
                done = true;
                append();
            }

        }

    } //moveDown

    /**
     * Appends the shape to the grid given the situation.
     */
    public void append() {
        for (int i = 3; i >= 0; i--) {
            int row = coords[i][0];
            int col = coords[i][1];
            grid[row][col] = eRect(current[i].getFill());
        }
    } //append

    /**
     * Scans the board from the bottom up to see how many rows
     * are full of rectangles.
     */
    public void checkBoard() {
        for (int i = 19; i >= 0; i--) {
            int count = 0;
            for (int j = 0; j < 10; j++) {
                Rectangle test = grid[i][j];
                if (test != null) {
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
            addShape(row, i, eRect(white.getFill()));
            grid[row][i] = null;
            numLines++;
        }
    } //clearRow

    /**
     * Shifts all the squares above a row that has been cleared down one.
     * @param row the row to start from
     */
    public void shiftDown(int row) {
        for (int i = row; i >= 0; i--) {
            for (int j = 0; j < 10; j++) {
                if (grid[i - 1][j] != null) {
                    addShape(i, j, grid[i - 1][j]);
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
            for (int j = 0; j < width; j++) {
                Rectangle fill = new Rectangle(s, s, Color.WHITE);
                fill.setStroke(Color.BLACK);
                gp.add(fill, j, i);
            }
        }
    } //defaultStart

} //Tetris
