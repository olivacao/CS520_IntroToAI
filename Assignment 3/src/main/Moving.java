import java.util.ArrayList;
import java.util.Random;

public class Moving {
    private Map map;
    private Cell target;
    private Cell currentCell;
    private int stepCounter;
    private int searchCounter;
    private int motionCounter;

    private static int CONTAINING = 1;
    private static int FINDING = 2;

    public Moving(){
        this.map = new Map();

        Random rand = new Random();
        this.currentCell = map.getMap()[rand.nextInt(50)][rand.nextInt(50)];
        this.target = map.getMap()[rand.nextInt(50)][rand.nextInt(50)];
    }

    public Cell getTarget() {
        return target;
    }

    public Map getMap() {
        return map;
    }

    /**
     * Search for target
     * allowing the agent to move to and search any cell in the map with a single step.
     * @param method put 1 for rule1, 2 for rule2
     * @return stepCounter
     */
    public int Search(int method){
        boolean targetNotFound = true;
        CellType[] info = new CellType[2];
        while (targetNotFound){
            stepCounter++;
            Cell searchCell = null;

            saveBeliefStatus();

            info = randomWalk();
            updateBelief(info[0], info[1]);
            normalizeBelief();

            if (method == CONTAINING)
                searchCell = getCell_HighestProbContaining();
            else if (method == FINDING)
                searchCell = getCell_HighestProbFinding();
            else
                return 0;

            double searchCellBelief = searchCell.getBelief();//belief before search is performed
            double FalseNavProb = getFalseNavProb(searchCell.getCellType());

            //FOR TEST PURPOSE
            //printMapBelief();
            System.out.println("---------Step "+stepCounter+"----------");
            System.out.println("Target at:("+target.getLen()+", "+target.getWid()+")");
            if (method == CONTAINING)
                System.out.println("Search Cell at:("+searchCell.getLen()+", "+searchCell.getWid()+") with belief: "+searchCellBelief);
            else if (method == FINDING)
                System.out.println("Search Cell at:("+searchCell.getLen()+", "+searchCell.getWid()+") with belief: "+searchCellBelief*(1-FalseNavProb));

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
        CellType[] info = new CellType[2];
        int[] steps = new int[2];

        while (targetNotFound){
            searchCounter++;
            Cell searchCell = null;

            saveBeliefStatus();

            info = randomWalk();
            updateBelief(info[0], info[1]);
            normalizeBelief();

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
     * Use distance based strategy to search for target
     * move to the most preferred cell and search
     * Preference is calculated by its belief and distance from the current cell
     * @param method put 1 for rule1, 2 for rule2
     * @return [# of search, # of motion]
     */
    public int[] SearchOrMotion(int method){
        boolean targetNotFound = true;
        CellType[] info = new CellType[2];
        int[] steps = new int[2];
        while (targetNotFound) {

            saveBeliefStatus();

            info = randomWalk();
            updateBelief(info[0], info[1]);
            normalizeBelief();

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

    /**
     * Target moving randomly to an adjacent cell.
     * @return cellType info reported by surveillance
     */
    public CellType[] randomWalk(){
        ArrayList<Cell> neighborCells = new ArrayList<Cell>();
        if (target.getLen()-1 >= 0)
            neighborCells.add(map.getMap()[target.getLen()-1][target.getWid()]);
        if (target.getLen()+1 <= 49)
            neighborCells.add(map.getMap()[target.getLen()+1][target.getWid()]);
        if (target.getWid()-1 >= 0)
            neighborCells.add(map.getMap()[target.getLen()][target.getWid()-1]);
        if (target.getWid()+1 <= 49)
            neighborCells.add(map.getMap()[target.getLen()][target.getWid()+1]);

        Random rand = new Random();
        Cell destination= neighborCells.get(rand.nextInt(neighborCells.size()));

        CellType[] border = new CellType[2];
        if (rand.nextDouble() > 0.5){
            border[0] = target.getCellType();
            border[1] = destination.getCellType();
        }
        else {
            border[0] = destination.getCellType();
            border[1] = target.getCellType();
        }

        target = destination;

        return border;
    }

    /**
     * update belief based on the belief of previous state and the transition model.
     * @param ct1  first cellType reported by surveillance
     * @param ct2  second cellType reported by surveillance
     */
    private void updateBelief(CellType ct1, CellType ct2){
        for (Cell[] c:map.getMap()) {
            for (Cell cell : c) {
                if (cell.getCellType() != ct1 && cell.getCellType() != ct2)
                    cell.setBelief(0);

                if (cell.getCellType() == ct1){
                    ArrayList<Cell> neighborCells = new ArrayList<Cell>();
                    if (cell.getLen()-1 >= 0 &&
                            map.getMap()[cell.getLen()-1][cell.getWid()].getCellType() == ct2)
                        neighborCells.add(map.getMap()[cell.getLen()-1][cell.getWid()]);
                    if (cell.getLen()+1 <= 49 &&
                            map.getMap()[cell.getLen()+1][cell.getWid()].getCellType() == ct2)
                        neighborCells.add(map.getMap()[cell.getLen()+1][cell.getWid()]);
                    if (cell.getWid()-1 >= 0 &&
                            map.getMap()[cell.getLen()][cell.getWid()-1].getCellType() == ct2)
                        neighborCells.add(map.getMap()[cell.getLen()][cell.getWid()-1]);
                    if (cell.getWid()+1 <= 49 &&
                            map.getMap()[cell.getLen()][cell.getWid()+1].getCellType() == ct2)
                        neighborCells.add(map.getMap()[cell.getLen()][cell.getWid()+1]);
                    for (Cell neighborCell:neighborCells) {
                        neighborCell.setBelief(neighborCell.getBelief()+cell.getBeliefPrime()/neighborCells.size());
                    }
                }

                if (cell.getCellType() == ct2){
                    ArrayList<Cell> neighborCells = new ArrayList<Cell>();
                    if (cell.getLen()-1 >= 0 &&
                            map.getMap()[cell.getLen()-1][cell.getWid()].getCellType() == ct1)
                        neighborCells.add(map.getMap()[cell.getLen()-1][cell.getWid()]);
                    if (cell.getLen()+1 <= 49 &&
                            map.getMap()[cell.getLen()+1][cell.getWid()].getCellType() == ct1)
                        neighborCells.add(map.getMap()[cell.getLen()+1][cell.getWid()]);
                    if (cell.getWid()-1 >= 0 &&
                            map.getMap()[cell.getLen()][cell.getWid()-1].getCellType() == ct1)
                        neighborCells.add(map.getMap()[cell.getLen()][cell.getWid()-1]);
                    if (cell.getWid()+1 <= 49 &&
                            map.getMap()[cell.getLen()][cell.getWid()+1].getCellType() == ct1)
                        neighborCells.add(map.getMap()[cell.getLen()][cell.getWid()+1]);
                    for (Cell neighborCell:neighborCells) {
                        neighborCell.setBelief(neighborCell.getBelief()+cell.getBeliefPrime()/neighborCells.size());
                    }
                }
            }
        }

    }

    /**
     * save the belief of previous state.
     */
    private void saveBeliefStatus(){
        for (Cell[] c:map.getMap()) {
            for (Cell cell : c) {
                cell.setBeliefPrime(cell.getBelief());
                cell.setBelief(0);
            }
        }
    }

    private int ManhattanDistance(Cell c1, Cell c2){
        return Math.abs(c1.getLen() - c2.getLen()) + Math.abs(c1.getWid() - c2.getWid());
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
                cell.setBeliefPrime(0);
            }
        }
        Random rand = new Random();
        this.currentCell = map.getMap()[rand.nextInt(50)][rand.nextInt(50)];
        this.target = map.getMap()[rand.nextInt(50)][rand.nextInt(50)];
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



}
