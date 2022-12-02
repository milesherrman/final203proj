import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

public final class WorldView {
    private final PApplet screen;
    private final WorldModel world;
    private final int tileWidth;
    private final int tileHeight;
    private final Viewport viewport;

    public WorldView(int numRows, int numCols, PApplet screen, WorldModel world, int tileWidth, int tileHeight) {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }

    public void drawBackground(WorldView view) {
        for (int row = 0; row < view.viewport.getNumRows(); row++) {
            for (int col = 0; col < view.viewport.getNumCols(); col++) {
                Point worldPoint = viewport.viewportToWorld(col, row);
                Optional<PImage> image = world.getBackgroundImage(view.world, worldPoint);
                if (image.isPresent()) {
                    view.screen.image(image.get(), col * view.tileWidth, row * view.tileHeight);
                }
            }
        }
    }

    public void drawEntities(WorldView view) {
        for (Entity entity : view.world.getEntities()) {
            Point pos = entity.getEntityPos();

            if (viewport.contains(pos)) {
                Point viewPoint = viewport.worldToViewport(pos.getX(), pos.getY());
                view.screen.image(entity.getCurrentImage(), viewPoint.getX() * view.tileWidth, viewPoint.getY() * view.tileHeight);
            }
        }
    }

    public void shiftView(WorldView view, int colDelta, int rowDelta) {
        int newCol = Functions.clamp(view.viewport.getCol() + colDelta, 0, view.world.getNumCols() - view.viewport.getNumCols());
        int newRow = Functions.clamp(view.viewport.getRow() + rowDelta, 0, view.world.getNumRows() - view.viewport.getNumRows());

        viewport.shift(newCol, newRow);
    }

    public void drawViewport() {
        drawBackground(this);
        drawEntities(this);
    }

    public Viewport getViewport(){
        return this.viewport;
    }
}
