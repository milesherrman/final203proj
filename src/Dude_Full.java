import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dude_Full implements NormalDude{
        private final String id;
        private Point position;
        private final List<PImage> images;
        private int imageIndex;
        private final int resourceLimit;
        private int resourceCount;
        private final double actionPeriod;
        private final double animationPeriod;


        public Dude_Full(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod) {
            this.id = id;
            this.position = position;
            this.images = images;
            this.imageIndex = 0;
            this.resourceLimit = resourceLimit;
            this.resourceCount = resourceCount;
            this.actionPeriod = actionPeriod;
            this.animationPeriod = animationPeriod;
        }

        public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
            Dude_Not_Full dude = world.createDudeNotFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit, this.images);
            world.removeEntity(scheduler, this);
            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);
        }

        public void transformDude_Fire(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Dude_Fire dude = world.createDude_Fire(this.id, this.position, this.actionPeriod, this.animationPeriod, this.images);
        world.removeEntity(scheduler, this);
        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);
        }

        public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(position, new ArrayList<>(List.of(House.class)));

        if (fullTarget.isPresent() && moveTo(world, fullTarget.get().getEntityPos(), scheduler)) {
            transformFull(world, scheduler, imageStore);
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