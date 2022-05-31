package snakegame;

import java.util.Objects;

public class Coordinates {
    public int x;
    public int y;

    Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(Coordinates coords) {
        this.x += coords.x;
        this.y += coords.y;
    }

    public static Coordinates of(Coordinates coords) {
        return new Coordinates(coords.x, coords.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}