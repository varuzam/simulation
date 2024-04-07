
public class VisualizerConsole extends Visualizer {

    public void printWorld(WorldMap map) {
        for (int y = 0; y < map.height; y++) {
            System.out.print('.');
            for (int x = 0; x < map.width; x++) {
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

}
