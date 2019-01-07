public class Q2_1 {
    public static void main(String[] args) {
        Moving moving = new Moving();
        int iteration = 1000;
        double rule1Avg = 0;
        double rule2Avg = 0;

        for (int i = 0; i < iteration; i++) {
            rule1Avg += moving.Search(1);
            moving.reset();
            rule2Avg += moving.Search(2);
            moving.reset();
        }
        System.out.println("Rule 1 avg steps: " + rule1Avg / iteration);
        System.out.println("Rule 2 avg steps: " + rule2Avg / iteration);
    }
}
