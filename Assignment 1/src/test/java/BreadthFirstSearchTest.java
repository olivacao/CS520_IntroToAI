import org.junit.jupiter.api.Test;

public class BreadthFirstSearchTest {

    @Test
    public void BreadthFirstSearchTest() {
        Maze maze = new Maze();
        maze.generateMaze(100, 0.3);
        maze.printMaze();
        BreadthFirstSearch bfs = new BreadthFirstSearch(maze);
        bfs.solveMaze();
        System.out.println("Length: " + bfs.getLength());
        System.out.println("Expanded: " + bfs.getExpanded());
        System.out.println("Fringe: " + bfs.getMaxFringe());
    }
}
