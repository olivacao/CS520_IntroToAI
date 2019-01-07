import java.util.*;

public class Sweeper {
    private Board board;
    private int[][] randBoard;

    public Sweeper (Board board) {
        this.board = board;
    }

    public Sweeper (Board board, int[][] randBoard) {
        this.board = board;
        this.randBoard = randBoard;
    }


    private List<int[]> run() {
        List<int[]> query = new ArrayList<int[]>();
        for (int i = 0; i < board.getLength(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                if (!board.getBoard()[i][j].isUseless()) {
                    Cell cell = board.getBoard()[i][j];
                    switch (cell.getCellType()) {
                        case MINE: {
                            cell.setUseless(true);
                            break;
                        }
                        case CLUE: {
                            if (board.countNeighbourMines(cell) == cell.getVal()) {
                                List<Cell> neighbours = board.getNeighbours(cell);
                                for (Cell neighbour: neighbours) {
                                    if (neighbour.getCellType()==CellType.UNKNOWN){
                                        query.add(new int[]{neighbour.getLen(), neighbour.getWid()});
                                    }
                                }
                                cell.setUseless(true);
                            } else if(board.countNeighbourUnknown(cell) == cell.getVal() - board.countNeighbourMines(cell)){
                                List<Cell> neighbours = board.getNeighbours(cell);
                                for (Cell neighbour: neighbours) {
                                    if (neighbour.getCellType()==CellType.UNKNOWN){
                                        neighbour.setCellType(CellType.MINE);
                                        neighbour.setUseless(true);
                                    }
                                }
                                cell.setUseless(true);
                            }
                            break;
                        }
                        case CLEAR: {
                            //add all neighbours, set as useless
                            List<Cell> neighbours = board.getNeighbours(cell);
                            for (Cell neighbour: neighbours) {
                                if (neighbour.getCellType()==CellType.UNKNOWN && !query.contains(new int[]{neighbour.getLen(), neighbour.getWid()})){
                                    query.add(new int[]{neighbour.getLen(), neighbour.getWid()});
                                }
                            }
                            cell.setUseless(true);
                            break;
                        }
                    }
                }
            }
        }
        board.printBoard();
        return query;
    }


    private List<int[]> randomGuess() {
        List<int[]> query = new ArrayList<int[]>();
        Random random = new Random();
        int randLen = random.nextInt(board.getLength());
        int randWid = random.nextInt(board.getWidth());
        while (board.getBoard()[randLen][randWid].getCellType() != CellType.UNKNOWN) {
            randLen = random.nextInt(board.getLength());
            randWid = random.nextInt(board.getWidth());
        }
        query.add(new int[]{randLen, randWid});
        return query;
    }


