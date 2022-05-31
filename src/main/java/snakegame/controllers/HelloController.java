package snakegame.controllers;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import snakegame.Coordinates;
import snakegame.GameLogic;
import snakegame.db.DatabaseService;


public class HelloController {
    @FXML
    private TilePane tilePane;
    @FXML
    private Label scoreLabel;
    @FXML
    private Pane gameoverPane;
    @FXML
    private Label exitHint;

    private Scene menuScene;
    private GameLogic gameLogic;
    private GameLogic.Direction direction = GameLogic.Direction.LEFT;
    private final int boardSize = 10;
    private boolean isGameOver = false;

    public HelloController() {
        System.out.println();
    }

    public void init(Scene menuScene) {
        this.menuScene = menuScene;

        gameLogic = new GameLogic(boardSize);
        gameLogic.setController(this);

        create_board(boardSize);
        handle_keyboard_input();

        scoreLabel.setText("001");

        Thread gameThread = new Thread(gameLogic::game_loop);
        gameThread.start();
    }

    // METHODS --------------------------------------------------

    private void create_board(int dimension) {
        tilePane.setPrefColumns(dimension);
        tilePane.setPrefRows(dimension);
        tilePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
        int tileSize = (int) tilePane.getPrefWidth() / dimension;

        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                Rectangle boardTile = new Rectangle(tileSize, tileSize);
                boardTile.setFill(BOARD_COLORS.VOID);
                tilePane.getChildren().add(boardTile);
            }
        }
    }

    public void changeTile(Coordinates coords, GameLogic.GameObjects object) {
        Rectangle tile = (Rectangle) tilePane.getChildren().get(coords.y * boardSize + coords.x);
        Color tileColor = switch (object) {
            case VOID -> BOARD_COLORS.VOID;
            case FRUIT -> BOARD_COLORS.FRUIT;
            case SNAKE_HEAD -> BOARD_COLORS.SNAKE_HEAD;
            case SNAKE_PART -> BOARD_COLORS.SNAKE_PART;
            default -> BOARD_COLORS.VOID;
        };

        tile.setFill(tileColor);
    }

    private void handle_keyboard_input() {
        Scene scene = tilePane.getScene();
        scene.setOnKeyPressed( event -> {
            if (isGameOver) {
                if (event.getCode() == KeyCode.SPACE) {
                    back_to_menu();
                }

                return;
            }

            if (event.getCode() == KeyCode.W && gameLogic.getMovementDirection() != GameLogic.Direction.DOWN ||
                event.getCode() == KeyCode.A && gameLogic.getMovementDirection() != GameLogic.Direction.RIGHT ||
                event.getCode() == KeyCode.S && gameLogic.getMovementDirection() != GameLogic.Direction.UP ||
                event.getCode() == KeyCode.D && gameLogic.getMovementDirection() != GameLogic.Direction.LEFT) {
                    direction = GameLogic.Direction.of(event.getCode());
            }
        });
    }

    public GameLogic.Direction getDirection() {
        return direction;
    }

    public void updateScore() {

        Platform.runLater( () -> {
            String newScore = String.valueOf(Integer.parseInt(scoreLabel.getText()) + 1);
            while (newScore.length() != 3) {
                newScore = "0" + newScore;
            }
            scoreLabel.setText(newScore);
        });

    }

    public void end_game() {
        // inserting score into database
        DatabaseService.Instance().addScore(Integer.parseInt(scoreLabel.getText()));

        // smoothly coming GameOver screen
        FadeTransition transition = new FadeTransition(Duration.seconds(0.3), gameoverPane);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
        gameoverPane.setVisible(true);

        // flashing BackToMenu hint
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.4), evt -> exitHint.setVisible(false)),
                new KeyFrame(Duration.seconds( 0.6), evt -> exitHint.setVisible(true)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        isGameOver = true;
    }

    private void back_to_menu() {
        Stage stage = (Stage)scoreLabel.getScene().getWindow();
        stage.setScene(menuScene);
    }


    // INNERCLASSES ---------------------------------------------

    private class BOARD_COLORS {
        private static Color VOID = new Color(249/255f,249/255f,225/255f, 1);
        //249, 249, 225
        private static Color FRUIT = new Color(238/255f, 83/255f, 103/255f,1);
        private static Color SNAKE_HEAD = new Color(28/255f, 152/255f, 50/255f, 1);
        private static Color SNAKE_PART = new Color(84/255f, 168/255f, 61/255f, 1);
    }
}