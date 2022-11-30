import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fairy implements Moveable{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double actionPeriod;
    private final double animationPeriod;

    public Fairy(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(position, new ArrayList<>(List.of(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getEntityPos();

            if (moveTo(world, fairyTarget.get(), scheduler)) {

                Sapling sapling = world.createSapling(Functions.SAPLING_KEY + "_" + fairyTarget.get().getEntityID(),
                        tgtPos, imageStore.getImageList(imageStore, Functions.SAPLING_KEY), 0);

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
    }

    public boolean _targetReached(WorldModel world, Entity target, EventScheduler scheduler) {
        world.removeEntity(scheduler, target);
        return true;
    }

    public boolean _stumpCheck(WorldModel world, Point destPos) {
        return true;
    }

    public double getActionPeriod() { return actionPeriod; }
    public double getAnimationPeriod() {return this.animationPeriod;}
    public void setImgIdx(int value) {imageIndex = value;}
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
}
