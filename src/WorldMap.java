import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class WorldMap {
    private int height;
    private int width;
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

    public List<Coord> getLocationsByType(Object entityClass) {
        List<Coord> entities = new ArrayList<>();
        for (Coord coord : cells.keySet()) {
            if (get(coord).getClass() == entityClass
                    || get(coord).getClass().getSuperclass() == entityClass) {
                entities.add(coord);
            }
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

    // find the closest entity with type of entityClass. Use BFS algo
    public Coord findClosestEntityCoord(Coord start, Object entityClass) {
        Map<Coord, Integer> visited = new HashMap<>(); // Map<coordinates, path length>
        Queue<Coord> q = new LinkedList<>();
        visited.put(start, 0);
        q.add(start);
        Coord p = null;
        while (!q.isEmpty()) {
            p = q.poll();
            if (cells.containsKey(p) && cells.get(p).getClass() == entityClass)
                break;
            List<Coord> neighbours = Arrays.asList(new Coord(p.x, p.y - 1), new Coord(p.x - 1, p.y),
                    new Coord(p.x + 1, p.y), new Coord(p.x, p.y + 1));
            for (Coord neighbour : neighbours) {
                if (neighbour.x >= 0 && neighbour.x < width && neighbour.y >= 0 && neighbour.y < height
                        && !visited.containsKey(neighbour)) {
                    visited.put(neighbour, visited.get(p) + 1);
                    q.add(neighbour);
                }
            }
        }
        return p;
    }

    public List<Coord> findPathToClosestEntity(Coord start, Object entityClass) {
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
            if (cells.containsKey(finish) && cells.get(finish).getClass() == entityClass)
                break;
            List<Coord> neighbours = Arrays.asList(
                    new Coord(finish.x, finish.y - 1),
                    new Coord(finish.x - 1, finish.y),
                    new Coord(finish.x + 1, finish.y),
                    new Coord(finish.x, finish.y + 1));
            for (Coord neighbour : neighbours) {
                if (neighbour.x >= 0 && neighbour.x < width && neighbour.y >= 0 && neighbour.y < height
                        && !visited.containsKey(neighbour)
                        && (get(neighbour) == null || get(neighbour).getClass() == entityClass)) {
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
