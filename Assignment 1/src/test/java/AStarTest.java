import org.junit.jupiter.api.Test;

public class AStarTest {
    @Test
    public void AStarTest() {
        Maze maze = new Maze();
        maze.generateMaze(1000, 0.2);
        maze.printMaze();

        System.out.println("DFS");
//        maze.initialize();
        DepthFirstSearch dfs = new DepthFirstSearch(maze);
        dfs.solveMaze();

        System.out.println("BFS");
        maze.initialize();
        BreadthFirstSearch bfs = new BreadthFirstSearch(maze);
        bfs.solveMaze();

        System.out.println("Manhattan");
        maze.initialize();
        AStar aStar = new AStar(maze, 1);
        aStar.solveMaze();

        System.out.println();

        System.out.println("Euclidean");
        maze.initialize();
        AStar aStar1 = new AStar(maze, 0);
        aStar1.solveMaze();

        System.out.println("DFS");
        System.out.println("Length: " + dfs.getLength());
        System.out.println("Expanded: " + dfs.getExpanded());
        System.out.println("Fringe: " + dfs.getMaxFringe());
        System.out.println();

        System.out.println("BFS");
        System.out.println("Length: " + bfs.getLength());
        System.out.println("Expanded: " + bfs.getExpanded());
        System.out.println("Fringe: " + bfs.getMaxFringe());
        System.out.println();

        System.out.println("Manhattan");
        System.out.println("Length: " + aStar.getLength());
        System.out.println("Expanded: " + aStar.getExpanded());
        System.out.println("Fringe: " + aStar.getMaxFringe());
        System.out.println();

        System.out.println("Euclidean");
        System.out.println("Length: " + aStar1.getLength());
        System.out.println("Expanded: " + aStar1.getExpanded());
        System.out.println("Fringe: " + aStar1.getMaxFringe());
        System.out.println();

    }
}