    private List<int[]> assume() {

        List<List<int[]>> querys = new ArrayList<List<int[]>>();


        List<Cell> assumableCells = new ArrayList<Cell>();
        for(int i = 0; i < board.getLength(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                Cell cell = board.getBoard()[i][j];
                if (cell.getCellType() == CellType.CLUE && !cell.isUseless() && cell.getVal() - board.countNeighbourMines(cell) < board.countNeighbourUnknown(cell)) {
                    // Only assume if known knowledges are greater than unknown
                    if (board.countNeighbourUnknown(cell) < 3)
                        assumableCells.add(cell);

                }
            }
        }

        for (Cell cell: assumableCells) {
            int unkownMines = cell.getVal() - board.countNeighbourMines(cell);
            List<Cell> unknownNeighbourList = new ArrayList<Cell>();

            List<Cell> neighbours = board.getNeighbours(cell);
            for (Cell neighbour: neighbours) {
                if (neighbour.getCellType() == CellType.UNKNOWN) {
                    unknownNeighbourList.add(neighbour);
                }
            }

            int unkownNeighboursNum = unknownNeighbourList.size();
            List<Cell> assumption = new ArrayList<Cell>();
            switch (unkownMines) {

                case 2: {
                    for (int i = 0; i < unkownNeighboursNum - 1; i++) {
                        for (int j = i + 1; j < unkownNeighboursNum; j++) {
                            assumption.add(unknownNeighbourList.get(i));
                            assumption.add(unknownNeighbourList.get(j));
                            List<int[]> query = new ArrayList<int[]>();
                            if (makeAssumption(assumption, unknownNeighbourList, query)) {
                                System.out.println("QUERY WORKS");
                                querys.add(query);
//                                return querys;
                            } else {
                                querys.clear();
                            }

                        }
                    }
                    break;
                }
                case 3: {
                    for (int i = 0; i < unkownNeighboursNum - 2; i++) {
                        for (int j = i + 1; j < unkownNeighboursNum - 1; j++) {
                            for (int k = j + 1; k < unkownNeighboursNum; k++) {
                                assumption.add(unknownNeighbourList.get(i));
                                assumption.add(unknownNeighbourList.get(j));
                                assumption.add(unknownNeighbourList.get(k));
                                List<int[]> query = new ArrayList<int[]>();

                                if (makeAssumption(assumption, unknownNeighbourList, query)) {
                                    querys.add(query);
                                    System.out.println("QUERY WORKS");

//                                    return querys;
                                } else {
                                    querys.clear();
                                }
                            }

                        }
                    }
                    break;
                }
                case 4: {
                    for (int i = 0; i < unkownNeighboursNum - 3; i++) {
                        for (int j = i + 1; j < unkownNeighboursNum - 2; j++) {
                            for (int k = j + 1; k < unkownNeighboursNum - 1; k++) {
                                for (int l = k + 1; l < unkownNeighboursNum; l++) {
                                    assumption.add(unknownNeighbourList.get(i));
                                    assumption.add(unknownNeighbourList.get(j));
                                    assumption.add(unknownNeighbourList.get(k));
                                    assumption.add(unknownNeighbourList.get(l));
                                    List<int[]> query = new ArrayList<int[]>();
                                    if (makeAssumption(assumption, unknownNeighbourList, query)) {
                                        querys.add(query);
                                        System.out.println("QUERY WORKS");
//                                        return querys;
                                    } else {
                                        querys.clear();
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
                case 5: {
                    for (int i = 0; i < unkownNeighboursNum - 4; i++) {
                        for (int j = i + 1; j < unkownNeighboursNum - 3; j++) {
                            for (int k = j + 1; k < unkownNeighboursNum - 2; k++) {
                                for (int l = k + 1; l < unkownNeighboursNum - 1; l++) {
                                    for (int m = l + 1; m < unkownNeighboursNum; m++) {
                                        assumption.add(unknownNeighbourList.get(i));
                                        assumption.add(unknownNeighbourList.get(j));
                                        assumption.add(unknownNeighbourList.get(k));
                                        assumption.add(unknownNeighbourList.get(l));
                                        assumption.add(unknownNeighbourList.get(m));
                                        List<int[]> query = new ArrayList<int[]>();
                                        if (makeAssumption(assumption, unknownNeighbourList, query)) {
                                            querys.add(query);
                                            System.out.println("QUERY WORKS");
//                                            return querys;
                                        } else {
                                            querys.clear();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
                case 6: {
                    for (int i = 0; i < unkownNeighboursNum - 5; i++) {
                        for (int j = i + 1; j < unkownNeighboursNum - 4; j++) {
                            for (int k = j + 1; k < unkownNeighboursNum - 3; k++) {
                                for (int l = k + 1; l < unkownNeighboursNum - 2; l++) {
                                    for (int m = l + 1; m < unkownNeighboursNum - 1; m++) {
                                        for (int n = m + 1; n < unkownNeighboursNum; n++) {
                                            assumption.add(unknownNeighbourList.get(i));
                                            assumption.add(unknownNeighbourList.get(j));
                                            assumption.add(unknownNeighbourList.get(k));
                                            assumption.add(unknownNeighbourList.get(l));
                                            assumption.add(unknownNeighbourList.get(m));
                                            assumption.add(unknownNeighbourList.get(n));
                                            List<int[]> query = new ArrayList<int[]>();

                                            if (makeAssumption(assumption, unknownNeighbourList, query)) {
                                                querys.add(query);
                                                System.out.println("QUERY WORKS");
//                                                return querys;
                                            } else {
                                                querys.clear();
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                    break;
                }
                case 7: {
                    for (int i = 0; i < unkownNeighboursNum - 6; i++) {
                        for (int j = i + 1; j < unkownNeighboursNum - 5; j++) {
                            for (int k = j + 1; k < unkownNeighboursNum - 4; k++) {
                                for (int l = k + 1; l < unkownNeighboursNum - 3; l++) {
                                    for (int m = l + 1; m < unkownNeighboursNum - 2; m++) {
                                        for (int n = m + 1; n < unkownNeighboursNum - 1; n++) {
                                            for (int o = n + 1; o < unkownNeighboursNum; o++) {
                                                assumption.add(unknownNeighbourList.get(i));
                                                assumption.add(unknownNeighbourList.get(j));
                                                assumption.add(unknownNeighbourList.get(k));
                                                assumption.add(unknownNeighbourList.get(l));
                                                assumption.add(unknownNeighbourList.get(m));
                                                assumption.add(unknownNeighbourList.get(n));
                                                assumption.add(unknownNeighbourList.get(o));
                                                List<int[]> query = new ArrayList<int[]>();

                                                if (makeAssumption(assumption, unknownNeighbourList, query)) {
                                                    querys.add(query);
                                                    System.out.println("QUERY WORKS");
//                                                    return querys;
                                                } else {
                                                    querys.clear();
                                                }
                                            }

                                        }

                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }


        }


        int minLen = Integer.MAX_VALUE;
        List<int[]> minQuery = new ArrayList<int[]>();
        for (List<int[]> query: querys) {
            if (query.size() < minLen) {
                minQuery = query;
            }
        }

        return minQuery;
    }


    /**
     *
     * @param assumption
     * @param unkownNeighbourList
     * @param querys
     * @return
     */
    private boolean makeAssumption(List<Cell> assumption, List<Cell> unkownNeighbourList, List<int[]> querys) {
        boolean hasValidAssumption = false;
        for (Cell cell: assumption) {
            cell.setCellType(CellType.ASSUMEMINE);
        }
        for (Cell cell: unkownNeighbourList) {
            if (cell.getCellType() != CellType.ASSUMEMINE) {
                cell.setCellType(CellType.ASSUMENOTMINE);
                querys.add(new int[]{cell.getLen(), cell.getWid()});
            }
        }


        List<Cell> relatedClues = new ArrayList<Cell>();
        for (Cell cell: unkownNeighbourList) {
            board.getRelatedClues(cell, relatedClues);
        }


        if (runAssumption(relatedClues, querys)) {
            hasValidAssumption = true;
            recoverAssumption();
            return true;

        } else {
            querys.clear();
        }

        // Recover after assumption
        recoverAssumption();
        return false;
    }

    /**
     * Run assumption based on the target cell's assumption info. Assume as much related cell as possible and determine it's good or not.
     * @param relatedClues
     * @return
     */
    private boolean runAssumption(List<Cell> relatedClues, List<int[]> querys) {
        List<Cell> newRelatedClues = new ArrayList<Cell>();
        List<Cell> newAssumptions = new ArrayList<Cell>();
        for (Cell cell: relatedClues) {

            if (board.countNeighbourMines(cell) + board.countNeighbourAssumedMines(cell) == cell.getVal()) {
                List<Cell> neighbours = board.getNeighbours(cell);
                for (Cell neighbour: neighbours) {
                    if (neighbour.getCellType()==CellType.UNKNOWN){
                        neighbour.setCellType(CellType.ASSUMENOTMINE);
                        querys.add(new int[]{neighbour.getLen(), neighbour.getWid()});
                        newAssumptions.add(neighbour);
                        List<Cell> newRelatedList = new ArrayList<Cell>();
                        board.getRelatedClues(neighbour, newRelatedList);
                        for (Cell tmp: newRelatedList) {
                            if (!relatedClues.contains(tmp)) {
                                newRelatedClues.add(tmp);
                            }
                        }
                    }
                }

            } else if(board.countNeighbourUnknown(cell) == cell.getVal() - board.countNeighbourMines(cell) - board.countNeighbourAssumedMines(cell)){
                List<Cell> neighbours = board.getNeighbours(cell);
                for (Cell neighbour: neighbours) {
                    if (neighbour.getCellType()==CellType.UNKNOWN){
                        neighbour.setCellType(CellType.ASSUMEMINE);


                        newAssumptions.add(neighbour);
                        List<Cell> newRelatedList = new ArrayList<Cell>();
                        board.getRelatedClues(neighbour, newRelatedList);
                        for (Cell tmp: newRelatedList) {
                            if (!relatedClues.contains(tmp)) {
                                newRelatedClues.add(tmp);
                            }
                        }

                    }
                }
            } else if(cell.getVal() < board.countNeighbourAssumedMines(cell) + board.countNeighbourMines(cell)) {

                return false;


            } else if (cell.getVal() > board.countNeighbourMines(cell) + board.countNeighbourAssumedMines(cell)) {
                return false;
            }

        }
        if (newRelatedClues.size() == 0) {
            return true;
        }

        return runAssumption(newRelatedClues, querys);

    }



    /**
     * Clear all assumptions and recover unkown cells after assumption is complete.
     */
    private void recoverAssumption() {
        for(int i = 0; i < board.getLength(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                Cell cell = board.getBoard()[i][j];
                if (cell.getCellType() == CellType.ASSUMENOTMINE) {
                    cell.setCellType(CellType.UNKNOWN);
                } else if(cell.getCellType() == CellType.ASSUMEMINE) {
                    cell.setCellType(CellType.UNKNOWN);

                }
            }
        }
    }


    private boolean runDirect() {
        List<int[]> query = run();

        while (!query.isEmpty()) {
            if (runAutoQuery(query)) {
                query = run();
            }
            else {
                System.out.println("DIRECT");
                return false;
            }
        }
        return true;
    }

    private boolean runDirectManual() {
        List<int[]> query = run();

        while (!query.isEmpty()) {
            if (runManualQuery(query)) {
                query = run();
            }
            else {
                System.out.println("DIRECT");
                return false;
            }
        }
        return true;
    }

    private boolean runAssume() {
        List<int[]> query = assume();
        if (!runAutoQuery(query)) {
            System.out.println("ASSUMPTION");
            return false;
        }
        return true;
    }

    private boolean runAssumeManual() {
        List<int[]> query = assume();
        if (!runManualQuery(query)) {
            System.out.println("ASSUMPTION");
            return false;
        }
        return true;
    }

    private boolean runLoopAssume() {
        List<int[]> query = assume();
        while (!query.isEmpty()) {
            if (runAutoQuery(query)) {
                runDirect();
                query = run();
            }else {
                System.out.println("ASSUMPTION");
                return false;
            }
        }

        return true;
    }

    private boolean runRandomGuess() {
        List<int[]> query = randomGuess();
        if (!runAutoQuery(query)) {
            System.out.println("RANDOM GUESS");
            return false;
        }
        return true;
    }

    private boolean runRandomGuessManual() {
        List<int[]> query = randomGuess();
        if (!runManualQuery(query)) {
            System.out.println("RANDOM GUESS");
            return false;
        }
        return true;
    }


    private boolean runAutoQuery(List<int[]> query) {
        // Print the chain of influence
        System.out.println("Chain of influence: ");
        for (int[] node: query) {
            System.out.print(node[0] + ", " + node[1] + "\n");
        }
        System.out.println();

        for (int i = 0; i < query.size(); i++) {
            int val = randBoard[query.get(i)[0]][query.get(i)[1]];

            Cell cell = board.getBoard()[query.get(i)[0]][query.get(i)[1]];
            cell.setVal(val);
            cell.setUseless(false);

            if (val == 9) {
                cell.setCellType(CellType.ENDMINE);
                board.printBoard();
                System.out.println("BOOM! GAME OVER");
                return false;
            } else if (val == 0) {
                cell.setCellType(CellType.CLEAR);
            } else {
                cell.setCellType(CellType.CLUE);
            }
        }

        if(board.getMinesTotal() - board.getMinesFound() == board.getUnknowns()) {
            List<Cell> unkownList = board.getUnkownsList();
            for (Cell unkown: unkownList) {
                unkown.setCellType(CellType.MINE);
            }
        }

        if (board.getMinesFound() == board.getMinesTotal()) {
            board.printBoard();
            System.out.println("Congratulations!");
            return false;
        }
        return true;
    }

    private boolean runManualQuery(List<int[]> query) {
        String input = new String();
        Scanner scanner = new Scanner(System.in);
        Sweeper sweeper = new Sweeper(board);
        System.out.println("Format: [len wid val]");
        for (int i = 0; i < query.size(); i++) {
            System.out.println(query.get(i)[0] + " " + query.get(i)[1] + " Please input the value(0-9): ");
            input = scanner.next();
            int val = Integer.parseInt(input);

            Cell cell = board.getBoard()[query.get(i)[0]][query.get(i)[1]];
            cell.setVal(val);
            cell.setUseless(false);

            if (val == 9) {
                cell.setCellType(CellType.ENDMINE);
                board.printBoard();
                System.out.println("BOOM! GAME OVER");
                return false;
            } else if (val == 0) {
                cell.setCellType(CellType.CLEAR);
            } else {
                cell.setCellType(CellType.CLUE);
            }
        }

        if(board.getMinesTotal() - board.getMinesFound() == board.getUnknowns()) {
            List<Cell> unkownList = board.getUnkownsList();
            for (Cell unkown: unkownList) {
                unkown.setCellType(CellType.MINE);
            }
        }

        if (board.getMinesFound() == board.getMinesTotal()) {
            board.printBoard();
            System.out.println("Congratulations!");
            return false;
        }
        return true;
    }


    public void autoRun() {
        while (board.getMinesFound()!= board.getMinesTotal()) {


            if (!runDirect()) {
                return;
            }

            if (!runAssume()) {
                return;
            }

            if (!runDirect()) {
                return;
            }

            if (!runRandomGuess()) {
                return;
            }

        }
    }

    public void manualRun() {
        while (board.getMinesFound()!= board.getMinesTotal()) {


            if (!runDirectManual()) {
                return;
            }

            if (!runAssumeManual()) {
                return;
            }

            if (!runDirectManual()) {
                return;
            }

            if (!runRandomGuessManual()) {
                return;
            }

        }
    }

}
