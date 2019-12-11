package cs1302.arcade;

import java.util.Random;
import javafx.stage.Modality;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import cs1302.tetris.Tetris;
import cs1302.mancala.Mancala;

/**
 * Application subclass for {@code ArcadeApp}.
 * @version 2019.fa
 */
public class ArcadeApp extends Application {

    Group group = new Group();           // main container
    HBox buttons = new HBox();
    Rectangle r = new Rectangle(20, 20); // some rectangle
    Button tetris = new Button();
    Button mancala = new Button();

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        Stage tetrisStage = new Stage();
        tetrisStage.initModality(Modality.APPLICATION_MODAL);
        Tetris game = new Tetris();
        EventHandler<ActionEvent> tetrisStart = event -> {
            game.start(tetrisStage);
        };
        tetris.setOnAction(tetrisStart);
        buttons.getChildren().add(tetris);

        Stage mancalaStage = new Stage();
        mancalaStage.initModality(Modality.APPLICATION_MODAL);
        EventHandler<ActionEvent> mancalaStart = event -> {
            Mancala game2 = new Mancala();
            game2.start(mancalaStage);
        };
        mancala.setOnAction(mancalaStart);
        buttons.getChildren().add(mancala);

        group.getChildren().add(buttons);

        StackPane sp = new StackPane();

        sp.getChildren().add(group);

        sp.setId("pane");

        tetris.setTranslateX(-150.0);
        tetris.setTranslateY(500.0);

        tetris.setId("tetris_but");
        mancala.setId("man_but");

        mancala.setTranslateX(20.0);
        mancala.setTranslateY(500.0);

        Scene scene = new Scene(sp, 600, 600);
        scene.getStylesheets().addAll("file:src/main/java/cs1302/arcade/style.css");
        stage.setTitle("cs1302-arcade!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

        // the group must request input focus to receive key events
        // @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#requestFocus--
        group.requestFocus();

    } // start

} // ArcadeApp
