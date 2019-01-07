import java.util.Comparator;

public class MyComparator {
    private static int EUCLIDEAN_DISTANCE = 0;
    private static int MANHATTAN_DISTANCE = 1;

    private final int PATH_LENGTH = 00;
    private final int EXPANDED_NODES = 01;
    private final int MAX_FRINGE = 02;

    private final int BFS = 10;
    private final int DFS = 11;
    private final int A_STAR_MANHATTAN = 12;
    private final int A_STAR_EUCLIDEAN = 13;

    public Comparator<Maze> getMyComparator(int property, int algorithm) {

        Comparator<Maze> comparator = new Comparator<Maze>() {
            @Override
            public int compare(Maze o1o, Maze o2o) {
                Maze o1 = o1o.clone();
                Maze o2 = o2o.clone();
                switch (property){
                    case PATH_LENGTH: {
                        switch (algorithm) {
                            case BFS: {
                                return new BreadthFirstSearch(o2).getLength() - new BreadthFirstSearch(o1).getLength();
                            }
                            case DFS: {
                                return new DepthFirstSearch(o2).getLength() - new DepthFirstSearch(o1).getLength();
                            }
                            case A_STAR_MANHATTAN: {
                                return new AStar(o2, MANHATTAN_DISTANCE).getLength() - new AStar(o1, MANHATTAN_DISTANCE).getLength();
                            }
                            case A_STAR_EUCLIDEAN: {
                                return new AStar(o2, EUCLIDEAN_DISTANCE).getLength() - new AStar(o1, EUCLIDEAN_DISTANCE).getLength();
                            }
                        }
                        break;
                    }
                    case EXPANDED_NODES: {
                        switch (algorithm) {
                            case BFS: {
                                return new BreadthFirstSearch(o2).getExpanded() - new BreadthFirstSearch(o1).getExpanded();
                            }
                            case DFS: {
                                return new DepthFirstSearch(o2).getExpanded() - new DepthFirstSearch(o1).getExpanded();
                            }
                            case A_STAR_MANHATTAN: {
                                return new AStar(o2, MANHATTAN_DISTANCE).getExpanded() - new AStar(o1, MANHATTAN_DISTANCE).getExpanded();
                            }
                            case A_STAR_EUCLIDEAN: {
                                return new AStar(o2, EUCLIDEAN_DISTANCE).getExpanded() - new AStar(o1, EUCLIDEAN_DISTANCE).getExpanded();
                            }
                        }
                        break;
                    }
                    case MAX_FRINGE: {
                        switch (algorithm) {
                            case BFS: {
                                return new BreadthFirstSearch(o2).getMaxFringe() - new BreadthFirstSearch(o1).getMaxFringe();
                            }
                            case DFS: {
                                return new DepthFirstSearch(o2).getMaxFringe() - new DepthFirstSearch(o1).getMaxFringe();
                            }
                            case A_STAR_MANHATTAN: {
                                return new AStar(o2, MANHATTAN_DISTANCE).getMaxFringe() - new AStar(o1, MANHATTAN_DISTANCE).getMaxFringe();
                            }
                            case A_STAR_EUCLIDEAN: {
                                return new AStar(o2, EUCLIDEAN_DISTANCE).getMaxFringe() - new AStar(o1, EUCLIDEAN_DISTANCE).getMaxFringe();
                            }
                        }
                        break;
                    }
                }
                return 0;
            }
        };
        return comparator;
    }

}
