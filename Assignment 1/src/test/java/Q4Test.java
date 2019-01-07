import org.junit.jupiter.api.Test;

public class Q4Test {
    @Test
    public void Q4Test(){

        int iteration = 1000;
        int dim = 200;

        //DFS
        for (double p = 0.1; p < 0.4 ; p+=0.1) {
            int counter=0;
            int path = 0;
            for (int i = 0; i <iteration ; i++) {
                Maze maze = new Maze();
                maze.generateMaze(dim,p);
                DepthFirstSearch dfs = new DepthFirstSearch(maze);
                if(dfs.isSolvable()) {
                    counter++;
                    path+=dfs.getLength();
                }
            }
            double avg = path*1.000/counter;
            System.out.println("DFS: For p="+((int) (p * 100)) / 100.0+" , the average path length:"+avg);
        }

        System.out.println();

        //BFS
        for (double p = 0.1; p < 0.4 ; p+=0.1) {
            int counter=0;
            int path = 0;
            for (int i = 0; i <iteration ; i++) {
                Maze maze = new Maze();
                maze.generateMaze(dim,p);
                BreadthFirstSearch bfs = new BreadthFirstSearch(maze);
                if(bfs.isSolvable()) {
                    counter++;
                    path+=bfs.getLength();
                }
            }
            double avg = path*1.000/counter;
            System.out.println("BFS: For p="+((int) (p * 100)) / 100.0+" , the average path length:"+avg);
        }

        System.out.println();

        //A* with EUCLIDEAN
        for (double p = 0.1; p < 0.4 ; p+=0.1) {
            int counter=0;
            int path = 0;
            for (int i = 0; i <iteration ; i++) {
                Maze maze = new Maze();
                maze.generateMaze(dim,p);
                AStar aStar = new AStar(maze,0);
                if(aStar.isSolvable()) {
                    counter++;
                    path+=aStar.getLength();
                }
            }
            double avg = path*1.000/counter;
            System.out.println("A* with Euclidean: For p="+((int) (p * 100)) / 100.0+" , the average path length:"+avg);
        }

        System.out.println();

        //A* with MANHATTAN
        for (double p = 0.1; p < 0.4 ; p+=0.1) {
            int counter=0;
            int path = 0;
            for (int i = 0; i <iteration ; i++) {
                Maze maze = new Maze();
                maze.generateMaze(dim,p);
                AStar aStar = new AStar(maze,1);
                if(aStar.isSolvable()) {
                    counter++;
                    path+=aStar.getLength();
                }
            }
            double avg = path*1.000/counter;
            System.out.println("A* with Manhattan: For p="+((int) (p * 100)) / 100.0+" , the average path length:"+avg);
        }
    }
}
