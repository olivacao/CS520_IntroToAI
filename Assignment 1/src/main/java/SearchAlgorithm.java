import java.util.Stack;

public class SearchAlgorithm {
    private Maze maze;
    private int length = 0;
    private int maxFringe = Integer.MIN_VALUE;
    private int expanded = 0;
    private boolean isSolvable = false;

    public int getExpanded() {
        return expanded;
    }

    public int getMaxFringe() {
        return maxFringe;
    }

    public int getLength() {
        return length;
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public int[][] printRoute(Stack<Node> stack) {
        return null;
    }

    public Stack<Node> getRoute(){
        return null;
    }
}
