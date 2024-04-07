abstract class Creature extends Entity {
    public int speed;
    public int healthPoints = 100;
    public Object food;
    protected short starvationCounter;
    private static final Exception WrongFoodException = null;

    public void eat(Entity entity) throws Exception {
        if (entity.getClass() == food) {
            healthPoints = (healthPoints > 80 && healthPoints <= 100) ? 100 : healthPoints + 20;
            starvationCounter = 0;
        } else {
            throw WrongFoodException;
        }
    }

    public void move() {
        // each 5th moves without food decrease healthPoints
        starvationCounter++;
        if (starvationCounter % 5 == 0)
            healthPoints -= 20;
    }

    public String toString() {
        return String.format("%s{sign=%s, speed=%d, health=%d}", getClass(), sign, speed, healthPoints);
    }
}
