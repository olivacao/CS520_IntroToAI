import java.util.ArrayList;
import java.util.Random;

public class Stationary {
    private Map map;
    private Cell target;
    private int stepCounter;
    private int searchCounter;
    private int motionCounter;
    private Cell currentCell;

    private static int CONTAINING = 1;
    private static int FINDING = 2;

    public Stationary(){
        this.map = new Map();
        this.stepCounter = 0;
        this.stepCounter = 0;
        this.motionCounter = 0;

        Random rand = new Random();
        this.currentCell = map.getMap()[rand.nextInt(50)][rand.nextInt(50)];
        this.target = map.getMap()[rand.nextInt(50)][rand.nextInt(50)];
    }

    public Cell getTarget(){
        return target;
    }

    /**
     * Search for target as as described in question 3)
     * @param method put 1 for rule1, 2 for rule2
     * @return stepCounter
     */
    public int Search(int method){
        boolean targetNotFound = true;
        while (targetNotFound){
            stepCounter++;
            Cell searchCell = null;
            if (method == CONTAINING)
                searchCell = getCell_HighestProbContaining();
            else if (method == FINDING)
                searchCell = getCell_HighestProbFinding();
            else
                return 0;

            double searchCellBelief = searchCell.getBelief();//belief before search is performed
            double FalseNavProb = getFalseNavProb(searchCell.getCellType());

//            //FOR TEST PURPOSE
//            if (method == CONTAINING)
//                System.out.println(searchCellBelief);
//            else if (method == FINDING)
//                System.out.println(searchBelief*(1-FalseNavProb));

            if (searchCell == target){
                Random rand = new Random();
                if(rand.nextDouble() > FalseNavProb){
                    targetNotFound = false;
                    System.out.println("Target found at ("+searchCell.getLen()+", "+searchCell.getWid()+")\n" +
                            "Target Terrain Type: " + target.getCellType() + "\nTotal Steps: " + stepCounter);
                }
            }

            if (targetNotFound){
                for (Cell[] c:map.getMap()) {
                    for (Cell cell:c) {
                        double updatedBelief = cell.getBelief()/(1 + searchCellBelief*(FalseNavProb - 1));
                        cell.setBelief(updatedBelief);
                    }
                }
                searchCell.setBelief(searchCellBelief*FalseNavProb/(1 + searchCellBelief*(FalseNavProb - 1)));
                normalizeBelief();
            }

        }
        return stepCounter;
    }

    /**
     * Use simple strategy to search for target as described in question 4)
     * which is to simply move to the cell indicated by previous rules and search
     * @param method put 1 for rule1, 2 for rule2
     * @return [# of search, # of motion]
     */
    public int[] naiveSearchOrMotion(int method){
        boolean targetNotFound = true;
        int[] steps = new int[2];

        while (targetNotFound){
            searchCounter++;
            Cell searchCell = null;
            if (method == CONTAINING)
                searchCell = getCell_HighestProbContaining();
            else if (method == FINDING)
                searchCell = getCell_HighestProbFinding();
            else
                return steps;

            motionCounter += ManhattanDistance(currentCell, searchCell);
            currentCell = searchCell;

            double searchCellBelief = searchCell.getBelief();//belief before search is performed
            double FalseNavProb = getFalseNavProb(searchCell.getCellType());

            if (searchCell == target){
                Random rand = new Random();
                if(rand.nextDouble() > FalseNavProb){
                    targetNotFound = false;
                    System.out.println("Target found at ("+searchCell.getLen()+", "+searchCell.getWid()+")\n" +
                            "Target Terrain Type: " + target.getCellType()
                            + "\nSearch Steps: " + searchCounter + "\nMotion Steps: " + motionCounter);
                }
            }

            if (targetNotFound){
                for (Cell[] c:map.getMap()) {
                    for (Cell cell:c) {
                        double updatedBelief = cell.getBelief()/(1 + searchCellBelief*(FalseNavProb - 1));
                        cell.setBelief(updatedBelief);
                    }
                }
                searchCell.setBelief(searchCellBelief*FalseNavProb/(1 + searchCellBelief*(FalseNavProb - 1)));
                normalizeBelief();
            }
        }
        steps[0] = searchCounter;
        steps[1] = motionCounter;
        return steps;
    }

