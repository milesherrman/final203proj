import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dude_Fire implements Moveable{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double actionPeriod;
    private final double animationPeriod;

    public Dude_Fire(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fireTarget = world.findNearest(position, new ArrayList<>(List.of(Fire.class)));

        if (fireTarget.isPresent() && moveTo(world, fireTarget.get().getEntityPos(), scheduler)) {
            world.removeEntity(scheduler, fireTarget.get());
        } else {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
        }
    }
    public double getActionPeriod() { return actionPeriod; }
    public void setImgIdx(int value) {imageIndex = value;}
    public double getAnimationPeriod() { return this.animationPeriod; }
    public String getEntityID(){ return this.id; }
    public Point getEntityPos(){return this.position;}
    public void setEntityPos(Point point){this.position = point;}
    public List<PImage> getEntityImage(){return this.images;}
    public int getEntityImgIdx(){return this.imageIndex;}
    public boolean _targetReached(WorldModel world, Point target, EventScheduler scheduler) {
        return true;
    }
    public boolean _stumpCheck(WorldModel world, Point newPos) {
        return world.getOccupancyCell(newPos).getClass() != Stump.class;
    }
}
