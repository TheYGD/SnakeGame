package snakegame;

import java.util.Deque;
import java.util.LinkedList;

public class Snake {
    private Deque<Coordinates> snakeParts;

    // METHODS ------------------------------
    public Snake(int xSize, int ySize) {
        int xPos = xSize / 2 + 1;
        int yPos = ySize / 2;
        snakeParts = new LinkedList<>();
        snakeParts.add(new Coordinates(xPos, yPos));
    }

    public void move(Coordinates nextCoords, boolean fruitEaten) {
        // move head
        snakeParts.addFirst(nextCoords);

        // delete last if didnt eat fruit, else new part spawn on the last coords - nothing changes
        if (!fruitEaten) {
            snakeParts.removeLast();
        }
    }

    public boolean isThisLastPart(Coordinates coords) {
        return coords.equals(snakeParts.getLast());
    }

    public Coordinates getHeadCoords() {
        return snakeParts.getFirst();
    }

    public Coordinates getTailCoords() {
        return snakeParts.getLast();
    }

    public boolean isSnakeHere(Coordinates coords) {
        return snakeParts.contains(coords);
    }

    public int length() {
        return snakeParts.size();
    }
}
