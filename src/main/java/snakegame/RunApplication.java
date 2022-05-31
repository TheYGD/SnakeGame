package snakegame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RunApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("/snakegame/scenes/menu-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Snake");


        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}