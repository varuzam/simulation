import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class WorldMap {
    public final int height;
    public final int width;
    private Map<Coord, Entity> cells = new HashMap<>();

    WorldMap(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public Entity get(Coord coord) {
        return cells.get(coord);
    }

    // public Map<coord, Entity> getByType(Object entityClass) {
    // Map<coord, Entity> entities = new HashMap<>();
    // for (coord coord : cells.keySet()) {
    // if (cells.get(coord).getClass() == entityClass
    // || cells.get(coord).getClass().getSuperclass() == entityClass) {
    // entities.put(coord, cells.get(coord));
    // }
    // }

    // return entities;
    // }

    public List<Coord> getLocationsByType(Class entityClass) {
        List<Coord> entities = new ArrayList<>();
        for (Coord coord : cells.keySet()) {
            if (entityClass.isInstance(get(coord)))
                entities.add(coord);
        }

        return entities;
    }

    public Entity put(Coord coord, Entity entity) {
        return cells.put(coord, entity);
    }

    // return null if the key does not exist
    public Entity putIfAbsent(Coord coord, Entity entity) {
        return cells.putIfAbsent(coord, entity);
    }

    public Entity remove(Coord coord) {
        return cells.remove(coord);
    }

    public void move(Coord from, Coord to) {
        Entity entity = get(from);
        remove(from);
        put(to, entity);
    }

    public boolean checkBoundaries(Coord coord) {
        return coord.x >= 0 && coord.x < width && coord.y >= 0 && coord.y < height;
    }

    // find the path to the closest entity with type of entityClass. Use BFS algo
    public List<Coord> findPathToClosestEntity(Coord start, Class entityClass) {
        class Cell {
            Coord previos_cell_coord;
            int cost;

            Cell(Coord coord, int cost) {
                this.previos_cell_coord = coord;
                this.cost = cost;
            }
        }
        Map<Coord, Cell> visited = new HashMap<>();
        Queue<Coord> q = new LinkedList<>();
        visited.put(start, new Cell(null, 0));
        q.add(start);
        Coord finish = null;
        while (!q.isEmpty()) {
            finish = q.poll();
            if (entityClass.isInstance(get(finish)))
                break;
            for (Coord neighbour : finish.getNeighbours()) {
                if (checkBoundaries(neighbour) && !visited.containsKey(neighbour)
                        && (get(neighbour) == null || entityClass.isInstance(get(neighbour)))) {
                    visited.put(neighbour, new Cell(finish, visited.get(finish).cost + 1));
                    q.add(neighbour);
                }
            }
        }
        // Find the path
        Coord cur = finish;
        LinkedList<Coord> path = new LinkedList<>();
        while (cur != null) {
            // skip start and finish coords
            if (!cur.equals(start))
                path.addFirst(cur);
            cur = visited.get(cur).previos_cell_coord;
        }

        return path;
    }

}
