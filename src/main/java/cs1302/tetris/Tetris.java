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
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;

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
    private Rectangle white;
    private GridPane gp;
    private Timeline tl;
    private Rectangle[] current = new Rectangle[4];
    private Rectangle[][] grid = new Rectangle[20][10];
    private int numLines = 0;
    private boolean playing = true;
    private boolean dropping = true;
    private boolean spawn = false;
    private boolean done = false;
    private Tetrominoe piece;
    private Tetrominoe[] shapes;
    private Color[] colors = {
        Color.WHITE, Color.LIGHTGREEN, Color.RED, Color.ORANGE, Color.NAVY,
        Color.YELLOW, Color.CYAN, Color.MEDIUMPURPLE
    };
    private Random rand = new Random();
    private int s = 30;
    private int[][] coords = new int[4][2];
    private double time = 1000;
    private int assBlast = 0; //variable for dealing with rotating L, J, and T piece
    private int level = 1;
    private Label clears;
    private Label scores;
    private Label levels;
    private int playScore = 0;
    private Button quit;
    private Text tetris;
    private Stage helperStage;



    /**
     * The start method for the application.
     * @param stage the stage to add things to
     */
    public void start(Stage stage) {
        //helperStage = stage;
        init(stage);
        EventHandler<ActionEvent> game = event -> {
            if (!spawn) {
                randomShape(shapes);
                done = false;
                makeShape();
                assBlast = 0;
                spawn = true;
            }
            moveDown();
            if (done) {
                checkBoard();
                done = false;
                spawn = false;
                updateSpeed();
                updateStatus(stage);
            }
        };
        KeyFrame fuck = new KeyFrame(Duration.millis(time), game);
        tl.getKeyFrames().add(fuck);
        tl.play();

        container = new HBox(score, board);
        container.setOnKeyPressed(move());
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
     * Updates the game speed depending on the level of the game.
     */
    public void updateSpeed() {
        if (numLines < 5) {
            level = 1;
            time = 1000;
            tl.stop();
            tl.play();
        } else if (numLines >= 5 && numLines < 10) {
            level = 2;
            time = 750;
            tl.stop();
            tl.play();
        } else {
            level = 3;
            time = 500;
            tl.stop();
            tl.play();
        }
    } //updateSpeed

    /**
     * Updates the status to accomadate the new lines cleared.
     * @param stage the stage to close
     */
    public void updateStatus(Stage stage) {
        tetris = new Text("TETRIS");
        tetris.setUnderline(true);
        tetris.setFont(new Font(s));
        quit = new Button("Quit");
        EventHandler<ActionEvent> exit = event -> {
            stage.close();
        };
        quit.setOnAction(exit);
        clears = new Label("Lines clear: " + Integer.toString(numLines));
        scores = new Label("Score: " + Integer.toString(calcScore()));
        levels = new Label("Level: " + Integer.toString(level));
        //stats.getChildren().addAll(clears, scores, levels);
        String str = "Press R to rotate \n Press Left and Right to move\n Press down fall faster";
        Text guide = new Text(str);

        VBox nBox = new VBox();
        nBox.getChildren().addAll(tetris, clears, scores, levels, guide, quit);
        score = nBox;
    } //updateStatus

    /**
     * Calculates the score based on the lines cleared.
     * @return the value of the players score for the game.
     */
    public int calcScore() {
        return playScore += (numLines * 100);
    } //calcScore

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
     * @param stage the stage to add stuff to
     */
    public void init(Stage stage) {
        time = 1000;
        numLines = 0;
        //stats = new Pane();
        board = new VBox();
        score = new VBox();
        container = new HBox();
        width = 10;
        height = 20;
        white = new Rectangle(s, s, Color.WHITE);
        white.setStroke(Color.BLACK);
        gp = new GridPane();
        board.getChildren().add(gp);
//        System.out.println(board.getChildren().toString());
        tl = new Timeline();
        tl.setCycleCount(Animation.INDEFINITE);
        tl.setAutoReverse(true);
        defaultStart();
        shapes = Tetrominoe.values();
        clean();
        spawn = false;
        done = false;
        assBlast = 0;
        updateStatus(stage);
    } //init

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
        if (!done && canStart(row, col)) {
            addShape(row, col, r);
        }
    } //genShape

    /**
     * Tests a coord to see if it can actually be inserted.
     * @param row the row of insertion
     * @param col the column of insertion
     * @return if the piece can be placed or not
     */
    public boolean canStart(int row, int col) {
        if (grid[row][col] == null) {
            return true;
        } else {
            done = true;
            gameOver();
            return false;
        }
    } //canStart()



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
     * @param row the row to check
     * @param col the column to check
     * @return if the piece can be moved
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
        tl.stop();
        Text mes = new Text("GAME OVER");
        Button retry = new Button("Retry?");
        EventHandler<ActionEvent> redo = event -> {
            init(helperStage);
            tl.play();
        };
        retry.setOnAction(redo);
        score.getChildren().addAll(mes, retry);
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
     * @return the newly made Rectangle
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
            int row = coords[i][0];
            int col = coords[i][1];
            addShape(row, col, eRect(white.getFill()));
        }
        if (assBlast == 0) {
            int row = coords[0][0];
            int col = coords[0][1];
            addShape(row - 1, col, eRect(current[3].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row, col, eRect(current[0].getFill()));
            coords[1][0] = row;
            coords[1][1] = col;
            addShape(row, col + 1, eRect(current[3].getFill()));
            coords[2][0] = row;
            coords[2][1] = col + 1;
            addShape(row + 1, col + 1, eRect(current[3].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col + 1;
            assBlast++;
        } else {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row, col, eRect(current[3].getFill()));
            coords[0][0] = row;
            coords[0][1] = col;
            addShape(row, col + 1, eRect(current[3].getFill()));
            coords[1][0] = row;
            coords[1][1] = col + 1;
            addShape(row + 1, col - 1, eRect(current[3].getFill()));
            coords[2][0] = row + 1;
            coords[2][1] = col - 1;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col;
            assBlast = 0;
        }
    } //rotateS

    /**
     * Rotates a Z-Piece.
     */
    public void rotateZ() {
        for (int i = 0; i < 4; i++) {
            int row = coords[i][0];
            int col = coords[i][1];
            addShape(row, col, eRect(white.getFill()));
        }
        if (assBlast == 0) {
            int row = coords[2][0];
            int col = coords[2][1];
            addShape(row - 1, col + 1, eRect(current[2].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col + 1;
            addShape(row, col, eRect(current[2].getFill()));
            coords[1][0] = row;
            coords[1][1] = col;
            addShape(row, col + 1, eRect(current[2].getFill()));
            coords[2][0] = row;
            coords[2][1] = col + 1;
            addShape(row + 1, col, eRect(current[2].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col;
            assBlast++;
        } else {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row - 1, col - 1, eRect(current[2].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col - 1;
            addShape(row - 1, col, eRect(current[2].getFill()));
            coords[1][0] = row - 1;
            coords[1][1] = col;
            addShape(row, col, eRect(current[2].getFill()));
            coords[2][0] = row;
            coords[2][1] = col;
            addShape(row, col + 1, eRect(current[2].getFill()));
            coords[3][0] = row;
            coords[3][1] = col + 1;
            assBlast = 0;
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
        if (assBlast == 0) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[2][0] = row + 1;
            coords[2][1] = col;
            addShape(row + 2, col, eRect(current[1].getFill()));
            coords[3][0] = row + 2;
            coords[3][1] = col;
            assBlast++;
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
            assBlast = 0;
        }
    } //rotateLine

    /**
     * Rotates the T-piece.
     */
    public void rotateT() {
        for (int i = 0; i < 4; i++) {
            int row = coords[i][0];
            int col = coords[i][1];
            addShape(row, col, eRect(white.getFill()));
        }
        if (assBlast == 0) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[1][0] = row;
            coords[1][1] = col - 1;
            addShape(row, col, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col;
            assBlast++;
        } else if (assBlast == 1) {
            int row = coords[2][0];
            int col = coords[2][1];
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[1][0] = row;
            coords[1][1] = col - 1;
            addShape(row, col, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col;
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
            int row = coords[2][0];
            int col = coords[2][1];
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row, col, eRect(current[1].getFill()));
            coords[1][0] = row;
            coords[1][1] = col;
            addShape(row, col + 1, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col + 1;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col;
            assBlast++;
        } else if (assBlast == 3) {
            int row = coords[1][0];
            int col = coords[1][1];
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
            addShape(row - 1, col + 1, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col + 1;
            addShape(row, col, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col;
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[1][0] = row;
            coords[1][1] = col - 1;
            addShape(row, col + 1, eRect(current[1].getFill()));
            coords[3][0] = row;
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
            int row = coords[2][0];
            int col = coords[2][1];
            addShape(row - 1, col - 1, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col - 1;
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[1][0] = row - 1;
            coords[1][1] = col;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col;
            assBlast++;
        } else if (assBlast == 3) {
            int row = coords[2][0];
            int col = coords[2][1];
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[0][0] = row;
            coords[0][1] = col - 1;
            addShape(row, col, eRect(current[1].getFill()));
            coords[1][0] = row;
            coords[1][1] = col;
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
            int row = coords[i][0];
            int col = coords[i][1];
            addShape(row, col, eRect(white.getFill()));
        }
        if (assBlast == 0) {
            int row = coords[1][0];
            int col = coords[1][1];
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row - 1, col + 1, eRect(current[1].getFill()));
            coords[1][0] = row - 1;
            coords[1][1] = col + 1;
            addShape(row, col, eRect(current[1].getFill()));
            coords[2][0] = row;
            coords[2][1] = col;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col;
            assBlast++;
        } else if (assBlast == 1) {
            int row = coords[2][0];
            int col = coords[2][1];
            addShape(row - 1, col - 1, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col - 1;
            addShape(row, col - 1, eRect(current[1].getFill()));
            coords[1][0] = row;
            coords[1][1] = col - 1;
            addShape(row, col + 1, eRect(current[1].getFill()));
            coords[3][0] = row;
            coords[3][1] = col + 1;
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
            int row = coords[2][0];
            int col = coords[2][1];
            addShape(row - 1, col, eRect(current[1].getFill()));
            coords[0][0] = row - 1;
            coords[0][1] = col;
            addShape(row, col, eRect(current[1].getFill()));
            coords[1][0] = row;
            coords[1][1] = col;
            addShape(row + 1, col - 1, eRect(current[1].getFill()));
            coords[2][0] = row + 1;
            coords[2][1] = col - 1;
            addShape(row + 1, col, eRect(current[1].getFill()));
            coords[3][0] = row + 1;
            coords[3][1] = col;
            assBlast++;
        } else if (assBlast == 3) {
            int row = coords[1][0];
            int col = coords[1][1];
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
            } else if (!done) {
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
            grid[row][col] = current[i];
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
        }
        numLines++;
    } //clearRow

    /**
     * Shifts all the squares above a row that has been cleared down one.
     * @param row the row to start from
     */
    public void shiftDown(int row) {
        for (int i = row; i >= 0; i--) {
            for (int j = 0; j < 10; j++) {
                if (grid[i - 1][j] != null) {
                    Rectangle add = eRect(grid[i - 1][j].getFill());
                    addShape(i, j, add);
                    grid[i][j] = grid[i - 1][j];
                    addShape(i - 1, j, eRect(white.getFill()));
                    grid[i - 1][j] = null;
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
