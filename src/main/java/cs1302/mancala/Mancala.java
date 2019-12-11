package cs1302.mancala;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.scene.Group;
import javafx.stage.Modality;

/**
 * This class represents a Mancala Game.
 */
public class Mancala extends Application {
    //created as instnace variables to be edited elsehwere
    private static HBox Hbox;
    private static VBox vbox;
    private static Stage stage;
    private static Stage win;

    //gridpane for pits
    private static GridPane pits;

    //initial ints that represent the amount of stones in a pit or store
    //the pits of the game
    private static int p1 = 4;
    private static int p2 = 4;
    private static int p3 = 4;
    private static int p4 = 4;
    private static int p5 = 4;
    private static int p6 = 4;
    private static int p7 = 4;
    private static int p8 = 4;
    private static int p9 = 4;
    private static int p10 = 4;
    private static int p11 = 4;
    private static int p12 = 4;
    //ints for the player stores for their collected pieces(points)
    private static int store1 = 0;
    private static int store2 = 0;

    //arrays used for movement from each pit of the pits
    private static int [] p1Array = {p2, p3, p4, p5, p6, store1, p7, p8, p9, p10, p11, p12, p1};
    private static int [] p2Array = {p3, p4, p5, p6, store1, p7, p8, p9, p10, p11, p12, p1, p2};
    private static int [] p3Array = {p4, p5, p6, store1, p7, p8, p9, p10, p11, p12, p1, p2, p3};
    private static int [] p4Array = {p5, p6, store1, p7, p8, p9, p10, p11, p12, p1, p2, p3, p4};
    private static int [] p5Array = {p6, store1, p7, p8, p9, p10, p11, p12, p1, p2, p3, p4, p5};
    private static int [] p6Array = {store1, p7, p8, p9, p10, p11, p12, p1, p2, p3, p4, p5, p6};
    private static int [] p7Array = {p8, p9, p10, p11, p12, store2, p1, p2, p3, p4, p5, p6, p7};
    private static int [] p8Array = {p9, p10, p11, p12, store2, p1, p2, p3, p4, p5, p6, p7, p8};
    private static int [] p9Array = {p10, p11, p12, store2, p1, p2, p3, p4, p5, p6, p7, p8, p9};
    private static int [] p10Array = {p11, p12, store2, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
    private static int [] p11Array = {p12, store2, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11};
    private static int [] p12Array = {store2, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12};

    //both players stores shapes
    private static Button s1 = new Button("" + store1);
    private static Button s2 = new Button("" + store2);

    //player 1 pit buttons
    private static Button pit1 = new Button("" + p1);
    private static Button pit2 = new Button("" + p2);
    private static Button pit3 = new Button("" + p3);
    private static Button pit4 = new Button("" + p4);
    private static Button pit5 = new Button("" + p5);
    private static Button pit6 = new Button("" + p6);

    //player 2 pit buttons
    private static Button pit7 = new Button("" + p7);
    private static Button pit8 = new Button("" + p8);
    private static Button pit9 = new Button("" + p9);
    private static Button pit10 = new Button("" + p10);
    private static Button pit11 = new Button("" + p11);
    private static Button pit12 = new Button("" + p12);

    //arrays for enabling disabling buttons
    private static Button [] p1Buttons;
    private static Button [] p2Buttons;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        set();
        Scene scene = new Scene(vbox);
        scene.getStylesheets().addAll("file:src/main/java/cs1302/mancala/style.css");

        stage.setTitle("Mancala");
        stage.setScene(scene);
        stage.setWidth(1150);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
    } //Mancala

    // public static void main(String[] args) {
    //     try {
    //         Application.launch(Mancala.class, args);
    //     } catch (UnsupportedOperationException e) {
    //         System.out.println(e);
    //         System.err.println("If this is a DISPLAY problem, then your X server connection");
    //         System.err.println("has likely timed out. This can generally be fixed by logging");
    //         System.err.println("out and logging back in.");
    //         System.exit(1);
    //     } // try
    // } // main

    private static void set() {
        pits = new GridPane();
        pits.setPadding(new Insets(10, 10, 10, 10));
        pits.setHgap(6);
        pits.setVgap(6);

        vbox = new VBox();
        Hbox = new HBox();
        //uses the helper methods to set up the game
        addPits();
        makeArrays();
        disable(p2Buttons);
        setIds();
        setButtonActions();
        setSeeds();

        //creates scene
        Hbox.getChildren().addAll(stackPoints(s1), pits, stackPoints(s2));
        Hbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(Hbox);
        vbox.setId("pane");
    } //init

