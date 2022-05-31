package snakegame.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MenuController {
    public void btn_play_game(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/snakegame/scenes/game-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        HelloController controller = fxmlLoader.getController();
        controller.init(((Node) event.getSource()).getScene());
    }

    public void btn_scoreboard(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/snakegame/scenes/scoreboard-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        ScoreboardController controller = fxmlLoader.getController();
        controller.init(((Node) event.getSource()).getScene());
    }

    public void btn_exit(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
}
