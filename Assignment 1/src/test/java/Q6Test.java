import org.junit.jupiter.api.Test;

public class Q6Test {
    @Test
    public void Q6Test(){
        int iteration = 1000;
        int dim = 200;

        //A* with EUCLIDEAN
        for (double p = 0.1; p < 0.9 ; p+=0.1) {
            int counter=0;
            int expanded = 0;
            for (int i = 0; i <iteration ; i++) {
                Maze maze = new Maze();
                maze.generateMaze(dim,p);
                AStar aStar = new AStar(maze,0);
                if(aStar.isSolvable()) {
                    counter++;
                    expanded+=aStar.getExpanded();
                }
            }
            double avg = expanded*1.000/counter;
            System.out.println("A* with Euclidean: For p="+((int) (p * 100)) / 100.0+" , the average # of nodes expanded:"+avg);
        }

        System.out.println();

        //A* with MANHATTAN
        for (double p = 0.1; p < 0.9 ; p+=0.1) {
            int counter=0;
            int expanded = 0;
            for (int i = 0; i <iteration ; i++) {
                Maze maze = new Maze();
                maze.generateMaze(dim,p);
                AStar aStar = new AStar(maze,1);
                if(aStar.isSolvable()) {
                    counter++;
                    expanded+=aStar.getExpanded();
                }
            }
            double avg = expanded*1.000/counter;
            System.out.println("A* with Manhattan: For p="+((int) (p * 100)) / 100.0+" , the average # of nodes expanded:"+avg);
        }
    }
}
