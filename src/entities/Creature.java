package entities;

abstract public class Creature extends Entity {
    public int speed;
    public int healthPoints = 100;
    public Class food;
    protected short starvationCounter;
    private static final Exception WrongFoodException = null;

    public void eat(Entity entity) throws Exception {
        if (entity.getClass() == food) {
            // TODO магические числа, вынести в константы с понятными названиями
            healthPoints = (healthPoints > 80 && healthPoints <= 100) ? 100 : healthPoints + 20;
            starvationCounter = 0;
        } else {
            // TODO во первых, тут null, во вторых - надо кидать new, иначе будет
            // некорректный stacktrace
            throw WrongFoodException;
        }
    }

    public void move() {
        // each 5th moves without food decrease healthPoints
        starvationCounter++;
        // TODO магические числа
        // TODO if без {} писать не принято
        if (starvationCounter % 5 == 0)
            healthPoints -= 20;
    }

    public String toString() {
        return String.format("%s{sign=%s, speed=%d, health=%d}", getClass(), sign, speed, healthPoints);
    }
}
