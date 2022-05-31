package snakegame;

import javafx.scene.input.KeyCode;
import snakegame.controllers.HelloController;

import java.util.Random;

public class GameLogic {
    private Direction movementDirection;
    private final BoardSize boardSize;
    private final Snake snake;
    private HelloController gameController;
    private Coordinates fruitCoords;

    // METHODS ------------------------------
    public GameLogic(int boardSizeX, int boardSizeY) {
        boardSize = new BoardSize(boardSizeX, boardSizeY);
        snake = new Snake(boardSize.x, boardSize.y);
    }

    public GameLogic(int boardSize) {
        this(boardSize, boardSize);
    }

    public void game_loop() {
        createFruit();

        while (true) {
            update_direction();
            Coordinates nextSnakeHeadCoords = Coordinates.of(snake.getHeadCoords());
            nextSnakeHeadCoords.move(movementDirection.coords);

            // end the game if snake is off the board or eats itself
            if (off_the_board(nextSnakeHeadCoords) || isSnakeHere(nextSnakeHeadCoords)) {
                break;
            }

            boolean fruitEaten = isFruitHere(nextSnakeHeadCoords);
            Coordinates currentSnakeHead = snake.getHeadCoords();

            if (!fruitEaten) { // delete last snake part
                Coordinates snakeTail = snake.getTailCoords();
                gameController.changeTile(snakeTail, GameObjects.VOID);
            }

            if (snake.length() > 1) {
                gameController.changeTile(snake.getHeadCoords(), GameObjects.SNAKE_PART);
            }

            snake.move(nextSnakeHeadCoords, fruitEaten);
            gameController.changeTile(nextSnakeHeadCoords, GameObjects.SNAKE_HEAD);

            if (fruitEaten) {
                createFruit();
                if (snake.length() == 2) {
                    gameController.changeTile(snake.getTailCoords(), GameObjects.SNAKE_PART);
                }
                gameController.updateScore();
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        end_the_game();
    }

    private boolean off_the_board(Coordinates nextSnakeHeadCoords) {
        return nextSnakeHeadCoords.x < 0 || nextSnakeHeadCoords.x >= boardSize.x ||
                nextSnakeHeadCoords.y < 0 || nextSnakeHeadCoords.y >= boardSize.y;
    }

    private void createFruit() {
        Random random = new Random();
        Coordinates fruitCoords = new Coordinates(random.nextInt(boardSize.x), random.nextInt(boardSize.y));

        while (isSnakeHere(fruitCoords) || fruitCoords.equals(snake.getTailCoords())) {
            fruitCoords.x = random.nextInt(boardSize.x);
            fruitCoords.y = random.nextInt(boardSize.y);
        }

        this.fruitCoords = fruitCoords;
        gameController.changeTile(fruitCoords, GameObjects.FRUIT);
    }

    private void update_direction() {
        movementDirection = gameController.getDirection();
    }

    private boolean isFruitHere(Coordinates coords) {
        return coords.equals(fruitCoords);
    }

    private boolean isSnakeHere(Coordinates coords) {
        if (!snake.isSnakeHere(coords))
            return false;

        return !snake.getTailCoords().equals(coords); // if there is snake but it is its tail then snake doesnt eat itself
    }

    private void end_the_game() {
        gameController.end_game();
    }

    public void setController(HelloController controller) {
        this.gameController = controller;
    }

    public Direction getMovementDirection() {
        return movementDirection;
    }


    // INNERCLASSES -------------------------
    public enum Direction {
        UP(0,-1),
        DOWN(0,1),
        LEFT(-1,0),
        RIGHT(1,0);

        Coordinates coords;

        Direction(int x, int y) {
            coords = new Coordinates(x, y);
        }

        public static Direction of(KeyCode keyCode) {
            switch (keyCode) {
                case W:
                    return UP;
                case A:
                    return LEFT;
                case S:
                    return DOWN;
                case D:
                    return RIGHT;
                default:
                    System.err.println("INVALID KEY!!!");
                    return LEFT;
            }
        }
    }
    private class BoardSize {
        int x;
        int y;

        BoardSize(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public enum GameObjects {
        SNAKE_HEAD,
        SNAKE_PART,
        FRUIT,
        VOID
    }
}
