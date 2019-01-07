import org.junit.jupiter.api.Test;

public class Q7Test {
    @Test
    public void Q7Test(){
        int iteration = 1000;
        int dim = 200;

        //DFS
        for (double p = 0.1; p < 0.5 ; p+=0.1) {
            int counter=0;
            int expanded = 0;

            for (int i = 0; i <iteration ; i++) {
                Maze maze = new Maze();
                maze.generateMaze(dim,p);
                DepthFirstSearch dfs = new DepthFirstSearch(maze);
                if(dfs.isSolvable()) {
                    counter++;
                    expanded+=dfs.getExpanded();
                }
            }
            double avg = expanded*1.000/counter;
            System.out.println("DFS: For p="+((int) (p * 100)) / 100.0+" , the average # of nodes expanded:"+avg);
        }

        System.out.println();

        //BFS
        for (double p = 0.1; p < 0.5 ; p+=0.1) {
            int counter=0;
            int expanded = 0;

            for (int i = 0; i <iteration ; i++) {
                Maze maze = new Maze();
                maze.generateMaze(dim,p);
                BreadthFirstSearch bfs = new BreadthFirstSearch(maze);
                if(bfs.isSolvable()) {
                    counter++;
                    expanded+=bfs.getExpanded();
                }
            }
            double avg = expanded*1.000/counter;
            System.out.println("BFS: For p="+((int) (p * 100)) / 100.0+" , the average # of nodes expanded:"+avg);
        }
    }
}
