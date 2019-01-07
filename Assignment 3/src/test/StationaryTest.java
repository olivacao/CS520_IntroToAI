public class StationaryTest {
    public static void main(String[] args){
        Stationary stationary = new Stationary();

//        for (int i = 0; i <10 ; i++) {
//            stationary.Search(1);
//            stationary.reset();
//        }

        stationary.naiveSearchOrMotion(1);
        stationary.reset();
        stationary.SearchOrMotion(1);
    }
}
