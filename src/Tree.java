import processing.core.PImage;

import java.util.List;

public class Tree implements Plant{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double actionPeriod;
    private final double animationPeriod;
    private int health;

    public Tree(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
    }

    public boolean transformTree(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Stump stump = world.createStump(Functions.STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList(imageStore, Functions.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }
        return false;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (!transformTree(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
        }
    }

    public double getActionPeriod() { return actionPeriod; }
    public double getAnimationPeriod() {return this.animationPeriod;}
    public void setImgIdx(int value) {imageIndex = value;}
    public String getEntityID(){ return this.id;}
    public Point getEntityPos(){return this.position;}
    public void setEntityPos(Point point){this.position = point;}
    public List<PImage> getEntityImage(){return this.images;}
    public int getEntityImgIdx(){return this.imageIndex;}
    public void changeHealth(int add) {health += add;}

}

