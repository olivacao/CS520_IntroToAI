import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HillClimbTest {
    @Test
    public void HillClimbTest() {
        Maze maze = new Maze();
        maze.generateMaze(8,0.2);
        maze.printMaze();
        System.out.println();
        DepthFirstSearch dfs = new DepthFirstSearch(maze);
        if (dfs.solveMaze()){
            System.out.println("Harder version:");
            HillClimb hc = new HillClimb(maze,0);
            hc.getHardMaze();
        }
        else
            System.out.println("This maze is not solvable.");
    }
}
