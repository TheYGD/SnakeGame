package snakegame.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import snakegame.db.DatabaseScoreRecord;
import snakegame.db.DatabaseService;

import java.util.List;

public class ScoreboardController {
    @FXML
    private ListView listView;
    private Scene menuScene;

    public void init(Scene menuScene) {
        this.menuScene = menuScene;

        listView.getScene().getStylesheets().add(
                getClass().getResource("/snakegame/styles/scoreboard-style.css").toExternalForm());

        List<DatabaseScoreRecord> records = DatabaseService.Instance().getScores();

        for (DatabaseScoreRecord record : records) {
            add_to_the_list(record);
        }
    }


    private void add_to_the_list(DatabaseScoreRecord record) {
        String score = String.format("%17s", record.getScore());
        String date = String.format("%44s", record.getDate());

        listView.getItems().add(score + date);
    }

    public void back_to_menu() {
        Stage stage = (Stage)listView.getScene().getWindow();
        stage.setScene(menuScene);
    }
}
