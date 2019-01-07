public class Cell {
    private int len;
    private int wid;
    private CellType cellType;
    private double belief;
    private double beliefPrime;//belief of previous state
    private double preference;//a measure of belief given distance

    public Cell(int len, int wid){
        this.len = len;
        this.wid = wid;
        this.cellType = null;
        this.belief = 0;
        this.beliefPrime = 0;
        this.preference = 0;
    }

    public int getLen(){
        return len;
    }

    public int getWid(){
        return wid;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public double getBelief() {
        return belief;
    }

    public void setBelief(double belief) {
        this.belief = belief;
    }

    public double getPreference(){
        return preference;
    }

    public void setPreference(double preference){
        this.preference = preference;
    }

    public double getBeliefPrime(){
        return beliefPrime;
    }

    public void setBeliefPrime(double beliefPrime){
        this.beliefPrime = beliefPrime;
    }
}
