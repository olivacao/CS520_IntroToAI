class MovingTest {
    public static void main(String[] args){
        Moving moving = new Moving();

//        moving.Search(1);
        moving.naiveSearchOrMotion(2);
        moving.reset();
        moving.SearchOrMotion(2);
    }
}