    /**
     * Sets the action for each pit button.
     */
    public static void setButtonActions() {
        button1();
        button2();
        button3();
        button4();
        button5();
        button6();
        button7();
        button8();
        button9();
        button10();
        button11();
        button12();
    } //setButtonActions

    /**
     * Sets action for pit1.
     */
    private static void button1() {
        pit1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int moveAgain = p1;
                    while (p1 != 0) {
                        for (int i = 0; i < p1Array.length; i ++) {
                            p1Array[i] ++;
                            p1 --;
                            if (p1 == 0) {
                                break;
                            } //if
                        } //for
                    } //while
                    if (moveAgain > 12) {
                        p1 ++;
                    } //if
                    updateIntsp1();
                    setSeeds();
                    for (int i = 0; i < p1Buttons.length; i++) {
                        if (Integer.parseInt(p1Buttons[i].getText().toString()) == 0) {
                            p1Buttons[i].setDisable(true);
                        } else {
                            p1Buttons[i].setDisable(false);
                        } //if
                    } //for
                    if (moveAgain - 6 != 0) {
                        disable(p1Buttons);
                        enable(p2Buttons);
                    } //if
                    checkGameOver();
                } //handle
            });
    } //button1

    /**
     * Sets action for pit2.
     */
    private static void button2() {
        pit2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int moveAgain = p2;
                    while (p2 != 0) {
                        for (int i = 0; i < p2Array.length; i ++) {
                            p2Array[i] ++;
                            p2 --;
                            if (p2 == 0) {
                                break;
                            } //if
                        } //for
                    } //while
                    if (moveAgain > 12) {
                        p2 ++;
                    } //if
                    updateIntsp2();
                    setSeeds();
                    for (int i = 0; i < p1Buttons.length; i++) {
                        if (Integer.parseInt(p1Buttons[i].getText().toString()) == 0) {
                            p1Buttons[i].setDisable(true);
                        } else {
                            p1Buttons[i].setDisable(false);
                        } //if
                    } //for
                    if (moveAgain - 5 != 0) {
                        disable(p1Buttons);
                        enable(p2Buttons);
                    } //if
                    checkGameOver();
                } //handle
            });
    } //button2

    /**
     * Sets action for pit3.
     */
    private static void button3() {
        pit3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int moveAgain = p3;
                    while (p3 != 0) {
                        for (int i = 0; i < p3Array.length; i ++) {
                            p3Array[i] ++;
                            p3 --;
                            if (p3 == 0) {
                                break;
                            } //if
                        } //for
                    } //while
                    if (moveAgain > 12) {
                        p3 ++;
                    } //if
                    updateIntsp3();
                    setSeeds();
                    for (int i = 0; i < p1Buttons.length; i++) {
                        if (Integer.parseInt(p1Buttons[i].getText().toString()) == 0) {
                            p1Buttons[i].setDisable(true);
                        } else {
                            p1Buttons[i].setDisable(false);
                        } //if
                    } //for
                    if (moveAgain - 4 != 0) {
                        disable(p1Buttons);
                        enable(p2Buttons);
                    } //if
                    checkGameOver();
                } //handle
            });
    } //button3

    /**
     * Sets action for pit4.
     */
    private static void button4() {
        pit4.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int moveAgain = p4;
                    while (p4 != 0) {
                        for (int i = 0; i < p4Array.length; i ++) {
                            p4Array[i] ++;
                            p4 --;
                            if (p4 == 0) {
                                break;
                            } //if
                        } //for
                    } //while
                    if (moveAgain > 12) {
                        p4 ++;
                    } //if
                    updateIntsp4();
                    setSeeds();
                    for (int i = 0; i < p1Buttons.length; i++) {
                        if (Integer.parseInt(p1Buttons[i].getText().toString()) == 0) {
                            p1Buttons[i].setDisable(true);
                        } else {
                            p1Buttons[i].setDisable(false);
                        } //if
                    } //for
                    if (moveAgain - 3 != 0) {
                        disable(p1Buttons);
                        enable(p2Buttons);
                    } //if
                    checkGameOver();
                } //handle
            });
    } //button4

    /**
     * Sets action for pit5.
     */
    private static void button5() {
        pit5.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int moveAgain = p5;
                    while (p5 != 0) {
                        for (int i = 0; i < p5Array.length; i ++) {
                            p5Array[i] ++;
                            p5 --;
                            if (p5 == 0) {
                                break;
                            } //if
                        } //for
                    } //while
                    if (moveAgain > 12) {
                        p5 ++;
                    } //if
                    updateIntsp5();
                    setSeeds();
                    for (int i = 0; i < p1Buttons.length; i++) {
                        if (Integer.parseInt(p1Buttons[i].getText().toString()) == 0) {
                            p1Buttons[i].setDisable(true);
                        } else {
                            p1Buttons[i].setDisable(false);
                        } //if
                    } //for
                    if (moveAgain - 2 != 0) {
                        disable(p1Buttons);
                        enable(p2Buttons);
                    } //if
                    checkGameOver();
                } //handle
            });
    } //button5

    /**
     * Sets action for pit6.
     */
    private static void button6() {
        pit6.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int moveAgain = p6;
                    while (p6 != 0) {
                        for (int i = 0; i < p6Array.length; i ++) {
                            p6Array[i] ++;
                            p6 --;
                            if (p6 == 0) {
                                break;
                            } //if
                        } //for
                    } //while
                    if (moveAgain > 12) {
                        p6 ++;
                    } //if
                    updateIntsp6();
                    setSeeds();
                    for (int i = 0; i < p1Buttons.length; i++) {
                        if (Integer.parseInt(p1Buttons[i].getText().toString()) == 0) {
                            p1Buttons[i].setDisable(true);
                        } else {
                            p1Buttons[i].setDisable(false);
                        } //if
                    } //for
                    if (moveAgain != 1) {
                        disable(p1Buttons);
                        enable(p2Buttons);
                    } //if
                    checkGameOver();
                } //handle
            });
    } //buton6

    /**
     * Sets action for pit7.
     */
    private static void button7() {
        pit7.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int moveAgain = p7;
                    while (p7 != 0) {
                        for (int i = 0; i < p7Array.length; i ++) {
                            p7Array[i] ++;
                            p7 --;
                            if (p7 == 0) {
                                break;
                            } //if
                        } //for
                    } //while
                    if (moveAgain > 12) {
                        p7 ++;
                    } //if
                    updateIntsp7();
                    setSeeds();
                    for (int i = 0; i < p2Buttons.length; i++) {
                        if (Integer.parseInt(p2Buttons[i].getText().toString()) == 0) {
                            p2Buttons[i].setDisable(true);
                        } else {
                            p2Buttons[i].setDisable(false);
                        } //if
                    } //for
                    if (moveAgain - 6 != 0) {
                        disable(p2Buttons);
                        enable(p1Buttons);
                    } //if
                    checkGameOver();
                } //handle
            });
    } //button7

    /**
     * Sets action for pit8.
     */
    private static void button8() {
        pit8.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int moveAgain = p8;
                    while (p8 != 0) {
                        for (int i = 0; i < p8Array.length; i ++) {
                            p8Array[i] ++;
                            p8 --;
                            if (p8 == 0) {
                                break;
                            } //if
                        } //for
                    } //while
                    if (moveAgain > 12) {
                        p8 ++;
                    } //if
                    updateIntsp8();
                    setSeeds();
                    for (int i = 0; i < p2Buttons.length; i++) {
                        if (Integer.parseInt(p2Buttons[i].getText().toString()) == 0) {
                            p2Buttons[i].setDisable(true);
                        } else {
                            p2Buttons[i].setDisable(false);
                        } //if
                    } //for
                    if (moveAgain - 5 != 0) {
                        disable(p2Buttons);
                        enable(p1Buttons);
                    } //if
                    checkGameOver();
                } //handle
            });
    } //button8

    /**
     * Sets action for pit9.
     */
    private static void button9() {
        pit9.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int moveAgain = p9;
                    while (p9 != 0) {
                        for (int i = 0; i < p9Array.length; i ++) {
                            p9Array[i] ++;
                            p9 --;
                            if (p9 == 0) {
                                break;
                            } //if
                        } //for
                    } //while
                    if (moveAgain > 12) {
                        p9 ++;
                    } //if
                    updateIntsp9();
                    setSeeds();
                    for (int i = 0; i < p2Buttons.length; i++) {
                        if (Integer.parseInt(p2Buttons[i].getText().toString()) == 0) {
                            p2Buttons[i].setDisable(true);
                        } else {
                            p2Buttons[i].setDisable(false);
                        } //if
                    } //for
                    if (moveAgain - 4 != 0) {
                        disable(p2Buttons);
                        enable(p1Buttons);
                    } //if
                    checkGameOver();
                } //handle
            });
    } //button9

    /**
     * Sets action for pit10.
     */
    private static void button10() {
        pit10.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int moveAgain = p10;
                    while (p10 != 0) {
                        for (int i = 0; i < p10Array.length; i ++) {
                            p10Array[i] ++;
                            p10 --;
                            if (p10 == 0) {
                                break;
                            } //if
                        } //for
                    } //while
                    if (moveAgain > 12) {
                        p10 ++;
                    } //if
                    updateIntsp10();
                    setSeeds();
                    for (int i = 0; i < p2Buttons.length; i++) {
                        if (Integer.parseInt(p2Buttons[i].getText().toString()) == 0) {
                            p2Buttons[i].setDisable(true);
                        } else {
                            p2Buttons[i].setDisable(false);
                        } //if
                    } //for
                    if (moveAgain - 3 != 0) {
                        disable(p2Buttons);
                        enable(p1Buttons);
                    } //if
                    checkGameOver();
                } //handle
            });
    } //button10

    /**
     * Sets action for pit11.
     */
    private static void button11() {
        pit11.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int moveAgain = p11;
                    while (p11 != 0) {
                        for (int i = 0; i < p11Array.length; i ++) {
                            p11Array[i] ++;
                            p11 --;
                            if (p11 == 0) {
                                break;
                            } //if
                        } //for
                    } //while
                    if (moveAgain > 12) {
                        p11 ++;
                    } //if
                    updateIntsp11();
                    setSeeds();
                    for (int i = 0; i < p2Buttons.length; i++) {
                        if (Integer.parseInt(p2Buttons[i].getText().toString()) == 0) {
                            p2Buttons[i].setDisable(true);
                        } else {
                            p2Buttons[i].setDisable(false);
                        } //if
                    } //for
                    if (moveAgain - 2 != 0) {
                        disable(p2Buttons);
                        enable(p1Buttons);
                    } //if
                    checkGameOver();
                } //handle
            });
    } //button11

    /**
     * Sets action for pit12.
     */
    private static void button12() {
        pit12.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int moveAgain = p12;
                    while (p12 != 0) {
                        for (int i = 0; i < p12Array.length; i ++) {
                            p12Array[i] ++;
                            p12 --;
                            if (p12 == 0) {
                                break;
                            } //if
                        } //for
                    } //while
                    if (moveAgain > 12) {
                        p12 ++;
                    } //if
                    updateIntsp12();
                    setSeeds();
                    for (int i = 0; i < p2Buttons.length; i++) {
                        if (Integer.parseInt(p2Buttons[i].getText().toString()) == 0) {
                            p2Buttons[i].setDisable(true);
                        } else {
                            p2Buttons[i].setDisable(false);
                        } //if
                    } //for
                    if (moveAgain != 1) {
                        disable(p2Buttons);
                        enable(p1Buttons);
                    } //if
                    checkGameOver();
                } //handle
            });
    } //buton12

    /**
     * Populates the gridpane with the pits.
     */
    private static void addPits() {
        //adds p1 pits
        pits.add(pit6, 0, 0);
        pits.add(pit5, 1, 0);
        pits.add(pit4, 2, 0);
        pits.add(pit3, 3, 0);
        pits.add(pit2, 4, 0);
        pits.add(pit1, 5, 0);

        //adds p2 pits
        pits.add(pit7, 0, 1);
        pits.add(pit8, 1, 1);
        pits.add(pit9, 2, 1);
        pits.add(pit10, 3, 1);
        pits.add(pit11, 4, 1);
        pits.add(pit12, 5, 1);
    } //addShapes

    /**
     * Sets id's for the pits and stores for styling.
     */
    private static void setIds() {
        pit1.setId("pit");
        pit2.setId("pit");
        pit3.setId("pit");
        pit4.setId("pit");
        pit5.setId("pit");
        pit6.setId("pit");
        pit7.setId("pit");
        pit8.setId("pit");
        pit9.setId("pit");
        pit10.setId("pit");
        pit11.setId("pit");
        pit12.setId("pit");
        s1.setId("store");
        s2.setId("store");
    } //setIds


    /**
     * Overlays player points on their store.
     *
     * @param b button of player store with int of seeds
     * @return sp the StackPane of the rectangle and button with the seeds
     */
    private static StackPane stackPoints(Button b) {
        StackPane sp = new StackPane();
        Rectangle p1Store = new Rectangle(110.0, 300.0);
        p1Store.setId("rect");
        sp.getChildren().addAll(p1Store, b);
        return sp;
    } //stackPoints

    /**
     * Creates arrays containg the buttons so they can be easily disabled when it is the other
     * players turn.
     */
    private static void makeArrays() {
        //makes an array of player one's buttons
        Button [] ba1 = new Button[6];
        ba1[0] = pit1;
        ba1[1] = pit2;
        ba1[2] = pit3;
        ba1[3] = pit4;
        ba1[4] = pit5;
        ba1[5] = pit6;

        //makes an array of player two's buttons
        Button [] ba2 = new Button[6];
        ba2[0] = pit7;
        ba2[1] = pit8;
        ba2[2] = pit9;
        ba2[3] = pit10;
        ba2[4] = pit11;
        ba2[5] = pit12;

        //sets the instance arrays equal to the new ones created
        p1Buttons = ba1;
        p2Buttons = ba2;
    } //makeArrays

    /**
     * After each move the array holding the state of the board from each pit is updated.
     */
    private static void updateArrays() {
        int [] one = {p2, p3, p4, p5, p6, store1, p7, p8, p9, p10, p11, p12, p1};
        p1Array = one;
        int [] two = {p3, p4, p5, p6, store1, p7, p8, p9, p10, p11, p12, p1, p2};
        p2Array = two;
        int [] three = {p4, p5, p6, store1, p7, p8, p9, p10, p11, p12, p1, p2, p3};
        p3Array = three;
        int [] four = {p5, p6, store1, p7, p8, p9, p10, p11, p12, p1, p2, p3, p4};
        p4Array = four;
        int [] five = {p6, store1, p7, p8, p9, p10, p11, p12, p1, p2, p3, p4, p5};
        p5Array = five;
        int [] six = {store1, p7, p8, p9, p10, p11, p12, p1, p2, p3, p4, p5, p6};
        p6Array = six;
        int [] seven = {p8, p9, p10, p11, p12, store2, p1, p2, p3, p4, p5, p6, p7};
        p7Array = seven;
        int [] eight = {p9, p10, p11, p12, store2, p1, p2, p3, p4, p5, p6, p7, p8};
        p8Array = eight;
        int [] nine = {p10, p11, p12, store2, p1, p2, p3, p4, p5, p6, p7, p8, p9};
        p9Array = nine;
        int [] ten = {p11, p12, store2, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
        p10Array = ten;
        int [] eleven = {p12, store2, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11};
        p11Array = eleven;
        int [] twelve = {store2, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12};
        p12Array = twelve;
    } //updateArrays

    /**
     * Sets the seed text to be displayed.
     */
    private static void setSeeds() {
        pit1.setText("" + p1);
        pit2.setText("" + p2);
        pit3.setText("" + p3);
        pit4.setText("" + p4);
        pit5.setText("" + p5);
        pit6.setText("" + p6);
        pit7.setText("" + p7);
        pit8.setText("" + p8);
        pit9.setText("" + p9);
        pit10.setText("" + p10);
        pit11.setText("" + p11);
        pit12.setText("" + p12);
        s1.setText("" + store1);
        s2.setText("" + store2);
    } //setSeeds

    /**
     * Updates the int array from pit1.
     */
    private static void updateIntsp1() {
        p2 = p1Array[0];
        p3 = p1Array[1];
        p4 = p1Array[2];
        p5 = p1Array[3];
        p6 = p1Array[4];
        store1 = p1Array[5];
        p7 = p1Array[6];
        p8 = p1Array[7];
        p9 = p1Array[8];
        p10 = p1Array[9];
        p11 = p1Array[10];
        p12 = p1Array[11];

        updateArrays();
    } //upateIntsp1

    /**
     * Updates the int array from pit2.
     */
    private static void updateIntsp2() {
        p3 = p2Array[0];
        p4 = p2Array[1];
        p5 = p2Array[2];
        p6 = p2Array[3];
        store1 = p2Array[4];
        p7 = p2Array[5];
        p8 = p2Array[6];
        p9 = p2Array[7];
        p10 = p2Array[8];
        p11 = p2Array[9];
        p12 = p2Array[10];
        p1 = p2Array[11];

        updateArrays();
    } //upateIntsp2

    /**
     * Updates the int array from pit3.
     */
    private static void updateIntsp3() {
        p4 = p3Array[0];
        p5 = p3Array[1];
        p6 = p3Array[2];
        store1 = p3Array[3];
        p7 = p3Array[4];
        p8 = p3Array[5];
        p9 = p3Array[6];
        p10 = p3Array[7];
        p11 = p3Array[8];
        p12 = p3Array[9];
        p1 = p3Array[10];
        p2 = p3Array[11];

        updateArrays();
    } //upateIntsp3

    /**
     * Updates the int array from pit4.
     */
    private static void updateIntsp4() {
        p5 = p4Array[0];
        p6 = p4Array[1];
        store1 = p4Array[2];
        p7 = p4Array[3];
        p8 = p4Array[4];
        p9 = p4Array[5];
        p10 = p4Array[6];
        p11 = p4Array[7];
        p12 = p4Array[8];
        p1 = p4Array[9];
        p2 = p4Array[10];
        p3 = p4Array[11];

        updateArrays();
    } //upateIntsp4

    /**
     * Updates the int array from pit5.
     */
    private static void updateIntsp5() {
        p6 = p5Array[0];
        store1 = p5Array[1];
        p7 = p5Array[2];
        p8 = p5Array[3];
        p9 = p5Array[4];
        p10 = p5Array[5];
        p11 = p5Array[6];
        p12 = p5Array[7];
        p1 = p5Array[8];
        p2 = p5Array[9];
        p3 = p5Array[10];
        p4 = p5Array[11];

        updateArrays();
    } //upateIntsp5

    /**
     * Updates the int array from pit6.
     */
    private static void updateIntsp6() {
        store1 = p6Array[0];
        p7 = p6Array[1];
        p8 = p6Array[2];
        p9 = p6Array[3];
        p10 = p6Array[4];
        p11 = p6Array[5];
        p12 = p6Array[6];
        p1 = p6Array[7];
        p2 = p6Array[8];
        p3 = p6Array[9];
        p4 = p6Array[10];
        p5 = p6Array[11];

        updateArrays();
    } //upateIntsp6

    /**
     * Updates the int array from pit7.
     */
    private static void updateIntsp7() {
        p8 = p7Array[0];
        p9 = p7Array[1];
        p10 = p7Array[2];
        p11 = p7Array[3];
        p12 = p7Array[4];
        store2 = p7Array[5];
        p1 = p7Array[6];
        p2 = p7Array[7];
        p3 = p7Array[8];
        p4 = p7Array[9];
        p5 = p7Array[10];
        p6 = p7Array[11];

        updateArrays();
    } //upateIntsp7

    /**
     * Updates the int array from pit8.
     */
    private static void updateIntsp8() {
        p9 = p8Array[0];
        p10 = p8Array[1];
        p11 = p8Array[2];
        p12 = p8Array[3];
        store2 = p8Array[4];
        p1 = p8Array[5];
        p2 = p8Array[6];
        p3 = p8Array[7];
        p4 = p8Array[8];
        p5 = p8Array[9];
        p6 = p8Array[10];
        p7 = p8Array[11];

        updateArrays();
    } //upateIntsp8

    /**
     * Updates the int array from pit9.
     */
    private static void updateIntsp9() {
        p10 = p9Array[0];
        p11 = p9Array[1];
        p12 = p9Array[2];
        store2 = p9Array[3];
        p1 = p9Array[4];
        p2 = p9Array[5];
        p3 = p9Array[6];
        p4 = p9Array[7];
        p5 = p9Array[8];
        p6 = p9Array[9];
        p7 = p9Array[10];
        p8 = p9Array[11];

        updateArrays();
    } //upateIntsp9

    /**
     * Updates the int array from pit10.
     */
    private static void updateIntsp10() {
        p11 = p10Array[0];
        p12 = p10Array[1];
        store2 = p10Array[2];
        p1 = p10Array[3];
        p2 = p10Array[4];
        p3 = p10Array[5];
        p4 = p10Array[6];
        p5 = p10Array[7];
        p6 = p10Array[8];
        p7 = p10Array[9];
        p8 = p10Array[10];
        p9 = p10Array[11];

        updateArrays();
    } //upateIntsp10

    /**
     * Updates the int array from pit11.
     */
    private static void updateIntsp11() {
        p12 = p11Array[0];
        store2 = p11Array[1];
        p1 = p11Array[2];
        p2 = p11Array[3];
        p3 = p11Array[4];
        p4 = p11Array[5];
        p5 = p11Array[6];
        p6 = p11Array[7];
        p7 = p11Array[8];
        p8 = p11Array[9];
        p9 = p11Array[10];
        p10 = p11Array[11];

        updateArrays();
    } //upateIntsp11

    /**
     * Updates the int array from pit12.
     */
    private static void updateIntsp12() {
        store2 = p12Array[0];
        p1 = p12Array[1];
        p2 = p12Array[2];
        p3 = p12Array[3];
        p4 = p12Array[4];
        p5 = p12Array[5];
        p6 = p12Array[6];
        p7 = p12Array[7];
        p8 = p12Array[8];
        p9 = p12Array[9];
        p10 = p12Array[10];
        p11 = p12Array[11];

        updateArrays();
    } //upateIntsp12

    /**
     * Checks to see if one players pits are empty and the game is over.
     *
     * @return true if one side is empty, flase otherwise
     */
    private static void checkGameOver() {
        boolean check = false;
        if ((p1 + p2 + p3 + p4 + p5 + p6) == 0) {
            check = true;
        } else if ((p7 + p8 + p9 + p10 + p11 + p12) == 0) {
            check = true;
        } else {
            check = false;
        } //if
        if (check) {
            int player1Total = (p1 + p2 + p3 + p4 + p5 + p6 + store1);
            int player2Total = (p7 + p8 + p9 + p10 + p11 + p12 + store2);
            Stage winStage = new Stage();
            win = winStage;
            win.initModality(Modality.APPLICATION_MODAL);
            win.setTitle("Game Over");
            win.setMinWidth(250);
            win.setMinHeight(250);
            Label winner = winner(player1Total, player2Total);
            Label score = new Label("Player1: " + player1Total +
                                    "    Player2: " + player2Total);
            score.setFont(new Font(30.0));
            VBox winBox = new VBox();
            winBox.getChildren().addAll(winner, score, options());
            winBox.setAlignment(Pos.CENTER);
            Scene winScene = new Scene(winBox);
            win.setScene(winScene);
            win.sizeToScene();
            win.showAndWait();
        } //if
    } //checkGameOver

    /**
     * Helper method to return a winner label to be displayed when the game ends.
     *
     * @param player1Total total of player one's points
     * @param player2Total total of player two's points
     * @return Label label to be displayd with the proper winner
     */
    private static Label winner(int player1Total, int player2Total) {
        if (player1Total > player2Total) {
                Label winner = new Label("Player 1 Wins!");
                winner.setFont(new Font(30.0));
                return winner;
            } else {
                Label winner = new Label("Player 2 Wins!");
                winner.setFont(new Font(30.0));
                return winner;
            } //if
    } //winner

    private static HBox options() {
        Button quit = new Button("Quit");
            quit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    win.close();
                    stage.close();
                } //handle
            });
            Button replay = new Button("Play again?");
            replay.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    win.close();
                    stage.close();
                    Mancala game = new Mancala();
                    game.start(new Stage());
                } //handle
            });
            HBox options = new HBox();
            options.getChildren().addAll(quit, replay);
            options.setAlignment(Pos.CENTER);
            return options;
    } //options

    /**
     * Disables the buttons after players turn ends.
     *
     * @param ba array of buttons to be disabled
     */
    private static void disable(Button [] ba) {
        for (int i = 0; i < ba.length; i++) {
            ba[i].setDisable(true);
        } //for
    } //disable

    /**
     * Enables buttons when players turn begins.
     *
     * @param ba array of buttons to be enabled.
     */
    private static void enable(Button [] ba) {
        for (int i = 0; i < ba.length; i++) {
            ba[i].setDisable(false);
            if (Integer.parseInt(ba[i].getText().toString()) == 0) {
                ba[i].setDisable(true);
            } //if
        } //for
    } //enable

} //Mancala