    /**
     * Use distance based strategy to search for target as described in question 4)
     * move to the most preferred cell and search
     * Preference is calculated by its belief and distance from the current cell
     * @param method put 1 for rule1, 2 for rule2
     * @return [# of search, # of motion]
     */
    public int[] SearchOrMotion(int method){
        boolean targetNotFound = true;
        int[] steps = new int[2];

        while (targetNotFound) {
            if (method == CONTAINING)
                updatePreference_HPC();
            else if (method == FINDING)
                updatePreference_HPF();
            else
                return steps;

            //printMapPreference();
            Cell preferredCell = getMostPreferred();
            motionCounter += ManhattanDistance(preferredCell, currentCell);
            currentCell = preferredCell;
            //System.out.println(currentCell.getLen() + ", " + currentCell.getWid());

            searchCounter++;
            double FalseNavProb = getFalseNavProb(currentCell.getCellType());
            if (currentCell == target){
                Random rand = new Random();
                if(rand.nextDouble() > FalseNavProb){
                    targetNotFound = false;
                    System.out.println("Target found at ("+currentCell.getLen()+", "+currentCell.getWid()+")\n" +
                            "Target Terrain Type: " + target.getCellType()
                            + "\nSearch Steps: " + searchCounter + "\nMotion Steps: " + motionCounter);
                }
            }

            if (targetNotFound){
                double searchCellBelief = currentCell.getBelief();
                for (Cell[] c:map.getMap()) {
                    for (Cell cell:c) {
                        double updatedBelief = cell.getBelief()/(1 + searchCellBelief*(FalseNavProb - 1));
                        cell.setBelief(updatedBelief);
                    }
                }
                currentCell.setBelief(searchCellBelief*FalseNavProb/(1 + searchCellBelief*(FalseNavProb - 1)));
                normalizeBelief();
            }

        }
        steps[0] = searchCounter;
        steps[1] = motionCounter;
        return steps;
    }

