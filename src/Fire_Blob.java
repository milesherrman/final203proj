import processing.core.PImage;
import java.util.random.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fire_Blob implements Moveable{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double actionPeriod;
    private final double animationPeriod;

    //Set initial move to invalid spot
    private Point randomMove = new Point(-1, -1);

    public Fire_Blob(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        //loop until a valid random point is generated
        while(!world.withinBounds(randomMove) || world.getOccupancyCell(randomMove) != null){
            randomMove = new Point(1 + (int)(Math.random() * 39), 1 + (int)(Math.random() * 29));
        }

        //if at calculated random point, create new fire, else, keep pathing
        if (moveTo(world, randomMove, scheduler)) {
            //Creates a new fire mob at random (valid) point in the world
            //Will change later to create immobile fire entity
            Fire_Blob fire_blob = world.createFire_Blob(Functions.FIRE_BLOB_KEY + "_", randomMove, 0.5 ,0.1, imageStore.getImageList(imageStore, Functions.FIRE_BLOB_KEY));
            world.addEntity(fire_blob);
            fire_blob.scheduleActions(scheduler, world, imageStore);
            randomMove = new Point(-1,-1);
        }
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
    }

    public boolean _targetReached(WorldModel world, Point target, EventScheduler scheduler) {
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
