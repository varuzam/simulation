import java.util.List;
import java.util.Random;

import entities.*;

public class Simulation {

    private WorldMap map;
    private int tickCounter = 0;
    private Visualizer visualizer;

    Simulation(Config config) throws Exception {
        map = new WorldMap(config.worldHeight, config.worldWidth);
        visualizer = (Visualizer) config.visualizerClass.getConstructor().newInstance();
    }

    public void start() throws Exception {
        accomodateWorld();
        while (true) {
            tickCounter++;
            Thread.sleep(2000);

            // spawn grass each n tick
            if (tickCounter % 5 == 0) {
                Random r = new Random();
                map.putIfAbsent(new Coord(r.nextInt(map.width), r.nextInt(map.height)), new Grass());
            }

            visualizer.printWorld(map);

            int predatorsCount = 0;
            int herbivoresCount = 0;
            // TODO кажется, что карта должна иметь множество индексов, а не одну
            // мапу, которая на каждый тик итерируется
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
            map.putIfAbsent(new Coord(r.nextInt(map.width), r.nextInt(map.height)), new Grass());
            map.putIfAbsent(new Coord(r.nextInt(map.width), r.nextInt(map.height)), new Rock());
        }
        for (byte i = 0; i < 2; i++) {
            map.putIfAbsent(new Coord(r.nextInt(map.width), r.nextInt(map.height)), new Predator());
        }
        for (byte i = 0; i < 6; i++) {
            map.putIfAbsent(new Coord(r.nextInt(map.width), r.nextInt(map.height)), new Herbivore());
        }
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
            if (creature.healthPoints >= 0) {
                int stepsCount = Math.min(creature.speed, foodLocationPath.size() - 1);
                // eventually it will has moved to the lastest cell before the food cell
                map.move(creatureLocation, foodLocationPath.get(stepsCount - 1));
            } else {
                map.remove(creatureLocation);
            }
        }
    }

}
