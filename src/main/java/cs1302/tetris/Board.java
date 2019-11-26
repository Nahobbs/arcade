package cs1302.tetris;

import cs1302.tetris.Shape.Tetrominoe;
import javafx.animation.Timeline;
import javafx.scene.Control.*;
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

    /**
     * The default constructor for the Board.
     */
    public Board() {

    } //Board constructor


} //Board
