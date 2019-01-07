import java.util.*;

public class HillClimb {
    private Maze maze;
    private int length;
    private int method;

    private static int DFS=0;

    public HillClimb(Maze maze, int method){
        this.maze=maze;
        this.method=method;
        if(method==DFS){
            DepthFirstSearch dfs = new DepthFirstSearch(maze);
            this.length=dfs.getLength();
        }

    }

    public void getHardMaze(){
        boolean canBeHarder = true;
        while (canBeHarder){
            canBeHarder=hillClimbHelper();
        }
        maze.printMaze();
    }

    public boolean hillClimbHelper(){
        boolean canBeHarder=true;
        PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getLength() == o2.getLength()) {
                    return 0;
                } else if (o1.getLength() < o2.getLength()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        for (int i=0;i<maze.getDim();i++){
            for (int j = 0; j <maze.getDim() ; j++) {
                //exclude the start and goal node
                if((i==0&&j==0)||(i==maze.getDim()-1&&j==maze.getDim()))
                    continue;

                Maze maze_= new Maze(flip(maze.toArray(),i,j));

                if(method==DFS){
                    DepthFirstSearch dfs = new DepthFirstSearch(maze_);
                    if(dfs.solveMaze()){
                        if (dfs.getLength()>length){
                        maze.getMaze()[i][j].setLength(dfs.getLength());
                        pq.add(maze.getMaze()[i][j]);
                        }
                    }
                }
            }

            if (pq.isEmpty())
                canBeHarder=false;
            else{
                Node tmp = pq.poll();
                maze = new Maze(flip(maze.toArray(),tmp.getRow(),tmp.getCol()));
                length = tmp.getLength();
            }

        }

        return canBeHarder;
    }




    private int[][] flip(int[][] maze, int x, int y){
        int[][]arr=maze;
        if(arr[x][y]==0)
            arr[x][y]=1;
        else if(arr[x][y]==1)
            arr[x][y]=0;
        return arr;
    }
}

