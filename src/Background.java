import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public final class Background {
    private final String id;
    private final List<PImage> images;
    private int imageIndex;

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public PImage getCurrentImage() {
        return getImages().get(getImageIndex());
    }


    public List<PImage> getImages(){
        return images;
    }
    public int getImageIndex(){
        return imageIndex;
    }
}
