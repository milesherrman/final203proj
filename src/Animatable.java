public interface Animatable extends Entity{
    default Action createAnimationAction(int repeatCount){return new Animation(this, repeatCount);}
    default void nextImage(){setImgIdx(getEntityImgIdx() + 1);}
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    double getAnimationPeriod();
    void setImgIdx(int value);
}


