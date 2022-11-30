import processing.core.PImage;

import java.nio.ByteOrder;
import java.util.List;

public class Sapling implements Plant{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double actionPeriod;
    private final double animationPeriod;
    private int health;
    private final int healthLimit;

    public Sapling(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public boolean transformSapling(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Stump stump = world.createStump(Functions.STUMP_KEY + "_" + this.id, this.position,
                    imageStore.getImageList(imageStore, Functions.STUMP_KEY));
            world.removeEntity(scheduler, this);
            world.addEntity(stump);

            return true;
        } else if (this.health >= this.healthLimit) {
            Tree tree = world.createTree(Functions.TREE_KEY + "_" + this.id, this.position,
                    Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Functions.getIntFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN),
                    imageStore.getImageList(imageStore, Functions.TREE_KEY));
            world.removeEntity(scheduler, this);
            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        return false;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        health += 1;
        if (!transformSapling(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
        }
    }
    public void setImgIdx(int value) {imageIndex = value;}
    public double getAnimationPeriod() {return this.animationPeriod;}
    public String getEntityID(){return this.id;}
    public Point getEntityPos(){return this.position;}
    public void setEntityPos(Point point){this.position = point;}
    public List<PImage> getEntityImage(){return this.images;}
    public int getEntityImgIdx(){return this.imageIndex;}
    public double getActionPeriod() { return actionPeriod; }
    public void changeHealth(int add) {health += add;}

}
