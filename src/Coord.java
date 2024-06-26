import java.util.Arrays;
import java.util.List;

public class Coord {
    public final int x;
    public final int y;

    Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public List<Coord> getNeighbours() {
        return Arrays.asList(new Coord(x, y - 1), new Coord(x - 1, y), new Coord(x + 1, y), new Coord(x, y + 1));
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Coord that = (Coord) o;
        return this.x == that.x && this.y == that.y;
    }

    public int hashCode() {
        return x * 7 + y * 11;
    }

    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}
