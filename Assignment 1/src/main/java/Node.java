public class Node {
    private int row;
    private int col;
    private boolean isOccupied;
    private Node prev = null;
    private boolean isVisited = false;
    private int G;
    private double H;
    private double F;
    private int length;

    public Node(int row, int col, boolean val, Node prev) {
        this.row = row;
        this.col = col;
        this.isOccupied = val;
        this.prev = prev;
    }

    public Node(int row, int col, boolean val) {
        this.row = row;
        this.col = col;
        this.isOccupied = val;
    }

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }


    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public double getH() {
        return H;
    }

    public void setH(double h) {
        H = h;
    }

    public double getF() {
        return H + G;
    }

    public void setLength(int s){length=s;}

    public int getLength(){return length;}
}