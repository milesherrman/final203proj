import processing.core.PImage;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    private int numRows;
    private int numCols;
    private Background[][] background;
    private Entity[][] occupancy;
    private Set<Entity> entities;

    public WorldModel() {

    }

    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getEntityPos())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }

    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);
            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setEntityPos(new Point(-1, -1));
            entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public Background getBackgroundCell(Point pos) {
        return background[pos.getY()][pos.getX()];
    }

    public void setBackgroundCell(Point pos, Background background) {
        this.background[pos.getY()][pos.getX()] = background;
    }

    /**
     * Helper method for testing. Don't move or modify this method.
     */
    public List<String> log(){
        List<String> list = new ArrayList<>();
        for (Entity entity : entities) {
            String log = entity.log();
            if(log != null) list.add(log);
        }
        return list;
    }

    public boolean withinBounds(Point pos) {
        return pos.getY() >= 0 && pos.getY() < numRows && pos.getX() >= 0 && pos.getX() < numCols;
    }

    public void addEntity(Entity entity) {
        if (withinBounds(entity.getEntityPos())) {
            setOccupancyCell(entity.getEntityPos(), entity);
            entities.add(entity);
        }
    }

    public void parseSapling(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.SAPLING_NUM_PROPERTIES) {
            int health = Integer.parseInt(properties[Functions.SAPLING_HEALTH]);
            Entity entity = createSapling(id, pt, imageStore.getImageList(imageStore, Functions.SAPLING_KEY), health);
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.SAPLING_KEY, Functions.SAPLING_NUM_PROPERTIES));
        }
    }

    public void parseDude(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.DUDE_NUM_PROPERTIES) {
            Entity entity = createDudeNotFull(id, pt, Double.parseDouble(properties[Functions.DUDE_ACTION_PERIOD]), Double.parseDouble(properties[Functions.DUDE_ANIMATION_PERIOD]), Integer.parseInt(properties[Functions.DUDE_LIMIT]), imageStore.getImageList(imageStore, Functions.DUDE_KEY));
            tryAddEntity(entity);
        }
        else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.DUDE_KEY, Functions.DUDE_NUM_PROPERTIES));
        }
    }

    public void parseFairy(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.FAIRY_NUM_PROPERTIES) {
            Entity entity = createFairy(id, pt, Double.parseDouble(properties[Functions.FAIRY_ACTION_PERIOD]), Double.parseDouble(properties[Functions.FAIRY_ANIMATION_PERIOD]), imageStore.getImageList(imageStore, Functions.FAIRY_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.FAIRY_KEY, Functions.FAIRY_NUM_PROPERTIES));
        }
    }

    public void parseFire_Blob(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.FIRE_BLOB_NUM_PROPERTIES) {
            Entity entity = createFire_Blob(id, pt, Double.parseDouble(properties[Functions.FIRE_BLOB_ACTION_PERIOD]), Double.parseDouble(properties[Functions.FIRE_BLOB_ANIMATION_PERIOD]), imageStore.getImageList(imageStore, Functions.FIRE_BLOB_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.FIRE_BLOB_KEY, Functions.FIRE_BLOB_NUM_PROPERTIES));
        }
    }

    public void parseFire(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.FIRE_NUM_PROPERTIES) {
            Entity entity = createObstacle(id, pt, Double.parseDouble(properties[Functions.FIRE_ANIMATION_PERIOD]), imageStore.getImageList(imageStore, Functions.FIRE_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.FIRE_KEY, Functions.FIRE_NUM_PROPERTIES));
        }
    }

    public void parseTree(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.TREE_NUM_PROPERTIES) {
            Entity entity = createTree(id, pt, Double.parseDouble(properties[Functions.TREE_ACTION_PERIOD]), Double.parseDouble(properties[Functions.TREE_ANIMATION_PERIOD]), Integer.parseInt(properties[Functions.TREE_HEALTH]), imageStore.getImageList(imageStore, Functions.TREE_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.TREE_KEY, Functions.TREE_NUM_PROPERTIES));
        }
    }

    public void parseObstacle(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.OBSTACLE_NUM_PROPERTIES) {
            Entity entity = createObstacle(id, pt, Double.parseDouble(properties[Functions.OBSTACLE_ANIMATION_PERIOD]), imageStore.getImageList(imageStore, Functions.OBSTACLE_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.OBSTACLE_KEY, Functions.OBSTACLE_NUM_PROPERTIES));
        }
    }

    public void parseHouse(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.HOUSE_NUM_PROPERTIES) {
            Entity entity = createHouse(id, pt, imageStore.getImageList(imageStore, Functions.HOUSE_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.HOUSE_KEY, Functions.HOUSE_NUM_PROPERTIES));
        }
    }
    public void parseStump(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.STUMP_NUM_PROPERTIES) {
            Entity entity = createStump(id, pt, imageStore.getImageList(imageStore, Functions.STUMP_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.STUMP_KEY, Functions.STUMP_NUM_PROPERTIES));
        }
    }
    public Entity getOccupancyCell(Point pos) {
        return occupancy[pos.getY()][pos.getX()];
    }

    public void setOccupancyCell(Point pos, Entity entity) {
        occupancy[pos.getY()][pos.getX()] = entity;
    }

    public void parseSaveFile(Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        String lastHeader = "";
        int headerLine = 0;
        int lineCounter = 0;
        while(saveFile.hasNextLine()){
            lineCounter++;
            String line = saveFile.nextLine().strip();
            if(line.endsWith(":")){
                headerLine = lineCounter;
                lastHeader = line;
                switch (line){
                    case "Backgrounds:" -> background = new Background[numRows][numCols];
                    case "Entities:" -> {
                        occupancy = new Entity[numRows][numCols];
                        entities = new HashSet<>();
                    }
                }
            }else{
                switch (lastHeader){
                    case "Rows:" -> numRows = Integer.parseInt(line);
                    case "Cols:" -> numCols = Integer.parseInt(line);
                    case "Backgrounds:" -> parseBackgroundRow(line, lineCounter-headerLine-1, imageStore);
                    case "Entities:" -> parseEntity(line, imageStore);
                }
            }
        }
    }
    public void parseBackgroundRow(String line, int row, ImageStore imageStore) {
        String[] cells = line.split(" ");
        if(row < numRows){
            int rows = Math.min(cells.length, numCols);
            for (int col = 0; col < rows; col++){
                background[row][col] = new Background(cells[col], imageStore.getImageList(imageStore, cells[col]));
            }
        }
    }
    public void parseEntity(String line, ImageStore imageStore) {
        String[] properties = line.split(" ", Functions.ENTITY_NUM_PROPERTIES + 1);
        if (properties.length >= Functions.ENTITY_NUM_PROPERTIES) {
            String key = properties[Functions.PROPERTY_KEY];
            String id = properties[Functions.PROPERTY_ID];
            Point pt = new Point(Integer.parseInt(properties[Functions.PROPERTY_COL]), Integer.parseInt(properties[Functions.PROPERTY_ROW]));

            properties = properties.length == Functions.ENTITY_NUM_PROPERTIES ?
                    new String[0] : properties[Functions.ENTITY_NUM_PROPERTIES].split(" ");

            switch (key) {
                case Functions.OBSTACLE_KEY -> parseObstacle(properties, pt, id, imageStore);
                case Functions.DUDE_KEY -> parseDude(properties, pt, id, imageStore);
                case Functions.FAIRY_KEY -> parseFairy(properties, pt, id, imageStore);
                case Functions.HOUSE_KEY -> parseHouse(properties, pt, id, imageStore);
                case Functions.TREE_KEY -> parseTree(properties, pt, id, imageStore);
                case Functions.SAPLING_KEY -> parseSapling(properties, pt, id, imageStore);
                case Functions.STUMP_KEY -> parseStump(properties, pt, id, imageStore);
                case Functions.FIRE_BLOB_KEY -> parseFire_Blob(properties, pt, id, imageStore);
                case Functions.FIRE_KEY -> parseFire(properties, pt, id, imageStore);
                default -> throw new IllegalArgumentException("Entity key is unknown");
            }
        }else{
            throw new IllegalArgumentException("Entity must be formatted as [key] [id] [x] [y] ...");
        }
    }
    public Optional<Entity> findNearest(Point pos, List<Class> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind : kinds) {
            for (Entity entity : entities) {
                if (entity.getClass() == kind) {
                    ofType.add(entity);
                }
            }
        }
        return nearestEntity(ofType, pos);
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = Functions.distanceSquared(nearest.getEntityPos(), pos);

            for (Entity other : entities) {
                int otherDistance = Functions.distanceSquared(other.getEntityPos(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }
            return Optional.of(nearest);
        }
    }

    public House createHouse(String id, Point position, List<PImage> images) {
        return new House(id, position, images);
    }

    public Obstacle createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        return new Obstacle(id, position, images, animationPeriod);
    }

    public Tree createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Tree(id, position, images, actionPeriod, animationPeriod, health);
    }

    public Stump createStump(String id, Point position, List<PImage> images) {
        return new Stump(id, position, images);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public Sapling createSapling(String id, Point position, List<PImage> images, int health) {
        return new Sapling(id, position, images, Functions.SAPLING_ACTION_ANIMATION_PERIOD, Functions.SAPLING_ACTION_ANIMATION_PERIOD, 0, Functions.SAPLING_HEALTH_LIMIT);
    }

    public Fairy createFairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Fairy(id, position, images, actionPeriod, animationPeriod);
    }

    public Fire_Blob createFire_Blob(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Fire_Blob(id, position, images, actionPeriod, animationPeriod);
    }

    public Fire createFire(String id, Point position, double animationPeriod, List<PImage> images) {
        return new Fire(id, position, images, animationPeriod);
    }

    // need resource count, though it always starts at 0
    public Dude_Not_Full createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new Dude_Not_Full(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
    }

    // don't technically need resource count ... full
    public Dude_Full createDudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new Dude_Full(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public Dude_Fire createDudeFire(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Dude_Fire(id, position, images, actionPeriod, animationPeriod);
    }

    public void load(Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        parseSaveFile(saveFile, imageStore, defaultBackground);
        if(background == null){
            background = new Background[numRows][numCols];
            for (Background[] row : background)
                Arrays.fill(row, defaultBackground);
        }
        if(occupancy == null){
            occupancy = new Entity[numRows][numCols];
            entities = new HashSet<>();
        }
    }

    public void removeEntity(EventScheduler scheduler, Entity entity) {
        scheduler.unscheduleAllEvents(entity);
        removeEntityAt(entity.getEntityPos());
    }

    public void moveEntity(EventScheduler scheduler, Entity entity, Point pos) {
        Point oldPos = entity.getEntityPos();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            Optional<Entity> occupant = getOccupant(pos);
            occupant.ifPresent(target -> removeEntity(scheduler, target));
            setOccupancyCell(pos, entity);
            entity.setEntityPos(pos);
        }
    }

    public Optional<PImage> getBackgroundImage(WorldModel world, Point pos) {
        if (world.withinBounds(pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        } else {
            return Optional.empty();
        }
    }

    public void worldChangingEvent(Point click, EventScheduler scheduler, ImageStore imageStore){
            //create central fire blob
            /*Fire_Blob fire_blob = createFire_Blob("", click, 0.3, 0.3, imageStore.getImageList(imageStore, Functions.FIRE_BLOB_KEY));
            addEntity(fire_blob);
            fire_blob.scheduleActions(scheduler, this, imageStore);*/
            //set clicked spot to burnt ground
            //setBackgroundCell(click, burntGrass);
            Fire fire = createFire("", click, 0.1, imageStore.getImageList(imageStore, Functions.FIRE_KEY));
            addEntity(fire);
            fire.scheduleActions(scheduler, this, imageStore);

            List<Point> nextFires = spreadFire(click, imageStore, scheduler);
            for(Point point: nextFires){
                spreadFire(point, imageStore, scheduler);
            }
            //create immobile fire entities at valid spots around the blob
    }

    public List<Point> spreadFire(Point previous, ImageStore imageStore, EventScheduler scheduler){
        List<Point> newFires = FIRE_CARDINAL_NEIGHBORS.apply(previous).collect(Collectors.toList());
        for (Point move : newFires){
            Fire fire = new Fire("", move, imageStore.getImageList(imageStore, Functions.FIRE_KEY), 0.1);
            addEntity(fire);
            fire.scheduleActions(scheduler, this, imageStore);
        }
        return newFires;
    }

     final Function<Point, Stream<Point>> FIRE_CARDINAL_NEIGHBORS =
            point -> Stream.<Point>builder()
                    .add(new Point(point.x, point.y - 1))
                    .add(new Point(point.x, point.y + 1))
                    .add(new Point(point.x - 1, point.y))
                    .add(new Point(point.x + 1, point.y))
                    .build().filter(pt -> withinBounds(pt) && !(isOccupied(pt)));


    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }
    public Set<Entity> getEntities() {
        return entities;
    }
}
