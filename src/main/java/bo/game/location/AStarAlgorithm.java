package bo.game.location;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AStarAlgorithm {
    public static List<Location> findShortestPath(Location start, Location end, Board board){
        Map<Location, Node> locationNodes = new HashMap<>();
        board.getLocations().values().stream().forEach(l -> locationNodes.put(l, new Node(l)));

        // Initialize the open list
        Queue<Node> openList = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.f < o2.f? 1: (o1.f > o2.f? -1: 0);
            }
        });
        // Initialize the closed list (visited)
        List<Node> visited = new ArrayList<>();

        // put the starting node on the open list (you can leave its f at zero)
        Node startNode = new Node(start);
        startNode.f = 0;
        openList.add(startNode);

        // while the open list is not empty
        while (!openList.isEmpty()) {
            Node current = openList.remove();

            if (!visited.contains(current)){
                visited.add(current);

                if (current.location == end)
                    return reconstructPath(startNode, current, board);

                List<Node> neighbors =
                        board.getConnections(current.location.getName()).stream()
                                .map(locationName -> locationNodes.values().stream().filter(node -> node.location.getName() == locationName).findFirst().get())
                                .collect(Collectors.toList());

                for (Node neighbor : neighbors) {
                    if (!visited.contains(neighbor)){
                        // increment hops from start
                        neighbor.hopsFromStart = current.hopsFromStart + 1;

                        // calculate predicted distance to the end node
                        int predictedDistance = calcEstimatedCostToMove(neighbor.location, end, board);

                        // calculate distance to neighbor. 2. calculate dist from start node
                        int totalDistance = calcActualCostToMove(startNode, neighbor) + predictedDistance;

                        // update n's distance
                        neighbor.f = totalDistance;

                        // if a node with the same position as successor is in the OPEN list which has a lower f than successor, skip this successor
                        Optional<Node> betterNode = openList.stream().filter(n -> n.hopsFromStart == neighbor.hopsFromStart && n.f < neighbor.f).findFirst();
                        if (betterNode.isPresent())
                            continue;

                        // if a node with the same position as successor is in the CLOSED list which has a lower f than successor, skip this successor
                        betterNode = visited.stream().filter(n -> n.hopsFromStart == neighbor.hopsFromStart && n.f < neighbor.f).findFirst();
                        if (betterNode.isPresent())
                            continue;

                        // otherwise, add  the node to the open list
                        neighbor.parent = current;
                        openList.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private static List<Location> reconstructPath(Node startNode, Node endNode, Board board){
        List<Location> path = new ArrayList<>();
        Node current = endNode;
        while (current.location != startNode.location){
            Node parent = current.parent;
            final Location currentLocation = current.location;
            path.add(currentLocation);
            current = parent;
        }
        Collections.reverse(path);
        return path;
    }

    // Return g
    private static int calcActualCostToMove(Node location1, Node location2){
        int cost = location2.hopsFromStart - location1.hopsFromStart;
        return cost;
    }

    // Return h
    private static int calcEstimatedCostToMove(Location location1, Location location2, Board board){
        if (location1 == location2)
            return 0;
        if (board.getConnections(location1.getName()).contains(location2.getName())){
            return 1;
        }
        return 99;
    }

    static class Node{
        public Location location;
        public Node parent = null;
        public int hopsFromStart = 0;
        public int f = Integer.MAX_VALUE;

        public Node(Location l){
            this.location = l;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return location.equals(node.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location);
        }
    }
}