public class Cell {
    private int len;
    private int wid;
    private int val;
    private CellType cellType;
    private boolean isUseless;


    public Cell(int len, int wid) {
         this.cellType = CellType.UNKNOWN;
         this.isUseless = true;
         this.len = len;
         this.wid = wid;
         this.val = -1;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public boolean isUseless() {
        return isUseless;
    }

    public void setUseless(boolean useless) {
        isUseless = useless;
    }

    public int getLen() {
        return len;
    }

    public int getWid() {
        return wid;
    }

}