    public void printMapPreference(){
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50 ; j++) {
                if (i == currentCell.getLen()&& j == currentCell.getWid()){
                    System.out.print("*\t");
                }
                System.out.print(map.getMap()[i][j].getPreference()+"\t");
            }
            System.out.println();
        }
    }

    /**
     * Reset to initial state. Only keeping map info.
     */
    public void reset(){
        stepCounter = 0;
        motionCounter = 0;
        searchCounter = 0;
        for (Cell[] c:map.getMap()) {
            for (Cell cell:c) {
                cell.setBelief(1.0/2500.0);
                cell.setPreference(0);
            }
        }
        Random rand = new Random();
        this.currentCell = map.getMap()[rand.nextInt(50)][rand.nextInt(50)];
    }

    public Map getMap(){
        return map;
    }

    public int getStepCounter() {
        return stepCounter;
    }

    private int ManhattanDistance(Cell c1, Cell c2){
        return Math.abs(c1.getLen() - c2.getLen()) + Math.abs(c1.getWid() - c2.getWid());
    }

    /**
     * Find the cell with the highest probability of containing the target. Arbitrary pick if ties.
     * @return
     */
    private Cell getCell_HighestProbContaining(){
        double HighestProb = 0;
        ArrayList<Cell> cells = new ArrayList<Cell>();
        for (Cell[] c:map.getMap()) {
            for (Cell cell:c) {
                if (cell.getBelief() > HighestProb){
                    HighestProb = cell.getBelief();
                    cells.clear();
                    cells.add(cell);
                }
                else if (cell.getBelief() == HighestProb)
                    cells.add(cell);
            }
        }
        Random rand = new Random();
        Cell cellHPC= cells.get(rand.nextInt(cells.size()));
        return cellHPC;
    }

    /**
     * Find the cell with the highest probability of finding the target. Arbitrary pick if ties.
     * @return
     */
    private Cell getCell_HighestProbFinding(){
        double HighestProb = 0;
        ArrayList<Cell> cells = new ArrayList<Cell>();
        for (Cell[] c:map.getMap()) {
            for (Cell cell:c) {
                double ProbFinding = cell.getBelief()*(1-getFalseNavProb(cell.getCellType()));
                if (ProbFinding > HighestProb){
                    HighestProb = ProbFinding;
                    cells.clear();
                    cells.add(cell);
                }
                else if (ProbFinding == HighestProb)
                    cells.add(cell);
            }
        }
        Random rand = new Random();
        Cell cellHPF= cells.get(rand.nextInt(cells.size()));
        return cellHPF;
    }

    /**
     * To make sure belief of all cells add up to 1.
     */
    private void normalizeBelief(){
        double beliefSum = 0;
        double normFactor = 0;
        for (Cell[] c:map.getMap()) {
            for (Cell cell : c) {
                beliefSum += cell.getBelief();
            }
        }
        normFactor = 1.0/beliefSum;
        for (Cell[] c:map.getMap()) {
            for (Cell cell : c) {
                double belief = cell.getBelief()*normFactor;
                cell.setBelief(belief);
            }
        }

//        //FOR TEST PURPOSE
//        double updatedB = 0;
//        for (Cell[] c:map.getMap()) {
//            for (Cell cell : c) {
//                updatedB += cell.getBelief();
//            }
//        }
//        assert updatedB == 1;
    }

    /**
     * Return the probability of false navigation given cellType.
     * @param cellType
     * @return
     */
    private double getFalseNavProb(CellType cellType){
        double prob = 0;
        switch (cellType){
            case FLAT:
                prob = 0.1;
                break;
            case HILLY:
                prob = 0.3;
                break;
            case FORESTED:
                prob = 0.7;
                break;
            case CAVES:
                prob = 0.9;
                break;
            default:
                break;
        }
        return prob;
    }

    /**
     * Update preference of all cells based on the current location by rule 1.
     */
    private void updatePreference_HPC(){
        double shrinkFactor = 0.999;

        for (Cell[] c:map.getMap()) {
            for (Cell cell:c) {
                int dist = ManhattanDistance(currentCell, cell);
                cell.setPreference(cell.getBelief()*Math.pow(shrinkFactor, dist/4));
            }
        }
    }

    /**
     * Update preference of all cells based on the current location by rule 2.
     */
    private void updatePreference_HPF(){
        double shrinkFactor = 0.999;

        for (Cell[] c:map.getMap()) {
            for (Cell cell:c) {
                double ProbFinding = cell.getBelief()*(1-getFalseNavProb(cell.getCellType()));
                int dist = ManhattanDistance(currentCell, cell);
                cell.setPreference(ProbFinding*Math.pow(shrinkFactor, dist/4));
            }
        }
    }

    /**
     * Return the most preferred cell.
     * @return
     */
    private Cell getMostPreferred(){
        double HighestPreference = 0;
        ArrayList<Cell> cells = new ArrayList<Cell>();
        for (Cell[] c:map.getMap()) {
            for (Cell cell:c) {
                if (cell.getPreference() > HighestPreference){
                    HighestPreference = cell.getPreference();
                    cells.clear();
                    cells.add(cell);
                }
                else if (cell.getPreference() == HighestPreference)
                    cells.add(cell);
            }
        }
        Random rand = new Random();
        Cell cellHP= cells.get(rand.nextInt(cells.size()));


//        for (Cell c:cells) {
//            System.out.print("("+c.getLen()+", "+c.getWid()+")pre: "+c.getPreference()+"  ");
//        }
//        System.out.println();
        return cellHP;
    }

    /**
     * move the target to a random place.
     */
    public void moveTarget(){
        Random rand = new Random();
        this.target = map.getMap()[rand.nextInt(50)][rand.nextInt(50)];
    }



}
