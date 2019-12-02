package cs1302.tetris;

import cs1302.tetris.Shape.Tetrominoe;
import javafx.animation.Timeline;
import java.awt.Graphics;
import java.awt.Color;
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
        grphs.fillRect(x + 1, y + 1, 2, 2);


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




} //Board
