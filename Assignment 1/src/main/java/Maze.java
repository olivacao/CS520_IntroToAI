import java.util.Random;

public class Maze {

    private Node[][] maze;
    private int dim;
    private double p;

    public Maze() {
        this.maze = null;
    }

    public Maze(int[][] arr){
        for (int i = 0; i < arr.length ; i++) {
            assert arr.length == arr[i].length;
        }
        this.dim = arr.length;
        this.p = 0;
        this.maze = new Node[arr.length][arr.length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                maze[i][j] = new Node(i, j);
                if (arr[i][j] == 0) {
                    maze[i][j].setOccupied(false);
                } else {
                    maze[i][j].setOccupied(true);
                }
            }
        }
    }

    public Node[][] generateMaze(int dim, double p) {
        this.dim = dim;
        this.p = p;
        this.maze = new Node[dim][dim];

        Random rand = new Random();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                maze[i][j] = new Node(i, j);
                if (rand.nextDouble() > p) {
                    maze[i][j].setOccupied(false);
                } else {
                    maze[i][j].setOccupied(true);
                }
            }
        }
        // Set Start and Goal to be unoccupied.
        maze[0][0].setOccupied(false);
        maze[dim - 1][dim - 1].setOccupied(false);
        return maze;
    }

    public Node[][] getMaze() {
        if (maze == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.maze;
    }

    public void printMaze() {
        if (this.maze == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (maze[i][j].isOccupied()) {
                        sb.append("1").append(" ");
                    } else {
                        sb.append("0").append(" ");
                    }
                }
                sb.append("\n");
            }
            System.out.println(sb.toString());
        }
    }

    public int getDim() {
        return dim;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public int[][] toArray(){
        int[][] arr=new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (maze[i][j].isOccupied()) {
                    arr[i][j]=1;
                } else {
                    arr[i][j]=0;
                }
            }
        }
        return arr;
    }

    public void initialize() {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                maze[i][j].setPrev(null);
                maze[i][j].setH(Integer.MAX_VALUE);
                maze[i][j].setVisited(false);
                maze[i][j].setG(Integer.MAX_VALUE);
                maze[i][j].setLength(0);
            }
        }
    }

    public Maze clone() {
        Maze clone = new Maze();
        clone.dim = dim;
        clone.p = p;
        clone.maze = new Node[dim][dim];

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                clone.maze[i][j] = new Node(i, j);
                clone.maze[i][j].setVisited(false);
                clone.maze[i][j].setPrev(null);
                clone.maze[i][j].setOccupied(maze[i][j].isOccupied());
            }
        }
        return clone;
    }
}
