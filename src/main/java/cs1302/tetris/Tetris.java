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

    /**
     * The start method for the application.
     * @param stage the stage to add things to
     */
    public void start(Stage stage) {
        init(stage);
        EventHandler<ActionEvent> game = event -> {
            if (!spawn) {
                randomShape(shapes);
                makeShape();
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
        //gp.setGridLinesVisible(true);
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
     * Creates the shape for the current piece out of four
     * new rectangle objects.
     */
    public void makeShape() {
        int style = piece.ordinal();
        base.setFill(colors[style]);
        Rectangle r;
        int count = 0;
        if (style == 1) { //S-shape block
            for (int i = 6; i < 8; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 0;
                coords[count][1] = i;
                current[count] = r;
                addShape(0, i, r);
                count++;
            }
            for (int i = 5; i < 7; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 1;
                coords[count][1] = i;
                current[count] = r;
                addShape(1, i, r);
                count++;
            }
        } else if (style == 2) { //Z-shape block
            for (int i = 5; i < 7; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 0;
                coords[count][1] = i;
                current[count] = r;
                addShape(0, i, r);
                count++;
            }
            for (int i = 6; i < 8; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 1;
                coords[count][1] = i;
                current[count] = r;
                addShape(1, i, r);
                count++;
            }
        } else if (style == 3) { //L-shape block
            for (int i = 5; i < 8; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 0;
                coords[count][1] = i;
                current[count] = r;
                addShape(0, i, r);
                count++;
            }
            for (int i = 5; i < 6; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 1;
                coords[count][1] = i;
                current[count] = r;
                addShape(1, i, r);
                count++;
            }
        } else if (style == 4) { //Mirror L-shape block
            for (int i = 5; i < 8; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 0;
                coords[count][1] = i;
                current[count] = r;
                addShape(0, i, r);
                count++;
            }
            for (int i = 7; i < 8; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 1;
                coords[count][1] = i;
                current[count] = r;
                addShape(1, i, r);
                count++;
            }
        } else if (style == 5) { //Square shape block
            for (int i = 6; i < 8; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 0;
                coords[count][1] = i;
                current[count] = r;
                addShape(0, i, r);
                count++;
            }
            for (int i = 6; i < 8; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 1;
                coords[count][1] = i;
                current[count] = r;
                addShape(1, i, r);
                count++;
            }
        } else if (style == 6) { //Line block
            for (int i = 5; i < 9; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 0;
                coords[count][1] = i;
                current[count] = r;
                addShape(0, i, r);
                count++;
            }
        } else if (style == 7) { //T-shape block
            for (int i = 5; i < 8; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 0;
                coords[count][1] = i;
                current[count] = r;
                addShape(0, i, r);
                count++;
            }
            for (int i = 6; i < 7; i++) {
                r = new Rectangle(s, s, colors[style]);
                r.setStroke(Color.BLACK);
                coords[count][0] = 1;
                coords[count][1] = i;
                current[count] = r;
                addShape(1, i, r);
                count++;
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
     * Rotates left.
     */
    public void rotateLeft() {
        int style = piece.ordinal();
        switch (style) {
        case 1:
        }

    } //rotateLeft

    /**
     * Sub-rotation for the S-piece.
     */
    public void switches(int i, int row, int col, Rectangle r) {
    } //switches


    /**
     * Rotates an S-Piece.
     */
    public void rotateS() {
        if (turned) {
            for (int i = 0; i < 4; i++) {
                Rectangle r = new Rectangle(s, s, current[i].getFill());
                r.setStroke(Color.BLACK);
                Rectangle w = new Rectangle(s, s, white.getFill());
                w.setStroke(Color.BLACK);
                int row = coords[i][0];
                int col = coords[i][1];
                switch (i) {
                case 0:
                    addShape(row - 1, col, r);
                    addShape(row, col, w);
                    coords[i][0] = row - 1;
                    break;
                case 1:
                    addShape(row - 2, col + 1, r);
                    addShape(row, col, w);
                    coords[i][0] = row - 2;
                    coords[i][1] = col + 1;
                    break;
                case 2:
                    addShape(row + 1, col, r);
                    addShape(row, col, w);
                    coords[i][0] = row + 1;
                    break;
                case 3:
                    addShape(row, col + 1, r);
                    addShape(row, col, w);
                    coords[i][1] = col + 1;
                    break;
                default:
                    break;
                }
                turned = false;
            }
        } else {
            for (int i = 0; i < 4; i++) {
                Rectangle r = new Rectangle(s, s, current[i].getFill());
                r.setStroke(Color.BLACK);
                Rectangle w = new Rectangle(s, s, white.getFill());
                w.setStroke(Color.BLACK);
                int row = coords[i][0];
                int col = coords[i][1];
                switch (i) {
                case 0:
                    addShape(row + 1, col, r);
                    addShape(row, col, w);
                    coords[i][0] = row + 1;
                    break;
                case 1:
                    addShape(row + 2, col - 1, r);
                    addShape(row, col, w);
                    coords[i][0] = row + 2;
                    coords[i][1] = col - 1;
                    break;
                case 2:
                    addShape(row - 1, col, r);
                    addShape(row, col, w);
                    coords[i][0] = row - 1;
                    break;
                case 3:
                    addShape(row, col - 1, r);
                    addShape(row, col, w);
                    coords[i][1] = col - 1;
                    break;
                default:
                    break;
                }
                turned = true;
            }
        }
    } //rotateS

    /**
     * Rotates a Z-piece.
     */
    public void rotateZ() {
        if (turned) {
            for (int i = 0; i < 4; i++) {
                Rectangle r = new Rectangle(s, s, current[i].getFill());
                r.setStroke(Color.BLACK);
                Rectangle w = new Rectangle(s, s, white.getFill());
                w.setStroke(Color.BLACK);
                int row = coords[i][0];
                int col = coords[i][1];
                switch (i) {
                case 0:
                    addShape(row - 1, col, r);
                    addShape(row, col, w);
                    coords[i][0] = row - 1;
                    break;
                case 1:
                    addShape(row - 2, col + 1, r);
                    addShape(row, col, w);
                    coords[i][0] = row - 2;
                    coords[i][1] = col + 1;
                    break;
                case 2:
                    addShape(row + 1, col, r);
                    addShape(row, col, w);
                    coords[i][0] = row + 1;
                    break;
                case 3:
                    addShape(row, col + 1, r);
                    addShape(row, col, w);
                    coords[i][1] = col + 1;
                    break;
                default:
                    break;
                }
                turned = false;
            }
        } else {
            for (int i = 0; i < 4; i++) {
                Rectangle r = new Rectangle(s, s, current[i].getFill());
                r.setStroke(Color.BLACK);
                Rectangle w = new Rectangle(s, s, white.getFill());
                w.setStroke(Color.BLACK);
                int row = coords[i][0];
                int col = coords[i][1];
                switch (i) {
                case 0:
                    addShape(row + 1, col, r);
                    addShape(row, col, w);
                    coords[i][0] = row + 1;
                    break;
                case 1:
                    addShape(row + 2, col - 1, r);
                    addShape(row, col, w);
                    coords[i][0] = row + 2;
                    coords[i][1] = col - 1;
                    break;
                case 2:
                    addShape(row - 1, col, r);
                    addShape(row, col, w);
                    coords[i][0] = row - 1;
                    break;
                case 3:
                    addShape(row, col - 1, r);
                    addShape(row, col, w);
                    coords[i][1] = col - 1;
                    break;
                default:
                    break;
                }
                turned = true;
            }
        }
    } //rotateS

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
        for (int i = 3; i >= 0; i--) {
            Rectangle r = new Rectangle(s, s, current[i].getFill());
            r.setStroke(Color.BLACK);
            Rectangle w = new Rectangle(s, s, white.getFill());
            w.setStroke(Color.BLACK);
            int row = coords[i][0];
            int col = coords[i][1];
            if (row + 1 < 20) {
                if (testMove(row + 1, col)) {
                    //remove(row + 1, col);
                    addShape(row + 1, col, r);
                    //remove(row, col);
                    addShape(row, col, w);
                    coords[i][0] = row + 1;
                } else {
                    done = true;
                    gridAdd();
                    break;
                }
            } else {
                done = true;
                gridAdd();
                break;

            }
        }

    } //moveDown

    /**
     * Adds a shape to the Rectangle grid.
     */
    public void gridAdd() {
        for (int i = 3; i >= 0; i--) {
            Rectangle r = new Rectangle(s, s, current[i].getFill());
            r.setStroke(Color.BLACK);
            Rectangle w = new Rectangle(s, s, white.getFill());
            w.setStroke(Color.BLACK);
            int row = coords[i][0];
            int col = coords[i][1];
            grid[row][col] = r;
        }

    } //gridAdd

    /**
     * Scans the board from the bottom up to see how many rows
     * are full of rectangles.
     */
    public void checkBoard() {
        for (int i = 19; i >= 0; i--) {
            int count = 0;
            for (int j = 0; j < 9; j++) {
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
                if (grid[i][j].getFill() != Color.WHITE) {
                    addShape(i, j, grid[i][j]);
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
