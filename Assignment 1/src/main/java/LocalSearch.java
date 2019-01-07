import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class LocalSearch {
    private static int EUCLIDEAN_DISTANCE = 0;
    private static int MANHATTAN_DISTANCE = 1;

    private final int PATH_LENGTH = 00;
    private final int EXPANDED_NODES = 01;
    private final int MAX_FRINGE = 02;

    private final int BFS = 10;
    private final int DFS = 11;
    private final int A_STAR_MANHATTAN = 12;
    private final int A_STAR_EUCLIDEAN = 13;

    private int iterationCounter = 0;
    private int lastRes = 0;

    private int[][] lastMaze;

    public void LocalBeamSearch(int property, Maze maze, int algorithm, boolean PRINT_ROUTE, int SUCCESSOR_NUMBER) {

        switch (algorithm) {
            case BFS: {
                if (!new BreadthFirstSearch(maze).isSolvable()) {
                    return;
                }
                break;
            }
            case DFS: {
                if (!new DepthFirstSearch(maze).isSolvable()) {
                    return;
                }
                break;
            }
            case A_STAR_MANHATTAN: {
                if (!new AStar(maze, MANHATTAN_DISTANCE).isSolvable()) {
                    return;
                }
                break;
            }
            case A_STAR_EUCLIDEAN: {
                if (!new AStar(maze,EUCLIDEAN_DISTANCE).isSolvable()) {
                    return;
                }
                break;
            }
        }

        Comparator<Maze> comparator = new MyComparator().getMyComparator(property, algorithm);
        PriorityQueue<Maze> pq = new PriorityQueue<>(comparator);
        PriorityQueue<Maze> tmp = new PriorityQueue<>(comparator);

        maze.initialize();
        pq.add(maze);

        while (true) {
            while (!pq.isEmpty()) {
                Maze last = pq.poll();
                if (pq.isEmpty()) {
                    switch (algorithm) {
                        case BFS: {
                            BreadthFirstSearch bfs = new BreadthFirstSearch(last);
                            show(property, bfs, PRINT_ROUTE);
                            break;
                        }
                        case DFS: {
                            DepthFirstSearch dfs = new DepthFirstSearch(last);
                            show(property, dfs, PRINT_ROUTE);
                            break;
                        }
                        case A_STAR_MANHATTAN: {
                            AStar aStar = new AStar(last, MANHATTAN_DISTANCE);
                            show(property, aStar, PRINT_ROUTE);
                            break;
                        }
                        case A_STAR_EUCLIDEAN: {
                            AStar aStar = new AStar(last, EUCLIDEAN_DISTANCE);
                            show(property, aStar, PRINT_ROUTE);
                            break;
                        }
                    }
                }

                last.initialize();
                tmp.add(last);

                for (int i = 0; i < last.getDim(); i++) {
                    for (int j = 0; j < last.getDim(); j++) {
                        if ((i == 0 && j == 0) || (i == last.getDim() - 1 && j == last.getDim() - 1)) {
                            continue;
                        }
                        else {
                            Maze tmpMaze = last.clone();
                            tmpMaze.getMaze()[i][j].setOccupied(!tmpMaze.getMaze()[i][j].isOccupied());
                            tmp.add(tmpMaze);
                        }
                    }
                }
            }

            if (iterationCounter > SUCCESSOR_NUMBER / 2) {
                SUCCESSOR_NUMBER *= 2;
            }
            if (SUCCESSOR_NUMBER > maze.getDim()*maze.getDim()) {
                break;
            }
            pq.clear();
            for (int i = 0; i < SUCCESSOR_NUMBER; i++) {
                pq.add(tmp.poll());
            }
            tmp.clear();
        }


        if (PRINT_ROUTE) {
            MazeDrawer mazeDrawer = new MazeDrawer();
            mazeDrawer.drawMaze(lastMaze,"Maze");
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public void show(int property, SearchAlgorithm algorithm, boolean PRINT_ROUTE) {
        int currentRes = -1;
        switch (property) {
            case PATH_LENGTH: {
                currentRes = algorithm.getLength();
                System.out.println("Length: " + currentRes);
                break;
            }
            case MAX_FRINGE: {
                currentRes = algorithm.getMaxFringe();
                System.out.println("Fringe: " + currentRes);
                break;
            }
            case EXPANDED_NODES: {
                currentRes = algorithm.getExpanded();
                System.out.println("Expanded: " + currentRes);
                break;
            }
        }

        if (currentRes == lastRes) {
            iterationCounter++;
        } else {
            iterationCounter = 1;
            lastRes = currentRes;
        }

        if (PRINT_ROUTE) {
            lastMaze = algorithm.printRoute(algorithm.getRoute());
        }
    }

}
