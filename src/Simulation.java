import java.util.List;
import java.util.Random;

public class Simulation {

    final int worldHeight;
    final int worldWidth;
    private WorldMap map;
    private int tickCounter = 0;

    Simulation(Config config) {
        this.worldHeight = config.worldHeight;
        this.worldWidth = config.worldWidth;
        map = new WorldMap(worldHeight, worldWidth);
    }

    public void start() throws Exception {
        accomodateWorld();
        while (true) {
            tickCounter++;
            Thread.sleep(2000);

            // spawn grass each n tick
            if (tickCounter % 5 == 0) {
                Random r = new Random();
                map.putIfAbsent(new Coord(r.nextInt(worldWidth), r.nextInt(worldHeight)), new Grass());
            }

            printWorld();

            int predatorsCount = 0;
            int herbivoresCount = 0;
            List<Coord> creatureLocations = map.getLocationsByType(Creature.class);
            // do actions
            for (Coord loc : creatureLocations) {
                // during doing actions some creature may be eaten
                if (map.get(loc) == null)
                    continue;
                if (map.get(loc).getClass() == Predator.class)
                    predatorsCount++;
                if (map.get(loc).getClass() == Herbivore.class)
                    herbivoresCount++;
                doAction(loc);
            }

            // check condition for ending the simulation
            if (predatorsCount == 0 || herbivoresCount == 0)
                break;
        }
    }

    private void accomodateWorld() {
        // TODO: implement getting all values from config
        Random r = new Random();
        for (byte i = 0; i < 10; i++) {
            map.putIfAbsent(new Coord(r.nextInt(worldWidth), r.nextInt(worldHeight)), new Grass());
            map.putIfAbsent(new Coord(r.nextInt(worldWidth), r.nextInt(worldHeight)), new Rock());
        }
        for (byte i = 0; i < 2; i++) {
            map.putIfAbsent(new Coord(r.nextInt(worldWidth), r.nextInt(worldHeight)), new Predator());
        }
        for (byte i = 0; i < 6; i++) {
            map.putIfAbsent(new Coord(r.nextInt(worldWidth), r.nextInt(worldHeight)), new Herbivore());
        }
    }

    private void printWorld() {
        for (int y = 0; y < worldHeight; y++) {
            System.out.print('.');
            for (int x = 0; x < worldWidth; x++) {
                Entity entity = map.get(new Coord(x, y));
                if (entity != null) {
                    System.out.print(entity.sign);
                } else {
                    System.out.print(" ");
                }
                System.out.print('.');
            }
            System.out.println("");
        }
        System.out.println();
    }

    private void doAction(Coord creatureLocation) throws Exception {
        Creature creature = (Creature) map.get(creatureLocation);
        List<Coord> foodLocationPath = map.findPathToClosestEntity(creatureLocation, creature.food);
        // System.out.print(creature);
        // System.out.println(foodLocationPath);
        if (foodLocationPath.size() == 1) {
            creature.eat(map.get(foodLocationPath.get(0)));
            map.remove(foodLocationPath.get(0));
        } else {
            creature.move();
            if (creature.healthPoints >= 0)
                if (creature.speed > foodLocationPath.size() - 1)
                    // move to the lastest cell before the food cell
                    map.move(creatureLocation, foodLocationPath.get(foodLocationPath.size() - 1));
                else
                    map.move(creatureLocation, foodLocationPath.get(creature.speed - 1));
            else
                map.remove(creatureLocation);
        }
    }

}
