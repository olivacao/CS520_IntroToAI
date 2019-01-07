import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepthFirstSearchTest {
    @Test
    public void DepthFirstSearchTest() {
        Maze maze = new Maze();
        maze.generateMaze(10, 0.2);
        maze.printMaze();
        DepthFirstSearch dfs = new DepthFirstSearch(maze);
        dfs.solveMaze();
        System.out.println("Length: " + dfs.getLength());
        System.out.println("Expanded: " + dfs.getExpanded());
        System.out.println("Fringe: " + dfs.getMaxFringe());
    }

}
