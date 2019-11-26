package cs1302.tetris;

import cs1302.tetris.Shape.Tetrominoe;
import javafx.animation.Timeline;
//import javafx.scene.Control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The grid that the game is played on, as in,
 * the main window the player has as to what is going
 * on.
 */
public class Board {

    private final int WIDTH = 10;
    private final int HEIGHT = 20;

    private boolean fallen;
    private Timeline anime;
    private VBox board;
    private VBox status;
    private HBox container;
    private Tetrominoe[] shapes;
    private Shape dropPiece;
    private Shape nextPiece;

    /**
     * The default constructor for the Board.
     */
    public Board() {
        fallen = false;
        //anime = new Timeline();
        board = new VBox();
        status = new VBox();
        container = new HBox();
        shapes = new Tetrominoe[WIDTH * HEIGHT];

    } //Board constructor

    /**
     * Returns the shape currently located at the index on the board.
     * @param x the x location of the piece
     * @param y the y location of the piece
     * @return the joint location in the array given the params
     */
    public Tetrominoe shape(int x, int y) {
        return shapes[y * WIDTH + x];
    } //shape




} //Board
