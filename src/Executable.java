public interface Executable extends Animatable{
    default Action createActivityAction(WorldModel world, ImageStore imageStore){
        return new Activity(this, world, imageStore);
    }
    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    default void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, createAnimationAction(0), this.getAnimationPeriod());
    }

    double getActionPeriod();
}
