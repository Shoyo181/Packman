package com.example.packman;

import com.example.packman.Elementer.Levende.Levende;
import javafx.scene.input.KeyCode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class PackmanGui extends Application {

    final int WIN = 1000;
    BorderPane mainPane;
    Levende.Retning r = Levende.Retning.INGEN;

    @Override
    public void start(Stage stage) throws IOException {
        mainPane = new BorderPane();
        BanePane bane = new BanePane("test", WIN);
        mainPane.setCenter(bane);

        bane.setOnKeyPressed(e -> {
            if( e.getCode() == KeyCode.UP)
                r = Levende.Retning.OPP;
            if( e.getCode() == KeyCode.DOWN)
                r = Levende.Retning.NED;
            if( e.getCode() == KeyCode.LEFT)
                r = Levende.Retning.VENSTRE;
            if( e.getCode() == KeyCode.RIGHT)
                r = Levende.Retning.HØYRE;
            if ( e.getCode() == KeyCode.SPACE)
                bane.start();
            if ( e.getCode() == KeyCode.ESCAPE)
                bane.pause();
            if ( e.getCode() == KeyCode.W)
               r = Levende.Retning.INGEN;

            System.out.println("PacMan er i retning: " + r);
            bane.oppdaterPacManRetning(r);
        });


        Scene scene = new Scene(mainPane, WIN, WIN);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        bane.requestFocus();
    }




    public static void main(String[] args) {
        launch();
    }
}
