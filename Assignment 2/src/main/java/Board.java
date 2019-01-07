import java.util.ArrayList;
import java.util.List;

public class Board {
    private int length;
    private int width;

    private int minesTotal;
    private Cell[][] board;


    public Board(int length, int width, int minesTotal) {
        this.length = length;
        this.width = width;
        this.minesTotal = minesTotal;

        this.board = new Cell[length][width];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = new Cell(i, j);
            }
        }
    }

    public void printBoard() {
        int mines = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                switch (board[i][j].getCellType()) {
                    case CLUE: {
                        System.out.print(board[i][j].getVal());
                        break;
                    }
                    case MINE: {
                        mines++;
                        System.out.print("M");
                        break;
                    }
                    case CLEAR: {
                        System.out.print("-");
                        break;
                    }
                    case UNKNOWN: {
                        System.out.print("?");
                        break;
                    }
                    case ENDMINE: {
                        System.out.print("X");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Mines Found: " + mines);
        System.out.println();
    }

    public Cell[][] getBoard() {
        return board;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getMinesTotal() {
        return minesTotal;
    }

    public int getMinesFound() {
        int mines = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j].getCellType() == CellType.MINE) {
                    mines++;
                }
            }
        }
        return mines;
    }

    public int getUnknowns() {
        int unknowns = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j].getCellType() == CellType.UNKNOWN) {
                    unknowns++;
                }
            }
        }
        return unknowns;
    }

    public List<Cell> getUnkownsList() {
        List<Cell> unkownList = new ArrayList<Cell>();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j].getCellType() == CellType.UNKNOWN) {
                    unkownList.add(board[i][j]);
                }
            }
        }
        return unkownList;
    }

    public List<Cell> getNeighbours(Cell cell) {
        List<Cell> neighbours = new ArrayList<Cell>();
        for (int m = Math.max(0, cell.getLen() - 1); m <= Math.min(length - 1, cell.getLen() + 1); m++) {
            for (int n = Math.max(0, cell.getWid() - 1); n <= Math.min(width - 1, cell.getWid() + 1); n++) {
                if (m == cell.getLen() && n == cell.getWid()){
                    continue;
                }else {
                    neighbours.add(board[m][n]);
                }
            }
        }
        return neighbours;
    }



    public int countNeighbourMines(Cell cell) {
        int mineNum = 0;
        for (int m = Math.max(0, cell.getLen() - 1); m <= Math.min(length - 1, cell.getLen() + 1); m++) {
            for (int n = Math.max(0, cell.getWid() - 1); n <= Math.min(width - 1, cell.getWid() + 1); n++) {
                if (m == cell.getLen() && n == cell.getWid()) {
                    continue;
                }
                else if (board[m][n].getCellType() == CellType.MINE) {
                    mineNum++;
                }
            }
        }
        return mineNum;
    }


    public int countNeighbourAssumedMines(Cell cell) {
        int mineNum = 0;
        for (int m = Math.max(0, cell.getLen() - 1); m <= Math.min(length - 1, cell.getLen() + 1); m++) {
            for (int n = Math.max(0, cell.getWid() - 1); n <= Math.min(width - 1, cell.getWid() + 1); n++) {
                if (m == cell.getLen() && n == cell.getWid()) {
                    continue;
                }
                else if (board[m][n].getCellType() == CellType.ASSUMEMINE) {
                    mineNum++;
                }
            }
        }
        return mineNum;
    }

    public int countNeighbourUnknown(Cell cell) {
        int unknownNum = 0;
        for (int m = Math.max(0, cell.getLen() - 1); m <= Math.min(length - 1, cell.getLen() + 1); m++) {
            for (int n = Math.max(0, cell.getWid() - 1); n <= Math.min(width - 1, cell.getWid() + 1); n++) {
                if (m == cell.getLen() && n == cell.getWid()){
                    continue;
                }
                else if (board[m][n].getCellType() == CellType.UNKNOWN) {
                    unknownNum++;
                }
            }
        }
        return unknownNum;
    }

    public void getRelatedClues(Cell cell, List<Cell> relatedClues) {
        List<Cell> neighbours = getNeighbours(cell);
        for (Cell neighbour: neighbours) {
            if (neighbour.getCellType() == CellType.CLUE && !relatedClues.contains(neighbour)) {
                relatedClues.add(neighbour);
                getRelatedClues(neighbour, relatedClues);
            }
        }
    }


}
