class MapTest {
    public static void main(String[] args){
        Map map = new Map();
        map.printMap();
        System.out.println("\n"+map.getMap()[25][25].getBelief());
        for (int i:map.getTerrainNum()) {
            System.out.print(i+", ");
        }
    }
}