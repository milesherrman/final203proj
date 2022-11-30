import java.util.*;

import processing.core.PImage;

public interface Entity {
    default String log() {
        return this.getEntityID().isEmpty() ? null : String.format("%s %d %d %d", this.getEntityID(),
                this.getEntityPos().getX(), this.getEntityPos().getY(), this.getEntityImgIdx());
    }
    default PImage getCurrentImage(){return getEntityImage().get(getEntityImgIdx() % getEntityImage().size());}
    String getEntityID();
    Point getEntityPos();
    void setEntityPos(Point point);
    List<PImage> getEntityImage();
    int getEntityImgIdx();
}
