import java.util.Random;

public class Map {
    private Cell[][]map;
    private int[] terrainNum;

    public Map(){
        this.map = new Cell[50][50];
        this.terrainNum = new int[4];

        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50 ; j++) {
                float p = random.nextFloat();
                Cell cell = new Cell(i, j);
                cell.setBelief(1.0/2500.0);
                if (p < 0.2) {
                    cell.setCellType(CellType.FLAT);
                    terrainNum[0]++;
                }
                else if (p < 0.5) {
                    cell.setCellType(CellType.HILLY);
                    terrainNum[1]++;
                }
                else if (p < 0.8) {
                    cell.setCellType(CellType.FORESTED);
                    terrainNum[2]++;
                }
                else {
                    cell.setCellType(CellType.CAVES);
                    terrainNum[3]++;
                }
                map[i][j] = cell;
            }
        }
    }

    public int[] getTerrainNum(){
        return this.terrainNum;
    }

    public Cell[][] getMap(){
        return this.map;
    }

    public void printMap(){
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50 ; j++) {
                if (map[i][j].getCellType() == CellType.FLAT)
                    System.out.print("- ");
                else if (map[i][j].getCellType() == CellType.HILLY)
                    System.out.print("H ");
                else if (map[i][j].getCellType() == CellType.FORESTED)
                System.out.print("F ");
                else if (map[i][j].getCellType() == CellType.CAVES)
                    System.out.print("C ");
            }
            System.out.println();
        }
    }
}
