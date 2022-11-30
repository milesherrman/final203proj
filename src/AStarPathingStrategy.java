import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.math.*;

class AStarPathingStrategy implements PathingStrategy
{
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        Map<Point, Node> openListMap = new HashMap<>();
        PriorityQueue<Node> openList = new PriorityQueue<>((n1,n2) -> n1.getF() - n2.getF());
        Map<Point, Node> closedList = new HashMap<>();
        List<Point> path = new ArrayList<>();

        //current node
        Node current = new Node(start);
        //set g and h of current node
        current.setG(0);
        current.setH(Math.abs(current.getPosition().x - end.x) + Math.abs(current.getPosition().y - end.y));
        current.setF();
        //add current node to open list
        openList.add(current);
        openListMap.put(start, current);

        while(openList.size() > 0){
            current = openList.poll();
            if(withinReach.test(current.getPosition(), end)){
                //add points to path
                while(current.getPrevious() != null){
                    path.add(0, current.getPosition());
                    current = current.getPrevious();
                }
                return path;
            }

            int gVal;
            //loop through valid neighbors
            for (Point move : potentialNeighbors.apply(current.getPosition()).filter(canPassThrough).filter(pt -> !closedList.containsKey(pt)).collect(Collectors.toList())) {
                //g value of move is current g value + 1
                gVal = current.getG() + 1;
                Node nextMove = new Node(move);
                nextMove.setG(gVal);
                //calculate h value for next move
                nextMove.setH(Math.abs(nextMove.getPosition().x - end.x) + Math.abs(nextMove.getPosition().y - end.y));
                //calculate and set f value of next move
                nextMove.setF();
                //set current as previous node
                nextMove.setPrevious(current);
                //check if move is already on open list
                if (openListMap.containsKey(move)) {
                    //if so, compare g value
                    if (gVal < openListMap.get(move).getG()) {
                        //if g value is better, replace previous g value
                        openListMap.replace(move, nextMove);
                        openList.remove(openListMap.get(move));
                        openList.add(nextMove);
                    }
                } else {
                    //add node to open list
                    openList.add(nextMove);
                    openListMap.put(move, nextMove);
                }
            }
            //move current node to closed list
            closedList.put(current.getPosition(), current);
            // remove current from open list
            openList.remove(current);
        }
        return path;
    }
}
