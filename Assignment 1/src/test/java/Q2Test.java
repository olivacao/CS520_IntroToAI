import org.junit.jupiter.api.Test;

public class Q2Test {
    @Test
    public void Q2Test(){
        Maze maze = new Maze();
        maze.generateMaze(10,0.2);
        System.out.println("DFS");

        DepthFirstSearch dfs = new DepthFirstSearch(maze.clone());
        dfs.solveMaze();
        MazeDrawer mz1 = new MazeDrawer();
        mz1.drawMaze(dfs.printRoute(dfs.getRoute()),"DFS");
        System.out.println("BFS");

        BreadthFirstSearch bfs = new BreadthFirstSearch(maze.clone());
        bfs.solveMaze();
        MazeDrawer mz2 = new MazeDrawer();
        mz2.drawMaze(bfs.printRoute(bfs.getRoute()),"BFS");
        System.out.println("Manhattan");

        AStar aStar = new AStar(maze.clone(), 1);
        aStar.solveMaze();
        MazeDrawer mz3 = new MazeDrawer();
        mz3.drawMaze(aStar.printRoute(aStar.getRoute()),"Manhattan");
        System.out.println();

        System.out.println("Euclidean");

        AStar aStar1 = new AStar(maze.clone(), 0);
        aStar1.solveMaze();
        MazeDrawer mz4 = new MazeDrawer();
        mz4.drawMaze(aStar1.printRoute(aStar1.getRoute()),"Euclidean");

        while (true){

        }
    }
}
