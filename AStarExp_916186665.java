import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;

public class AStarExp_916186665 implements AIModule {

    private double getHeuristic(final TerrainMap map, final Point pt1, final Point pt2) {
        double curHeight = map.getTile(pt1);
        double goalHeight = map.getTile(pt2);
        double hdiff = goalHeight - curHeight;
        double chebDist = Math.max(Math.abs(pt1.x - pt2.x), Math.abs(pt1.y - pt2.y));
        if (hdiff > 0) {
            if (hdiff > chebDist) {
                return 2 * (chebDist - 1) + Math.pow(2.0, hdiff);
            } else {
                return 2 * hdiff + Math.abs((chebDist - hdiff));
            }
        } else {
            if (Math.abs(hdiff) > chebDist) {
                return 0;
            } else {
                return Math.abs((chebDist - 0.5 * Math.abs(hdiff)));
            }
        }
    }

    public List<Point> createPath(final TerrainMap map) {
        final ArrayList<Point> path = new ArrayList<Point>();
        HashMap<Point, Point> hashPath = new HashMap<Point, Point>();
        HashMap<Point, Double> fx = new HashMap<Point, Double>();
        Point start = map.getStartPoint();
        Point goal = map.getEndPoint();
        PriorityQueue<Point> openList = new PriorityQueue<Point>(new Comparator<Point>() {
            public int compare(Point pt1, Point pt2) {
                double fx1 = fx.get(pt1) + getHeuristic(map, pt1, goal);
                double fx2 = fx.get(pt2) + getHeuristic(map, pt2, goal);
                if (fx1 > fx2)
                    return 1;
                else if (fx1 < fx2)
                    return -1;
                else
                    return 0;
            }
        });
        fx.put(start, 0.0);
        openList.add(start);
        while (!openList.isEmpty()) {
            Point cur = openList.remove();
            if (cur.equals(goal)) {
                break;
            }
            Point[] successor = map.getNeighbors(cur);
            for (int i = 0; i < successor.length; i++) {
                double temp = fx.get(cur) + map.getCost(cur, successor[i]);
                if (!fx.containsKey(successor[i]) || temp < fx.get(successor[i])) {
                    fx.put(successor[i], temp);
                    openList.add(successor[i]);
                    hashPath.put(successor[i], cur);
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