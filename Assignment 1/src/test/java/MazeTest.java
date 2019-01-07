import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MazeTest {
    @Test
    public void MazeTest() {
    int[][]arr = {{0,0,1},{1,0,1},{1,0,0}};
    Maze maze = new Maze(arr);
    maze.printMaze();

    Maze maze1 = new Maze();
    maze1.generateMaze(5,0.3);
    maze1.printMaze();
    Maze maze2 = new Maze(maze1.toArray());
    maze2.printMaze();
    }

}