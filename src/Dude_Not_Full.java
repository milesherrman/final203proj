import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Dude_Not_Full implements NormalDude{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int resourceLimit;
    private int resourceCount;
    private final double actionPeriod;
    private final double animationPeriod;
    private boolean is_Dude_Fire = false;

    public Dude_Not_Full(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            Dude_Full dude = world.createDudeFull(id, position, actionPeriod, animationPeriod, resourceLimit, images);

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);
            return true;
        }
        return false;
    }

    public void set_Dude_Fire() {
        is_Dude_Fire = true;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if(is_Dude_Fire){
            pathToHouse(world, imageStore, scheduler);
        }
        else {
            Optional<Entity> target = world.findNearest(position, new ArrayList<>(List.of(Tree.class, Sapling.class)));

            if (target.isEmpty() || !moveTo(world, target.get().getEntityPos(), scheduler) || !transformNotFull(world, scheduler, imageStore)) {
                scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
            }
        }
    }

    public void pathToHouse(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        Optional<Entity> houseTarget = world.findNearest(position, new ArrayList<>(List.of(House.class)));

        if (is_Dude_Fire && houseTarget.isPresent() && moveTo(world, houseTarget.get().getEntityPos(), scheduler)){
            Dude_Fire dude = world.createDude_Fire(this.id, this.position, 0.6, 0.3, imageStore.getImageList(imageStore, Functions.DUDE_FIRE_KEY));
            world.removeEntity(scheduler, this);
            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);
        }
        else{
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
        }
    }

    public double getActionPeriod() { return actionPeriod; }
    public void setImgIdx(int value) {imageIndex = value;}
    public double getAnimationPeriod() {return this.animationPeriod;}
    public String getEntityID(){
        return this.id;
    }
    public Point getEntityPos(){
        return this.position;
    }
    public void setEntityPos(Point point){
        this.position = point;
    }
    public List<PImage> getEntityImage(){
        return this.images;
    }
    public int getEntityImgIdx(){
        return this.imageIndex;
    }
    public boolean _targetReached(WorldModel world, Point target, EventScheduler scheduler) {
        if(!is_Dude_Fire) {
            resourceCount += 1;
            Entity plant = world.getOccupancyCell(target);
            ((Plant) plant).changeHealth(-1);
        }
        return true;
    }
    public boolean _stumpCheck(WorldModel world, Point newPos) {
        return world.getOccupancyCell(newPos).getClass() != Stump.class;
    }
}

