package entities;

public class Predator extends Creature {

    public Predator() {
        sign = "P";
        speed = 2;
        food = Herbivore.class;
    }

}
