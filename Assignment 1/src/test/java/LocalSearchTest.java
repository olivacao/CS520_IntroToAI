import org.junit.jupiter.api.Test;

public class LocalSearchTest {
    private final int PATH_LENGTH = 00;
    private final int EXPANDED_NODES = 01;
    private final int MAX_FRINGE = 02;

    private final int BFS = 10;
    private final int DFS = 11;
    private final int A_STAR_MANHATTAN = 12;
    private final int A_STAR_EUCLIDEAN = 13;

    private static int EUCLIDEAN_DISTANCE = 0;
    private static int MANHATTAN_DISTANCE = 1;

    @Test
    public void LocalSearchTest() {
        System.out.println("Original");
        Maze maze = new Maze();
        maze.generateMaze(10, 0.2);
        maze.printMaze();
        System.out.println();

        LocalSearch localSearch = new LocalSearch();
        localSearch.LocalBeamSearch(MAX_FRINGE, maze, A_STAR_MANHATTAN, true, 20);



    }

}
