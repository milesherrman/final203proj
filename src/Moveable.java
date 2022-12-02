import java.util.List;

public interface Moveable extends Executable{
    default boolean moveTo(WorldModel world, Point target, EventScheduler scheduler) {

        if (Functions.adjacent(this.getEntityPos(), target)) {
            return _targetReached(world, target, scheduler);
            }
        else {
            Point nextPos = nextPosition(world, target);

            if (!this.getEntityPos().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    default Point nextPosition(WorldModel world, Point destPos) {
        Point newPos = new Point(this.getEntityPos().getX(), this.getEntityPos().getY());
        //SingleStepPathingStrategy path = new SingleStepPathingStrategy();
        AStarPathingStrategy path = new AStarPathingStrategy();
        List<Point> potentialMoves = path.computePath(this.getEntityPos(),
                destPos, (x) -> world.withinBounds(x) && !(world.isOccupied(x) && _stumpCheck(world, x)),
                Point::isAdjacent, PathingStrategy.CARDINAL_NEIGHBORS);

        if (potentialMoves.size() != 0){newPos = potentialMoves.get(0);}
        return newPos;
    }

    boolean _targetReached(WorldModel world, Point target, EventScheduler scheduler);
    boolean _stumpCheck(WorldModel world, Point newPos);
}
