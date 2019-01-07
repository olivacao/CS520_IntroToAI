import org.junit.jupiter.api.Test;

public class Q3Test {
    @Test
    public void Q3Test(){

        int iteration = 1000;
        int dim = 200;

        //DFS
        for (double p = 0.1; p <= 0.9 ; p+=0.1) {
            int counter = 0;
            for (int i = 0; i <iteration ; i++) {
                Maze maze = new Maze();
                maze.generateMaze(dim,p);
                DepthFirstSearch dfs = new DepthFirstSearch(maze);
                if(dfs.solveMaze())
                    counter++;
            }
            double probability = counter*1.000/iteration;
            System.out.println("DFS: For p="+((int) (p * 100)) / 100.0+" , the probability of solving is:"+probability);
        }

        System.out.println();

        //BFS
        for (double p = 0.1; p <= 0.9 ; p+=0.1) {
            int counter = 0;
            for (int i = 0; i <iteration ; i++) {
                Maze maze = new Maze();
                maze.generateMaze(dim,p);
                BreadthFirstSearch bfs = new BreadthFirstSearch(maze);
                if(bfs.solveMaze())
                    counter++;
            }
            double probability = counter*1.000/iteration;
            System.out.println("BFS: For p="+((int) (p * 100)) / 100.0+" , the probability of solving is:"+probability);
        }

        System.out.println();

        //A* with EUCLIDEAN
        for (double p = 0.1; p <= 0.9 ; p+=0.1) {
            int counter = 0;
            for (int i = 0; i <iteration ; i++) {
                Maze maze = new Maze();
                maze.generateMaze(dim,p);
                AStar aStar_E = new AStar(maze,0);
                if(aStar_E.solveMaze())
                    counter++;
            }
            double probability = counter*1.000/iteration;
            System.out.println("A* with Euclidean: For p="+((int) (p * 100)) / 100.0+" , the probability of solving is:"+probability);
        }

        System.out.println();

        //A* with MANHATTAN
        for (double p = 0.1; p <= 0.9 ; p+=0.1) {
            int counter = 0;
            for (int i = 0; i <iteration ; i++) {
                Maze maze = new Maze();
                maze.generateMaze(dim,p);
                AStar aStar_M = new AStar(maze,1);
                if(aStar_M.solveMaze())
                    counter++;
            }
            double probability = counter*1.000/iteration;
            System.out.println("A* with Manhattan: For p="+((int) (p * 100)) / 100.0+" , the probability of solving is:"+probability);
        }
    }
}

