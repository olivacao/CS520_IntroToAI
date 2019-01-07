import java.util.*;

public class SweeperTest {
    public static void main(String[] args) {

        System.out.println("Please select input method:");
        System.out.println(" 1.Random generate.");
        System.out.println(" 2.Manual input.");
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        System.out.println("Please enter the size of the board in the format of: [Length Width TotalNumberofMines]");
        int len = sc.nextInt();
        int wid = sc.nextInt();
        int mines = sc.nextInt();
        switch (m){
            case 1: {
                int[][] randBoard = generateRandomBoard(len, wid, mines);
                Board board = new Board(len, wid, mines);
                Sweeper sweeper = new Sweeper(board, randBoard);
                sweeper.autoRun();
                break;
            }

            case 2: {
                Board board = new Board(len, wid, mines);
                Sweeper sweeper = new Sweeper(board);
                sweeper.manualRun();
                break;
//                System.out.println("Format: [len wid val]");
//
//                boolean continueGame = true;
//
//                while (continueGame) {
//
//                    List<int[]> query = sweeper.run();
//                    for (int i = 0; i < query.size(); i++) {
//                        System.out.println(query.get(i)[0] + " " + query.get(i)[1] + " Please input the value(0-9): ");
//                        input = scanner.next();
//                        if (input.equals("q")) {
//                            System.out.println("Game ended by user.");
//                            continueGame = false;
//                            break;
//                        }
//
//                        int val = Integer.parseInt(input);
//
//                        Cell cell = board.getBoard()[query.get(i)[0]][query.get(i)[1]];
//                        cell.setVal(val);
//                        cell.setUseless(false);
//
//                        if (val == 9) {
//                            cell.setCellType(CellType.ENDMINE);
//                            System.out.println("BOOM! GAME OVER");
//                            board.printBoard();
//                            continueGame = false;
//                            break;
//                        } else if (val == 0) {
//                            cell.setCellType(CellType.CLEAR);
//                        } else {
//                            cell.setCellType(CellType.CLUE);
//                        }
//                    }
//
//
//                }
//                break;

            }
            default:
                break;
        }
    }

    public static int[][] generateRandomBoard(int len, int wid, int mines){
        int[][] randBoard = new int[len][wid];
        int i = 0;
        while (i < mines) {
            Random random = new Random();
            int randLen = random.nextInt(len);
            int randWid = random.nextInt(wid);
            if (randBoard[randLen][randWid]!=9){
                randBoard[randLen][randWid]=9;
                i++;
            }
        }
        for (int p = 0; p <len ; p++) {
            for (int q = 0; q <wid ; q++) {
                if (randBoard[p][q]==9)
                    continue;
                else {
                    int val = 0;
                    for (int m = Math.max(0, p - 1); m <= Math.min(len - 1, p + 1); m++) {
                        for (int n = Math.max(0, q - 1); n <= Math.min(wid - 1, q + 1); n++) {
                            if (randBoard[m][n]==9)
                                val++;
                        }
                    }
                    randBoard[p][q]=val;
                }
            }
        }
        printRandBoard(randBoard);
        return randBoard;
    }

    public static void printRandBoard(int[][] board){
        System.out.println("----------Board----------");
        for (int[]i:board) {
            for (int j:i) {
                if (j==9)
                    System.out.print("M");
                else
                    System.out.print(j);
            }
            System.out.println();
        }
        System.out.println("-------------------------");

    }

}