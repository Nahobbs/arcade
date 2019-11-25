package cs1302.tetris;

import java.util.Random;

/**
 * The basic overview of a Shape object for the Board.
 * Contains the methods and outlines for each individual
 * shape that can fall from the top.
 */
public class Shape {

    /**
     * Enumerates the different types of shapes that can come out
     * of the class.
     */
    protected enum Tetrominoe {
        None, S, Z, Line, T, Sqr, ML, L
    } //Tetrominoe

    private Tetrominoe shape;
    private int[][] loc;

    /**
     * The default constructor for the shape. Takes in no value and
     * makes the default shape style "none".
     */
    public Shape() {

        //loc = new int[4][2];
        setShape(Tetrominoe.None);
    } //Shape constructor

    /**
     * Sets the coordinates of the shape based on the style of shape
     * that is passed in.
     * @param style the type of shape this instance is
     */
    public void setShape(Tetrominoe style) {
        //the complete list of all possible arrangements of the pieces
        int[][][] possibleLocs = new int[][][] {
            {{0,0},{0,0},{0,0},{0,0}}, //no shape
            {{0,-1},{0,0},{-1,0},{-1,1}}, //S shape
            {{0,-1},{0,0},{1,0},{1,1}}, //Z shape
            {{0,-1},{0,0},{0,1},{0,2}}, //line
            {{-1,0},{0,0},{1,0},{0,1}}, //T shape
            {{0,0},{1,0},{0,1},{1,1}}, //Square
            {{-1,-1},{0,-1},{0,0},{0,1}}, //Mirror L
            {{1,-1},{0,-1},{0,0},{0,1}} // L Shape
        };

        int[][] shapeSpot = new int[4][2];
        for (int i = 0; i < 4; i++) {
            shapeSpot[i] = possibleLocs[style.ordinal()][i];
        }
        loc = shapeSpot;


    } //setShape



} //Shape
