public class Q1_3 {
    public static void main(String[] args) {
        Stationary stationary = new Stationary();

        int iteration = 1000;
        double rule1Avg = 0;
        double rule2Avg = 0;

        for (int i = 0; i < iteration; i++) {
            rule1Avg += stationary.Search(1);
            stationary.reset();
            rule2Avg += stationary.Search(2);
            stationary.reset();
            stationary.moveTarget();
        }
        System.out.println("Rule 1 avg steps: " + rule1Avg / iteration);
        System.out.println("Rule 2 avg steps: " + rule2Avg / iteration);
    }
}
