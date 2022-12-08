import processing.core.PImage;

import java.util.List;

public class FireStation implements Entity{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public FireStation(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public String getEntityID(){return this.id;}
    public Point getEntityPos(){return this.position;}
    public void setEntityPos(Point point){this.position = point;}
    public List<PImage> getEntityImage(){return this.images;}
    public int getEntityImgIdx(){return this.imageIndex;}
}
