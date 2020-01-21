import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.*;


// The AI that takes a very suboptimal path and by appling A* and heuristics.
/*
 * 
 * 
 * 
 */
public class myAI implements AIModule
{

    //Heuristic for getting the h(n) sources: http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html

    /*
    Diagonal distance
        function heuristic(node) =
        dx = abs(node.x - goal.x)
        dy = abs(node.y - goal.y)
        return D * (dx + dy) + (D2 - 2 * D) * min(dx, dy)
    */

    /*  Euclidean distance
        function heuristic(node) =
        dx = abs(node.x - goal.x)
        dy = abs(node.y - goal.y)
        return D * sqrt(dx * dx + dy * dy)
    */

    /*  Breaking ties
        dx1 = current.x - goal.x
        dy1 = current.y - goal.y
        dx2 = start.x - goal.x
        dy2 = start.y - goal.y
        cross = abs(dx1*dy2 - dx2*dy1)
        heuristic += cross*0.001
    */
    
    /*  The Manhattan Distance
        function heuristic(node) =
        dx = abs(node.x - goal.x)
        dy = abs(node.y - goal.y)
        return D * (dx + dy)
    */
    private double getHeuristic(final TerrainMap map, final Point pt1, final Point pt2)
    {
            return Math.abs(pt1.x - pt2.x) + Math.abs(pt1.y - pt2.y);
    }
    
    public Point getMin(ArrayList<Point> open, HashMap<Point, Double> score){
        double min = 99999999;
        Point minPoint = null;
        for(Point p : open){
            if(score.get(p) < min){
                min = score.get(p);
                minPoint = p;
            }
        }
        return minPoint;
    }

    public List<Point> createPath(final TerrainMap map)
    {
        // // Holds the resulting path
        final ArrayList<Point> path = new ArrayList<Point>();
        final ArrayList<Point> closed = new ArrayList<Point>();
        final ArrayList<Point> open = new ArrayList<Point>();
        HashMap<Point, Point> hashPath = new HashMap<Point, Point>();
        HashMap<Point, Double> gx = new HashMap<Point, Double>();
        HashMap<Point, Double> hx = new HashMap<Point, Double>();
        HashMap<Point, Double> fx = new HashMap<Point, Double>();
        Point start = map.getStartPoint();
        Point goal = map.getEndPoint();
        open.add(start);
        gx.put(start, 0.0);
        hx.put(start, getHeuristic(map, start, goal));
        fx.put(start, gx.get(start) + hx.get(start));

        while (!open.isEmpty()) {
            Point curPoint = getMin(open, fx);
            if (curPoint.equals(goal)) {
                break;
            }
            open.remove(curPoint);
            closed.add(curPoint);

            for (Point neighbor : map.getNeighbors(curPoint)) {
                if (closed.contains(neighbor)) {
                    continue;
                }
                double temp = gx.get(curPoint) + map.getCost(curPoint, neighbor);
                if (!open.contains(neighbor) || temp < gx.get(neighbor)) {
                    hashPath.put(neighbor, curPoint);
                    gx.put(neighbor, temp);
                    hx.put(neighbor, getHeuristic(map, neighbor, goal));
                    fx.put(neighbor, gx.get(neighbor) + hx.get(neighbor));
                    if (!open.contains(neighbor)) {
                        open.add(neighbor);
                    }
                }
            }
        }
        Point current = goal;
        while (hashPath.containsKey(current)) {
                path.add(current);
                current = hashPath.get(current);
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }
}

